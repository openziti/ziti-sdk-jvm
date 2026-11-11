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

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.openziti.identity.makeSSLContext
import org.openziti.net.internal.BypassProvider
import org.openziti.net.internal.Sockets
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.TimeUnit
import javax.net.ServerSocketFactory
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

// The controller client with the sockets it gets in seamless mode on the JVM (SocketChannelSocket), talking
// HTTP/2. The ziti controller does not negotiate h2 today, but one that does must work as well.
class SeamlessHttp2Test {

    private val loopback = InetAddress.getByName("127.0.0.1")
    private val ca = HeldCertificate.Builder().certificateAuthority(0).commonName("ca").build()
    private val servers = mutableListOf<MockWebServer>()

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    private fun server(vararg protocols: Protocol, smallBuffers: Boolean = false): MockWebServer {
        val cert = HeldCertificate.Builder().commonName("server").addSubjectAlternativeName("127.0.0.1").signedBy(ca).build()
        return MockWebServer().also { s ->
            s.useHttps(HandshakeCertificates.Builder().heldCertificate(cert, ca.certificate).build().sslSocketFactory())
            s.protocols = protocols.toList()
            if (smallBuffers) s.serverSocketFactory = SmallReceiveBuffers
            s.start(loopback, 0)
            servers += s
        }
    }

    private object SmallReceiveBuffers : ServerSocketFactory() {
        override fun createServerSocket(): ServerSocket = ServerSocket().apply { receiveBufferSize = 4096 }
        override fun createServerSocket(port: Int) = createServerSocket().apply { bind(InetSocketAddress(port)) }
        override fun createServerSocket(port: Int, backlog: Int) =
            createServerSocket().apply { bind(InetSocketAddress(port), backlog) }
        override fun createServerSocket(port: Int, backlog: Int, addr: InetAddress?) =
            createServerSocket().apply { bind(InetSocketAddress(addr, port), backlog) }
    }

    // the JVM bypass provider, with a send buffer small enough that writes come up short
    private class SmallSendBuffers : BypassProvider {
        private val jvm = Sockets.JavaBypassProvider()
        override fun getSSLSocketFactory(ssl: SSLContext): SSLSocketFactory = jvm.getSSLSocketFactory(ssl)
        override fun getSocketFactory(): SocketFactory = object : SocketFactory() {
            private val f = jvm.getSocketFactory()
            override fun createSocket(): Socket = f.createSocket().apply { sendBufferSize = 4096 }
            override fun createSocket(host: String?, port: Int) = f.createSocket(host, port)
            override fun createSocket(host: String?, port: Int, la: InetAddress?, lp: Int) = f.createSocket(host, port, la, lp)
            override fun createSocket(host: InetAddress?, port: Int) = f.createSocket(host, port)
            override fun createSocket(a: InetAddress?, port: Int, la: InetAddress?, lp: Int) = f.createSocket(a, port, la, lp)
        }
    }

    // ControllerHttp's client, with the sockets of the JVM bypass provider that seamless mode installs
    private fun seamlessClient(smallBuffers: Boolean = false): OkHttpClient {
        val ssl = makeSSLContext(null, emptyList(), listOf(ca.certificate))
        val jvm: () -> BypassProvider = { if (smallBuffers) SmallSendBuffers() else Sockets.JavaBypassProvider() }
        val trust = HandshakeCertificates.Builder().addTrustedCertificate(ca.certificate).build().trustManager
        return ControllerHttp.client(ssl).newBuilder()
            .socketFactory(LazyBypassSocketFactory(jvm))
            .sslSocketFactory(LazyBypassSSLSocketFactory(ssl, jvm), trust)
            .build()
    }

    private fun MockWebServer.root() = url("/").newBuilder().host("127.0.0.1").build()

    // the server reads the request body slowly, so the client's socket buffer fills and some writes come up short
    private fun slowReader() = MockResponse.Builder().throttleBody(256 * 1024, 50, TimeUnit.MILLISECONDS).body("ok").build()

    private val bigBody = ByteArray(8 * 1024 * 1024)

    @Test(timeout = 30_000)
    fun get_overHttp2() {
        val s = server(Protocol.HTTP_2, Protocol.HTTP_1_1)
        s.enqueue(MockResponse.Builder().body("ok").build())

        seamlessClient().newCall(Request.Builder().url(s.root()).build()).execute().use {
            assertEquals(Protocol.HTTP_2, it.protocol)
            assertEquals("ok", it.body.string())
        }
    }

    @Test(timeout = 30_000)
    fun largeBody_overHttp2() {
        val s = server(Protocol.HTTP_2, Protocol.HTTP_1_1)
        s.enqueue(slowReader())

        seamlessClient().newCall(Request.Builder().url(s.root()).post(bigBody.toRequestBody()).build()).execute().use {
            assertEquals(Protocol.HTTP_2, it.protocol)
            assertEquals("ok", it.body.string())
        }
        assertEquals(bigBody.size.toLong(), s.takeRequest().bodySize)
    }

    @Test(timeout = 30_000)
    fun shortWrites_overHttp2() {
        val s = server(Protocol.HTTP_2, Protocol.HTTP_1_1, smallBuffers = true)
        s.enqueue(slowReader())

        seamlessClient(smallBuffers = true).newCall(Request.Builder().url(s.root()).post(bigBody.toRequestBody()).build()).execute().use {
            assertEquals(Protocol.HTTP_2, it.protocol)
            assertEquals("ok", it.body.string())
        }
        assertEquals(bigBody.size.toLong(), s.takeRequest().bodySize)
    }

    @Test(timeout = 30_000)
    fun shortWrites_overHttp1() {
        val s = server(Protocol.HTTP_1_1, smallBuffers = true)
        s.enqueue(slowReader())

        seamlessClient(smallBuffers = true).newCall(Request.Builder().url(s.root()).post(bigBody.toRequestBody()).build()).execute().use {
            assertEquals(Protocol.HTTP_1_1, it.protocol)
            assertEquals("ok", it.body.string())
        }
        assertEquals(bigBody.size.toLong(), s.takeRequest().bodySize)
    }

    @Test(timeout = 30_000)
    fun largeBody_overHttp1() {
        val s = server(Protocol.HTTP_1_1)
        s.enqueue(slowReader())

        seamlessClient().newCall(Request.Builder().url(s.root()).post(bigBody.toRequestBody()).build()).execute().use {
            assertEquals(Protocol.HTTP_1_1, it.protocol)
            assertEquals("ok", it.body.string())
        }
        assertEquals(bigBody.size.toLong(), s.takeRequest().bodySize)
    }
}
