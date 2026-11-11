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

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertSame
import org.junit.Test
import org.openziti.net.internal.BypassProvider
import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

// Sockets.provider is lateinit and only set by Sockets.init, which not every entry point calls
// (Ziti.newContext(IdentityConfig), Ziti.createEnrollment). The factories must cope with that.
class LazyBypassSocketsTest {

    private class RecordingSocketFactory : SocketFactory() {
        var created = 0

        override fun createSocket(): Socket { created++; return Socket() }
        override fun createSocket(host: String?, port: Int): Socket = throw UnsupportedOperationException()
        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket =
            throw UnsupportedOperationException()
        override fun createSocket(host: InetAddress?, port: Int): Socket = throw UnsupportedOperationException()
        override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket =
            throw UnsupportedOperationException()
    }

    private class RecordingSSLSocketFactory : SSLSocketFactory() {
        var layered: List<Any?>? = null

        override fun getDefaultCipherSuites(): Array<String> = arrayOf("RECORDED")
        override fun getSupportedCipherSuites(): Array<String> = arrayOf("RECORDED")
        override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
            layered = listOf(s, host, port, autoClose)
            return Socket()
        }
        override fun createSocket(host: String?, port: Int): Socket = throw UnsupportedOperationException()
        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket =
            throw UnsupportedOperationException()
        override fun createSocket(host: InetAddress?, port: Int): Socket = throw UnsupportedOperationException()
        override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket =
            throw UnsupportedOperationException()
    }

    private class FakeProvider(
        val sockets: RecordingSocketFactory = RecordingSocketFactory(),
        val ssl: RecordingSSLSocketFactory = RecordingSSLSocketFactory(),
    ) : BypassProvider {
        var socketFactoryCalls = 0
        override fun getSocketFactory(): SocketFactory { socketFactoryCalls++; return sockets }
        override fun getSSLSocketFactory(ssl: SSLContext): SSLSocketFactory = this.ssl
    }

    @Test
    fun usesDefaultFactoriesWhileUninitialized() {
        val ctx = SSLContext.getDefault()

        val socket = LazyBypassSocketFactory { null }.createSocket()
        val suites = LazyBypassSSLSocketFactory(ctx) { null }.defaultCipherSuites

        assertNotNull(socket)
        assertFalse(socket.isConnected)
        assertArrayEquals(ctx.socketFactory.defaultCipherSuites, suites)
    }

    @Test
    fun delegatesOnceProviderAppears() {
        val fake = FakeProvider()
        var current: BypassProvider? = null
        val factory = LazyBypassSocketFactory { current }

        factory.createSocket()
        assertEquals(0, fake.socketFactoryCalls)

        current = fake
        factory.createSocket()

        assertEquals(1, fake.socketFactoryCalls)
        assertEquals(1, fake.sockets.created)
    }

    @Test
    fun sslFactoryDelegatesLayeredSocketCreation() {
        val fake = FakeProvider()
        val factory = LazyBypassSSLSocketFactory(SSLContext.getDefault()) { fake }
        val raw = Socket()

        factory.createSocket(raw, "h", 443, true)

        val args = checkNotNull(fake.ssl.layered)
        assertSame(raw, args[0])
        assertEquals(listOf("h", 443, true), args.drop(1))
    }
}
