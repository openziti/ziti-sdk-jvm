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

package org.openziti.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.openziti.api.ApiSession
import org.openziti.api.ZitiAuthenticator
import org.openziti.net.Channel
import org.openziti.net.ZitiProtocol
import java.io.Closeable
import java.io.DataInputStream
import java.math.BigInteger
import java.net.InetAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.time.OffsetDateTime
import java.util.Date
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.seconds

class ChannelImplTest {

    private val refreshed = ZitiAuthenticator.ZitiAccessToken(
        ZitiAuthenticator.TokenType.BEARER, "refreshed-token", OffsetDateTime.now().plusMinutes(5)
    )

    // A router that never answers a TokenUpdate (some routers drop the failure reply) must not wedge the caller.
    @Test
    fun updateTokenReturnsWhenRouterNeverReplies() = runTest(timeout = 5.seconds) {
        withConnectedChannel { router, ch, _ ->
            ch.updateToken(refreshed)

            // the update really went out; otherwise returning early would prove nothing
            assertEquals("refreshed-token", router.tokenUpdates.next())
        }
    }

    // The router keeps the old token until it expires, so a stalled update drops the connection;
    // the channel then reconnects with whichever session is current.
    @Test
    fun stalledTokenUpdateReconnectsWithCurrentSession() = runTest(timeout = 5.seconds) {
        withConnectedChannel { router, ch, session ->
            assertEquals("old-session-token", router.hellos.next())

            session.set(ApiSession().identityId("test-identity").token("current-session-token"))
            ch.updateToken(refreshed)

            assertEquals("current-session-token", router.hellos.next())
        }
    }
}

private suspend fun <T : Any> LinkedBlockingQueue<T>.next(): T? =
    withContext(Dispatchers.IO) { poll(3, TimeUnit.SECONDS) }

private suspend fun withConnectedChannel(
    block: suspend (FakeRouter, ChannelImpl, AtomicReference<ApiSession>) -> Unit
) {
    val tls = selfSignedTls()
    FakeRouter(tls.server).use { router ->
        val session = AtomicReference(ApiSession().identityId("test-identity").token("old-session-token"))
        val ch = ChannelImpl("tls://127.0.0.1:${router.port}", tls.client) { session.get() }.apply { start() }

        try {
            withContext(Dispatchers.IO) {
                val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3)
                while (ch.state !is Channel.State.Connected) {
                    check(System.nanoTime() < deadline) { "channel did not connect to fake router: ${ch.state}" }
                    Thread.sleep(10)
                }
            }
            block(router, ch, session)
        } finally {
            ch.close()
        }
    }
}

private class SelfSignedTls(val server: SSLContext, val client: SSLContext)

private fun selfSignedTls(): SelfSignedTls {
    val keys = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
    val subject = X500Name("CN=fake-router")
    val now = System.currentTimeMillis()
    val signer = JcaContentSignerBuilder("SHA256withRSA").build(keys.private)
    val cert: X509Certificate = JcaX509CertificateConverter().getCertificate(
        JcaX509v3CertificateBuilder(
            subject, BigInteger.ONE, Date(now - 60_000), Date(now + 3_600_000), subject, keys.public
        ).build(signer)
    )

    val serverKeys = KeyStore.getInstance("PKCS12").apply {
        load(null, null)
        setKeyEntry("router", keys.private, charArrayOf(), arrayOf(cert))
    }
    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        .apply { init(serverKeys, charArrayOf()) }

    val clientTrust = KeyStore.getInstance("PKCS12").apply {
        load(null, null)
        setCertificateEntry("router", cert)
    }
    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        .apply { init(clientTrust) }

    return SelfSignedTls(
        server = SSLContext.getInstance("TLS").apply { init(kmf.keyManagers, null, null) },
        client = SSLContext.getInstance("TLS").apply { init(null, tmf.trustManagers, null) },
    )
}

/**
 * Minimal edge router: accepts any number of connections, acknowledges hello and latency probes,
 * records the session token of every hello and the body of every TokenUpdate, and never replies to a TokenUpdate.
 */
private class FakeRouter(tls: SSLContext) : Closeable {
    private class Frame(val type: Int, val seq: Int, val headers: Map<Int, ByteArray>, val body: ByteArray)

    private val server = tls.serverSocketFactory.createServerSocket(0, 5, InetAddress.getLoopbackAddress())
    val port: Int get() = server.localPort
    val hellos = LinkedBlockingQueue<String>()
    val tokenUpdates = LinkedBlockingQueue<String>()

    init {
        thread(isDaemon = true, name = "fake-router") {
            runCatching {
                while (true) {
                    val sock = server.accept()
                    thread(isDaemon = true, name = "fake-router-conn") { runCatching { serve(sock) } }
                }
            }
        }
    }

    override fun close() = server.close()

    private fun serve(sock: Socket) = sock.use {
        val input = DataInputStream(sock.getInputStream())
        val output = sock.getOutputStream()
        while (true) {
            val frame = readFrame(input)
            when (frame.type) {
                ZitiProtocol.ContentType.HelloType.id -> {
                    hellos.put(String(frame.headers.getValue(ZitiProtocol.Header.SessionToken.id)))
                    output.write(successReply(frame.seq))
                    output.flush()
                }
                ZitiProtocol.ContentType.LatencyType.id -> {
                    output.write(successReply(frame.seq))
                    output.flush()
                }
                ZitiProtocol.ContentType.TokenUpdate.id -> tokenUpdates.put(String(frame.body))
            }
        }
    }

    // wire format: magic(4) type(4) seq(4) headersLen(4) bodyLen(4), all little endian, then headers, then body
    private fun readFrame(input: DataInputStream): Frame {
        val header = ByteBuffer.wrap(ByteArray(20).also { input.readFully(it) }).order(ByteOrder.LITTLE_ENDIAN)
        header.position(4)
        val type = header.int
        val seq = header.int
        val headersLen = header.int
        val bodyLen = header.int

        val rawHeaders = ByteBuffer.wrap(ByteArray(headersLen).also { input.readFully(it) }).order(ByteOrder.LITTLE_ENDIAN)
        val headers = mutableMapOf<Int, ByteArray>()
        while (rawHeaders.hasRemaining()) {
            val key = rawHeaders.int
            headers[key] = ByteArray(rawHeaders.int).also { rawHeaders.get(it) }
        }
        return Frame(type, seq, headers, ByteArray(bodyLen).also { input.readFully(it) })
    }

    // Result message with headers ReplyFor(1) = request seq and ResultSuccess(2) = true
    private fun successReply(replyTo: Int): ByteArray {
        val headersLen = (4 + 4 + 4) + (4 + 4 + 1)
        return ByteBuffer.allocate(20 + headersLen).order(ByteOrder.LITTLE_ENDIAN)
            .put(ZitiProtocol.VERSION)
            .putInt(ZitiProtocol.ContentType.ResultType.id)
            .putInt(0)
            .putInt(headersLen)
            .putInt(0)
            .putInt(1).putInt(4).putInt(replyTo)
            .putInt(2).putInt(1).put(1)
            .array()
    }
}
