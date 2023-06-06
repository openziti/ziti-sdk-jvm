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

package org.openziti.net.nio

import kotlinx.coroutines.*
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.jupiter.api.assertThrows
import org.junit.rules.Timeout
import org.openziti.identity.ZitiTestHelper
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousCloseException
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.InterruptedByTimeoutException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

private const val testhost = "httpbingo.org"
private const val connectTimeout = 2L
private const val readTimeout = 5L

class AsyncTLSChannelTest {

    lateinit var ch: AsyncTLSChannel

    @Rule
    @JvmField val timeout = Timeout.seconds(15)

    @After
    fun tearDown() {
        if (::ch.isInitialized)
            ch.runCatching { close() }
    }

    @Test
    fun connect() {
        ch = AsyncTLSChannel.open() as AsyncTLSChannel
        ch.connect(InetSocketAddress(testhost, 443)).get(connectTimeout, TimeUnit.SECONDS)
        verifyConnection(ch)

    }

    @Test
    fun readWithTimeout() {
        ch = AsyncTLSChannel.open() as AsyncTLSChannel
        ch.connect(InetSocketAddress(testhost, 443)).get(connectTimeout, TimeUnit.SECONDS)

        // first read should fail with timeout since we have not sent request yet
        assertThrows<InterruptedByTimeoutException>{
            runBlocking { ch.readSuspend(ByteBuffer.allocate(128), 100, TimeUnit.MILLISECONDS) }
        }

        verifyConnection(ch)
    }

    @Test(expected = AsynchronousCloseException::class)
    fun readCancel(): Unit {
        runBlocking {
            ch = AsyncTLSChannel.open() as AsyncTLSChannel
            ch.connectSuspend(InetSocketAddress(testhost, 443), TimeUnit.SECONDS.toMillis(connectTimeout))

            ch.startHandshake()
            ch.getSession()

            val readOp = async(Dispatchers.IO) {
                ch.readSuspend(ByteBuffer.allocate(128))
            }

            delay(100)
            ch.close()

            withTimeout(TimeUnit.SECONDS.toMillis(readTimeout)) {
                readOp.await()
            }
        }
    }

    @Test
    fun useUnconnected() {
        val transport = AsynchronousSocketChannel.open()
        ch = AsyncTLSChannel(transport, SSLContext.getDefault())

        ch.connect(InetSocketAddress("google.com", 443)).get(connectTimeout, TimeUnit.SECONDS)
        verifyConnection(ch)
    }

    @Test
    fun useConnected() {
        val transport = AsynchronousSocketChannel.open()
        transport.connect(InetSocketAddress("google.com", 443)).get(connectTimeout, TimeUnit.SECONDS)
        ch = AsyncTLSChannel(transport, SSLContext.getDefault())

        verifyConnection(ch)
    }

    @Test
    fun testALPN_Unconnected() {
        val transport = AsynchronousSocketChannel.open()
        val ssl = SSLContext.getDefault()
        ch = AsyncTLSChannel(transport, ssl)

        val params = ssl.defaultSSLParameters
        params.applicationProtocols = arrayOf("h2","http1.1")
        ch.setSSLParameters(params)

        ch.connect(InetSocketAddress("google.com", 443)).get(connectTimeout, TimeUnit.SECONDS)
        ch.startHandshake()
        ch.getSession()
        val p = ch.getApplicationProtocol()
        assertEquals("h2", p)
    }

    @Test
    fun testALPN_Connected() {
        val transport = AsynchronousSocketChannel.open()
        transport.connect(InetSocketAddress(testhost, 443)).get(connectTimeout, TimeUnit.SECONDS)
        val ssl = SSLContext.getDefault()
        ch = AsyncTLSChannel(transport, ssl)

        val params = ssl.defaultSSLParameters
        params.applicationProtocols = arrayOf("h2","http1.1")
        ch.setSSLParameters(params)

        ch.startHandshake()
        ch.getSession()
        assertEquals("h2", ch.getApplicationProtocol())
    }

    @Test(expected = SSLHandshakeException::class)
    fun test_untrustedServer() {
        val s = AsynchronousSocketChannel.open()
        val tls = SSLContext.getInstance("TLSv1.3")
        tls.init(null, arrayOf(ZitiTestHelper.TrustNoOne), SecureRandom())
        ch = AsyncTLSChannel(s, tls)
        ch.connect(InetSocketAddress(testhost, 443)).get(connectTimeout, TimeUnit.SECONDS)
        ch.startHandshake()
        ch.getSession()
    }

    @Test
    fun sslError() {
        assertThrows<SSLException>{
            ch = AsyncTLSChannel.open() as AsyncTLSChannel
            ch.connect(InetSocketAddress(testhost, 80)).get(connectTimeout, TimeUnit.SECONDS)
            ch.startHandshake()
            ch.getSession()
        }
    }

    @Test
    fun testMultiBufferWrite() {
        val payload = ByteBuffer.allocate(1024)
        Random.nextBytes(payload.array())

        val req = """POST /post HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: ${testhost}
User-Agent: HTTPie/1.0.2
Content-Length: ${payload.remaining()}

"""
        val wb = arrayOf(StandardCharsets.US_ASCII.encode(req), payload)
        val writeTotal = wb.fold(0L){ c, b -> c + b.remaining() }
        ch = AsyncTLSChannel.open() as AsyncTLSChannel
        ch.connect(InetSocketAddress(testhost, 443)).get(connectTimeout, TimeUnit.SECONDS)

        val wf = CompletableFuture<Long>()
        ch.write(wb, 0, 2, connectTimeout, TimeUnit.SECONDS, wf, FutureHandler())
        val wc = wf.get(2, TimeUnit.SECONDS)
        assertEquals(writeTotal, wc)

        val rb = ByteBuffer.allocate(10 * 1024)
        ch.read(rb).get(readTimeout, TimeUnit.SECONDS)
        rb.flip()

        val resp = StandardCharsets.UTF_8.decode(rb).toString().reader().readLines()
        assertThat(resp.first(), startsWith("HTTP/1.1 200 OK"))

    }

    fun verifyConnection(ch: AsynchronousSocketChannel) {
        assertNotNull(ch.localAddress)
        assertNotNull(ch.remoteAddress)
        val addr = ch.remoteAddress as InetSocketAddress
        val req = """GET / HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: keep-alive
Host: ${addr.hostName}
User-Agent: HTTPie/1.0.2


"""
        val wc = ch.write(StandardCharsets.US_ASCII.encode(req)).get(5, TimeUnit.SECONDS)
        assertEquals(req.length, wc)

        val resp = ByteBuffer.allocate(128)
        val rc = ch.read(resp).get(readTimeout, TimeUnit.SECONDS)
        assertEquals(rc, resp.position())
        resp.flip()

        val lines = StandardCharsets.UTF_8.decode(resp).toString().reader().readLines()
        assertThat(lines[0], startsWith("HTTP/1.1"))
    }

}