/*
 * Copyright (c) 2018-2025 NetFoundry Inc.
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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.*
import org.openziti.IdentityConfig
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import org.openziti.api.DNSName
import org.openziti.api.InterceptConfig
import org.openziti.api.InterceptV1Cfg
import org.openziti.api.PortRange
import org.openziti.edge.model.DialBind
import org.openziti.integ.BaseTest
import org.openziti.integ.ManagementHelper
import org.openziti.net.nio.acceptSuspend
import org.openziti.net.nio.readSuspend
import org.openziti.net.nio.writeSuspend
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import java.nio.channels.InterruptedByTimeoutException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

class ConnectionTests: BaseTest() {

    private val hostname = "test${System.nanoTime()}.ziti"
    private val port = 5000
    private lateinit var service: String
    private lateinit var cfg: IdentityConfig
    private lateinit var ztx: ZitiContext

    @BeforeEach
    fun before() {
        service = ManagementHelper.createService(
            configs = mapOf(
                InterceptV1Cfg to InterceptConfig(
                    protocols = setOf(Protocol.TCP),
                    addresses = setOf(DNSName(hostname)),
                    portRanges = sortedSetOf(PortRange(port, port)),
                )
            )
        )
        cfg = createIdentity()
        ztx = Ziti.newContext(cfg)
    }

    @AfterEach
    fun after() {
        ztx.destroy()
    }

    @Test
    fun `test dial without terminator`() = runTest(timeout = 10.seconds) {
        val srv = assertDoesNotThrow {
            ztx.serviceUpdates().filter { it.service.name == service }.first().service
        }

        assertFalse(srv.config.isEmpty())
        assertThrows<ConnectException> {
            ztx.dial(service)
        }.run {
            assert(message!!.contains("has no terminators"))
        }

        assertThrows<ExecutionException> {
            val ch = ztx.open()
            ch.connect(ZitiAddress.Dial(service)).get()
        }.run {
            assert(cause!!.message!!.contains("has no terminators"))
        }
        assertThrows<ExecutionException> {
            val ch = ztx.open()
            ch.connect(InetSocketAddress.createUnresolved(hostname, port)).get()
        }.run {
            assert(cause!!.message!!.contains("has no terminators"))
        }

        assertThrows<ConnectException> {
            ztx.connect(hostname, port)
        }.run {
            assertTrue(message!!.contains("has no terminators"))
        }
    }

    @Test
    fun `test bind-connect-read-timeout`() = runTest(timeout = 10.seconds) {
        val greeting = "Hello from Ziti".toByteArray()
        val s = assertDoesNotThrow {
            ztx.serviceUpdates().filter { it.service.name == service }.first().service
        }
        assertTrue(s.permissions.contains(DialBind.DIAL))
        assertTrue(s.permissions.contains(DialBind.BIND))

        ztx.openServer().use { srv ->

            srv.bind(ZitiAddress.Bind(service))

            // wait for binding -- test dispatcher skips delays
            withContext(Dispatchers.Default) {
                val zrv = srv as ZitiServerSocketChannel
                while (zrv.state != ZitiServerSocketChannel.State.bound) {
                    delay(50)
                }
            }

            launch(Dispatchers.IO) {
                val c = srv.acceptSuspend()
                c.writeSuspend(ByteBuffer.wrap(greeting))
                delay(1000)
                c.close()
            }

            ztx.open().use { clt ->
                clt.connect(ZitiAddress.Dial(service)).get(1, TimeUnit.SECONDS)

                val buf = ByteBuffer.allocate(1024)

                // read 1: return greeting
                val read1 = clt.readSuspend(buf, 100, TimeUnit.MILLISECONDS)
                assertEquals(read1, greeting.size)
                val readMsg = ByteArray(read1)
                buf.flip().get(readMsg)
                assertContentEquals(greeting, readMsg)
                assertFalse(buf.hasRemaining())

                buf.clear()
                // read 2: should time out
                assertThrows<InterruptedByTimeoutException> {
                    clt.readSuspend(buf, 600, TimeUnit.MILLISECONDS)
                }

                buf.clear()
                // read 3: should get EOF
                assertEquals(-1, clt.readSuspend(buf, 600, TimeUnit.MILLISECONDS))
            }
        }
    }

    @Test
    fun `test socket-connect-read-timeout`() = runTest(timeout = 10.seconds) {
        val greeting = "Hello from Ziti".toByteArray()
        val s = assertDoesNotThrow {
            ztx.serviceUpdates().filter { it.service.name == service }.first().service
        }
        assertTrue(s.permissions.contains(DialBind.DIAL))
        assertTrue(s.permissions.contains(DialBind.BIND))

        ztx.openServer().use { srv ->

            srv.bind(ZitiAddress.Bind(service))

            // wait for binding -- test dispatcher skips delays
            withContext(Dispatchers.Default) {
                val zrv = srv as ZitiServerSocketChannel
                while (zrv.state != ZitiServerSocketChannel.State.bound) {
                    delay(50)
                }
            }

            launch(Dispatchers.IO) {
                val c = srv.acceptSuspend()
                c.writeSuspend(ByteBuffer.wrap(greeting))
                val b = ByteBuffer.allocate(1024)
                c.readSuspend(b)
            }

            ztx.connect(hostname, port).use { clt ->
                assertTrue(clt.isConnected)
                val buf = ByteArray(1024)
                clt.soTimeout = 500
                val input = clt.getInputStream()

                // read 1: return greeting
                val read1 = input.read(buf)
                assertEquals(read1, greeting.size)
                val readMsg = buf.sliceArray(0..<read1)
                assertContentEquals(greeting, readMsg)

                // other reads would get a timeout
                for (i in 0 .. 10) {
                    assertThrows<SocketTimeoutException> {
                        input.read(buf)
                    }
                }
            }
        }
    }

}