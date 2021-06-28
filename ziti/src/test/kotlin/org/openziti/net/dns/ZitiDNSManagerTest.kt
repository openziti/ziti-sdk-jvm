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

package org.openziti.net.dns

import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ZitiDNSManagerTest {

    @After
    fun tearDown() {
        ZitiDNSManager.reset()
    }

    @Test
    fun testDNSevents() {
        val events = mutableListOf<DNSResolver.DNSEvent>()
        val block = CountDownLatch(1)
        ZitiDNSManager.subscribe {
            events.add(it)
            block.countDown()
        }

        ZitiDNSManager.registerHostname("test.dns.ziti")

        assertTrue(block.await(10, TimeUnit.SECONDS))
        assertEquals(1, events.size)
        assertEquals("test.dns.ziti", events.first().hostname)
        assertFalse(events.first().removed)
        assertArrayEquals(byteArrayOf(100.toByte(), 64.toByte(), 1, 2), events.first().ip.address)
    }

    @Test
    fun testSubscribeLater() {
        ZitiDNSManager.registerHostname("test1.dns.ziti")
        ZitiDNSManager.registerHostname("test2.dns.ziti")

        val block = CountDownLatch(2)
        ZitiDNSManager.subscribe {
            println(it)
            block.countDown()
        }

        assertTrue(block.await(1, TimeUnit.SECONDS))

    }
}