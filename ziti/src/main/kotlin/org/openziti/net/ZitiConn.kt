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

package org.openziti.net

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.openziti.ZitiConnection
import org.openziti.api.NetworkSession
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.*
import java.net.ConnectException
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.channels.Channel as Chan

/**
 *
 */
internal class ZitiConn(networkSession: NetworkSession, val channel: Channel) : ZitiConnection,
    Channel.MessageReceiver,
    Closeable, Logged by ZitiLog("ziti-conn") {
    enum class State {
        New,
        Connected,
        Closed
    }

    var connId: Int = -1
    var state: State = State.New
    val seq = AtomicInteger(1)

    var readBuf: ByteBuffer = ByteBuffer.allocate(0)


    private val input = Input()
    private val output = Output()

    private val recChan = Chan<Message>(16)
    override var timeout: Long = 5000

    init {
        connId = channel.registerReceiver(this)
        startSession(networkSession)
    }

    override fun isClosed(): Boolean {
        return state == State.Closed
    }
    override suspend fun receive(msg: Message) {
        recChan.send(msg)
    }

    internal fun startSession(ns: NetworkSession) {
        val connectMsg = Message(ZitiProtocol.ContentType.Connect, ns.token.toByteArray(UTF_8))
        connectMsg.setHeader(ZitiProtocol.Header.ConnId, connId)
        d("starting network connection ${ns.id}/$connId")
        val reply = runBlocking { channel.SendAndWait(connectMsg) }
        when (reply.content) {
            ZitiProtocol.ContentType.StateConnected -> {
                d("network connection established ${ns.id}/$connId")
                state = State.Connected
            }
            ZitiProtocol.ContentType.StateClosed -> {
                state = State.Closed
                val err = reply.body.toString(UTF_8)
                w("connection rejected: ${err}")
                throw ConnectException(err)
            }
            else -> {
                state = State.Closed
                throw IOException("Invalid response type")
            }
        }
    }

    override suspend fun send(data: ByteArray) = send(data, false)

    suspend fun send(data: ByteArray, sync: Boolean) {
        if (state != State.Connected) {
            throw IllegalStateException("ZitiConnection is closed")
        }
        val dataMessage = Message(ZitiProtocol.ContentType.Data, data)
        dataMessage.setHeader(ZitiProtocol.Header.ConnId, connId)
        dataMessage.setHeader(ZitiProtocol.Header.SeqHeader, seq.getAndIncrement())

        if (sync) channel.SendSynch(dataMessage) else channel.Send(dataMessage)
    }

    override suspend fun receive(out: ByteArray, off: Int, len: Int): Int =
        when (state) {
            State.New -> throw IllegalStateException("not connected")
            State.Closed -> -1
            State.Connected -> {
                val n = readNext()
                if (n <= 0) {
                    n
                } else {
                    readBuf.get(out, off, min(n, len))
                    n - readBuf.remaining()
                }
            }
        }

    private suspend fun readNext(): Int {
        if (readBuf.hasRemaining())
            return readBuf.remaining()

        try {
            val m = if (timeout > 0) withTimeout(timeout) {
                recChan.receiveOrNull()
            } else recChan.receiveOrNull()

            if (m == null)
                return -1

            when (m.content) {
                ZitiProtocol.ContentType.StateClosed -> {
                    this@ZitiConn.close()
                    return -1
                } // handle close

                ZitiProtocol.ContentType.Data -> {
                    v("Data ${m.body.size} bytes")
                    readBuf = ByteBuffer.wrap(m.body)
                }

                else -> {
                    e("unexpected message")
                    return -1
                }
            }

            return readBuf.remaining()
        } catch (to: TimeoutCancellationException) {
            d("timeout[$timeout] reached $to")
            return 0
        } catch (closeEx: ClosedReceiveChannelException) {
            return -1
        }
    }

    override fun getInputStream(): InputStream = input
    override fun getOutputStream(): OutputStream = output

    override fun close() {
        val closeMsg = Message(ZitiProtocol.ContentType.StateClosed).apply {
            setHeader(ZitiProtocol.Header.ConnId, connId)
        }
        d("closing conn = ${this.connId}")
        runBlocking { channel.SendSynch(closeMsg) }

        state = State.Closed
        connId = -1
        recChan.close()
    }

    inner class Output : OutputStream() {
        val buf = ByteArrayOutputStream(1024)

        override fun write(b: Int) = buf.write(b)
        override fun write(b: ByteArray, off: Int, len: Int) {
            buf.write(b, off, len)
        }

        override fun flush() {
            runBlocking {
                val data = buf.toByteArray()
                buf.reset()
                send(data)
            }
        }
    }

    inner class Input : InputStream() {
        override fun read(): Int = runBlocking {
            val b = ByteArray(1)
            val res = receive(b, 0, 1)
            if (res < 0) {
                res
            } else {
                b[0].toInt() and 0xFF
            }
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int = runBlocking {
            receive(b, off, len)
        }

        override fun available(): Int {
            if (readBuf.remaining() > 0)
                return readBuf.remaining()
            else {
                return recChan.poll()?.let { m ->
                    when (m.content) {
                        ZitiProtocol.ContentType.StateClosed -> {
                            this@ZitiConn.close()
                            0
                        } // handle close

                        ZitiProtocol.ContentType.Data -> {
                            v("Data ${m.body.size} bytes")
                            readBuf = ByteBuffer.wrap(m.body)
                            readBuf.remaining()
                        }
                        else -> {
                            error("unexpected message ${m}")
                        }
                    }
                } ?: 0
            }
        }
    }
}