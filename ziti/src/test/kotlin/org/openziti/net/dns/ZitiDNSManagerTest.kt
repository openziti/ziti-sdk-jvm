/*
 * Copyright (c) 2018-2023 NetFoundry Inc.
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
import org.openziti.api.asInterceptAddr

class ZitiDNSManagerTest {

    @After
    fun tearDown() {
        ZitiDNSManager.reset()
    }

    @Test
    fun cidrMatchTest() {
        val addr = "10.64.127.155"

        val cases = listOf(
            "10.64.127.0/24" to true,
            "10.64.127.0/25" to false,
            "10.64.127.128/25" to true,
            "10.65.127.0/24" to false
        )

        for ((i,r) in cases) {
            val icpt = i.asInterceptAddr()
            if (r) {
                assertTrue("$i should match", icpt.matches(addr))
            } else {
                assertFalse("$i should NOT match", icpt.matches(addr))
            }
        }
    }

    @Test
    fun testWildcard() {
        val hostIP = ZitiDNSManager.registerHostname("host.ziggy.IO")
        assertNotNull(hostIP)

        val domain = "bar.ziti"
        ZitiDNSManager.registerDomain("*.$domain")

        val valid = ZitiDNSManager.resolve("FOO.$domain")
        assertNotNull(valid)
        assertEquals("foo.$domain", ZitiDNSManager.lookup(valid!!))

        val invalid = ZitiDNSManager.resolve("foo.${domain}.com")
        assertNull(invalid)

        val hostAddr = ZitiDNSManager.resolve("HOST.ziggy.io")
        assertNotNull(hostAddr)
        assertSame(hostIP, hostAddr)

        ZitiDNSManager.unregisterDomain(domain)
        assertNull(ZitiDNSManager.resolve("FOO.$domain"))
    }
}