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

package org.openziti.identity

import io.jsonwebtoken.Jwts
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.api.http.ZitiHttpException
import org.openziti.api.http.edgeMapper
import java.io.IOException
import java.net.InetAddress
import java.util.Base64
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertFailsWith

// Enrollment against a mock controller over HTTPS: first with the server pinned by the key in the JWT, then,
// once a certificate is issued, with a TLS context built from the new identity.
class EnrollerTest {

    private val servers = mutableListOf<MockWebServer>()
    private val requests = ConcurrentLinkedQueue<RecordedRequest>()

    private lateinit var ca: HeldCertificate
    private lateinit var serverCert: HeldCertificate

    // per-test behavior of the mock controller
    private var enrollAnswer: () -> MockResponse = { json(mapOf("data" to mapOf("cert" to serverCert.certificatePem()))) }
    private var controllersDelaySeconds = 0L

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    private fun MockWebServer.base() = "https://127.0.0.1:$port"

    private fun json(body: Any, code: Int = 200) = MockResponse.Builder().code(code)
        .addHeader("Content-Type", "application/json")
        .body(if (body is String) body else edgeMapper.writeValueAsString(body))
        .build()

    private fun summary(r: RecordedRequest) =
        r.method + " " + r.url.encodedPath + (r.url.encodedQuery?.let { "?$it" } ?: "")

    private fun controller(requestClientCert: Boolean = false): MockWebServer {
        ca = HeldCertificate.Builder().certificateAuthority(0).commonName("test ca").build()
        serverCert = HeldCertificate.Builder().commonName("ctrl").addSubjectAlternativeName("127.0.0.1").signedBy(ca).build()
        val s = MockWebServer()
        s.useHttps(
            HandshakeCertificates.Builder()
                .heldCertificate(serverCert, ca.certificate)
                .addTrustedCertificate(ca.certificate)
                .build().sslSocketFactory()
        )
        if (requestClientCert) s.requestClientAuth()
        s.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests += request
                return when (request.url.encodedPath) {
                    "/.well-known/est/cacerts" -> MockResponse.Builder().code(200)
                        .addHeader("Content-Type", "application/pkcs7-mime")
                        .body(Base64.getMimeEncoder().encodeToString(ca.certificate.encoded))
                        .build()
                    "/version" -> json("""{"data":{"version":"v1","apiVersions":{"edge":{"v1":{"path":"/edge/client/v1"}}}},"meta":{}}""")
                    "/edge/client/v1/enroll" -> enrollAnswer()
                    "/edge/client/v1/authenticate" ->
                        json("""{"data":{"id":"sid","token":"stok","identityId":"iid","expiresAt":"2030-01-01T00:00:00Z"},"meta":{}}""")
                    "/edge/client/v1/controllers" -> {
                        val answer = """{"data":[{"apiAddresses":{"edge-client":[{"url":"${s.base()}/edge/client/v1","version":"v1"}]}}],"meta":{}}"""
                        MockResponse.Builder().code(200).addHeader("Content-Type", "application/json").body(answer)
                            .headersDelay(controllersDelaySeconds, TimeUnit.SECONDS).build()
                    }
                    else -> MockResponse.Builder().code(404).build()
                }
            }
        }
        s.start(InetAddress.getByName("127.0.0.1"), 0)
        servers += s
        return s
    }

    private fun jwt(s: MockWebServer, method: String = "ott", serverKey: java.security.Key = serverCert.keyPair.public) =
        ZitiJWT(
            Jwts.claims().issuer(s.base()).id("tok123").subject("alice").add("em", method).build(),
            serverKey
        )

    @Test
    fun ottEnrollment() {
        val s = controller()

        val cfg = Enroller(jwt(s)).enroll()

        assertEquals(
            listOf(
                "GET /.well-known/est/cacerts",
                "GET /version",
                "POST /edge/client/v1/enroll?method=ott&token=tok123",
                "POST /edge/client/v1/authenticate?method=cert",
                "GET /edge/client/v1/controllers?limit=10&offset=0",
            ),
            requests.map(::summary),
        )
        val enroll = requests.toList()[2]
        assertEquals("application/x-pem-file", enroll.headers["Content-Type"])
        assertTrue(enroll.body!!.utf8().startsWith("-----BEGIN CERTIFICATE REQUEST-----"))
        assertEquals("stok", requests.last().headers["zt-session"])

        assertEquals(s.base() + "/edge/client/v1", cfg.controller)
        assertEquals(listOf(s.base() + "/edge/client/v1"), cfg.apiEndpoints!!.toList())
        assertEquals(serverCert.certificatePem(), cfg.id.cert)
        assertTrue(cfg.id.key!!.startsWith("-----BEGIN PRIVATE KEY-----"))
        assertTrue(cfg.id.ca.contains(ca.certificatePem().lines().first()))
    }

    // the TLS context is swapped for one holding the new identity: from the enroll call on, the client certificate is presented
    @Test
    fun caEnrollment_swapsTls() {
        val s = controller(requestClientCert = true)
        val key = serverCert.privateKeyPkcs8Pem()
        val cert = serverCert.certificatePem()

        val cfg = Enroller(jwt(s, method = "ca")).enroll(key, cert)

        val sent = requests.toList()
        assertEquals("POST /edge/client/v1/enroll?method=ca&token=tok123", summary(sent[2]))
        assertEquals(0L, sent[2].bodySize)
        assertEquals("application/json", sent[2].headers["Accept"])
        assertEquals(key, cfg.id.key)
        assertEquals(cert, cfg.id.cert)

        // before the swap (cacerts, version) there is no client certificate, after it (enroll, authenticate, controllers) there is
        val presented = sent.map { it.handshake?.peerCertificates?.size ?: 0 }
        assertEquals(listOf(0, 0), presented.take(2))
        assertTrue(presented.drop(2).toString(), presented.drop(2).all { it > 0 })
    }

    @Test
    fun enrollError_throwsZitiHttpException() {
        val s = controller()
        enrollAnswer = { json("""{"error":{"code":"INVALID_ENROLLMENT_TOKEN","message":"bad token"}}""", code = 400) }

        val ex = assertFailsWith<ZitiHttpException> { Enroller(jwt(s)).enroll() }

        assertEquals(400, ex.code)
        assertEquals("bad token", ex.message)
        assertNotNull(ex.responseBody)
    }

    @Test
    fun wrongServerKey_isRejected() {
        val s = controller()
        val otherKey = HeldCertificate.Builder().build().keyPair.public

        assertFailsWith<IOException> { Enroller(jwt(s, serverKey = otherKey)) }

        assertTrue("nothing may be sent to a server that fails the key check", requests.isEmpty())
    }

    // enrolling must not hang on a controller that stops answering once the certificate is issued
    @Test
    fun listControllers_isBoundedTo3Seconds() {
        val s = controller()
        controllersDelaySeconds = 6
        val enroller = Enroller(jwt(s))

        val started = System.nanoTime()
        assertFailsWith<IOException> { enroller.enroll() }

        val elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - started)
        assertTrue("took ${elapsedMs}ms", elapsedMs < 4500)
    }
}
