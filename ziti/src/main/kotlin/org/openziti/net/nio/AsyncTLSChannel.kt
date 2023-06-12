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

package org.openziti.net.nio

import kotlinx.coroutines.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import org.openziti.util.EMPTY
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import org.openziti.util.transferTo
import java.io.EOFException
import java.io.IOException
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
import javax.net.ssl.*
import javax.net.ssl.SSLEngineResult.HandshakeStatus.*
import javax.net.ssl.SSLEngineResult.Status.*
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

/**
 * Implementation TLS stream conforming to [AsynchronousSocketChannel].
 *
 */
@Suppress("BlockingMethodInNonBlockingContext")
class AsyncTLSChannel(
    ch: AsynchronousSocketChannel,
    val ssl: SSLContext,
    val group: AsynchronousChannelGroup,
    private val id: Int = counter.incrementAndGet()
) : AsynchronousSocketChannel(group.provider()), Logged by ZitiLog("async-tls/${id}"), CoroutineScope {

    override val coroutineContext: CoroutineContext

    companion object {
        internal const val SSL_BUFFER_SIZE = 32 * 1024
        fun open(group: AsynchronousChannelGroup? = null) = Provider.openAsynchronousSocketChannel(group)
        internal val counter = AtomicInteger()
    }

    open class Group(val provider: AsynchronousChannelProvider, val dispatcher: CoroutineDispatcher) :
        AsynchronousChannelGroup(provider), CoroutineScope {

        override val coroutineContext: CoroutineContext = SupervisorJob() + dispatcher + CoroutineName("tls-group")
        private val channels = ConcurrentHashMap<Int,AsyncTLSChannel>()
        private var shut = false
        override fun shutdown() { shut = true }
        override fun isShutdown(): Boolean = shut

        override fun shutdownNow() {
            shutdown()
            coroutineContext.cancelChildren()
            coroutineContext.cancel()
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean = runBlocking {
            select {
                coroutineContext.job.onJoin { true }
                onTimeout(unit.toMillis(timeout)) { false }
            }
        }

        override fun isTerminated(): Boolean = dispatcher.isActive

        internal fun registerChannel(ch: AsyncTLSChannel) {
            channels[ch.id()] = ch
        }

        internal fun unregisterChannel(ch: AsyncTLSChannel) {
            channels.remove(ch.id())
        }
    }

    private object DefaultGroup: Group(Provider, Dispatchers.IO)

    object Provider : AsynchronousChannelProvider() {
        override fun openAsynchronousSocketChannel(group: AsynchronousChannelGroup?): AsynchronousSocketChannel {
            return AsyncTLSChannel(AsynchronousSocketChannel.open(), SSLContext.getDefault(), group ?: DefaultGroup)
        }

        override fun openAsynchronousServerSocketChannel(group: AsynchronousChannelGroup?): AsynchronousServerSocketChannel {
            error("server channels are not supported")
        }

        override fun openAsynchronousChannelGroup(
            nThreads: Int,
            threadFactory: ThreadFactory
        ): AsynchronousChannelGroup = Group(this, Executors.newFixedThreadPool(nThreads, threadFactory).asCoroutineDispatcher())

        override fun openAsynchronousChannelGroup(
            executor: ExecutorService,
            initialSize: Int
        ): AsynchronousChannelGroup = Group(this, executor.asCoroutineDispatcher())
    }

    constructor(ch: AsynchronousSocketChannel, ssl: SSLContext) : this(ch, ssl, DefaultGroup)
    constructor(ssl: SSLContext) : this(AsynchronousSocketChannel.open(), ssl)

    enum class State {
        initial,
        connecting,
        handshaking,
        connected,
        closed
    }

    internal fun id() = id

    internal val handshake = CompletableDeferred<SSLSession>()
    private var state: State by Delegates.observable(State.initial) { _, ov, nv ->
        d { "transitioning [$ov -> $nv] " }
    }

    internal val transport: AsynchronousSocketChannel = ch
    internal val sslParams = ssl.defaultSSLParameters
    internal val engine: SSLEngine = ssl.createSSLEngine()

    private val plainIn = Channel<ByteBuffer>(1)
    private val plainReadBuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val readOp = AtomicBoolean(false)
    private val writeOp = AtomicBoolean(false)
    private val closeWrite = AtomicBoolean(false)

    init {
        require(group is Group){"group is not AsyncTLSChannel.Group"}
        coroutineContext = SupervisorJob(group.coroutineContext.job) + group.dispatcher + CoroutineName("tls-$id")

        coroutineContext.job.invokeOnCompletion {
            group.unregisterChannel(this)
        }

        group.registerChannel(this)
        transport.remoteAddress?.let { state = State.connecting }
        handshake.invokeOnCompletion { ex ->
            if (ex == null) {
                d {"handshake completed"}
                state = State.connected
            } else {
                w {"handshake failed ${ex}"}
                runCatching { transport.close() }
                state = State.closed
            }
        }
    }

    private suspend fun postConnect() {
        if (state == State.connecting) {
            state = State.handshaking

            val addr = transport.remoteAddress as InetSocketAddress
            sslParams.serverNames = listOf(SNIHostName(addr.hostString))
            engine.sslParameters = sslParams
            engine.useClientMode = true
            engine.beginHandshake()

            readInternal()
            d { "engine status = ${engine.handshakeStatus}" }
            continueHS(EMPTY)
        }
    }

     override fun <A : Any?> connect(remote: SocketAddress, attachment: A, handler: CompletionHandler<Void, in A>?) {
        requireNotNull(handler)

        connect(remote).handle { v, ex ->
            if (ex != null) {
                handler.failed(ex, attachment)
            } else {
                handler.completed(v, attachment)
            }
        }
    }

    override fun connect(remote: SocketAddress): CompletableFuture<Void?> {
        checkState(State.initial)

        if (remote !is InetSocketAddress) {
            return CompletableFuture<Void?>().apply {
                completeExceptionally(IllegalArgumentException(remote.toString()))
            }        }

        return async {
            transport.connectSuspend(remote)
            state = State.connecting
            null
        }.asCompletableFuture()
        // return result
    }

    override fun getLocalAddress(): SocketAddress = transport.localAddress

    override fun getRemoteAddress(): SocketAddress = transport.remoteAddress

    override fun <T : Any?> getOption(name: SocketOption<T>?): T = transport.getOption(name)

    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): AsynchronousSocketChannel {
        transport.setOption(name, value)
        return this
    }

    override fun <A : Any?> write(
        src: ByteBuffer,
        timeout: Long,
        unit: TimeUnit?,
        attachment: A,
        handler: CompletionHandler<Int, in A>
    ) {
        write(arrayOf(src), 0, 1, timeout, unit, attachment, object : CompletionHandler<Long, A> {
            override fun completed(result: Long, a: A) = handler.completed(result.toInt(), a)
            override fun failed(exc: Throwable, a: A) = handler.failed(exc, a)
        })
    }

    override fun write(src: ByteBuffer): Future<Int> = CompletableFuture<Int>().also {
        write(src, 0, TimeUnit.SECONDS, it, FutureHandler())
    }

    override fun <A : Any?> write(
        srcs: Array<out ByteBuffer>,
        offset: Int,
        length: Int,
        timeout: Long,
        unit: TimeUnit?,
        attachment: A,
        handler: CompletionHandler<Long, in A>?
    ) {
        requireNotNull(handler) { "handler is required" }
        checkState(State.connecting, State.handshaking, State.connected)

        if (closeWrite.get()) {
            v { "cannot write after shutdownOutput() was called" }
            throw ClosedChannelException()
        }

        if (!writeOp.compareAndSet(false, true)) {
            throw WritePendingException()
        }

        launch {
            runCatching {
                postConnect()
                handshake.await()
                val sslbuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)
                val res = engine.wrap(srcs, offset, length, sslbuf)

                val consumed = res.bytesConsumed()
                val produced = res.bytesProduced()
                if (produced > 0) {
                    sslbuf.flip()
                    if (transport.writeCompletely(sslbuf) < produced)
                        throw IOException("failed to write complete SSL message")
                }
                v { "ssl state $res" }
                consumed
            }.onSuccess {
                writeOp.set(false)
                handler.completed(it.toLong(), attachment)
            }.onFailure {
                writeOp.set(false)
                val ex = when(it) {
                    is CancellationException -> AsynchronousCloseException()
                    else -> it
                }
                handler.failed(ex, attachment)
            }
        }
    }

    override fun isOpen(): Boolean = (state != State.closed)

    override fun bind(local: SocketAddress?): AsynchronousSocketChannel {
        checkState(State.initial)
        transport.bind(local)
        return this
    }

    override fun supportedOptions(): MutableSet<SocketOption<*>> = transport.supportedOptions()

    override fun shutdownInput(): AsynchronousSocketChannel {
        transport.shutdownInput()
        return this
    }

    override fun close() {
        if (state != State.closed && state != State.initial) {
            shutdownOutput()
            shutdownInput()
        }
        state = State.closed
        cancel()
    }

    override fun shutdownOutput(): AsynchronousSocketChannel {
        when (state) {
            State.initial -> throw NotYetConnectedException()
            State.connecting,
            State.handshaking,
            State.connected -> {}
            State.closed -> throw ClosedChannelException()
        }

        if (!closeWrite.compareAndSet(false, true)) {
            throw ClosedChannelException()
        }

        v { "closing outbound" }
        launch {
            runCatching {
                if (state == State.handshaking || state == State.connected) {
                    engine.closeOutbound()
                    val b = ByteBuffer.allocate(128)
                    engine.wrap(EMPTY, b)
                    transport.writeCompletely(b)
                }
                transport.shutdownOutput()
            }.onFailure {
                v { "failed to write SSLCloseNotify: $it" }
            }
        }

        return this
    }

    override fun <A> read(
        dst: ByteBuffer, timeout: Long, unit: TimeUnit,
        attachment: A, handler: CompletionHandler<Int, in A>
    ) {
        read(arrayOf(dst), 0, 1, timeout, unit, attachment, object : CompletionHandler<Long, A> {
            override fun completed(result: Long, attachment: A) = handler.completed(result.toInt(), attachment)
            override fun failed(exc: Throwable, attachment: A) = handler.failed(exc, attachment)
        })
    }

    override fun read(dst: ByteBuffer): Future<Int> = CompletableFuture<Int>().also {
        read(dst, 0, TimeUnit.MILLISECONDS, it, FutureHandler())
    }

    override fun <A : Any?> read(
        _dsts: Array<out ByteBuffer>,
        offset: Int,
        length: Int,
        timeout: Long,
        unit: TimeUnit,
        attachment: A,
        handler: CompletionHandler<Long, in A>?
    ) {
        requireNotNull(handler)
        checkState(State.connecting, State.handshaking, State.connected)

        if ((offset < 0) || (length < 0) || (offset > _dsts.size - length)) {
            throw IndexOutOfBoundsException()
        }

        if (!readOp.compareAndSet(false, true)) {
            throw ReadPendingException()
        }

        val dsts = _dsts.sliceArray(offset until offset + length)

        if (plainReadBuf.hasRemaining()) {
            launch {
                val count = plainReadBuf.transferTo(dsts)
                readOp.set(false)
                handler.completed(count, attachment)
            }
            return
        }

        launch {
            try {
                postConnect()
                handshake.await()

                val inFlow = plainIn.receiveAsFlow()
                val inBuf = if (timeout > 0)
                    withTimeout(timeout) { inFlow.firstOrNull() }
                else
                    inFlow.firstOrNull()

                if (inBuf != null) {
                    val len = inBuf.transferTo(dsts)
                    if (inBuf.hasRemaining()) {
                        plainReadBuf.compact()
                        plainReadBuf.put(inBuf)
                        plainReadBuf.flip()
                    }
                    readOp.set(false)
                    handler.completed(len, attachment)
                } else {
                    readOp.set(false)
                    handler.completed(-1, attachment)
                }
            } catch (ex: Throwable) {
                readOp.set(false)
                when (ex) {
                    is TimeoutCancellationException -> handler.failed(InterruptedByTimeoutException(), attachment)
                    is CancellationException -> handler.failed(AsynchronousCloseException(), attachment)
                    else -> handler.failed(ex, attachment)
                }
            } finally {
                readOp.set(false)
            }
        }
    }

    internal fun getSession(): SSLSession = runBlocking {
        handshake.await()
    }

    internal fun startHandshake() {
        if (state == State.connecting) {
            launch(CoroutineName("postConnect")) {
                postConnect()
            }
        }
    }

    fun setSSLParameters(params: SSLParameters) {
        sslParams.applicationProtocols = params.applicationProtocols
        sslParams.protocols = params.protocols
        sslParams.cipherSuites = params.cipherSuites
        sslParams.serverNames = params.serverNames
        sslParams.algorithmConstraints = params.algorithmConstraints
        sslParams.needClientAuth = params.needClientAuth
        sslParams.useCipherSuitesOrder = params.useCipherSuitesOrder
        sslParams.sniMatchers = params.sniMatchers
    }

    fun setEnabledProtocols(protocols: Array<out String>?) { sslParams.protocols = protocols }
    fun getEnabledProtocols(): Array<String>? = sslParams.protocols

    fun setEnabledCipherSuites(suites: Array<String>) { sslParams.cipherSuites = suites }
    fun getEnabledCipherSuites(): Array<String>? = sslParams.cipherSuites

    fun getSupportedCipherSuites(): Array<String>? = engine.supportedCipherSuites
    fun getSupportedProtocols(): Array<String>? = engine.supportedProtocols
    fun getApplicationProtocol(): String? = engine.applicationProtocol

    private fun checkState(vararg expected: State) {
        if (state in expected) return

        e { "invalid stata[$state], expected on of $expected" }
        when (state) {
            State.closed -> throw ClosedChannelException()
            State.connecting, State.handshaking -> throw ConnectionPendingException()
            State.connected -> throw AlreadyConnectedException()
            State.initial -> throw NotYetConnectedException()
        }
    }

    private fun readInternal() = launch {
        v("start reading loop()")
        val sslInBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE * 2).apply { flip() }
        var plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
        var res: SSLEngineResult
        try {
            do {
                sslInBuf.compact()
                val read = transport.readSuspend(sslInBuf)
                if (read < 0) break
                sslInBuf.flip()

                do {
                    res = engine.unwrap(sslInBuf, plainBuf)
                    if (res.status == OK) {
                        when (res.handshakeStatus) {
                            NOT_HANDSHAKING, FINISHED -> {
                                if (!handshake.isCompleted) handshake.complete(engine.session)
                            }
                            else -> continueHS(sslInBuf)
                        }
                    } else if (res.status == BUFFER_OVERFLOW) {
                        if (plainBuf.position() > 0) {
                            plainBuf.flip()
                            plainIn.send(plainBuf)
                            plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
                        }
                    }
                } while (sslInBuf.hasRemaining() && res.status in arrayOf(OK, BUFFER_OVERFLOW))

                if (plainBuf.position() > 0) {
                    plainBuf.flip()
                    plainIn.send(plainBuf)
                    plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
                }
            } while (res.status != CLOSED)

            v("reader closed")
            plainIn.close()
        } catch (ex: Exception) {
            handshake.completeExceptionally(ex)
            state = State.closed
            if (ex is SSLException && ex.cause is EOFException) {
                v("closed due to close_notify")
                plainIn.close()
            } else {
                plainIn.close(ex)
            }
            transport.close()
        }
    }

    private suspend fun continueHS(inBuf: ByteBuffer) {
        val hsBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)
        while (true) {
            v{ "engine.handshakeStatus = ${engine.handshakeStatus}" }
            when (engine.handshakeStatus!!) {
                NEED_TASK ->
                    runCatching { CompletableFuture.supplyAsync { engine.delegatedTask?.run() }.await() }
                        .onFailure {
                            handshake.completeExceptionally(it)
                            return@continueHS
                        }

                NEED_WRAP -> {
                    while (engine.handshakeStatus == NEED_WRAP) {
                        val res = engine.wrap(EMPTY, hsBuf)
                        t{"wrap: $res buf[$hsBuf]"}
                    }

                    hsBuf.flip()
                    kotlin.runCatching {
                        transport.writeCompletely(hsBuf)
                        hsBuf.clear()
                    }.onFailure {
                        handshake.completeExceptionally(it)
                        return@continueHS
                    }
                }
                NEED_UNWRAP -> {
                    while (engine.handshakeStatus == NEED_UNWRAP) {
                        val res = engine.unwrap(inBuf, hsBuf)
                        t{"unwrap: $res"}
                        if (res.status == BUFFER_UNDERFLOW)
                            return
                    }
                }
                FINISHED, NOT_HANDSHAKING -> {
                    if (!handshake.isCompleted) handshake.complete(engine.session)
                    return
                }
                else -> {}
            }
        }
    }
}