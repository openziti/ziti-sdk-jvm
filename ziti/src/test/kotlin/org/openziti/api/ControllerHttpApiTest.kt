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

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import mockwebserver3.Dispatcher
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import mockwebserver3.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.ZitiException
import org.openziti.ZitiException.Errors
import org.openziti.api.http.edgeMapper
import org.openziti.edge.model.Capabilities
import org.openziti.edge.model.ServiceDetail
import org.openziti.impl.isTransientFailure
import java.net.InetAddress
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import javax.net.ssl.SSLContext
import kotlin.test.assertFailsWith

// Controller talking to a mock controller over plain HTTP: what goes on the wire and how answers are interpreted.
class ControllerHttpApiTest {

    private val servers = mutableListOf<MockWebServer>()
    private val requests = ConcurrentLinkedQueue<RecordedRequest>()
    private val ssl = SSLContext.getDefault()

    @After
    fun tearDown() {
        servers.forEach { it.close() }
    }

    // the handler runs with the server as receiver, so it can use base()
    private fun server(handler: MockWebServer.(RecordedRequest) -> MockResponse): MockWebServer {
        val s = MockWebServer()
        s.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                requests += request
                return handler(s, request)
            }
        }
        s.start(InetAddress.getByName("127.0.0.1"), 0)
        servers += s
        return s
    }

    private fun MockWebServer.base() = "http://127.0.0.1:$port"

    private fun json(body: String, code: Int = 200) =
        MockResponse.Builder().code(code).addHeader("Content-Type", "application/json").body(body).build()

    private fun status(code: Int, body: String = "") = MockResponse.Builder().code(code).body(body).build()

    private fun versionJson(base: String) =
        """{"data":{"version":"v1","revision":"r","capabilities":["OIDC_AUTH"],"apiVersions":{"edge":{"v1":{"path":"/edge/client/v1","apiBaseUrls":["$base/edge/client/v1"]}}}},"meta":{}}"""

    private val sessionJson =
        """{"data":{"id":"sid","token":"tok","identityId":"iid","expiresAt":"2030-01-01T00:00:00Z","identity":{"name":"alice"}},"meta":{}}"""

    private val notFound = """{"error":{"code":"NOT_FOUND","message":"nf"}}"""

    private fun summary(r: RecordedRequest) =
        r.method + " " + r.url.encodedPath + (r.url.encodedQuery?.let { "?$it" } ?: "")

    private fun closedBase(): String {
        val s = MockWebServer()
        s.start(InetAddress.getByName("127.0.0.1"), 0)
        val base = s.base()
        s.close()
        return base
    }

    private val expires: OffsetDateTime get() = OffsetDateTime.now().plusHours(1)

    // --- finding and logging in to a controller

    @Test
    fun getActiveController_probesVersionAtRoot() = runTest {
        val s = server { json(versionJson(base())) }

        val controller = Controller.getActiveController(listOf(s.base()), ssl)

        assertEquals("GET /version", summary(requests.first()))
        assertTrue(Capabilities.OIDC_AUTH in controller.capabilities)
        assertEquals(s.base(), controller.endpoint)
    }

    @Test
    fun getActiveController_skipsUnreachableEndpoint() = runTest {
        val alive = server { json(versionJson(base())) }

        val controller = Controller.getActiveController(listOf(closedBase(), alive.base()), ssl)

        assertEquals(alive.base(), controller.endpoint)
    }

    @Test
    fun getActiveController_noneReachable_throwsNotAvailable() = runTest {
        assertFailsWith<Controller.NotAvailableException> {
            Controller.getActiveController(listOf(closedBase()), ssl)
        }
    }

    @Test
    fun login_cert() = runTest {
        val s = server { r ->
            when (r.url.encodedPath) {
                "/version" -> json(versionJson(base()))
                "/edge/client/v1/authenticate" -> json(sessionJson)
                else -> status(404)
            }
        }
        val controller = Controller(s.base(), ssl)

        val session = controller.login()

        assertEquals("tok", session.token)
        assertEquals(listOf("GET /version", "POST /edge/client/v1/authenticate?method=cert"), requests.map(::summary))
        val body = edgeMapper.readTree(requests.last().body!!.utf8())
        assertEquals("ziti-sdk-java", body["sdkInfo"]["type"].asText())
        assertEquals(s.base() + "/edge/client/v1", controller.endpoint)
    }

    @Test
    fun login_password() = runTest {
        val s = server { r ->
            when (r.url.encodedPath) {
                "/version" -> json(versionJson(base()))
                else -> json(sessionJson)
            }
        }

        Controller(s.base(), ssl).login(Login("u", "p"))

        assertEquals("POST /edge/client/v1/authenticate?method=password", summary(requests.last()))
        val body = edgeMapper.readTree(requests.last().body!!.utf8())
        assertEquals("u", body["username"].asText())
        assertEquals("p", body["password"].asText())
    }

    @Test
    fun setAccessToken_bearerAndApiSession() = runTest {
        val s = server { json(sessionJson) }
        val controller = Controller(s.base(), ssl)

        controller.setAccessToken(ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.BEARER, "T", expires))
        assertEquals("GET /current-api-session", summary(requests.last()))
        assertEquals("Bearer T", requests.last().headers["Authorization"])
        assertNull(requests.last().headers["zt-session"])

        controller.setAccessToken(ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.API_SESSION, "T2", expires))
        assertEquals("T2", requests.last().headers["zt-session"])
        assertNull(requests.last().headers["Authorization"])
    }

    // --- listing

    @Test
    fun getServices_pagesUntilTotalCount() = runTest {
        val s = server { r ->
            val offset = r.url.queryParameter("offset")!!.toInt()
            val items = (0 until minOf(100, 250 - offset)).joinToString(",") { """{"id":"s${offset + it}","name":"n"}""" }
            json("""{"data":[$items],"meta":{"pagination":{"limit":100,"offset":$offset,"totalCount":250}}}""")
        }

        val services = Controller(s.base(), ssl).getServices().toList()

        assertEquals(250, services.size)
        assertEquals("s0", services.first().id)
        assertEquals("s249", services.last().id)
        assertEquals(listOf("0", "100", "200"), requests.map { it.url.queryParameter("offset") })
        assertTrue(requests.all { it.url.encodedPath == "/services" && it.url.queryParameterValues("configTypes") == listOf("all") })
    }

    @Test
    fun getServiceTerminators_pages() = runTest {
        val s = server { r ->
            val offset = r.url.queryParameter("offset")!!.toInt()
            val items = (0 until minOf(100, 150 - offset)).joinToString(",") { """{"id":"t${offset + it}"}""" }
            json("""{"data":[$items],"meta":{"pagination":{"limit":100,"offset":$offset,"totalCount":150}}}""")
        }

        val terminators = Controller(s.base(), ssl).getServiceTerminators(ServiceDetail().id("svc1")).toList()

        assertEquals(150, terminators.size)
        assertEquals(listOf("0", "100"), requests.map { it.url.queryParameter("offset") })
        assertTrue(requests.all { it.url.encodedPath == "/services/svc1/terminators" })
    }

    @Test
    fun getServiceRouters_isNotPaged() = runTest {
        val s = server { json("""{"data":{"edgeRouters":[{"name":"er1","hostname":"h1"}]},"meta":{}}""") }

        val routers = Controller(s.base(), ssl).getServiceRouters(ServiceDetail().id("svc1"))

        assertEquals(listOf("er1"), routers.map { it.name })
        assertEquals("GET /services/svc1/edge-routers", summary(requests.single()))
    }

    @Test
    fun listControllers_requestsFirstPage() = runTest {
        val s = server { json("""{"data":[{"id":"c1","name":"ctrl"}],"meta":{}}""") }

        val controllers = Controller(s.base(), ssl).listControllers()

        assertEquals(listOf("c1"), controllers.map { it.id })
        assertEquals("GET /controllers?limit=100&offset=0", summary(requests.single()))
    }

    @Test
    fun getServiceUpdates_andEdgeRouters() = runTest {
        val s = server { r ->
            if (r.url.encodedPath == "/current-api-session/service-updates") json("""{"data":{"lastChangeAt":"2030-01-01T00:00:00Z"},"meta":{}}""")
            else json("""{"data":[{"name":"er","hostname":"h"}],"meta":{}}""")
        }
        val controller = Controller(s.base(), ssl)

        assertNotNull(controller.getServiceUpdates())
        assertEquals(1, controller.getEdgeRouters().size)
        assertEquals(
            listOf("GET /current-api-session/service-updates", "GET /current-identity/edge-routers"),
            requests.map(::summary),
        )
    }

    @Test
    fun createNetSession_postsSessionCreate() = runTest {
        val s = server { json("""{"data":{"id":"sess1","token":"stok"},"meta":{}}""") }

        val session = Controller(s.base(), ssl).createNetSession(ServiceDetail().id("svc1"), SessionType.DIAL)

        assertEquals("sess1", session.id)
        assertEquals("POST /sessions", summary(requests.single()))
        val body = edgeMapper.readTree(requests.single().body!!.utf8())
        assertEquals("svc1", body["serviceId"].asText())
        assertEquals("Dial", body["type"].asText())
    }

    // --- MFA

    @Test
    fun getMFAEnrollment_404_returnsNull() = runTest {
        val s = server { status(404, notFound) }

        assertNull(Controller(s.base(), ssl).getMFAEnrollment())
    }

    @Test
    fun getMFAEnrollment_500_throwsZitiException() = runTest {
        val s = server { status(500, """{"error":{"code":"UNHANDLED","message":"boom"}}""") }

        assertFailsWith<ZitiException> { Controller(s.base(), ssl).getMFAEnrollment() }
    }

    @Test
    fun mfaOperations() = runTest {
        val s = server { r ->
            if (r.method == "GET") json("""{"data":{"isVerified":true,"recoveryCodes":["a","b"]},"meta":{}}""")
            else json("""{"data":{"id":"mfa1"},"meta":{}}""")
        }
        val controller = Controller(s.base(), ssl)

        assertNotNull(controller.postMFA())
        controller.verifyMFA("123")
        controller.authMFA("456")
        controller.removeMFA("789")
        val codes = controller.getMFARecoveryCodes("123", true)

        assertEquals(listOf("a", "b"), codes.toList())
        assertEquals(
            listOf(
                "POST /current-identity/mfa",
                "POST /current-identity/mfa/verify",
                "POST /authenticate/mfa",
                "DELETE /current-identity/mfa",
                "POST /current-identity/mfa/recovery-codes",
                "GET /current-identity/mfa",
            ),
            requests.map(::summary),
        )
        val bodies = requests.map { it.body?.utf8() }
        assertEquals("""{"code":"123"}""", bodies[1])
        assertEquals("""{"code":"456"}""", bodies[2])
        assertEquals("789", requests.toList()[3].headers["mfa-validation-code"])
    }

    // --- errors

    @Test
    fun errors_envelope_mapsToZitiError() = runTest {
        val s = server { status(401, """{"error":{"code":"UNAUTHORIZED","message":"m"}}""") }

        val ex = assertFailsWith<ZitiException> { Controller(s.base(), ssl).currentIdentity() }

        assertEquals(ZitiException.getZitiError("UNAUTHORIZED"), ex.code)
    }

    // a reachable server that answers with something other than an error envelope (a proxy's HTML error page,
    // an empty answer) is retried like an outage, not treated as a fatal error
    @Test
    fun errors_nonEnvelopeBody_isControllerUnavailable() = runTest {
        for (body in listOf("<html>bad gateway</html>", "")) {
            val s = server { status(502, body) }

            val ex = assertFailsWith<ZitiException> { Controller(s.base(), ssl).currentIdentity() }

            assertEquals("body: '$body'", Errors.ControllerUnavailable, ex.code)
            assertTrue(isTransientFailure(ex))
        }
    }

    @Test
    fun errors_connectionRefused_isControllerUnavailable() = runTest {
        val ex = assertFailsWith<ZitiException> { Controller(closedBase(), ssl).currentIdentity() }

        assertEquals(Errors.ControllerUnavailable, ex.code)
    }

    // --- calls whose failures are ignored

    @Test
    fun sendPostureResp_swallowsFailures() = runTest {
        val s = server { status(500) }

        Controller(s.base(), ssl).sendPostureResp(emptyList())

        assertEquals("POST /posture-response-bulk", summary(requests.single()))
        assertEquals("[]", requests.single().body!!.utf8())
    }

    @Test
    fun logout_failureIsIgnored() = runTest {
        val s = server { status(500) }

        Controller(s.base(), ssl).logout()

        assertEquals("DELETE /current-api-session", summary(requests.single()))
    }
}
