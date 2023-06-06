/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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

package org.openziti.net.nio

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.net.InetSocketAddress
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousCloseException
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.UnresolvedAddressException
import java.nio.channels.spi.AsynchronousChannelProvider
import kotlin.test.assertIs

class NetUtilsTest {

    @Test(expected = SocketTimeoutException::class, timeout = 2000)
    fun test_connectSuspendWithTimeout(): Unit = runBlocking {
        val sock = AsynchronousSocketChannel.open()
        val connectOp = async { sock.connectSuspend(InetSocketAddress("1.1.1.1", 4444), 1000) }
        connectOp.await()
    }

    @Test(expected = AsynchronousCloseException::class)
    fun testGroupShutdown() {
        val g = AsynchronousChannelProvider.provider().openAsynchronousChannelGroup(1) { r ->
            Thread(r, "test thread")
        }
        val c = AsynchronousSocketChannel.open(g)

        runBlocking {
            supervisorScope {
                c.connectSuspend(InetSocketAddress("google.com", 80), 1000)
                val buf = ByteBuffer.allocate(1024)
                val readOp = async { c.readSuspend(buf) }
                delay(100)
                g.shutdownNow()

                readOp.await()
            }
        }
    }

    @Test(expected = AsynchronousCloseException::class)
    fun testTlsGroupShutdown() {
        val g = AsyncTLSChannel.Provider.openAsynchronousChannelGroup(1) { r ->
            Thread(r, "test thread")
        }
        val c = AsyncTLSChannel.open(g)

        runBlocking {
            supervisorScope {
                c.connectSuspend(InetSocketAddress("google.com", 443), 1000)
                val buf = ByteBuffer.allocate(1024)
                val readOp = async { c.readSuspend(buf) }
                delay(100)
                g.shutdownNow()

                readOp.await()
            }
        }

    }

    @Test
    fun testConnectSuspend() {
        runBlocking {
            val s = AsyncTLSChannel.open()
            s.connectSuspend(InetSocketAddress("google.com", 443), 1000)
            assertIs<InetSocketAddress>(s.remoteAddress)
            assertEquals(443, (s.remoteAddress as InetSocketAddress).port)
            assertEquals("google.com", (s.remoteAddress as InetSocketAddress).hostName)
            s.close()
        }
    }

    @Test(expected = SocketTimeoutException::class)
    fun testConnectSuspendTimeout() {
        runBlocking {
            val s = AsyncTLSChannel.open()
            s.connectSuspend(InetSocketAddress("10.10.10.10", 443), 1)
            fail("connect should timeout")
        }
    }

    @Test(expected = UnresolvedAddressException::class)
    fun testConnectSuspendInvalidHost() {
        val s = AsyncTLSChannel.open()
        runBlocking {
            s.connectSuspend(InetSocketAddress("not.a.real.domain.name", 443), 1000)
        }
    }

}