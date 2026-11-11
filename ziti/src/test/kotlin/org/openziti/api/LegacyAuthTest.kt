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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.api.http.ZitiHttpException
import org.openziti.api.http.edgeMapper
import java.net.InetAddress
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import javax.net.ssl.SSLContext
import kotlin.test.assertFailsWith

class LegacyAuthTest {

    private val servers = mutableListOf<MockWebServer>()
    private val requests = ConcurrentLinkedQueue<RecordedRequest>()

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    private fun server(handler: (RecordedRequest) -> MockResponse): MockWebServer {
        val s = MockWebServer()
        s.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests += request
                return handler(request)
            }
        }
        s.start(InetAddress.getByName("127.0.0.1"), 0)
        servers += s
        return s
    }

    // the controller endpoint as the authenticator receives it: with the edge API prefix
    private fun MockWebServer.ep() = "http://127.0.0.1:$port/edge/client/v1"

    private fun session(token: String) = MockResponse.Builder().code(200)
        .addHeader("Content-Type", "application/json")
        .body("""{"data":{"id":"sid","token":"$token","identityId":"iid","expiresAt":"2030-01-01T00:00:00Z"},"meta":{}}""")
        .build()

    private fun status(code: Int) = MockResponse.Builder().code(code)
        .body("""{"error":{"code":"UNAUTHORIZED","message":"no"}}""").build()

    private fun summary(r: RecordedRequest) =
        r.method + " " + r.url.encodedPath + (r.url.encodedQuery?.let { "?$it" } ?: "")

    @Test
    fun login_postsCertAuthentication() = runTest {
        val s = server { session("tok") }
        val auth = LegacyAuth(s.ep(), SSLContext.getDefault())

        val token = auth.login()

        assertEquals(ZitiAuthenticator.TokenType.API_SESSION, token.type)
        assertEquals("tok", token.token)
        assertTrue(OffsetDateTime.parse("2030-01-01T00:00:00Z").isEqual(token.expiration))
        assertEquals("tok", auth.apiSession.token)
        assertEquals("POST /edge/client/v1/authenticate?method=cert", summary(requests.single()))
        val body = edgeMapper.readTree(requests.single().body!!.utf8())
        assertEquals("ziti-sdk-java", body["sdkInfo"]["type"].asText())
    }

    @Test
    fun login_unauthorized_throwsAuthException() = runTest {
        val s = server { status(401) }

        val ex = assertFailsWith<ZitiAuthenticator.AuthException> { LegacyAuth(s.ep(), SSLContext.getDefault()).login() }

        assertEquals(401, (ex.cause as ZitiHttpException).code)
    }

    @Test
    fun login_serverError_rethrowsOriginal() = runTest {
        val s = server { status(500) }

        val ex = assertFailsWith<ZitiHttpException> { LegacyAuth(s.ep(), SSLContext.getDefault()).login() }

        assertEquals(500, ex.code)
    }

    @Test
    fun refresh_sendsZtSessionHeader() = runTest {
        var n = 0
        val s = server { session(if (n++ == 0) "tok" else "tok2") }
        val auth = LegacyAuth(s.ep(), SSLContext.getDefault())
        auth.login()

        val token = auth.refresh()

        assertEquals("tok2", token.token)
        assertEquals(ZitiAuthenticator.TokenType.API_SESSION, token.type)
        assertEquals("tok2", auth.apiSession.token)
        val refresh = requests.last()
        assertEquals("GET /edge/client/v1/current-api-session", summary(refresh))
        assertEquals("tok", refresh.headers["zt-session"])
    }

    @Test
    fun refresh_unauthorized_throwsAuthException() = runTest {
        var n = 0
        val s = server { if (n++ == 0) session("tok") else status(401) }
        val auth = LegacyAuth(s.ep(), SSLContext.getDefault())
        auth.login()

        assertFailsWith<ZitiAuthenticator.AuthException> { auth.refresh() }
    }

    @Test
    fun refresh_serverError_isNotAnAuthException() = runTest {
        var n = 0
        val s = server { if (n++ == 0) session("tok") else status(500) }
        val auth = LegacyAuth(s.ep(), SSLContext.getDefault())
        auth.login()

        val ex = assertFailsWith<ZitiHttpException> { auth.refresh() }

        assertEquals(500, ex.code)
    }
}
