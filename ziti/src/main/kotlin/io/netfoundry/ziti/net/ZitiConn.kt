/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

import io.netfoundry.ziti.ZitiConnection
import io.netfoundry.ziti.api.NetworkSession
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.receiveOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
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
    Closeable, Logged by JULogged("ziti-conn") {
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

    val recChan = Chan<Message>()

    var timeout: Long = 5000

    init {
        connId = channel.registerReceiver(this)
        startSession(networkSession)
    }

    internal fun startSession(ns: NetworkSession) {
        val connectMsg = Message(ZitiProtocol.ContentType.Connect, ns.token.toByteArray(UTF_8))
        connectMsg.setHeader(ZitiProtocol.Header.ConnId, connId)

        val reply = runBlocking { channel.SendAndWait(connectMsg) }
        when (reply.content) {
            ZitiProtocol.ContentType.StateConnected -> {
                d("network connection established ${ns.id}/$connId")
                state = State.Connected
            }
            ZitiProtocol.ContentType.StateClosed -> {
                state = State.Closed
                throw ConnectException(reply.body.toString(UTF_8))
            }
            else -> {
                state = State.Closed
                throw IOException("Invalid response type")
            }
        }
    }

    override suspend fun sendData(data: ByteArray) {
        if (state != State.Connected) {
            throw IllegalStateException("ZitiConnection is closed")
        }
        val dataMessage = Message(ZitiProtocol.ContentType.Data, data)
        dataMessage.setHeader(ZitiProtocol.Header.ConnId, connId)
        dataMessage.setHeader(ZitiProtocol.Header.SeqHeader, seq.getAndIncrement())

        channel.Send(dataMessage)
    }

    override suspend fun readData(out: ByteArray, off: Int, len: Int): Int {
        when (state) {
            State.New -> throw IllegalStateException("not connected")
            State.Closed -> return -1
            State.Connected -> {
            }
        }

        val n = readNext()
        if (n <= 0) {
            return n
        }
        readBuf.get(out, off, min(n, len))
        return n - readBuf.remaining()
    }

    private suspend fun readNext(): Int {
        if (readBuf.hasRemaining())
            return readBuf.remaining()

        try {
            val m = withTimeout(timeout) {
                recChan.receiveOrNull()
            } ?: return -1

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
        } catch (closeEx: ClosedReceiveChannelException) {
            return -1
        }
    }

    fun getInputStream() = input
    fun getOutputStream() = output
    fun available() = input.available()

    override fun close() {
        val closeMsg = Message(ZitiProtocol.ContentType.StateClosed).apply {
            setHeader(ZitiProtocol.Header.ConnId, connId)
        }
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
            runBlocking { sendData(buf.toByteArray()) }
        }
    }

    inner class Input : InputStream() {
        override fun read(): Int = runBlocking {
            val b = ByteArray(1)
            val res = readData(b, 0, 1)
            if (res < 0) {
                res
            } else {
                b[0].toInt() and 0xFF
            }
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int = runBlocking {
            readData(b, off, len)
        }

        override fun available(): Int = readBuf.remaining()
    }
}