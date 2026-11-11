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

import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.api.ZitiAuthenticator
import org.openziti.edge.model.ListVersionEnvelope
import org.openziti.edge.model.MfaCode
import java.io.IOException
import java.net.InetAddress
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import kotlin.test.assertFailsWith

class EdgeClientTest {

    private val servers = mutableListOf<MockWebServer>()
    private val http: OkHttpClient = ControllerHttp.client(SSLContext.getDefault())

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    private fun server(vararg responses: MockResponse): MockWebServer =
        MockWebServer().also { s ->
            responses.forEach { s.enqueue(it) }
            s.start(InetAddress.getByName("127.0.0.1"), 0)
            servers += s
        }

    private fun MockWebServer.base() = "http://127.0.0.1:$port"

    private fun json(body: String, code: Int = 200) =
        MockResponse.Builder().code(code).addHeader("Content-Type", "application/json").body(body).build()

    private val versionJson = """{"data":{"version":"v1.2.3"},"meta":{}}"""

    private fun accessToken(type: ZitiAuthenticator.TokenType) =
        ZitiAuthenticator.ZitiAccessToken(type, "T", OffsetDateTime.now().plusHours(1))

    @Test
    fun get_sendsAcceptAndParsesEnvelope() = runTest {
        val server = server(json(versionJson))

        val envelope = EdgeClient(http, server.base()).get<ListVersionEnvelope>("/version")

        assertEquals("v1.2.3", envelope.data.version)
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("/version", req.url.encodedPath)
        assertEquals("application/json", req.headers["Accept"])
    }

    @Test
    fun queryOf_dropsNullsAndRepeatsIterables() = runTest {
        val query = queryOf("limit" to 100, "filter" to null, "configTypes" to listOf("all", "x"))

        assertEquals(listOf("limit" to "100", "configTypes" to "all", "configTypes" to "x"), query)

        val server = server(json("{}"))
        EdgeClient(http, server.base()).send("GET", "/services", query)
        assertEquals("limit=100&configTypes=all&configTypes=x", server.takeRequest().url.encodedQuery)
    }

    @Test
    fun post_sendsJsonBodyAndContentType() = runTest {
        val server = server(json(versionJson))

        EdgeClient(http, server.base()).post<ListVersionEnvelope>("/current-identity/mfa/verify", MfaCode().code("123456"))

        val req = server.takeRequest()
        assertEquals("POST", req.method)
        assertEquals("""{"code":"123456"}""", req.body?.utf8())
        assertEquals("application/json", req.headers["Content-Type"])
    }

    @Test
    fun post_withoutBody_sendsEmptyBody() = runTest {
        val server = server(json("{}"))

        EdgeClient(http, server.base()).send("POST", "/current-identity/mfa")

        val req = server.takeRequest()
        assertEquals("POST", req.method)
        assertEquals(0L, req.bodySize)
    }

    @Test
    fun stringBody_usesGivenContentType() = runTest {
        val server = server(json("{}"))

        EdgeClient(http, server.base())
            .send("POST", "/enroll", headers = mapOf("Content-Type" to "application/x-pem-file"), body = "PEM")

        val req = server.takeRequest()
        assertEquals("PEM", req.body?.utf8())
        assertEquals("application/x-pem-file", req.headers["Content-Type"])
    }

    @Test
    fun bearerToken_setsAuthorization() = runTest {
        val server = server(json("{}"))
        val edge = EdgeClient(http, server.base()).apply { token = accessToken(ZitiAuthenticator.TokenType.BEARER) }

        edge.send("GET", "/current-identity")

        val req = server.takeRequest()
        assertEquals("Bearer T", req.headers["Authorization"])
        assertNull(req.headers["zt-session"])
    }

    @Test
    fun apiSessionToken_setsZtSession() = runTest {
        val server = server(json("{}"))
        val edge = EdgeClient(http, server.base()).apply { token = accessToken(ZitiAuthenticator.TokenType.API_SESSION) }

        edge.send("GET", "/current-identity")

        val req = server.takeRequest()
        assertEquals("T", req.headers["zt-session"])
        assertNull(req.headers["Authorization"])
    }

    @Test
    fun extraHeaders_areSent() = runTest {
        val server = server(json("{}"))

        EdgeClient(http, server.base()).delete("/current-identity/mfa", mapOf("mfa-validation-code" to "123"))

        val req = server.takeRequest()
        assertEquals("DELETE", req.method)
        assertEquals("123", req.headers["mfa-validation-code"])
    }

    @Test
    fun nonSuccess_throwsZitiHttpException() = runTest {
        val body = """{"error":{"code":"UNAUTHORIZED"}}"""
        val server = server(json(body, code = 401))

        val ex = assertFailsWith<ZitiHttpException> { EdgeClient(http, server.base()).send("GET", "/x") }

        assertEquals(401, ex.code)
        assertEquals(body, ex.responseBody)
    }

    @Test
    fun emptyErrorBody_isZitiHttpException() = runTest {
        val server = server(MockResponse.Builder().code(502).build())

        val ex = assertFailsWith<ZitiHttpException> { EdgeClient(http, server.base()).send("GET", "/x") }

        assertEquals(502, ex.code)
        assertTrue(ex.responseBody.isNullOrEmpty())
    }

    @Test
    fun redirect_isNotFollowed() = runTest {
        val server = server(
            MockResponse.Builder().code(302).addHeader("Location", "/elsewhere").build(),
            json("{}"),
        )

        val ex = assertFailsWith<ZitiHttpException> { EdgeClient(http, server.base()).send("GET", "/x") }

        assertEquals(302, ex.code)
        assertEquals(1, server.requestCount)
    }

    @Test
    fun nonJson200_isIOException() = runTest {
        val server = server(MockResponse.Builder().code(200).body("<html>captive portal</html>").build())

        // an IOException (not a ZitiHttpException): a reachable server with a broken answer is retried like an outage
        assertFailsWith<IOException> { EdgeClient(http, server.base()).get<ListVersionEnvelope>("/version") }
    }

    @Test
    fun emptyBodyWhereDataExpected_isIOException() = runTest {
        val server = server(MockResponse.Builder().code(200).build())

        assertFailsWith<IOException> { EdgeClient(http, server.base()).get<ListVersionEnvelope>("/version") }
    }

    @Test
    fun baseUri_normalizesSlashes() = runTest {
        assertEquals("https://h:1280", EdgeClient(http, "https://h:1280/").baseUri)

        val edge = EdgeClient(http, "https://h:1280/")
        edge.setBasePath("/edge/client/v1/")
        assertEquals("https://h:1280/edge/client/v1", edge.baseUri)

        edge.setBasePath("")
        assertEquals("https://h:1280", edge.baseUri)

        edge.updateBaseUri("https://h:1280/edge/client/v1/")
        assertEquals("https://h:1280/edge/client/v1", edge.baseUri)

        val server = server(json("{}"))
        EdgeClient(http, server.base() + "/edge/client/v1/").send("GET", "/version")
        assertEquals("/edge/client/v1/version", server.takeRequest().url.encodedPath)
    }

    @Test
    fun baseUri_ipv6Literal() {
        assertEquals("https://[::1]:1280", EdgeClient(http, "https://[::1]:1280").baseUri)
    }

    @Test
    fun withClient_keepsStateAndSwapsClient() = runTest {
        val server = server(json("{}"), json("{}"))
        val counter = AtomicInteger()
        val replacement = http.newBuilder().addInterceptor { chain ->
            counter.incrementAndGet()
            chain.proceed(chain.request())
        }.build()
        val original = EdgeClient(http, server.base()).apply { token = accessToken(ZitiAuthenticator.TokenType.BEARER) }

        val copy = original.withClient(replacement)
        copy.send("GET", "/x")
        original.send("GET", "/x")

        assertEquals(1, counter.get())
        assertEquals(original.baseUri, copy.baseUri)
        assertSame(original.token, copy.token)
    }
}
