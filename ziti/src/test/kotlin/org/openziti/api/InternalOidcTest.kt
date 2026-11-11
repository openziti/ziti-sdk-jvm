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

package org.openziti.api

import kotlinx.coroutines.test.runTest
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.identity.makeSSLContext
import java.net.InetAddress
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertFailsWith

// The internal OIDC login against a mock IdP over HTTPS: the flow is a chain of redirects that must be read, not followed.
class InternalOidcTest {

    private val servers = mutableListOf<MockWebServer>()
    private val requests = ConcurrentLinkedQueue<RecordedRequest>()

    // per-test behavior of the mock IdP
    private var callbackState: ((sent: String) -> String) = { it }
    private var tokenEndpoint: (MockWebServer) -> String = { "${it.base()}/oidc/oauth/token" }
    private var tokenAnswer: (grant: String) -> MockResponse = { grant ->
        if (grant == "refresh_token") tokens("AT2", "RT2", 300) else tokens("AT", "RT", 600)
    }
    private var sentState = ""

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    private fun MockWebServer.base() = "https://127.0.0.1:$port"

    private fun tokens(access: String, refresh: String?, expiresIn: Int) = MockResponse.Builder().code(200)
        .addHeader("Content-Type", "application/json")
        .body(
            buildString {
                append("""{"access_token":"$access",""")
                if (refresh != null) append(""""refresh_token":"$refresh",""")
                append(""""expires_in":$expiresIn}""")
            }
        ).build()

    private fun redirect(location: String) = MockResponse.Builder().code(302).addHeader("Location", location).build()

    private fun form(r: RecordedRequest): Map<String, String> =
        r.body!!.utf8().split("&").filter { it.isNotEmpty() }.associate {
            val (k, v) = it.split("=", limit = 2)
            k to URLDecoder.decode(v, StandardCharsets.UTF_8)
        }

    private fun summary(r: RecordedRequest) = r.method + " " + r.url.encodedPath

    private lateinit var ca: HeldCertificate

    private fun idp(): MockWebServer {
        ca = HeldCertificate.Builder().certificateAuthority(0).commonName("test ca").build()
        val serverCert = HeldCertificate.Builder().commonName("idp").addSubjectAlternativeName("127.0.0.1").signedBy(ca).build()
        val s = MockWebServer()
        s.useHttps(HandshakeCertificates.Builder().heldCertificate(serverCert, ca.certificate).build().sslSocketFactory())
        s.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests += request
                return when (request.url.encodedPath) {
                    "/oidc/.well-known/openid-configuration" -> MockResponse.Builder().code(200)
                        .addHeader("Content-Type", "application/json")
                        .body("""{"authorization_endpoint":"${s.base()}/oidc/authorize","token_endpoint":"${tokenEndpoint(s)}"}""")
                        .build()
                    "/oidc/authorize" -> {
                        sentState = form(request)["state"]!!
                        redirect("/oidc/login/userpass?authRequestID=abc")
                    }
                    "/oidc/login/userpass" -> redirect("${s.base()}/oidc/authorize/callback?id=abc")
                    "/oidc/authorize/callback" ->
                        redirect("http://localhost:8080/auth/callback?code=CODE&state=${callbackState(sentState)}")
                    "/oidc/oauth/token" -> tokenAnswer(form(request)["grant_type"]!!)
                    else -> MockResponse.Builder().code(404).build()
                }
            }
        }
        s.start(InetAddress.getByName("127.0.0.1"), 0)
        servers += s
        return s
    }

    private fun oidc(s: MockWebServer) = InternalOIDC(s.base(), makeSSLContext(null, null, listOf(ca.certificate)))

    @Test
    fun login_fullFlow() = runTest {
        val s = idp()

        val token = oidc(s).login()

        assertEquals(ZitiAuthenticator.TokenType.BEARER, token.type)
        assertEquals("AT", token.token)
        // exactly these requests: the redirects were read, not followed
        assertEquals(
            listOf(
                "GET /oidc/.well-known/openid-configuration",
                "POST /oidc/authorize",
                "POST /oidc/login/userpass",
                "GET /oidc/authorize/callback",
                "POST /oidc/oauth/token",
            ),
            requests.map(::summary),
        )
        assertTrue(requests.all { it.headers["Cookie"] == null })

        val authorize = requests.toList()[1]
        assertEquals("application/x-www-form-urlencoded", authorize.headers["Content-Type"])
        assertEquals("application/json", authorize.headers["Accept"])
        with(form(authorize)) {
            assertEquals("code", this["response_type"])
            assertEquals("openziti", this["client_id"])
            assertEquals("S256", this["code_challenge_method"])
        }

        with(form(requests.last())) {
            assertEquals("authorization_code", this["grant_type"])
            assertEquals("CODE", this["code"])
            assertNotNull(this["code_verifier"])
        }
    }

    @Test
    fun login_stateMismatch_fails() = runTest {
        callbackState = { "somethingElse" }
        val s = idp()

        val ex = assertFailsWith<IllegalArgumentException> { oidc(s).login() }

        assertEquals("OIDC state mismatch", ex.message)
    }

    @Test
    fun login_httpTokenEndpoint_failsBeforeSending() = runTest {
        tokenEndpoint = { "http://127.0.0.1:${it.port}/oidc/oauth/token" }
        val s = idp()

        val ex = assertFailsWith<IllegalArgumentException> { oidc(s).login() }

        assertTrue(ex.message, ex.message!!.contains("HTTPS"))
        assertTrue(requests.none { it.url.encodedPath == "/oidc/oauth/token" })
    }

    @Test
    fun refresh_ok() = runTest {
        val s = idp()
        val auth = oidc(s)
        auth.login()

        val token = auth.refresh()

        assertEquals("AT2", token.token)
        assertEquals(ZitiAuthenticator.TokenType.BEARER, token.type)
        val req = requests.last()
        assertEquals("POST /oidc/oauth/token", summary(req))
        assertEquals(InternalOIDC.CLIENT_ID_BASIC_AUTH, req.headers["Authorization"])
        with(form(req)) {
            assertEquals("refresh_token", this["grant_type"])
            assertEquals("RT", this["refresh_token"])
            assertEquals("openziti", this["client_id"])
        }
    }

    @Test
    fun refresh_401_throwsAuthException() = runTest {
        val s = idp()
        val auth = oidc(s)
        auth.login()
        tokenAnswer = { MockResponse.Builder().code(401).build() }

        assertFailsWith<ZitiAuthenticator.AuthException> { auth.refresh() }
    }

    @Test
    fun refresh_noRefreshToken_throwsAuthException() = runTest {
        tokenAnswer = { tokens("AT", null, 600) }
        val s = idp()
        val auth = oidc(s)
        auth.login()
        val before = requests.size

        assertFailsWith<ZitiAuthenticator.AuthException> { auth.refresh() }

        assertEquals(before, requests.size)
    }

    @Test
    fun refresh_500_throwsPlainException() = runTest {
        val s = idp()
        val auth = oidc(s)
        auth.login()
        tokenAnswer = { MockResponse.Builder().code(500).build() }

        val ex = assertFailsWith<Exception> { auth.refresh() }

        assertNull(ex.cause)
        assertTrue(ex.message, ex.message!!.contains("500"))
        assertTrue(ex !is ZitiAuthenticator.AuthException)
    }
}
