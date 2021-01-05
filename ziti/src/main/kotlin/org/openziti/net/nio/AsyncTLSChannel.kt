/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.future.await
import org.openziti.util.*
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
import kotlin.properties.Delegates

/**
 * Implementation TLS stream conforming to [AsynchronousSocketChannel].
 *
 */
class AsyncTLSChannel(
    ch: AsynchronousSocketChannel,
    val ssl: SSLContext,
    provider: AsynchronousChannelProvider
) : AsynchronousSocketChannel(provider), Logged by ZitiLog("async-tls/${counter.incrementAndGet()}") {

    companion object {
        const val SSL_BUFFER_SIZE = 32 * 1024
        fun open(group: AsynchronousChannelGroup? = null) = Provider.openAsynchronousSocketChannel(group)
        val counter = AtomicInteger()
    }

    class Group(p: AsynchronousChannelProvider) : AsynchronousChannelGroup(p) {
        override fun shutdown() {}
        override fun isShutdown(): Boolean = false

        override fun shutdownNow() {}
        override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
            return false
        }

        override fun isTerminated(): Boolean = false
    }

    object Provider : AsynchronousChannelProvider() {
        override fun openAsynchronousSocketChannel(group: AsynchronousChannelGroup?): AsynchronousSocketChannel {
            val provider = group?.provider() ?: this
            return AsyncTLSChannel(AsynchronousSocketChannel.open(), SSLContext.getDefault(), provider)
        }

        override fun openAsynchronousServerSocketChannel(group: AsynchronousChannelGroup?): AsynchronousServerSocketChannel {
            error("server channels are not supported")
        }

        override fun openAsynchronousChannelGroup(
            nThreads: Int,
            threadFactory: ThreadFactory?
        ): AsynchronousChannelGroup = Group(this)

        override fun openAsynchronousChannelGroup(
            executor: ExecutorService?,
            initialSize: Int
        ): AsynchronousChannelGroup = Group(this)
    }

    constructor(ch: AsynchronousSocketChannel, ssl: SSLContext) : this(ch, ssl, Provider)
    constructor(ssl: SSLContext) : this(AsynchronousSocketChannel.open(), ssl)

    enum class State {
        initial,
        connecting,
        handshaking,
        connected,
        closed
    }

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
        transport.remoteAddress?.let { state = State.connecting }
    }

    private suspend fun postConnect() {
        if (state == State.connecting) {
            state = State.handshaking

            val addr = transport.remoteAddress as InetSocketAddress
            sslParams.serverNames = listOf(SNIHostName(addr.hostName))
            engine.sslParameters = sslParams
            engine.useClientMode = true
            engine.beginHandshake()

            readInternal()
            d { "engine status = ${engine.handshakeStatus}" }
            continueHS(EMPTY)
        }
    }

    override fun <A : Any?> connect(remote: SocketAddress, attachment: A, handler: CompletionHandler<Void?, in A>?) {
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
            }
        }

        val result = CompletableFuture<Void?>()
        GlobalScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                transport.connectSuspend(remote)
                state = State.connecting
            }.onSuccess {
                result.complete(null)
            }.onFailure {
                result.completeExceptionally(it)
            }
        }
        return result
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
            e { "cannot write after shutdownOutput() was called" }
            throw ClosedChannelException()
        }

        if (!writeOp.compareAndSet(false, true)) {
            throw WritePendingException()
        }

        GlobalScope.launch(Dispatchers.IO) {
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
                handler.failed(it, attachment)
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
        shutdownOutput()
        shutdownInput()
        state = State.closed
    }

    override fun shutdownOutput(): AsynchronousSocketChannel {
        when (state) {
            State.initial,
            State.connecting,
            State.handshaking -> throw NotYetConnectedException()
            State.connected -> {}
            State.closed -> throw ClosedChannelException()
        }

        if (!closeWrite.compareAndSet(false, true)) {
            throw ClosedChannelException()
        }

        d { "closing outbound" }
        GlobalScope.launch(Dispatchers.IO) {
            runCatching {
                engine.closeOutbound()
                val b = ByteBuffer.allocate(128)
                engine.wrap(EMPTY, b)
                transport.writeCompletely(b)
            }.onFailure {
                w { "failed to write SSLCloseNotify: $it" }
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
            GlobalScope.launch(Dispatchers.IO) {
                val count = plainReadBuf.transfer(dsts)
                readOp.set(false)
                handler.completed(count, attachment)
            }
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            postConnect()
            handshake.await()

            try {
                val inFlow = plainIn.receiveAsFlow()
                val inBuf = if (timeout > 0)
                    withTimeout(timeout) { inFlow.firstOrNull() }
                else
                    inFlow.firstOrNull()

                if (inBuf != null) {
                    val len = inBuf.transfer(dsts)
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
                e(ex) { "exception" }
                readOp.set(false)
                when (ex) {
                    is TimeoutCancellationException -> handler.failed(InterruptedByTimeoutException(), attachment)
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
            GlobalScope.launch(Dispatchers.IO) {
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

    private fun readInternal() = GlobalScope.launch(Dispatchers.IO) {
        d("start reading loop()")
        kotlin.runCatching {
            val sslInBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE * 2).apply { flip() }
            var plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
            var res: SSLEngineResult
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
        }.onFailure {
            e("reader closed", it)
            if (!handshake.isCompleted) handshake.completeExceptionally(it)
            state = State.closed
            plainIn.close(it)
            transport.close()
        }.onSuccess {
            w { "reader closed" }
            plainIn.close()
        }
    }

    private suspend fun continueHS(inBuf: ByteBuffer) {
        val hsBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)
        while (true) {
            d { "engine.handshakeStatus = ${engine.handshakeStatus}" }
            when (engine.handshakeStatus!!) {
                NEED_TASK -> CompletableFuture.supplyAsync { engine.delegatedTask.run() }.await()
                NEED_WRAP -> {
                    while (engine.handshakeStatus == NEED_WRAP) {
                        engine.wrap(EMPTY, hsBuf)
                    }

                    hsBuf.flip()
                    d { "hsbuf = $hsBuf" }
                    transport.writeSuspend(hsBuf)
                }
                NEED_UNWRAP, NEED_UNWRAP_AGAIN -> {
                    while (engine.handshakeStatus == NEED_UNWRAP) {
                        if (engine.unwrap(inBuf, hsBuf).status == BUFFER_UNDERFLOW)
                            return
                    }
                }
                FINISHED, NOT_HANDSHAKING -> {
                    if (!handshake.isCompleted) handshake.complete(engine.session)
                    state = State.connected
                    return
                }
            }
        }
    }
}