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
import kotlinx.coroutines.future.asCompletableFuture
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
import javax.net.ssl.SSLEngineResult.Status.*

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

    internal val handshake = CompletableFuture<SSLSession>()
    private var state: State
    internal val transport: AsynchronousSocketChannel
    internal lateinit var engine: SSLEngine

    private val sslPool = BufferPool(POOL_SIZE, SSL_BUFFER_SIZE)
    private val sslIn = Channel<ByteBuffer>(POOL_SIZE)
    private val sslbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val plnbuf = ByteBuffer.allocateDirect(2 * SSL_BUFFER_SIZE).apply { flip() }
    private val readOp = AtomicBoolean(false)

    private fun getTransport() = transport
    private val reader: Reader

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
        state = State.initial
        transport = ch
        reader = Reader(getTransport(), sslIn, sslPool)
        val addr = transport.remoteAddress as InetSocketAddress?
        addr?.let {
            state = State.connecting
            engine = ssl.createSSLEngine(it.hostName, it.port)
            try {
                runBlocking { doHandshake( true) }
                reader.startRead()
            } catch (xx: Exception) {
                e(xx){"failed to start"}
                val cause = xx.cause
                if (cause is IOException) throw cause
                throw IOException(cause)
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

        engine = ssl.createSSLEngine(remote.hostString, remote.port)
        state = State.connecting

        return GlobalScope.async(Dispatchers.IO) {
            transport.connectSuspend(remote)
            doHandshake(true)
            reader.startRead()
            state = State.connected
            null
        }.asCompletableFuture()
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
        _srcs: Array<out ByteBuffer>,
        offset: Int,
        length: Int,
        timeout: Long,
        unit: TimeUnit?,
        attachment: A,
        handler: CompletionHandler<Long, in A>?
    ) {
        requireNotNull(handler){"handler is required"}
        checkState(State.connected)

        val srcs = _srcs.slice(offset until offset + length)
        try {
            var consumed = 0L
            var produced = 0
            val sslbuf = ByteBuffer.allocateDirect(SSL_BUFFER_SIZE)

            loop@
            for (b in srcs) {
                while(b.hasRemaining()) {
                    val res = engine.wrap(b, sslbuf)
                    when(res.status) {
                        BUFFER_UNDERFLOW, BUFFER_OVERFLOW -> break@loop // buffer full
                        CLOSED -> {
                            if (produced > 0)
                                break@loop
                            handler.completed(-1, attachment)
                            return
                        }
                        OK -> {
                            consumed += res.bytesConsumed()
                            produced += res.bytesProduced()
                        }
                        else -> error("can't be here")
                    }
                }
            }

            if (consumed > 0) {
                sslbuf.flip()
                assert(produced == sslbuf.remaining())
                val wh = object : CompletionHandler<Int, A> {
                    val sslbytes = produced
                    val cltbytes = consumed
                    override fun completed(result: Int, a: A) {
                        if (result == -1) {
                            handler.completed(-1, a)
                        }
                        else if (result < sslbytes) { // write again
                            transport.write(sslbuf, timeout, unit, a, this)
                        }
                        else {
                            handler.completed(cltbytes, a)
                        }
                    }

                    override fun failed(exc: Throwable, a: A) = handler.failed(exc, a)
                }
                transport.write(sslbuf, timeout, unit, attachment, wh)
            } else {
                handler.completed(0, attachment)
            }

        } catch (ex: SSLException) {
            handler.failed(ex, attachment)
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
    }

    override fun shutdownOutput(): AsynchronousSocketChannel {
        engine.closeOutbound()
        val b = ByteBuffer.allocate(128)
        val res = engine.wrap(ByteBuffer.wrap(byteArrayOf()), b)
        if (res.bytesProduced() > 0) {
            transport.write(b, null, object : CompletionHandler<Int, Nothing?>{
                override fun completed(result: Int, n: Nothing?) {
                    transport.shutdownOutput()
                }

                override fun failed(exc: Throwable?, n: Nothing?) {}
            })
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
                when(ex) {
                    is TimeoutCancellationException -> handler.failed(InterruptedByTimeoutException(), attachment)
                    else -> handler.failed(ex, attachment)
                }
            } finally {
                readOp.set(false)
            }
        }


    }

    internal fun getSession(): SSLSession {
        return handshake.get()
    }

    internal fun startHandshake(result: CompletableFuture<Void>?) {
        handshake.whenCompleteAsync { s, ex ->
            ex?.let { result?.completeExceptionally(it) }
                ?: result?.complete(null)
        }

        if (state == State.connecting) {
            GlobalScope.launch(Dispatchers.IO) { doHandshake(true) }
        }
    }

    private suspend fun doHandshake(clientMode: Boolean): SSLSession {
        require(state == State.connecting)
        state = State.handshaking

        handshake.whenComplete { _, exc ->
            state = exc?.let { State.closed } ?: State.connected
        }

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
                    handshake.complete(engine.session)
                    return engine.session
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
}