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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.openziti.ZitiAddress
import org.openziti.api.Service
import org.openziti.api.SessionType
import org.openziti.crypto.Crypto
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
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel as Chan

internal class ZitiServerSocketChannel(val ctx: ZitiContextImpl): AsynchronousServerSocketChannel(null),
    Channel.MessageReceiver, Logged by ZitiLog() {

    lateinit var localAddr: ZitiAddress.Bind
    var channel: Channel? = null
    val connId: Int = ctx.nextConnId()
    var state: State = State.initial
    lateinit var incoming: Chan<Message>
    lateinit var token: String
    var keyPair: KeyPair? = null

    enum class State {
        initial,
        binding,
        bound,
        closed
    }

    override fun isOpen(): Boolean = state != State.closed

    private fun checkService(): Service {
        require(::localAddr.isInitialized)

        val servResult = runCatching { ctx.getService(localAddr.service, 5000L) }
        val service = servResult.getOrNull() ?:
        throw BindException("no permission to bind to service[${localAddr.service}]")

        if (!service.permissions.contains(SessionType.Bind)) {
            throw BindException("no permission to bind to service[${service.name}]")
        }
        return service
    }

    override fun bind(local: SocketAddress?, backlog: Int): AsynchronousServerSocketChannel {
        if (local !is ZitiAddress.Bind) throw UnsupportedAddressTypeException()
        when (state) {
            State.initial -> {}
            State.binding,
            State.bound -> throw AlreadyBoundException()
            State.closed -> throw ClosedChannelException()
        }
        state = State.binding
        localAddr = local
        incoming = Chan(backlog)

        checkService()

        ctx.launch {
            doBind()
        }
        return this
    }

    private suspend fun doBind(counter: Int = 0) {
        d{
            if (counter > 0) "starting rebind attempt[$counter]"
            else "starting bind"
        }

        delay(REBIND_DELAY * min(counter, MAX_REBIND_INTERVAL))

        val service = this.runCatching { checkService() }.getOrElse {
            // this is hard error. service is not(no longer) available for binding
            incoming.close(it)
            return
        }

        if (service.encryptionRequired) {
            keyPair = Crypto.newKeyPair()
        }

        try {
            val session = ctx.getNetworkSession(localAddr.service, SessionType.Bind)
            token = session.token
            val ch = ctx.getChannel(session)
            ch.registerReceiver(connId, this@ZitiServerSocketChannel)

            val connectMsg = Message(ZitiProtocol.ContentType.Bind, session.token.toByteArray(Charsets.UTF_8)).apply {
                setHeader(Header.ConnId, connId)
                setHeader(Header.SeqHeader, 0)
                val bindId = localAddr.identity ?: (if (localAddr.useEdgeId) ctx.getId()?.name else null)

                bindId?.let {
                    setHeader(Header.TerminatorIdentityHeader, it)
                }
                keyPair?.let {
                    setHeader(Header.PublicKeyHeader, it.publicKey.asBytes)
                }
            }

            d("starting network connection ${session.id}/$connId")
            val reply = ch.SendAndWait(connectMsg)
            when (reply.content) {
                ZitiProtocol.ContentType.StateConnected -> {
                    d("network connection established ${session.id}/$connId")
                    state = State.bound
                    channel = ch
                }
                ZitiProtocol.ContentType.StateClosed -> {
                    val err = reply.body.toString(Charsets.UTF_8)
                    w("connection rejected: ${err}")
                    ch.deregisterReceiver(connId)
                    throw IOException(err)
                }
                else -> {
                    ch.deregisterReceiver(connId)
                    throw IOException("Invalid response type")
                }
            }
        } catch (ex: Throwable) {
            e(ex){"failed to bind. scheduling rebind"}
            ctx.launch {
                doBind(counter + 1)
            }
            d{"scheduled rebind"}
        }
    }

    override fun getLocalAddress(): SocketAddress? {
        if (!::localAddr.isInitialized)
            return null

        return if (isOpen()) localAddr else throw ClosedChannelException()
    }

    override fun <A : Any?> accept(att: A, handler: CompletionHandler<AsynchronousSocketChannel, in A>) {
        if (state == State.closed) throw ClosedChannelException()
        if (state == State.initial) throw NotYetBoundException()

        ctx.launch {
            try {
                val req = incoming.receive()
                val ch = channel!!

                val child = ZitiSocketChannel(ctx)
                d{"accepting child conn[${child.connId}] on parent[$connId]"}
                val connIdBuf = ByteArray(4)
                ByteBuffer.wrap(connIdBuf).order(ByteOrder.LITTLE_ENDIAN).putInt(child.connId)
                val dialSuccess = Message(ZitiProtocol.ContentType.DialSuccess, connIdBuf)
                dialSuccess.setHeader(Header.SeqHeader, 0)
                dialSuccess.setHeader(Header.ConnId, connId)
                dialSuccess.setHeader(Header.ReplyFor, req.seqNo)

                keyPair?.let { kp ->
                    val sessKeys = req.getHeader(Header.PublicKeyHeader)?.let {
                        Crypto.kx(kp, Key.fromBytes(it), true)
                    }
                    child.setupCrypto(sessKeys)
                } ?: child.setupCrypto(null)

                val startMsg = ch.SendAndWait(dialSuccess)

                if (startMsg.content == ZitiProtocol.ContentType.StateConnected) {
                    child.state.set(ZitiSocketChannel.State.connected)
                    ch.registerReceiver(child.connId, child)
                    child.channel.complete(ch)
                    child.startCrypto(ch)
                    child.local = localAddr
                    child.remote = ZitiAddress.Session("$connId", localAddr.service,
                        req.getStringHeader(Header.CallerIdHeader), req.getHeader(Header.AppDataHeader))

                    handler.completed(child, att)
                } else {
                    val err = Charsets.UTF_8.decode(ByteBuffer.wrap(startMsg.body)).toString()
                    handler.failed(IOException(err), att)
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

    override fun accept(): Future<AsynchronousSocketChannel> {
        val result = CompletableFuture<AsynchronousSocketChannel>()
        accept(result, FutureHandler())
        return result
    }

    override fun supportedOptions(): Set<SocketOption<*>> = setOf()
    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): AsynchronousServerSocketChannel = this
    override fun <T : Any> getOption(name: SocketOption<T>?): T? = null

    override fun close() {
        if (state == State.bound) {
            val unbind = Message(ZitiProtocol.ContentType.Unbind, token.toByteArray(Charsets.UTF_8)).apply {
                setHeader(Header.ConnId, connId)
            }

            runBlocking {
                channel?.let {
                    it.SendSynch(unbind)
                    it.deregisterReceiver(connId)
                } }

        }
        state = State.closed
    }

    override suspend fun receive(msg: Result<Message>) {
        msg.onFailure {
            e(it){"received error. starting rebind"}
            channel = null
            ctx.launch { doBind(0) }
        }

        msg.onSuccess {
            receive(it)
        }
    }

    suspend fun receive(msg: Message) {
        when(msg.content) {
            ZitiProtocol.ContentType.Dial -> {
                if (incoming.trySend(msg).isFailure) { // backlog is full
                    val reject = Message(ZitiProtocol.ContentType.DialFailed)
                        .setHeader(Header.ConnId, connId)
                        .setHeader(Header.ReplyFor, msg.seqNo)
                        .setHeader(Header.SeqHeader, 0)

                    channel?.Send(reject)
                }
            }
            ZitiProtocol.ContentType.StateClosed -> {
                incoming.close()
                channel?.deregisterReceiver(connId)
                state = State.closed
            }
            else -> {
                e{"unexpected message[${msg.content}] on bound conn[$connId]"}
                incoming.close()
                state = State.closed
                channel?.deregisterReceiver(connId)
            }
        }
    }

    companion object {
        val REBIND_DELAY = 3.seconds
        val MAX_REBIND_INTERVAL = 5
    }
}