/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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

import com.google.gson.JsonObject
import org.junit.Assert.*
import org.junit.Test
import org.openziti.api.InterceptConfig
import org.openziti.api.Service

class ZitiDNSManagerTest {
    @Test
    fun testDNSevents() {
        val events = mutableListOf<DNSResolver.DNSEvent>()
        ZitiDNSManager.subscribe { events.add(it) }

        val dns = JsonObject().apply {
            addProperty("port", 80)
            addProperty("hostname", "test.dns.ziti")
        }
        ZitiDNSManager.registerService(
            Service(id = "id", name = "name", encryptionRequired = false, permissions = emptySet(),
                config = mapOf(InterceptConfig to dns), postureSets = null)
        )

        assertEquals(1, events.size)
        assertEquals("test.dns.ziti", events.first().hostname)
        assertFalse(events.first().removed)
        assertArrayEquals(byteArrayOf(169.toByte(), 254.toByte(), 1, 2), events.first().ip.address)
    }
}