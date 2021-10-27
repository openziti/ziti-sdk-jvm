/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

package org.openziti.util

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.net.InetAddress

internal class IPUtilTest {
    @Test
    fun testToCanonicalCIDR() {
        assertArrayEquals(byteArrayOf(11,11,0,0),
            IPUtil.toCanonicalCIDR(InetAddress.getByName("11.11.11.11"), 16).address)
        assertArrayEquals(byteArrayOf(10,64,0,0),
            IPUtil.toCanonicalCIDR(InetAddress.getByName("10.127.11.11"), 10).address)
    }

    @Test
    fun testMaskForPrefix() {
        assertArrayEquals(byteArrayOf(0,0,0,0), IPUtil.maskForPrefix(4, 0))
        assertArrayEquals(byteArrayOf(-1,0,0,0), IPUtil.maskForPrefix(4, 8))
        assertArrayEquals(byteArrayOf(-1,-128,0,0), IPUtil.maskForPrefix(4, 9))
        assertArrayEquals(byteArrayOf(-1,-1,-1,-1), IPUtil.maskForPrefix(4, 32))
    }
}