/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.api.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.Request
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.identity.makeSSLContext
import java.io.InterruptedIOException
import java.net.InetAddress
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLHandshakeException

class ControllerHttpTest {

    private val servers = mutableListOf<MockWebServer>()

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    // Servers listen on, and certificates are issued for, the IPv4 loopback address only: "localhost" may
    // resolve to ::1 as well, and OkHttp would then report the failed ::1 attempt instead of the real error.
    private val loopback = InetAddress.getByName("127.0.0.1")

    private fun plainServer(vararg responses: MockResponse): MockWebServer =
        MockWebServer().also { s ->
            responses.forEach { s.enqueue(it) }
            s.start(loopback, 0)
            servers += s
        }

    private fun ca(name: String) = HeldCertificate.Builder().certificateAuthority(0).commonName(name).build()

    // an HTTPS server whose certificate is signed by [ca]
    private fun tlsServer(ca: HeldCertificate, vararg responses: MockResponse): MockWebServer {
        val s = MockWebServer()
        val serverCert = HeldCertificate.Builder()
            .commonName("server")
            .addSubjectAlternativeName("127.0.0.1")
            .signedBy(ca)
            .build()
        s.useHttps(HandshakeCertificates.Builder().heldCertificate(serverCert, ca.certificate).build().sslSocketFactory())
        responses.forEach { s.enqueue(it) }
        s.start(loopback, 0)
        servers += s
        return s
    }

    private fun ok() = MockResponse.Builder().code(200).body("ok").build()

    private fun get(server: MockWebServer) =
        Request.Builder().url(server.url("/").newBuilder().host("127.0.0.1").build()).build()

    @Test
    fun usesBypassFactories() {
        val client = ControllerHttp.client(SSLContext.getDefault())

        assertTrue(client.socketFactory is LazyBypassSocketFactory)
        assertTrue(client.sslSocketFactory is LazyBypassSSLSocketFactory)
    }

    // OkHttp's default dispatcher uses non-daemon threads that linger for 60s, which would delay the exit of a
    // short-lived application using the SDK; the java.net.http client ran on daemon threads.
    @Test
    fun dispatcherThreadsAreDaemon() {
        val client = ControllerHttp.client(SSLContext.getDefault())
        val daemon = CompletableFuture<Boolean>()

        client.dispatcher.executorService.execute { daemon.complete(Thread.currentThread().isDaemon) }

        assertTrue(daemon.get(5, TimeUnit.SECONDS))
    }

    // one pool for all clients; and no per-host limit of 5 concurrent calls, which the old client did not have
    @Test
    fun dispatcherIsSharedAndNotThrottledPerHost() {
        val first = ControllerHttp.client(SSLContext.getDefault())
        val second = ControllerHttp.client(SSLContext.getDefault())

        assertSame(first.dispatcher, second.dispatcher)
        assertEquals(64, first.dispatcher.maxRequestsPerHost)
    }

    @Test
    fun redirectsAreNotFollowed() {
        val server = plainServer(
            MockResponse.Builder().code(302).addHeader("Location", "/other").build(),
            ok(),
        )

        ControllerHttp.client(SSLContext.getDefault()).newCall(get(server)).execute().use {
            assertEquals(302, it.code)
        }

        assertEquals(1, server.requestCount)
    }

    @Test
    fun tls_acceptsServerTrustedBySslContext() {
        val ca = ca("test ca")
        val server = tlsServer(ca, ok())
        val ssl = makeSSLContext(null, null, listOf(ca.certificate))

        ControllerHttp.client(ssl).newCall(get(server)).execute().use {
            assertEquals(200, it.code)
        }
    }

    // OkHttp is handed a placeholder trust manager; validation must still come from the SSLContext
    @Test
    fun tls_rejectsServerNotTrustedBySslContext() {
        val server = tlsServer(ca("server ca"), ok())
        val ssl = makeSSLContext(null, null, listOf(ca("some other ca").certificate))

        assertThrows(SSLHandshakeException::class.java) {
            ControllerHttp.client(ssl).newCall(get(server)).execute().close()
        }
    }

    @Test
    fun callTimeout_isApplied() {
        val server = plainServer(
            MockResponse.Builder().code(200).body("slow").bodyDelay(3, TimeUnit.SECONDS).build(),
        )
        val client = ControllerHttp.client(SSLContext.getDefault(), Duration.ofMillis(300))

        assertThrows(InterruptedIOException::class.java) {
            client.newCall(get(server)).execute().use { it.body.string() }
        }
    }

    @Test
    fun await_cancelsCallOnCoroutineCancel() = runBlocking {
        val server = plainServer(
            MockResponse.Builder().code(200).body("slow").headersDelay(5, TimeUnit.SECONDS).build(),
        )
        val client = ControllerHttp.client(SSLContext.getDefault())

        val job = launch(Dispatchers.IO) { client.newCall(get(server)).await() }
        assertNotNull("request never reached the server", server.takeRequest(5, TimeUnit.SECONDS))

        job.cancelAndJoin()

        assertTrue(job.isCancelled)
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2)
        while (client.dispatcher.runningCallsCount() > 0 && System.nanoTime() < deadline) {
            Thread.sleep(50)
        }
        assertEquals(0, client.dispatcher.runningCallsCount())
    }
}
