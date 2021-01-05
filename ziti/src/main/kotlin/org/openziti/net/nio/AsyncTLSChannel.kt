/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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
import kotlinx.coroutines.channels.first
import kotlinx.coroutines.flow.*
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
    internal lateinit var engine: SSLEngine

    private val sslInBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE * 2)
    private val sslIn = Channel<ByteBuffer>(POOL_SIZE)
    private val plainIn = Channel<ByteBuffer>(1)
    private val sslbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val plnbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val readOp = AtomicBoolean(false)
    private val writeOp = AtomicBoolean(false)
    private val closeWrite = AtomicBoolean(false)

//    private val reader: Reader = Reader(transport, sslIn, sslPool)
//
//    private class Reader(
//        val input: AsynchronousSocketChannel,
//        val output: SendChannel<ByteBuffer>,
//        val pool: BufferPool
//    ) {
//
//        fun startRead(): Job = GlobalScope.launch(Dispatchers.IO) {
//            val buf = pool.get()
//            try {
//                val res = input.readSuspend(buf)
//                if (res == -1) {
//                    pool.put(buf)
//                    output.close()
//                } else if (res > 0) {
//                    buf.flip()
//                    output.send(buf)
//                    startRead()
//                } else {
//                    throw IllegalStateException("unexpected read result($res)")
//                }
//            } catch(ex: Throwable) {
//                pool.put(buf)
//                output.close(ex)
//            }
//        }
//    }

    init {
        runBlocking {
            (transport.remoteAddress as InetSocketAddress?)?.let { postConnect(it) }
        }
    }



    internal suspend fun postConnect(addr: InetSocketAddress) {
        state = State.handshaking
        engine = ssl.createSSLEngine(addr.hostName, addr.port)
        engine.useClientMode = true
        engine.beginHandshake()

        flowRead()
        d{"engine status = ${engine.handshakeStatus}"}
        continueHS(EMPTY)
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
                postConnect(remote)
                handshake.await()
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
        checkState(State.connecting, State.handshaking, State.connected)

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
                    postConnect(transport.remoteAddress as InetSocketAddress)
                }
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

        if ((offset < 0) || (length < 0) || (offset > _dsts.size - length)) {
            throw IndexOutOfBoundsException()
        }

        if (!readOp.compareAndSet(false, true)) {
            throw ReadPendingException()
        }

        val dsts = _dsts.sliceArray(offset until offset + length)
        GlobalScope.launch(Dispatchers.IO) {

            var eof = false
            try {
                val inFlow = plainIn.receiveAsFlow()
                val inBuf = if (timeout > 0) withTimeout(timeout) {inFlow.firstOrNull()} else inFlow.firstOrNull()
                if (inBuf != null) {
                    val avail = inBuf.remaining()
                    val len = inBuf.transfer(dsts)
                    w("read $len out of $avail")
                    handler.completed(len, attachment)
                } else {
                    handler.completed(-1, attachment);
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
        // NOOP: handshake is start automatically
    }

    private suspend fun doHandshake(clientMode: Boolean): SSLSession {
        require(state == State.connecting)
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
                NOT_HANDSHAKING,
                FINISHED -> {
                    d { "handshake is complete" }
                    break@loop
                }

                NEED_TASK -> CompletableFuture.supplyAsync {
                        engine.delegatedTask?.run()
                    }.await()

                NEED_WRAP -> {
                    outBuf.clear()
                    do {
                        val res = engine.wrap(EMPTY, outBuf)
                    } while (res.handshakeStatus == NEED_WRAP)

                    outBuf.flip()
                    transport.writeCompletely(outBuf)
                }

                NEED_UNWRAP -> {
                    outBuf.clear()
                    do {
                        val res = engine.unwrap(inBuf, outBuf)
                        if (res.status == BUFFER_UNDERFLOW) {
                            inBuf.compact()
                            transport.readSuspend(inBuf)
                            inBuf.flip()
                        }
                    } while (res.handshakeStatus == NEED_UNWRAP && inBuf.hasRemaining())
                    outBuf.flip()
                    transport.writeCompletely(outBuf)
                }

                else -> error("should not be here, handshakeStatus(${engine.handshakeStatus})")
            }
        }
        return engine.session
    }

    fun setEnabledProtocols(protocols: Array<out String>?) { engine.enabledProtocols = protocols }
    fun getEnabledProtocols() = engine.enabledProtocols
    fun setEnabledCipherSuites(suites: Array<String>) { engine.enabledCipherSuites = suites }
    fun getEnabledCipherSuites() = engine.enabledCipherSuites
    fun getSupportedCipherSuites() = engine.supportedCipherSuites
    fun getSupportedProtocols() = engine.supportedProtocols

    internal fun checkState(vararg expected: State) {
        if (state in expected) return

        when(state) {
            State.closed -> throw ClosedChannelException()
            State.connecting, State.handshaking -> throw ConnectionPendingException()
            State.connected -> throw AlreadyConnectedException()
            State.initial -> throw NotYetConnectedException()
        }
    }

    internal fun flowRead() = GlobalScope.launch(Dispatchers.IO) {
        val sslPool = BufferPool(POOL_SIZE, SSL_BUFFER_SIZE)

        d{"start flowRead()"}
        engine.sslParameters.maximumPacketSize

        flow {
            while(true) {
                w{"getting pooled buffer"}
                emit(sslPool.get())
            }
        }.map {
            val read = transport.readSuspend(it)
            if (read < 0) {
                sslPool.put(it)
                plainIn.close()
                cancel()
                null
            } else
                it.flip()
        }.collect {
            if (it == null) {
                e{"cancelling"}
                plainIn.close()
                cancel()
            }
            else {
                sslInBuf.put(it)
                sslPool.put(it)
                sslInBuf.flip()
                w { "ssl in = $sslInBuf"}

                var plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
                while(sslInBuf.remaining() > 0) {
                    val res = engine.unwrap(sslInBuf, plainBuf)
                    w { "res = $res" }

                    if (res.status == OK) {
                        when (res.handshakeStatus) {
                            NOT_HANDSHAKING, FINISHED -> {
                                if (!handshake.isCompleted) handshake.complete(engine.session)
                            }
                            else -> continueHS(sslInBuf)
                        }
                    } else if (res.status == BUFFER_UNDERFLOW) {
                        break
                    } else if (res.status == BUFFER_OVERFLOW) {
                        plainBuf.flip()
                        plainIn.send(plainBuf)
                        plainBuf = ByteBuffer.allocate(SSL_BUFFER_SIZE)
                    } else if (res.status == CLOSED) {
                        plainIn.close()
                    }
                }
                plainBuf.flip()
                if (plainBuf.hasRemaining()) {
                    plainIn.send(plainBuf)
                }
                sslInBuf.compact()
            }
        }
    }

    internal suspend fun continueHS(sslInBuf: ByteBuffer) {
        val hsBuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)
        while(true) {
            d {"engine.handshakeStatus = ${engine.handshakeStatus}"}
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
                        if (engine.unwrap(sslInBuf, hsBuf).status == BUFFER_UNDERFLOW)
                            return
                    }
                }
                FINISHED, NOT_HANDSHAKING -> {
                    handshake.complete(engine.session)
                    state = State.connected
                    return
                }
            }
        }
    }
}