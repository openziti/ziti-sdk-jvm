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

import com.google.gson.Gson
import com.goterl.lazysodium.utils.Key
import com.goterl.lazysodium.utils.KeyPair
import com.goterl.lazysodium.utils.SessionPair
import kotlinx.coroutines.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import org.openziti.Errors
import org.openziti.ZitiAddress
import org.openziti.ZitiConnection
import org.openziti.ZitiException
import org.openziti.api.Session
import org.openziti.api.SessionType
import org.openziti.crypto.Crypto
import org.openziti.impl.ZitiContextImpl
import org.openziti.net.ZitiProtocol.CryptoMethod
import org.openziti.net.ZitiProtocol.Header
import org.openziti.net.nio.FutureHandler
import org.openziti.net.nio.readSuspend
import org.openziti.net.nio.writeCompletely
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.ByteArrayOutputStream
import java.io.Externalizable
import java.io.IOException
import java.io.ObjectOutputStream
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.SocketOption
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.CompletionHandler
import java.nio.channels.spi.AsynchronousChannelProvider
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.channels.Channel as Chan

internal class ZitiSocketChannel private constructor(internal val ctx: ZitiContextImpl, val connId: Int):
    AsynchronousSocketChannel(Provider),
    Channel.MessageReceiver,
    ZitiConnection,
    InputChannel<ZitiSocketChannel>,
    CoroutineScope by ctx,
    Logged by ZitiLog("ziti-conn[${ctx.name()}/${connId}]") {

    constructor(ztx:ZitiContextImpl): this(ztx, ztx.nextConnId())

    object Provider: AsynchronousChannelProvider() {
        override fun openAsynchronousSocketChannel(group: AsynchronousChannelGroup?): AsynchronousSocketChannel =
            TODO()

        override fun openAsynchronousServerSocketChannel(group: AsynchronousChannelGroup?): AsynchronousServerSocketChannel {
            TODO("Not yet implemented")
        }

        override fun openAsynchronousChannelGroup(nThreads: Int, threadFactory: ThreadFactory?): AsynchronousChannelGroup =
            TODO("Not yet implemented")

        override fun openAsynchronousChannelGroup(executor: ExecutorService?, initialSize: Int): AsynchronousChannelGroup =
            TODO("Not yet implemented")

    }

    internal enum class State {
        initial,
        connecting,
        connected,
        closed
    }
    val sentFin = AtomicBoolean(false)

    override var timeout: Long = 0

    val state = AtomicReference(State.initial)
    val channel = CompletableDeferred<Channel>()

    val seq = AtomicInteger(1)
    lateinit var serviceName: String
    var remote: SocketAddress? = null
    var local: ZitiAddress? = null
    val receiveQueue = Chan<ByteArray>(16)
    override val inputSupport = InputChannel.InputSupport(receiveQueue)
    val crypto = CompletableDeferred<Crypto.SecretStream?>()

    override fun getLocalAddress(): SocketAddress? = local

    override fun getRemoteAddress(): SocketAddress? = remote

    override fun supportedOptions(): MutableSet<SocketOption<*>> = mutableSetOf()
    override fun <T : Any> getOption(name: SocketOption<T>?): T? = null
    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): AsynchronousSocketChannel = this

    override fun isClosed() = !isOpen
    override fun isConnected() = isOpen && state.get() != State.initial

    private var connectOp: Job? = null
    private var writeOp: Job? = null
    private val writeMutex = Mutex()

    internal suspend fun <A: Any?> connectInternal(addr: ZitiAddress.Dial, attachment: A, handler: CompletionHandler<Void, in A>) {
        d{"connecting to $serviceName"}

        val (service,ns) = ctx.runCatching {
            val service = getService(serviceName) ?: throw ZitiException(Errors.ServiceNotAvailable)
            val ns = getNetworkSession(service, SessionType.DIAL)
            service to ns
        }.getOrElse {
            w{"failed to connect: $it"}
            channel.completeExceptionally(it)
            close()
            handler.failed(it, attachment)
            return
        }

        d{"using session[${ns.id}]"}
        val ch = ctx.runCatching { getChannel(ns) }.getOrElse {
            w{"failed to connect: $it"}
            channel.completeExceptionally(it)
            close()
            handler.failed(it, attachment)
            return
        }

        d{"using ch[$ch]"}
        channel.complete(ch)
        ch.registerReceiver(connId, this)
        val kp = if (service.encryptionRequired) Crypto.newKeyPair() else null
        runCatching { doZitiHandshake(ch, addr, ns, kp) }.onFailure {
            if (it is CancellationException) {
                d{"connect was canceled"}
                handler.failed(AsynchronousCloseException(), attachment)
            } else {
                w{"failed to connect: $it"}
                close()
                handler.failed(it, attachment)
            }
        }.onSuccess {
            handler.completed(null, attachment)
        }
    }


    override fun <A : Any?> connect(remote: SocketAddress, attachment: A, handler: CompletionHandler<Void, in A>) {

        ctx.isEnabled() || throw ShutdownChannelGroupException()

        state.getAndUpdate { st ->
            when(st) {
                State.initial -> {}
                State.connecting -> throw ConnectionPendingException()
                State.connected -> throw AlreadyConnectedException()
                State.closed -> throw ClosedChannelException()
                null -> error("not possible")
            }

            State.connecting
        }

        d{"connecting to $remote"}
        val addr = when (remote) {
            is InetSocketAddress -> ctx.getDialAddress(remote, Protocol.TCP) ?: throw UnresolvedAddressException()
            is ZitiAddress.Dial -> remote
            else -> throw UnsupportedAddressTypeException()
        }

        serviceName = addr.service

        val conOp = ctx.launch { connectInternal(addr, attachment, handler) }
        conOp.invokeOnCompletion { ex ->
            if (ex != null)
                e{" failed to connect: $ex"}
            else
                d{"connected"}
        }
        connectOp = conOp
    }

    override fun connect(remote: SocketAddress): Future<Void> {
        val result = CompletableFuture<Void>()
        connect(remote, result, FutureHandler())
        return result
    }

    override fun isOpen(): Boolean = state.get() != State.closed

    override fun bind(local: SocketAddress?): AsynchronousSocketChannel {
        return this
    }

    override fun shutdownInput(): ZitiSocketChannel {
        when(state.get()) {
            State.connecting, State.connected -> deregister()
            else -> {}
        }
        return super.shutdownInput()
    }

    private fun deregister() {
        ctx.launch {
            channel.runCatching {
                await().deregisterReceiver(connId)
            }
        }
    }

    override fun close() {
        connectOp?.cancel("close")
        writeOp?.cancel("close")

        deregister()
        super.close()

        runCatching { shutdownOutput() }
        runCatching { closeInternal() }
    }

    override fun shutdownOutput(): AsynchronousSocketChannel {
        if (state.get() == State.connected && sentFin.compareAndSet(false, true)) {
            val finMsg = Message(ZitiProtocol.ContentType.Data).apply {
                setHeader(Header.ConnId, connId)
                setHeader(Header.FlagsHeader, ZitiProtocol.EdgeFlags.FIN)
                setHeader(Header.SeqHeader, seq.getAndIncrement())
            }
            d("sending FIN")

            ctx.async {
                val ch = channel.getCompleted()
                ch.SendSynch(finMsg)
            }.invokeOnCompletion { ex ->
                ex.takeIf { it !is CancellationException }?.let { e ->
                    w{ "failed to send FIN message: $e" }
                }
            }
        }
        return this
    }

    internal fun closeInternal(): AsynchronousSocketChannel {
        synchronized(state) {
            when (state.get()) {
                State.initial ->
                    state.set(State.closed)
                State.connecting, State.connected -> {
                    val closeMsg = Message(ZitiProtocol.ContentType.StateClosed).apply {
                        setHeader(Header.ConnId, connId)
                    }
                    d("closing conn = ${this.connId}")
                    ctx.async {
                        val ch = channel.getCompleted()
                        ch.SendSynch(closeMsg)
                    }.invokeOnCompletion {
                        it.takeIf { it !is CancellationException }?.let {
                            w { "failed to send StateClosed message: ${it.localizedMessage}" }
                        }
                    }
                    state.set(State.closed)
                }
                State.closed -> {}
                else -> {}
            }
            ctx.close(this)
        }
        return this
    }

    override
    fun <A : Any?> read(
        dst: ByteBuffer, timeout: Long, unit: TimeUnit,
        att: A, handler: CompletionHandler<Int, in A>
    ) = super<InputChannel>.read(dst, timeout, unit, att, handler)

    override fun read(dst: ByteBuffer): Future<Int> = super<InputChannel>.read(dst)

    override fun <A : Any?> read(
        dsts: Array<out ByteBuffer>, offset: Int, length: Int,
        to: Long, unit: TimeUnit, att: A, handler: CompletionHandler<Long, in A>
    ) = super<InputChannel>.read(dsts, offset, length, to, unit, att, handler)

    override
    fun <A : Any?> write(src: ByteBuffer, to: Long, unit: TimeUnit?, att: A, handler: CompletionHandler<Int, in A>) {
        write(arrayOf(src), 0, 1, to, unit, att, object : CompletionHandler<Long, A>{
            override fun completed(result: Long, a: A): Unit = handler.completed(result.toInt(), a)
            override fun failed(exc: Throwable, a: A) = handler.failed(exc, a)
        })
    }

    override fun write(src: ByteBuffer): Future<Int> {
        val result = CompletableFuture<Int>()
        write(src, result, FutureHandler())
        return result
    }

    override fun <A : Any?> write(
        _srcs: Array<out ByteBuffer>, offset: Int, length: Int,
        timeout: Long, unit: TimeUnit?,
        att: A, handler: CompletionHandler<Long, in A>
    ) {
        when (state.get()) {
            State.initial,
            State.connecting -> throw NotYetConnectedException()
            State.connected -> {}
            State.closed -> throw ClosedChannelException()
            else -> error("should not be here")
        }

        channel.isCompleted || throw NotYetConnectedException()

        writeMutex.tryLock() || throw WritePendingException()

        val srcs = _srcs.slice(offset until offset + length)

        val wop = ctx.async {
            var sent = 0L
            for (b in srcs) {
                var data = ByteArray(b.remaining())
                b.get(data)

                crypto.getCompleted()?.let {
                    data = it.encrypt(data)
                }

                val dataMessage = Message(ZitiProtocol.ContentType.Data, data)
                dataMessage.setHeader(Header.ConnId, connId)
                dataMessage.setHeader(Header.SeqHeader, seq.getAndIncrement())
                sent += data.size
                v("sending $dataMessage")
                channel.await().Send(dataMessage)
            }
            sent
        }
        writeOp = wop

        wop.invokeOnCompletion { ex ->
            writeOp = null
            writeMutex.unlock()

            if (ex == null) {
                val sent = wop.getCompleted()
                handler.completed(sent, att)
            } else if (ex is TimeoutCancellationException) {
                handler.failed(InterruptedByTimeoutException(), att)
            } else if (ex is CancellationException) {
                handler.failed(AsynchronousCloseException(), att)
            } else {
                handler.failed(ex, att)
            }
        }
    }

    override suspend fun receive(msg: Result<Message>) {
        msg.onSuccess {
            receiveMsg(it)
        }.onFailure {
            close()
        }
    }

    private suspend fun receiveMsg(msg: Message) {
        v{"conn[$connId] received message[${msg.content}] with seq[${msg.getIntHeader(Header.SeqHeader)}]"}
        when (msg.content) {
            ZitiProtocol.ContentType.StateClosed -> {
                t{"signaling EOF"}
                receiveQueue.close()
                deregister()
                close()
            }
            ZitiProtocol.ContentType.Data -> {
                t{"received data(${msg.body.size} bytes) for conn[$connId]"}
                if (msg.body.size > 0) {
                    val crypt = crypto.await()
                    if (crypt != null) {
                        if (crypt.initialized()) {
                            receiveQueue.send(crypt.decrypt(msg.body))
                        } else {
                            crypt.init(msg.body)
                            d { "crypto init finished conn[$connId]" }
                        }
                    } else {
                        receiveQueue.send(msg.body)
                    }
                }
                msg.getIntHeader(Header.FlagsHeader)?.let {
                    if (it and ZitiProtocol.EdgeFlags.FIN != 0 ) {
                        d("received FIN")
                        receiveQueue.close()
                    }
                }
            }
            else -> {
                e{"unexpected message type[${msg.content}] for conn[$connId]"}
                receiveQueue.close(IllegalStateException())
                deregister()
                close()
            }
        }
    }

    override suspend fun send(data: ByteArray) = send(data, 0, data.size)

    suspend fun send(data: ByteArray, offset: Int, len: Int) {
        writeCompletely(ByteBuffer.wrap(data, offset, len))
    }

    override suspend fun receive(out: ByteArray, off: Int, len: Int): Int {
        val dst = ByteBuffer.wrap(out, off, len)
        return try {
            readSuspend(dst, timeout, TimeUnit.MILLISECONDS)
        } catch (ex: TimeoutCancellationException) {
            0
        }
    }

    internal suspend fun doZitiHandshake(ch: Channel, remote: ZitiAddress.Dial, ns: Session, kp: KeyPair?) {
        val connectMsg = Message(ZitiProtocol.ContentType.Connect, ns.token.toByteArray(UTF_8)).apply {
            setHeader(Header.ConnId, connId)
            setHeader(Header.SeqHeader, 0)
            kp?.let {
                setHeader(Header.PublicKeyHeader, it.publicKey.asBytes)
                setHeader(Header.CryptoMethodHeader, CryptoMethod.Libsodium)
            }

            remote.identity?.let {
                setHeader(Header.TerminatorIdentityHeader, it)
            }
            (remote.callerId ?: ctx.getId()?.name)?.let {
                    setHeader(Header.CallerIdHeader, it)
            }

            remote.appData?.let { obj ->
                val header = when(obj) {
                    is ByteArray -> obj
                    is String -> obj.toByteArray(UTF_8)
                    is Externalizable ->
                        runCatching {
                            val out = ByteArrayOutputStream()
                            obj.writeExternal(ObjectOutputStream(out))
                            out.toByteArray()
                        }.onFailure {
                            w { "failed to serialize provided app_data: ${it.localizedMessage}" }
                        }.getOrNull()
                    else -> Gson().toJson(obj).toByteArray(UTF_8)
                }
                header?.let { setHeader(Header.AppDataHeader, it) }
            }
        }

        d("starting network connection ${ns.id}/$connId")
        val reply = ch.SendAndWait(connectMsg)
        when (reply.content) {
            ZitiProtocol.ContentType.StateConnected -> {
                val peerPk = reply.getHeader(Header.PublicKeyHeader)
                if (kp == null || peerPk == null) {
                    crypto.complete(null)
                } else {
                    setupCrypto(Crypto.kx(kp, Key.fromBytes(peerPk), false))
                    startCrypto(ch)
                }

                local = ZitiAddress.Session(ns.id, serviceName, null, null)
                d("network connection established ${ns.id}/$connId")
                state.set(State.connected)
            }
            ZitiProtocol.ContentType.StateClosed -> {
                val err = reply.body.toString(UTF_8)
                w("connection rejected: $err")
                throw ConnectException(err)
            }
            else -> {
                throw IOException("Invalid response type")
            }
        }
    }

    internal fun setupCrypto(keys: SessionPair?) {
        crypto.complete(keys?.let { Crypto.newStream(it) })
    }

    internal suspend fun startCrypto(ch: Channel) {
        crypto.await()?.let {
            val header = it.header()
            val headerMessage = Message(ZitiProtocol.ContentType.Data, header)
                .setHeader(Header.ConnId, connId)
                .setHeader(Header.SeqHeader, seq.getAndIncrement())
            ch.Send(headerMessage)
        }
    }

    override fun toString(): String {
        val s = StringBuilder(this::class.java.simpleName)
        s.append("[$state]")
        when(state.get()) {
            State.connecting -> s.append("(remote=$remote)")
            State.connected -> s.append("($local -> $remote)")
            else -> {}
        }
        return s.toString()
    }
}