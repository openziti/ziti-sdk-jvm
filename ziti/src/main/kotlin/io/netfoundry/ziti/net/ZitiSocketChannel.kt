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

package io.netfoundry.ziti.net

import io.netfoundry.ziti.ZitiAddress
import io.netfoundry.ziti.api.SessionType
import io.netfoundry.ziti.impl.ZitiContextImpl
import io.netfoundry.ziti.net.nio.FutureHandler
import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.ZitiLog
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.IOException
import java.lang.Math.min
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.SocketOption
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.spi.AsynchronousChannelProvider
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.channels.Channel as Chan

internal class ZitiSocketChannel(internal val ctx: ZitiContextImpl): AsynchronousSocketChannel(Provider),
    Channel.MessageReceiver, Logged by ZitiLog() {

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

    val state = AtomicReference(State.initial)
    var connId: Int = 0
    lateinit var channel: Channel
    val seq = AtomicInteger(1)
    var remote: ZitiAddress? = null
    val receiveBuff = ByteBuffer.allocate(32 * 1024).apply { flip() }
    val signal = Chan<Int>()

    override fun getLocalAddress(): SocketAddress? = InetSocketAddress(connId)

    override fun getRemoteAddress(): SocketAddress? = remote

    override fun supportedOptions(): MutableSet<SocketOption<*>> = mutableSetOf()
    override fun <T : Any> getOption(name: SocketOption<T>?): T? = null
    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): AsynchronousSocketChannel = this

    override fun <A : Any?> connect(remote: SocketAddress?, attachment: A, handler: CompletionHandler<Void, in A>) {
        if (remote !is ZitiAddress) {
            throw UnsupportedAddressTypeException()
        }

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

        ctx.async {
            val ns = ctx.getNetworkSession(remote.service, SessionType.Dial)
            channel = ctx.getChannel(ns)
            connId = channel.registerReceiver(this@ZitiSocketChannel)

            val connectMsg = Message(ZitiProtocol.ContentType.Connect, ns.token.toByteArray(UTF_8))
            connectMsg.setHeader(ZitiProtocol.Header.ConnId, connId)
            d("starting network connection ${ns.id}/$connId")
            val reply = channel.SendAndWait(connectMsg)
            when (reply.content) {
                ZitiProtocol.ContentType.StateConnected -> {
                    d("network connection established ${ns.id}/$connId")
                    state.set(State.connected)
                }
                ZitiProtocol.ContentType.StateClosed -> {
                    state.set(State.closed)
                    val err = reply.body.toString(UTF_8)
                    w("connection rejected: ${err}")
                    throw ConnectException(err)
                }
                else -> {
                    state.set(State.closed)
                    throw IOException("Invalid response type")
                }
            }
        }.invokeOnCompletion { exc ->
            if (exc != null) {
                handler.failed(exc, attachment)
                channel.deregisterReceiver(connId)
            }
            else handler.completed(null, attachment)
        }
    }

    override fun connect(remote: SocketAddress?): Future<Void> {
        val result = CompletableFuture<Void>()
        connect(remote, result, FutureHandler())
        return result
    }

    override fun isOpen(): Boolean = state.get() != State.closed

    override fun bind(local: SocketAddress?): AsynchronousSocketChannel = this // NOOP

    override fun shutdownInput(): AsynchronousSocketChannel {
        state.set(State.closed)
        signal.close()
        channel.deregisterReceiver(connId)
        return this
    }

    override fun close() {
        shutdownOutput()
        shutdownInput()
    }

    override fun shutdownOutput(): AsynchronousSocketChannel {
        val closeMsg = Message(ZitiProtocol.ContentType.StateClosed).apply {
            setHeader(ZitiProtocol.Header.ConnId, connId)
        }
        d("closing conn = ${this.connId}")
        runBlocking { channel.SendSynch(closeMsg) }

        return this
    }

    override
    fun <A : Any?> read(dst: ByteBuffer, timeout: Long, unit: TimeUnit,
        att: A, handler: CompletionHandler<Int, in A>
    ) {
        read(arrayOf(dst), 0, 1, timeout, unit, att, object : CompletionHandler<Long, A>{
            override fun completed(result: Long, a: A) = handler.completed(result.toInt(), a)
            override fun failed(exc: Throwable?, a: A) = handler.failed(exc, a)
        })
    }

    override fun read(dst: ByteBuffer?): Future<Int> {
        val result = CompletableFuture<Int>()
        read(dst, result, FutureHandler())
        return result
    }

    internal fun transfer (dsts: Array<out ByteBuffer>): Long {
        synchronized(receiveBuff){
            if (state.get() == State.closed)
                return -1

            var copied = 0L
            for (b in dsts) {
                val count = min(b.remaining(), receiveBuff.remaining())
                receiveBuff.get(b.array(), b.position(), count)
                b.position(b.position() + count)
                copied += count
                if (!receiveBuff.hasRemaining()) break
            }
            return copied
        }
    }

    override fun <A : Any?> read(dsts: Array<out ByteBuffer>, offset: Int, length: Int,
        to: Long, unit: TimeUnit,
        att: A, handler: CompletionHandler<Long, in A>) {

        val slice = dsts.sliceArray(offset until offset + length)
        val copied = transfer(slice)
        if (copied > 0) {
            handler.completed(copied, att)
        } else {
            ctx.async {
                val received = if (to > 0) {
                    withTimeout(unit.toMillis(to)) {
                        signal.receive()
                    }
                }
                else signal.receive()

                if (received == -1) {
                    handler.completed(-1, att)
                } else {
                    handler.completed(transfer(slice), att)
                }
            }.invokeOnCompletion { ex ->
                if (ex != null) handler.failed(ex, att)
            }
        }
    }

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
        }

        val srcs = _srcs.slice(offset until offset + length)

        ctx.async {
            var sent = 0L
            for (b in srcs) {
                val data = ByteArray(b.remaining())
                b.get(data)

                val dataMessage = Message(ZitiProtocol.ContentType.Data, data)
                dataMessage.setHeader(ZitiProtocol.Header.ConnId, connId)
                dataMessage.setHeader(ZitiProtocol.Header.SeqHeader, seq.getAndIncrement())
                sent += data.size
                channel.Send(dataMessage)
            }
            handler.completed(sent, att)
        }.invokeOnCompletion { ex ->
            if (ex is TimeoutCancellationException) {
                handler.failed(InterruptedByTimeoutException(), att)
            } else if (ex != null) {
                handler.failed(ex, att)
            }
        }
    }

    override suspend fun receive(msg: Message) {
        synchronized(receiveBuff) {
            when (msg.content) {
                ZitiProtocol.ContentType.StateClosed -> {
                    d{"closed message type[${msg.content}] for conn[$connId]"}
                    state.set(State.closed)
                    signal.offer(-1)
                    channel.deregisterReceiver(connId)
                }
                ZitiProtocol.ContentType.Data -> {
                    t{"received data(${msg.body.size} bytes) for conn[$connId]"}
                    receiveBuff.compact()
                    receiveBuff.put(msg.body)
                    receiveBuff.flip()
                    signal.offer(msg.body.size)
                }
                else -> {
                    e{"unexpected message type[${msg.content}] for conn[$connId]"}
                    state.set(State.closed)
                    signal.offer(-1)
                    channel.deregisterReceiver(connId)
                }
            }
        }
    }
}