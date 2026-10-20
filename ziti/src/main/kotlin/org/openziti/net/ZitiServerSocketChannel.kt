/*
 * Copyright (c) 2018-2023 NetFoundry Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openziti.net

import com.goterl.lazysodium.utils.Key
import com.goterl.lazysodium.utils.KeyPair
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.openziti.ZitiAddress
import org.openziti.api.Service
import org.openziti.api.Session
import org.openziti.api.SessionType
import org.openziti.crypto.Crypto
import org.openziti.edge.model.TerminatorPrecedence
import org.openziti.impl.ZitiContextImpl
import org.openziti.net.ZitiProtocol.Header
import org.openziti.net.nio.FutureHandler
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.IOException
import java.lang.Math.min
import java.net.BindException
import java.net.SocketAddress
import java.net.SocketOption
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.*
import java.security.SecureRandom
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicReference
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel as Chan

/**
 * Server channel hosting a Ziti service.
 *
 * The hosted service is established with multiple terminators -- one per edge router
 * available for the service (up to [ZitiAddress.Bind.maxTerminators]) -- so it stays
 * reachable when a router goes away. All terminators share the same [listenerId],
 * which the routers use to identify this hosted instance. Dropped or rejected
 * bindings are re-established until the channel is closed.
 */
internal class ZitiServerSocketChannel(val ctx: ZitiContextImpl):
    AsynchronousServerSocketChannel(null),
    Logged by ZitiLog() {

    lateinit var localAddr: ZitiAddress.Bind

    // identifies all terminators of this hosted instance across routers
    private val listenerId = ByteArray(32).apply {
        SecureRandom().nextBytes(this)
    }

    // state transitions: Initial -> Binding (on bind) -> Bound (first binder established)
    // -> Closed (close); every transition is a CAS, so a concurrent close() is never overwritten
    private val stateRef = AtomicReference(State.Initial)
    val state: State
        get() = stateRef.get()

    lateinit var incoming: Chan<IncomingDial>
    @Volatile private var encrypted = false
    @Volatile private var bindJob: Job? = null

    // one Binding per edge router, keyed by router address
    private val binders = ConcurrentHashMap<String, Binder>()

    /**
     * Wake the bind loop. The element is the session a binder reported as rejected
     * or ended, or null when a Binding merely changed (e.g. its channel dropped).
     * Applied whenever the loop receives it (at the top of a bind cycle or while waiting
     * for a change): a reported session is discarded only if it is still the one held,
     * so a late report cannot discard a newer session.
     * Ordered (not conflated) so a rejection is not lost to a subsequent drop.
     */
    private val rebindSignal = Chan<Session?>(Chan.UNLIMITED)

    enum class State {
        Initial,
        Binding,
        Bound,
        Closed
    }

    enum class BinderState {
        Unbound,
        Binding,
        Bound
    }

    /**
     * A dial received on a binding. The channel, key pair and generation of the binding
     * are captured on arrival: a binder drops and re-binds on its own while the dial waits
     * in the queue, and a dial is only valid for the binding it arrived on.
     */
    internal inner class IncomingDial(
        val binder: Binder,
        val msg: Message,
        val channel: Channel,
        val keyPair: KeyPair?,
        private val generation: Int
    ) {
        val isCurrent: Boolean
            get() = binder.generation == generation && binder.state != BinderState.Unbound
    }

    override fun isOpen(): Boolean = state != State.Closed

    private fun checkService(addr: ZitiAddress.Bind): Service {
        val servResult = runCatching { ctx.getService(addr.service, 5000L) }
        val service = servResult.getOrElse {
            throw BindException("no permission to bind to service[${addr.service}]")
        }

        if (!service.permissions.contains(SessionType.BIND)) {
            throw BindException("no permission to bind to service[${service.name}]")
        }
        return service
    }

    // throws unless the channel can still be bound
    private fun requireBindable() {
        when (state) {
            State.Initial -> {}
            State.Binding,
            State.Bound -> throw AlreadyBoundException()
            State.Closed -> throw ClosedChannelException()
        }
    }

    override fun bind(local: SocketAddress?, backlog: Int): AsynchronousServerSocketChannel {
        if (local !is ZitiAddress.Bind) throw UnsupportedAddressTypeException()
        requireBindable()

        // validate before the state transition: a failed bind() leaves the channel
        // open and reusable
        checkService(local)

        // The address and the incoming queue are set before leaving `Initial`: a rejected
        // second bind() can't overwrite the address of the running one, and a concurrent
        // accept() never sees them unset. close() is lock-free, so it may have closed the
        // channel since the check above: the CAS decides
        synchronized(this) {
            requireBindable()
            localAddr = local
            incoming = Chan(backlog)
            if (!stateRef.compareAndSet(State.Initial, State.Binding)) {
                incoming.close()
                throw ClosedChannelException()
            }
        }

        val job = ctx.launch {
            try {
                bindLoop()
            } finally {
                // close() may have missed binders this loop created (it ran before the job
                // was published, or while the loop was starting): the loop cleans up after
                // itself once it sees the channel closed
                if (state == State.Closed) {
                    withContext(NonCancellable) { stopBinders() }
                }
            }
        }
        bindJob = job

        // close() reads bindJob after its CAS, we read the state after publishing the job:
        // one of the two sees the other, so the loop is always cancelled
        if (state == State.Closed) job.cancel()
        return this
    }

    /**
     * Establishes and maintains terminators for the bound service, one per edge router
     * the controller permits to host it (up to [ZitiAddress.Bind.maxTerminators]). The
     * router list is refreshed while below target. The bind session is fetched once and
     * kept for all binders until it is rejected or ended; dropped bindings are
     * re-established (reusing the session) until the channel is closed.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun bindLoop() = coroutineScope {
        var attempt = 0

        // last known list of edge routers for this service
        var routers: List<String> = emptyList()

        // bind session for this hosted service: fetched once and kept for all
        // binders until a binder reports it as rejected or ended
        var session: Session? = null

        // a session reported as rejected/ended is discarded only if we still hold it
        fun discardIfHeld(reported: Session?) {
            if (reported != null && session === reported) session = null
        }

        // wait for a change (Binding dropped, session rejected) or the periodic recheck.
        // The received signal must be applied: it may carry a rejected session
        suspend fun awaitChange() {
            select<Unit> {
                onTimeout(RECHECK_INTERVAL) { }
                rebindSignal.onReceive { discardIfHeld(it) }
            }
        }

        while (state != State.Closed) {
            if (attempt > 0) {
                d { "delaying rebind attempt[$attempt] for ${REBIND_DELAY * min(attempt, MAX_REBIND_INTERVAL)}" }
                delay(REBIND_DELAY * min(attempt, MAX_REBIND_INTERVAL))
            }
            if (state == State.Closed) return@coroutineScope

            val service = runCatching { checkService(localAddr) }.getOrElse {
                // this is a hard error. service is no longer available for Binding.
                // stop hosting, but leave the channel open: the app still owns it,
                // and close() will finish the cleanup
                d { "service[${localAddr.service}] is no longer available for Binding" }
                incoming.close(it)
                stopBinders()
                return@coroutineScope
            }

            encrypted = service.encryptionRequired

            // apply pending rebind signals
            while (true) {
                val r = rebindSignal.tryReceive()
                if (!r.isSuccess) break
                discardIfHeld(r.getOrNull())
            }

            val maxTerm = localAddr.maxTerminators.coerceAtLeast(1)
            val active = binders.values.count { it.state != BinderState.Unbound }

            // healthy -- session in hand and at the target implied by the last known
            // router list: nothing to do, no fetches, no session churn. wait for a
            // change (Binding dropped, session rejected) and re-evaluate periodically
            val lastTarget = maxTerm.coerceAtMost(routers.size)
            if (session != null && routers.isNotEmpty() && active >= lastTarget) {
                attempt = 0
                awaitChange()
                continue
            }

            try {
                // below target (or no router list yet): refresh it -- newly available
                // routers are picked up here. On failure the last known list is kept
                if (routers.isEmpty() || active < lastTarget) {
                    routers = ctx.getServiceRouters(service)
                }

                if (routers.isEmpty()) {
                    // nothing can host the service (yet)
                    attempt = 0
                    awaitChange()
                    continue
                }

                // get a session only when we don't have one; it is kept for all
                // binders until it is rejected or ended
                val bindSession = session ?: ctx.getNetworkSession(service, SessionType.BIND).also { session = it }

                val target = maxTerm.coerceAtMost(routers.size)
                val bound = processBindings(service, bindSession, routers, target)
                d { "bindings for service[${localAddr.service}]: active[$bound] target[$target]" }

                if (bound < target) {
                    attempt++
                    continue
                }

                attempt = 0
            } catch (ex: Throwable) {
                if (ex is CancellationException) throw ex
                e(ex) { "failed to bind service[${localAddr.service}]. scheduling rebind" }
                attempt++
            }
        }
    }

    /**
     * Ensures one [Binder] per edge router in [routers] (their tls addresses), up to
     * [target] terminators, preferring routers with lower latency. New bindings are
     * established with the bind [session]. Returns the number of active (bound or
     * in-progress) bindings.
     */
    private suspend fun processBindings(service: Service, session: Session, routers: List<String>, target: Int): Int = coroutineScope {
        val candidates = mutableListOf<Pair<Long, Channel>>() // (latency, channel)
        val unconnected = mutableListOf<Channel>()

        for (addr in routers) {
            val c = ctx.getChannel(addr)
            when (val s = c.state) {
                is Channel.State.Connected -> candidates.add(s.latency to c)
                else -> unconnected.add(c)
            }
        }

        if (candidates.isEmpty()) {
            // wait for at least one to connect; the rest are picked up by later iterations
            val ch = runCatching { ctx.connectAll(unconnected) }.getOrNull()
                ?: return@coroutineScope 0
            val latency = (ch.state as? Channel.State.Connected)?.latency ?: 0L
            candidates.add(latency to ch)
        } else {
            unconnected.forEach { it.tryConnect() }
        }

        // stable sort: routers reporting the same latency keep their order in [routers]
        val ordered = candidates.sortedBy { it.first }.map { it.second }

        // budget: currently active bindings, not just the ones started in this cycle --
        // latency reordering must not push the total above [target]
        var active = binders.values.count { it.state != BinderState.Unbound }
        val jobs = mutableListOf<Job>()
        for (ch in ordered) {
            if (active >= target) break

            val b = binders.computeIfAbsent(ch.name) { Binder(it) }
            if (b.state == BinderState.Unbound) {
                jobs += launch { b.bind(ch, service, session) }
                active++
            }
        }
        jobs.joinAll()

        return@coroutineScope binders.values.count { it.state != BinderState.Unbound }
    }

    private fun onBinderEstablished() {
        // CAS: a concurrent close() must not be overwritten
        stateRef.compareAndSet(State.Binding, State.Bound)
    }

    private fun terminatorCost(service: Service): Int {
        localAddr.cost?.let { if (it > 0) return it }

        val identity = ctx.getId() ?: return 0
        // hosting attributes are keyed by service id
        return identity.serviceHostingCosts[service.id] ?: identity.defaultHostingCost
    }

    private fun terminatorPrecedence(service: Service): ZitiAddress.Precedence {
        localAddr.precedence?.let { return it }

        val identity = ctx.getId() ?: return ZitiAddress.Precedence.Default
        // hosting attributes are keyed by service id
        val p = identity.serviceHostingPrecedences[service.id]
            ?: identity.defaultHostingPrecedence
            ?: return ZitiAddress.Precedence.Default

        return when (p) {
            TerminatorPrecedence.REQUIRED -> ZitiAddress.Precedence.Required
            TerminatorPrecedence.FAILED -> ZitiAddress.Precedence.Failed
            else -> ZitiAddress.Precedence.Default
        }
    }

    override fun getLocalAddress(): SocketAddress? {
        if (!::localAddr.isInitialized)
            return null

        return if (isOpen()) localAddr else throw ClosedChannelException()
    }

    override fun <A> accept(att: A, handler: CompletionHandler<AsynchronousSocketChannel, in A>) {
        if (state == State.Closed) throw ClosedChannelException()
        if (state == State.Initial) throw NotYetBoundException()

        ctx.launch {
            try {
                val dial = nextDial()
                val binder = dial.binder
                val msg = dial.msg
                val ch = dial.channel

                val child = ZitiSocketChannel(ctx)
                d { "accepting child conn[${child.connId}] on parent[${binder.connId}]" }
                msg.getIntHeader(Header.RouterProvidedConnId)?.toUInt()?.let {
                    d { "setting child[${child.connId}].rtConnId = $it (router provided)" }
                    child.rtConnId = it
                }

                val connIdBuf = ByteArray(4)
                ByteBuffer.wrap(connIdBuf).order(ByteOrder.LITTLE_ENDIAN).putInt(child.connId.toInt())
                val dialSuccess = Message(ZitiProtocol.ContentType.DialSuccess, connIdBuf)
                dialSuccess.setHeader(Header.SeqHeader, 0)
                dialSuccess.setHeader(Header.ConnId, binder.connId)
                dialSuccess.setHeader(Header.ReplyFor, msg.seqNo)

                dial.keyPair?.let { kp ->
                    val sessKeys = msg.getHeader(Header.PublicKeyHeader)?.let {
                        Crypto.kx(kp, Key.fromBytes(it), true)
                    }
                    child.setupCrypto(sessKeys)
                } ?: child.setupCrypto(null)

                runCatching {
                    ch.SendSynch(dialSuccess)
                }.onSuccess {
                    child.state.set(ZitiSocketChannel.State.connected)
                    ch.registerReceiver(child.rtConnId, child)
                    child.channel.complete(ch)
                    child.startCrypto(ch)
                    child.local = localAddr
                    child.remote = ZitiAddress.Session("${binder.connId}", localAddr.service,
                        msg.getStringHeader(Header.CallerIdHeader), msg.getHeader(Header.AppDataHeader))

                    handler.completed(child, att)
                }.onFailure { t ->
                    val err = t.message
                    handler.failed(IOException(err, t), att)
                }
            } catch (ex: Throwable) {
                when (ex) {
                    is ClosedReceiveChannelException ->
                        handler.failed(ClosedChannelException(), att)
                    is CancellationException ->
                        handler.failed(ClosedChannelException(), att)
                    else ->
                        handler.failed(ex, att)
                }
            }
        }
    }

    // The next dial whose binding is still the one it arrived on. A dial queued behind a
    // binding that has since dropped or re-bound is dead at the router: it is skipped (and
    // rejected if its router is still reachable) instead of failing accept() for the
    // healthy bindings
    private suspend fun nextDial(): IncomingDial {
        while (true) {
            val dial = incoming.receive()
            if (dial.isCurrent) return dial

            d { "dropping stale dial on conn[${dial.binder.connId}] to router[${dial.binder.routerName}]" }
            if (dial.channel.state is Channel.State.Connected) {
                try {
                    dial.binder.rejectDial(dial.channel, dial.msg)
                } catch (ex: Exception) {
                    if (ex is CancellationException) throw ex
                    d { "failed to reject stale dial: ${ex.message}" }
                }
            }
        }
    }

    override fun accept(): Future<AsynchronousSocketChannel> {
        val result = CompletableFuture<AsynchronousSocketChannel>()
        accept(result, FutureHandler())
        return result
    }

    override fun supportedOptions(): Set<SocketOption<*>> = setOf()
    override fun <T> setOption(name: SocketOption<T>?, value: T): AsynchronousServerSocketChannel = this
    override fun <T : Any> getOption(name: SocketOption<T>?): T? = null

    override fun close() {
        // single closer: atomically transition into closed
        while (true) {
            val s = stateRef.get()
            if (s == State.Closed) return
            if (stateRef.compareAndSet(s, State.Closed)) break
        }

        val job = bindJob
        bindJob = null

        runBlocking {
            // wait out any bind in flight: a bind cancelled mid-request must not
            // complete (register a receiver, get bound) after the binders are stopped.
            // A cancelled bind leaves its binder in `Binding` state, which is unbound
            // -- including a Bind request that was already written to the wire -- by the
            // loop's own cleanup (see bind()) or, failing that, by stopBinders() below
            job?.cancelAndJoin()
            runCatching { stopBinders() }
        }

        // incoming is only assigned on bind(): the channel may be closed unbound
        if (::incoming.isInitialized) {
            incoming.close()
        }
    }

    private suspend fun stopBinders() {
        if (binders.isEmpty()) return

        // run the unbinds concurrently, bounded in total time: a stalled router
        // (sends queue up until its transport recovers) must not hang the caller
        withTimeoutOrNull(UNBIND_TIMEOUT) {
            coroutineScope {
                binders.values.map { b ->
                    launch { runCatching { b.stop() } }
                }.joinAll()
            }
        }
        binders.clear()
    }

    /**
     * Binding to a single edge router: one terminator with its own connId and key pair.
     */
    internal inner class Binder(val routerName: String): Channel.MessageReceiver {
        val connId: UInt = ctx.nextConnId()
        @Volatile var channel: Channel? = null
        @Volatile var state: BinderState = BinderState.Unbound

        // bind session this Binding was established with
        @Volatile var boundSession: Session? = null
        var keyPair: KeyPair? = null

        // incremented on every (re)bind: a dial received under an earlier one is stale
        @Volatile var generation = 0

        suspend fun bind(ch: Channel, service: Service, session: Session) {
            if (state != BinderState.Unbound) return

            generation++
            boundSession = session
            channel = ch
            state = BinderState.Binding
            if (encrypted) keyPair = Crypto.newKeyPair()

            val connectMsg = Message(ZitiProtocol.ContentType.Bind, session.token.toByteArray(Charsets.UTF_8)).apply {
                setHeader(Header.ConnId, connId)
                setHeader(Header.SeqHeader, 0)
                setHeader(Header.ListenerId, listenerId)
                setHeader(Header.SupportsInspectHeader, true)
                setHeader(Header.RouterProvidedConnId, true)

                val bindId = localAddr.identity ?: (if (localAddr.useEdgeId) ctx.getId()?.name else null)
                bindId?.let { setHeader(Header.TerminatorIdentityHeader, it) }

                val cost = terminatorCost(service)
                if (cost > 0) setHeader(Header.CostHeader, cost.toShort())

                val precedence = terminatorPrecedence(service)
                if (precedence != ZitiAddress.Precedence.Default) {
                    setHeader(Header.PrecedenceHeader, precedence.value)
                }

                keyPair?.let { setHeader(Header.PublicKeyHeader, it.publicKey.asBytes) }
            }

            d { "requesting BIND on router[$routerName] conn[$connId]" }
            ch.registerReceiver(connId, this@Binder)

            try {
                // a stalled router must not hold up the bind loop
                val reply = withTimeoutOrNull(BIND_TIMEOUT) { ch.SendForReply(connectMsg).await() }
                    ?: throw IOException("router[$routerName] did not respond to BIND")
                when (reply.content) {
                    ZitiProtocol.ContentType.StateConnected -> {
                        d { "bound to router[$routerName] conn[$connId]" }
                        state = BinderState.Bound
                        onBinderEstablished()
                    }
                    ZitiProtocol.ContentType.StateClosed -> {
                        val err = reply.body.toString(Charsets.UTF_8)
                        w { "bind rejected by router[$routerName]: $err" }
                        // the session was rejected: the catch reports it to the bind loop
                        throw BindRejected(session, err)
                    }
                    else -> throw IOException("invalid response type: ${reply.content}")
                }
            } catch (ex: Throwable) {
                // in-flight replies are cancelled when the channel drops: this surfaces
                // here as CancellationException even though this coroutine was not cancelled
                if (ex is CancellationException) coroutineContext.ensureActive()

                e(ex) { "failed to bind to router[$routerName] conn[$connId]" }
                stop()
                // single signal per failure: carry the rejected session when applicable
                rebindSignal.trySend((ex as? BindRejected)?.session)
            }
        }

        suspend fun stop() {
            if (state == BinderState.Unbound) {
                channel = null
                return
            }

            val ch = channel
            state = BinderState.Unbound
            channel = null
            keyPair = null

            ch?.let {
                it.deregisterReceiver(connId)

                val bindSession = boundSession
                if (it.state is Channel.State.Connected && bindSession != null) {
                    d { "requesting UNBIND on router[$routerName] conn[$connId]" }
                    val unbind = Message(ZitiProtocol.ContentType.Unbind, bindSession.token.toByteArray(Charsets.UTF_8)).apply {
                        setHeader(Header.ConnId, connId)
                        setHeader(Header.ListenerId, listenerId)
                    }
                    // routers do not reply to UNBIND: fire and forget, do not wait on the write
                    runCatching { it.Send(unbind) }.onFailure { t ->
                        d { "failed to send UNBIND to router[$routerName]: ${t.message}" }
                    }
                }
            }
        }

        // tells the router the dial failed, so the dialer does not wait for a timeout
        suspend fun rejectDial(ch: Channel, msg: Message) {
            val reject = Message(ZitiProtocol.ContentType.DialFailed)
                .setHeader(Header.ConnId, connId)
                .setHeader(Header.ReplyFor, msg.seqNo)
                .setHeader(Header.SeqHeader, 0)

            ch.Send(reject)
        }

        override suspend fun receive(msg: Result<Message>) {
            msg.onFailure {
                // connection to the router dropped
                w { "Binding to router[$routerName] failed: ${it.message}" }
                state = BinderState.Unbound
                channel = null
                rebindSignal.trySend(null)
            }

            msg.onSuccess {
                receive(it)
            }
        }

        @OptIn(ExperimentalEncodingApi::class)
        suspend fun receive(msg: Message) {
            when (msg.content) {
                ZitiProtocol.ContentType.Dial -> {
                    // not bound anymore: there is nothing to queue the dial for or reply on
                    val ch = channel
                    if (ch != null) {
                        // capture the binding the dial arrived on: it may be gone by the time
                        // accept() gets to it
                        val queued = incoming.trySend(IncomingDial(this, msg, ch, keyPair, generation))
                        if (queued.isFailure) {
                            // a closed queue means the server is shutting down: not worth a warning
                            if (!queued.isClosed) {
                                w { "dial backlog is full, rejecting dial on router[$routerName] " +
                                        "conn[$connId] for service[${localAddr.service}]" }
                            }
                            rejectDial(ch, msg)
                        }
                    }
                }
                ZitiProtocol.ContentType.StateClosed,
                ZitiProtocol.ContentType.StateSessionEnded -> {
                    val reason = msg.body.toString(Charsets.UTF_8)
                    w { "Binding to router[$routerName] was closed: $reason" }
                    state = BinderState.Unbound
                    keyPair = null
                    channel?.deregisterReceiver(connId)
                    channel = null
                    // the session is no good: report it, the next bind cycle gets a fresh one
                    rebindSignal.trySend(boundSession)
                }
                ZitiProtocol.ContentType.ConnInspectRequest -> {
                    channel?.let {
                        val listenerStr = Base64.Default.encode(listenerId)
                        val body = "id[$connId] serviceName[${localAddr.service}] listenerId[$listenerStr] " +
                                "closed[${!isOpen}] encrypted[${keyPair != null}]"
                        val st: Byte = 2
                        val m = Message(ZitiProtocol.ContentType.ConnInspectResponse, body = body.toByteArray())
                            .setHeader(Header.ConnId, connId)
                            .setHeader(Header.ReplyFor, msg.seqNo)
                            .setHeader(Header.ConnTypeHeader, byteArrayOf(st))
                        it.Send(m)
                    }
                }
                else -> {
                    e { "unexpected message[${msg.content}] on bound conn[$connId]" }
                }
            }
        }
    }

    companion object {
        val REBIND_DELAY = 3.seconds
        val MAX_REBIND_INTERVAL = 5
        val RECHECK_INTERVAL = 30.seconds
        val BIND_TIMEOUT = 10.seconds
        val UNBIND_TIMEOUT = 10.seconds
    }

    // bind rejected by a router: carries the session so the bind loop can discard
    // it and fetch a fresh one on the next cycle
    private class BindRejected(val session: Session, message: String): IOException(message)
}
