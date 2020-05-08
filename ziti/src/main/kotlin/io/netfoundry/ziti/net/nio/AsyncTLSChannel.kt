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

package io.netfoundry.ziti.net.nio

import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.ZitiLog
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.SocketOption
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.spi.AsynchronousChannelProvider
import java.util.concurrent.*
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
) : AsynchronousSocketChannel(provider), Logged by ZitiLog("async-tls") {

    companion object {
        fun open(group: AsynchronousChannelGroup? = null) = Provider.openAsynchronousSocketChannel(group)
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

    class FutureHandler<A>: CompletionHandler<A, CompletableFuture<A>> {
        override fun completed(result: A, f: CompletableFuture<A>) { f.complete(result) }
        override fun failed(exc: Throwable, f: CompletableFuture<A>) { f.completeExceptionally(exc) }
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

    private val sslbuf = ByteBuffer.allocate(32 * 1024)
    private val plnbuf = ByteBuffer.allocate(32 * 1024).apply { flip() }

    init {
        state = State.initial
        transport = ch
        val addr = transport.remoteAddress as InetSocketAddress?
        addr?.let {
            state = State.connecting
            engine = ssl.createSSLEngine(it.hostName, it.port)
            val block = CompletableFuture<Void>()
            startHandshake(block)
            block.get()
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

    override fun connect(remote: SocketAddress): CompletableFuture<Void> {
        checkState(State.initial)

        if (!(remote is InetSocketAddress)) {
            return CompletableFuture<Void>().apply {
                completeExceptionally(IllegalArgumentException(remote.toString()))
            }
        }

        engine = ssl.createSSLEngine(remote.hostString, remote.port)
        engine.useClientMode = true

        val res = CompletableFuture<Void>()
        state = State.connecting
        transport.connect(remote, res, object : CompletionHandler<Void, CompletableFuture<Void>>{
            override fun completed(result: Void?, f: CompletableFuture<Void>) {
                startHandshake(f);
            }

            override fun failed(exc: Throwable?, f: CompletableFuture<Void>) {
                f.completeExceptionally(exc)
            }
        })

        return res;
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
            val sslbuf = ByteBuffer.allocate(32 * 1024)

            loop@
            for (b in srcs) {
                while(b.hasRemaining()) {
                    val res = engine.wrap(b, sslbuf)
                    when(res.status) {
                        BUFFER_UNDERFLOW, BUFFER_OVERFLOW -> break@loop // buffer full
                        CLOSED -> {
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

    fun ByteBuffer.transfer(dsts: Array<out ByteBuffer>): Long {
        var c = 0L
        for (d in dsts) {
            while(this.hasRemaining() && d.hasRemaining()) {
                d.put(this.get())
                c++
            }
        }
        return c
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
        unit: TimeUnit?,
        attachment: A,
        handler: CompletionHandler<Long, in A>?
    ) {
        requireNotNull(handler)

        if ((offset < 0) || (length < 0) || (offset > _dsts.size - length)) {
            throw IndexOutOfBoundsException()
        }
        val dsts = _dsts.sliceArray(offset until offset + length);
        if (plnbuf.hasRemaining()) {
            handler.completed(plnbuf.transfer(dsts), attachment)
            return
        }

        val readHandler = object : CompletionHandler<Int, A> {
            override fun completed(result: Int, attachment: A) {
                t{"read $result bytes: ssl=$sslbuf"}
                if (result == -1) {
                    handler.completed(-1, attachment)
                } else {
                    sslbuf.flip()
                    plnbuf.compact()

                    var produced = 0
                    var enough = false
                    while (sslbuf.hasRemaining() && !enough) {
                        t{"sslbuf = $sslbuf plnbuf = $plnbuf"}
                        try {
                            val res = engine.unwrap(sslbuf, plnbuf)
                            t{"$res"}
                            when(res.status) {
                                CLOSED -> {
                                    transport.shutdownInput()
                                    handler.completed(-1, attachment)
                                }

                                BUFFER_UNDERFLOW,
                                BUFFER_OVERFLOW ->
                                    enough = true // need to read more, or flush data to client

                                OK ->
                                    produced += res.bytesProduced()
                            }
                        } catch (ex: SSLException) {
                            handler.failed(ex, attachment)
                            return
                        }
                    }

                    sslbuf.compact()
                    if (produced > 0) {
                        plnbuf.flip()
                        handler.completed(plnbuf.transfer(dsts), attachment)
                    } else {
                        transport.read(sslbuf, timeout, unit, attachment, this) // TODO adjust tmieout
                    }
                }
            }

            override fun failed(exc: Throwable, attachment: A) = handler.failed(exc, attachment)
        }
        transport.read(sslbuf, timeout, unit, attachment, readHandler)
    }

    internal fun getSession(): SSLSession {
        return handshake.get()
    }

    internal fun startHandshake(result: CompletableFuture<Void>?) {
        result?.let {
            handshake.whenCompleteAsync { s, ex ->
                if (ex != null) {
                    it.completeExceptionally(ex)
                } else {
                    it.complete(null)
                }
            }
        }

        if (state == State.connecting) {
            state = State.handshaking
            engine.useClientMode = true
            engine.beginHandshake()
            continueHandshake(Handhaker(handshake, ByteBuffer.allocate(32 * 1024)))
        }
    }

    data class Handhaker (val result: CompletableFuture<SSLSession>, val input: ByteBuffer) {
        init {
            input.flip()
        }
    }

    private fun hsWrite(out: ByteBuffer, hs: Handhaker) {
        transport.write(out, hs, object : CompletionHandler<Int, Handhaker> {
            override fun completed(result: Int, attachment: Handhaker) {
                CompletableFuture.runAsync{
                    continueHandshake(attachment)
                }
            }

            override fun failed(exc: Throwable, attachment: Handhaker) {
                attachment.result.completeExceptionally(exc)
            }
        })
    }

    private fun continueHandshake(hs: Handhaker) {
        v{"continuing handshake: ${engine.handshakeStatus}"}
        try {
            when (engine.handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    val wrapped = ByteBuffer.allocate(32 * 1024)
                    val res = engine.wrap(ByteBuffer.allocate(0), wrapped)
                    wrapped.flip()
                    hsWrite(wrapped, hs)
                }

                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {
                    val output = ByteBuffer.allocate(32 * 1024)
                    while (hs.input.hasRemaining() && engine.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                        val res = engine.unwrap(hs.input, output)
                        if (res.bytesProduced() > 0) {
                            hsWrite(output, hs)
                            return
                        }
                        if (res.status == BUFFER_UNDERFLOW) {
                            break;
                        }
                    }


                    if (engine.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                        hs.input.compact()
                        transport.read(hs.input, hs, object : CompletionHandler<Int, Handhaker> {
                            override fun completed(n: Int, r: Handhaker) {
                                r.input.flip()
                                try {
                                    val res = engine.unwrap(r.input, output)

                                    r.input.compact().flip()

                                    output.flip()
                                    if (res.bytesProduced() > 0) {
                                        hsWrite(output, r)
                                    } else {
                                        continueHandshake(hs);
                                    }
                                } catch (sslex: SSLException) {
                                    r.result.completeExceptionally(sslex)
                                }
                            }

                            override fun failed(exc: Throwable?, r: Handhaker) {
                                r.result.completeExceptionally(exc)
                            }
                        })
                    } else {
                        CompletableFuture.runAsync { continueHandshake(hs) }
                    }
                }
                SSLEngineResult.HandshakeStatus.NEED_TASK -> {
                    CompletableFuture.runAsync {
                        engine.delegatedTask?.run()
                        continueHandshake(hs)
                    }
                }

                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING -> {
                    state = State.connected
                    hs.result.complete(engine.session)
                }

                else -> TODO()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            hs.result.completeExceptionally(ex)
        } finally {
            v{"exiting continue handshake: ${engine.handshakeStatus}"}
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