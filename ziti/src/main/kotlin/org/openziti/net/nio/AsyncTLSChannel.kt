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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.future.await
import org.openziti.util.*
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
import javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW
import javax.net.ssl.SSLEngineResult.Status.CLOSED
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
        const val POOL_SIZE = 4
        const val SSL_BUFFER_SIZE = 32 * 1024
        fun open(group: AsynchronousChannelGroup? = null) = Provider.openAsynchronousSocketChannel(group)
        val counter = AtomicInteger()
    }

    class Group(p: AsynchronousChannelProvider): AsynchronousChannelGroup(p) {
        override fun shutdown() {}
        override fun isShutdown(): Boolean = false

        override fun shutdownNow() {}
        override fun awaitTermination(timeout: Long, unit: TimeUnit?): Boolean {
            return false
        }
        override fun isTerminated(): Boolean = false
    }

    object Provider: AsynchronousChannelProvider() {
        override fun openAsynchronousSocketChannel(group: AsynchronousChannelGroup?): AsynchronousSocketChannel {
            val provider = group?.provider() ?: this
            return AsyncTLSChannel(AsynchronousSocketChannel.open(), SSLContext.getDefault(), provider)
        }
        override fun openAsynchronousServerSocketChannel(group: AsynchronousChannelGroup?): AsynchronousServerSocketChannel {
            error("server channels are not supported")
        }

        override fun openAsynchronousChannelGroup(nThreads: Int, threadFactory: ThreadFactory?): AsynchronousChannelGroup = Group(this)
        override fun openAsynchronousChannelGroup(executor: ExecutorService?, initialSize: Int): AsynchronousChannelGroup = Group(this)
    }



    constructor(ch: AsynchronousSocketChannel, ssl: SSLContext) : this(ch, ssl, Provider)
    constructor(ssl: SSLContext): this(AsynchronousSocketChannel.open(),ssl)

    enum class State {
        initial,
        connecting,
        handshaking,
        connected,
        closed
    }

    internal val handshake = CompletableDeferred<SSLSession>()
    private var state: State by Delegates.observable(State.initial) { _, ov, nv ->
        d{"transitioning [$ov -> $nv] "}
    }

    internal val transport: AsynchronousSocketChannel = ch
    internal val sslParams = ssl.defaultSSLParameters
    internal val engine: SSLEngine = ssl.createSSLEngine()

    private val sslPool = BufferPool(POOL_SIZE, SSL_BUFFER_SIZE)
    private val sslIn = Channel<ByteBuffer>(POOL_SIZE)
    private val sslbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val plnbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val readOp = AtomicBoolean(false)
    private val writeOp = AtomicBoolean(false)
    private val closeWrite = AtomicBoolean(false)

    private val reader: Reader = Reader(transport, sslIn, sslPool)

    private class Reader(
        val input: AsynchronousSocketChannel,
        val output: SendChannel<ByteBuffer>,
        val pool: BufferPool
    ) {

        fun startRead(): Job = GlobalScope.launch(Dispatchers.IO) {
            val buf = pool.get()
            try {
                val res = input.readSuspend(buf)
                if (res == -1) {
                    pool.put(buf)
                    output.close()
                } else if (res > 0) {
                    buf.flip()
                    output.send(buf)
                    startRead()
                } else {
                    throw IllegalStateException("unexpected read result($res)")
                }
            } catch(ex: Throwable) {
                pool.put(buf)
                output.close(ex)
            }
        }
    }

    init {
        transport.remoteAddress?.let { state = State.connecting }
    }

    internal suspend fun postConnect() {
        if (state == State.connecting) {
            val addr = transport.remoteAddress as InetSocketAddress
            sslParams.serverNames = listOf(SNIHostName(addr.hostName))
            engine.sslParameters = sslParams

            runCatching { doHandshake(true) }
                .onSuccess {
                    w { "connected with ALPN: `${engine.applicationProtocol}`" }
                    handshake.complete(it)
                    state = State.connected
                    reader.startRead()
                }
                .onFailure {
                    handshake.completeExceptionally(it)
                    state = State.closed
                    e(it) { "SSL handshake error" }
                }
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

        if (!(remote is InetSocketAddress)) {
            return CompletableFuture<Void?>().apply {
                completeExceptionally(IllegalArgumentException(remote.toString()))
            }
        }

        val result = CompletableFuture<Void?>()
        GlobalScope.launch (Dispatchers.IO) {
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
        write(arrayOf(src), 0, 1, timeout, unit, attachment, object : CompletionHandler<Long, A>{
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
        requireNotNull(handler){"handler is required"}
        checkState(State.connecting, State.connected)

        if (closeWrite.get()) {
            e{"cannot write after shutdownOutput() was called"}
            throw ClosedChannelException()
        }

        if (!writeOp.compareAndSet(false, true)) {
            throw WritePendingException()
        }

        GlobalScope.launch(Dispatchers.IO) {
            runCatching {
                if (state == State.connecting) {
                    postConnect()
                }
                val sslbuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)
                val res = engine.wrap(srcs, offset, length, sslbuf)

                val consumed = res.bytesConsumed()
                val produced = res.bytesProduced()
                if (produced > 0) {
                    sslbuf.flip()
                    if (transport.writeCompletely(sslbuf) < produced)
                        throw IOException("failed to write complete SSL message")
                }
                v{"ssl state $res"}
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

        d{"closing outbound"}
        GlobalScope.launch(Dispatchers.IO) {
            runCatching {
                engine.closeOutbound()
                val b = ByteBuffer.allocate(128)
                val res = engine.wrap(EMPTY, b)
                transport.writeCompletely(b)
            }.onFailure {
                w{"failed to write SSLCloseNotify: $it"}
            }
        }

        return this
    }

    override fun <A> read(dst: ByteBuffer, timeout: Long, unit: TimeUnit,
        attachment: A, handler: CompletionHandler<Int, in A>
    ) {
        read(arrayOf(dst), 0, 1, timeout, unit, attachment, object : CompletionHandler<Long, A>{
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
        checkState(State.connecting, State.connected)

        if ((offset < 0) || (length < 0) || (offset > _dsts.size - length)) {
            throw IndexOutOfBoundsException()
        }

        if (!readOp.compareAndSet(false, true)) {
            throw ReadPendingException()
        }

        val dsts = _dsts.sliceArray(offset until offset + length)
        GlobalScope.launch(Dispatchers.IO) {
            if (state == State.connecting) {
                postConnect()
            }

            var eof = false
            try {
                if (!plnbuf.hasRemaining()) {
                    plnbuf.clear()

                    // quick drain of ssl input
                    sslbuf.compact()
                    while (sslbuf.remaining() > SSL_BUFFER_SIZE) {
                        sslIn.poll()?.let {
                            sslbuf.put(it)
                            sslPool.put(it)
                        } ?: break
                    }
                    sslbuf.flip()

                    try {
                        var res = if (sslbuf.hasRemaining()) engine.unwrap(sslbuf, plnbuf) else null
                        var produced = res?.bytesProduced() ?: 0
                        while (produced == 0) {
                            val b = if (timeout > 0)
                                withTimeout(unit.toMillis(timeout)) { sslIn.receive() }
                            else
                                sslIn.receive()

                            sslbuf.compact()
                            sslbuf.put(b)
                            sslPool.put(b)
                            // try to drain again
                            while (sslbuf.remaining() > SSL_BUFFER_SIZE) {
                                sslIn.poll()?.let {
                                    sslbuf.put(it)
                                    sslPool.put(it)
                                } ?: break
                            }
                            sslbuf.flip()
                            res = engine.unwrap(sslbuf, plnbuf)
                            produced = res.bytesProduced()
                            if (res.status == CLOSED)
                                eof = true
                        }
                    } catch (closed: ClosedReceiveChannelException) {
                        eof = true
                    } catch (sslex: SSLException) {
                        if (sslex.cause is EOFException) {
                            eof = true
                        } else {
                            throw sslex
                        }
                    } finally {
                        plnbuf.flip()
                    }
                }

                if (plnbuf.hasRemaining()) {
                    val count = plnbuf.transfer(dsts)
                    v { "transferred ${count} decrypted bytes" }
                    readOp.set(false)
                    handler.completed(count, attachment)
                } else if (eof) {
                    readOp.set(false)
                    handler.completed(-1, attachment)
                }
            } catch (ex: Throwable) {
                e(ex){ "exception"}
                readOp.set(false)
                when(ex) {
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

    private suspend fun doHandshake(clientMode: Boolean): SSLSession {
        checkState(State.connecting)
        state = State.handshaking

        engine.useClientMode = clientMode
        engine.beginHandshake()

        v{"staring handshake loop"}

        val inBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
        inBuf.flip()
        val outBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)

        loop@
        while (true) {
            when (engine.handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING,
                SSLEngineResult.HandshakeStatus.FINISHED -> {
                    d { "handshake is complete" }
                    break@loop
                }

                SSLEngineResult.HandshakeStatus.NEED_TASK -> CompletableFuture.supplyAsync {
                        engine.delegatedTask?.run()
                    }.await()

                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    outBuf.clear()
                    do {
                        val res = engine.wrap(EMPTY, outBuf)
                    } while (res.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP)

                    outBuf.flip()
                    transport.writeCompletely(outBuf)
                }

                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {
                    outBuf.clear()
                    do {
                        val res = engine.unwrap(inBuf, outBuf)
                        if (res.status == BUFFER_UNDERFLOW) {
                            inBuf.compact()
                            transport.readSuspend(inBuf)
                            inBuf.flip()
                        }
                    } while (res.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP && inBuf.hasRemaining())
                    outBuf.flip()
                    transport.writeCompletely(outBuf)
                }

                else -> error("should not be here, handshakeStatus(${engine.handshakeStatus})")
            }
        }
        return engine.session
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
    fun getEnabledProtocols() = sslParams.protocols

    fun setEnabledCipherSuites(suites: Array<String>) { sslParams.cipherSuites = suites }
    fun getEnabledCipherSuites() = sslParams.cipherSuites

    fun getSupportedCipherSuites() = engine.supportedCipherSuites
    fun getSupportedProtocols() = engine.supportedProtocols
    fun getApplicationProtocol() = engine.applicationProtocol

    internal fun checkState(vararg expected: State) {
        if (state in expected) return

        when(state) {
            State.closed -> throw ClosedChannelException()
            State.connecting, State.handshaking -> throw ConnectionPendingException()
            State.connected -> throw AlreadyConnectedException()
            State.initial -> throw NotYetConnectedException()
        }
    }
}