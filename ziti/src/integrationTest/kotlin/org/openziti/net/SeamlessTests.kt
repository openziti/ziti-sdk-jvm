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

package org.openziti.net

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import org.openziti.api.DNSName
import org.openziti.api.InterceptConfig
import org.openziti.api.InterceptV1Cfg
import org.openziti.api.PortRange
import org.openziti.api.http.LazyBypassSocketFactory
import org.openziti.integ.BaseTest
import org.openziti.integ.ManagementHelper
import org.openziti.net.internal.SocketChannelSocket
import java.io.ByteArrayOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.test.assertContentEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

// Seamless mode replaces the JVM's socket implementation for good, so this class needs a JVM of its own
// (the integrationTest task forks one per test class).
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SeamlessTests : BaseTest() {

    private val hostname = "seamless${System.nanoTime()}.ziti"
    private val port = 5000
    private lateinit var service: String
    private lateinit var ztx: ZitiContext

    @BeforeAll
    fun initSeamless() {
        service = ManagementHelper.createService(
            configs = mapOf(
                InterceptV1Cfg to InterceptConfig(
                    protocols = setOf(Protocol.TCP),
                    addresses = setOf(DNSName(hostname)),
                    portRanges = sortedSetOf(PortRange(port, port)),
                )
            )
        )
        val cfg = createIdentity("seamless-${System.nanoTime()}")
        val identity = ByteArrayOutputStream().also { cfg.store(it) }.toByteArray()

        Ziti.init(identity, true)
        ztx = Ziti.getContexts().single()
    }

    @AfterAll
    fun cleanup() {
        Ziti.removeContext(ztx)
    }

    // every controller call (login, identity, services, sessions) goes through the bypass sockets
    @Test
    fun `context talks to the controller`() {
        assertTrue(Ziti.isSeamless())
        // plain sockets are Ziti sockets now; the SDK's own controller connections must not be
        assertIs<SocketChannelSocket>(LazyBypassSocketFactory().createSocket())

        runBlocking {
            withTimeout(30.seconds) { ztx.statusUpdates().first { it == ZitiContext.Status.Active } }
        }
        assertNotNull(ztx.getId())
        assertNotNull(ztx.getService(service, 10_000))
    }

    @Test
    fun `plain socket reaches a service the context hosts`() {
        val greeting = "Hello from seamless Ziti".toByteArray()

        ztx.openServer().use { srv ->
            srv.bind(ZitiAddress.Bind(service))
            val deadline = System.currentTimeMillis() + 10_000
            while ((srv as ZitiServerSocketChannel).state != ZitiServerSocketChannel.State.Bound) {
                check(System.currentTimeMillis() < deadline) { "service was not bound" }
                Thread.sleep(50)
            }

            val server = thread {
                srv.accept().get(10, TimeUnit.SECONDS).use { conn ->
                    conn.write(ByteBuffer.wrap(greeting)).get(5, TimeUnit.SECONDS)
                }
            }

            Socket().use { s ->
                s.connect(InetSocketAddress.createUnresolved(hostname, port), 5_000)
                s.soTimeout = 5_000
                assertContentEquals(greeting, s.getInputStream().readNBytes(greeting.size))
            }
            server.join(5_000)
        }
    }
}
