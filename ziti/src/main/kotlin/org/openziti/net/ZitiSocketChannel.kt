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

package org.openziti.net

import com.google.gson.Gson
import com.goterl.lazysodium.utils.Key
import com.goterl.lazysodium.utils.KeyPair
import com.goterl.lazysodium.utils.SessionPair
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
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
import org.openziti.util.transfer
import java.io.*
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

internal class ZitiSocketChannel(internal val ctx: ZitiContextImpl):
    AsynchronousSocketChannel(Provider),
    Channel.MessageReceiver,
    ZitiConnection,
    Logged by ZitiLog() {

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
    val connId: Int = ctx.nextConnId()
    val chPromise = CompletableDeferred<Channel>()
    val channel: Channel
        get() = runBlocking { chPromise.await() }

    val seq = AtomicInteger(1)
    lateinit var serviceName: String
    var remote: SocketAddress? = null
    var local: ZitiAddress? = null
    val receiveQueue = Chan<ByteArray>(16)
    val receiveBuff: ByteBuffer = ByteBuffer.allocate(32 * 1024).apply { flip() }
    val crypto = CompletableDeferred<Crypto.SecretStream?>()

    override fun getLocalAddress(): SocketAddress? = local

    override fun getRemoteAddress(): SocketAddress? = remote

    override fun supportedOptions(): MutableSet<SocketOption<*>> = mutableSetOf()
    override fun <T : Any> getOption(name: SocketOption<T>?): T? = null
    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): AsynchronousSocketChannel = this

    override fun isClosed() = !isOpen

    override fun <A : Any?> connect(remote: SocketAddress, attachment: A, handler: CompletionHandler<Void, in A>) {

        val addr = when (remote) {
            is InetSocketAddress -> {
                val s = ctx.getService(remote.hostString, remote.port) ?: throw UnresolvedAddressException()
                ZitiAddress.Dial(
                    service = s.name,
                    appData = DialData(
                        dstProtocol = Protocol.TCP,
                        dstIp = remote.address?.hostAddress,
                        dstHostname = remote.hostString,
                        dstPort = remote.port.toString()))
            }
            is ZitiAddress.Dial -> remote
            else -> throw UnsupportedAddressTypeException()
        }

        serviceName = addr.service

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

        ctx.launch {
            ctx.runCatching {
                val service = getService(serviceName) ?: throw ZitiException(Errors.ServiceNotAvailable)
                val ns = getNetworkSession(serviceName, SessionType.Dial)

                val kp = if (service.encryptionRequired) Crypto.newKeyPair() else null

                val ch = ctx.getChannel(ns)
                chPromise.complete(ch)
                ch.registerReceiver(connId, this@ZitiSocketChannel)
                doZitiHandshake(ch, addr, ns, kp)
            }.onSuccess {
                handler.completed(null, attachment)
            }.onFailure {
                e(it){"failed to connect"}
                chPromise.completeExceptionally(it)
                close()
                handler.failed(it, attachment)
            }
        }
    }

    override fun connect(remote: SocketAddress): Future<Void> {
        val result = CompletableFuture<Void>()
        connect(remote, result, FutureHandler())
        return result
    }

    override fun isOpen(): Boolean = state.get() != State.closed

    override fun bind(local: SocketAddress?): AsynchronousSocketChannel = this // NOOP

    override fun shutdownInput(): AsynchronousSocketChannel {
        when(state.get()) {
            State.initial -> throw NotYetConnectedException()
            State.connecting -> {} // connecting process failed
            State.connected -> {}
            State.closed -> return this
            else -> return this
        }

        runCatching { channel.deregisterReceiver(connId) }
        runCatching { receiveQueue.close() }
        return this
    }

    override fun close() {
        runCatching { shutdownOutput() }
        runCatching { shutdownInput() }
        runCatching { closeInternal() }
    }

    override fun shutdownOutput(): AsynchronousSocketChannel = runBlocking {
        if (state.get() == State.connected && sentFin.compareAndSet(false, true)) {
            val finMsg = Message(ZitiProtocol.ContentType.Data).apply {
                setHeader(Header.ConnId, connId)
                setHeader(Header.FlagsHeader, ZitiProtocol.EdgeFlags.FIN)
                setHeader(Header.SeqHeader, seq.getAndIncrement())
            }
            d{"sending FIN conn = $connId"}

            runCatching {
                channel.SendSynch(finMsg)
            }.onFailure {
                e(it){"failed to send FIN message"}
            }
        }

        this@ZitiSocketChannel
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
                    runBlocking {
                        runCatching {
                            channel.SendSynch(closeMsg)
                        }.onFailure {
                            w { "failed to send StateClosed message: $it" }
                        }
                    }
                    state.set(State.closed)
                }
                State.closed -> {}
                else -> {}
            }
        }
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

    override fun <A : Any?> read(dsts: Array<out ByteBuffer>, offset: Int, length: Int,
        to: Long, unit: TimeUnit,
        att: A, handler: CompletionHandler<Long, in A>) {
        t{"reading state=$state"}
        val slice = dsts.sliceArray(offset until offset + length)
        val copied = receiveBuff.transfer(slice)
        if (copied > 0) {
            t{"reading completed[$copied]"}
            handler.completed(copied, att)
            return
        }

        ctx.launch {
            var count = 0L
            t { "waiting for data with $receiveBuff" }

            try {
                var data: ByteArray? = receiveQueue.receive()
                do {
                    val dataBuf = ByteBuffer.wrap(data)
                    count += dataBuf.transfer(slice)
                    if (dataBuf.hasRemaining()) {
                        t { "saving ${dataBuf.remaining()} for later" }
                        receiveBuff.compact()
                        receiveBuff.put(dataBuf)
                        receiveBuff.flip()
                        break
                    }
                    data = receiveQueue.tryReceive().getOrNull()
                } while (data != null)

                t { "transferred $count" }
                handler.completed(count, att)

            } catch (e1: Exception) {
                if (count > 0) handler.completed(count, att)
                else if (e1 is ClosedReceiveChannelException) handler.completed(-1, att)
                else handler.failed(e1, att)
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
            else -> error("should not be here")
        }

        val srcs = _srcs.slice(offset until offset + length)

        ctx.async {
            var sent = 0L
            for (b in srcs) {
                var data = ByteArray(b.remaining())
                b.get(data)

                crypto.await()?.let {
                    data = it.encrypt(data)
                }

                val dataMessage = Message(ZitiProtocol.ContentType.Data, data)
                dataMessage.setHeader(Header.ConnId, connId)
                dataMessage.setHeader(Header.SeqHeader, seq.getAndIncrement())
                sent += data.size
                v("sending $dataMessage")
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

    override suspend fun receive(msg: Result<Message>) {
        msg.onSuccess {
            receiveMsg(it)
        }.onFailure {
            state.set(State.closed)
            close()
        }
    }

    private suspend fun receiveMsg(msg: Message) {
        v{"conn[$connId] received message[${msg.content}] with seq[${msg.getIntHeader(Header.SeqHeader)}]"}
        when (msg.content) {
            ZitiProtocol.ContentType.StateClosed -> {
                state.set(State.closed)
                t{"signaling EOF"}
                receiveQueue.close()
                channel.deregisterReceiver(connId)
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
                state.set(State.closed)
                receiveQueue.close(IllegalStateException())
                channel.deregisterReceiver(connId)
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

    override fun getInputStream(): InputStream {
        return object : InputStream() {
            override fun read(): Int {
                val b = byteArrayOf(-1)
                if (read(b, 0, 1) == -1) {
                    return -1
                }

                return (b[0].toInt() and 0xff)
            }

            override fun read(b: ByteArray, off: Int, len: Int): Int = runBlocking {
                receive(b, off, len)
            }
        }
    }

    override fun getOutputStream(): OutputStream = object : OutputStream() {
        override fun write(b: Int) = write(byteArrayOf(b.toByte()))

        override fun write(b: ByteArray, off: Int, len: Int) = runBlocking {
            send(b, off, len)
        }
    }

    internal suspend fun doZitiHandshake(channel: Channel, remote: ZitiAddress.Dial, ns: Session, kp: KeyPair?) {
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
        val reply = channel.SendAndWait(connectMsg)
        when (reply.content) {
            ZitiProtocol.ContentType.StateConnected -> {
                val peerPk = reply.getHeader(Header.PublicKeyHeader)
                if (kp == null || peerPk == null) {
                    crypto.complete(null)
                } else {
                    setupCrypto(Crypto.kx(kp, Key.fromBytes(peerPk), false))
                    startCrypto()
                }

                local = ZitiAddress.Session(ns.id, serviceName, null)
                d("network connection established ${ns.id}/$connId")
                state.set(State.connected)
            }
            ZitiProtocol.ContentType.StateClosed -> {
                state.set(State.closed)
                val err = reply.body.toString(UTF_8)
                w("connection rejected: $err")
                throw ConnectException(err)
            }
            else -> {
                state.set(State.closed)
                throw IOException("Invalid response type")
            }
        }
    }

    internal fun setupCrypto(keys: SessionPair?) {
        crypto.complete(keys?.let { Crypto.newStream(it) })
    }

    internal suspend fun startCrypto() {
        crypto.await()?.let {
            val header = it.header()
            val headerMessage = Message(ZitiProtocol.ContentType.Data, header)
                .setHeader(Header.ConnId, connId)
                .setHeader(Header.SeqHeader, seq.getAndIncrement())
            channel.Send(headerMessage)
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