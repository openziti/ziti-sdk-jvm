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

import java.net.Inet6Address
import java.net.InetAddress

object IPUtil {
    @JvmStatic
    fun isValidIPv4(host: String): Boolean {
        val octets = host.split('.')
        if (octets.size != 4) return false
        return octets.all {
            val v = it.toIntOrNull()
            v?.let { (0 <= v) and (v <= 255) } ?: false
        }
    }

    @JvmStatic
    fun toIPv4(host: String): ByteArray {
        val octets = host.split('.')
        require (octets.size == 4)

        return octets.map { it.toInt() }.map { v -> check((0 <= v) and (v <= 255)); (v and 0xff).toByte()}.toByteArray()
    }

    @JvmStatic
    fun isValidIPv6(host: String): Boolean {
        if (host.isEmpty() || host[0] != '[') return false
        return runCatching { Inet6Address.getByName(host) }.isSuccess
    }

    @JvmStatic
    fun toIPv6(host: String): ByteArray {
        require (host.isEmpty() || host[0] != '[')
        return Inet6Address.getByName(host).address
    }

    @JvmStatic
    fun maskForPrefix(size: Int, prefix: Int): ByteArray {
        return ByteArray(size) { idx ->
            if (idx < prefix / 8) 0xff.toByte()
            else if (idx == prefix / 8) (0xff shl ((idx + 1) * 8 - prefix)).toByte()
            else 0
        }
    }

    @JvmStatic
    fun toCanonicalCIDR(ip: InetAddress, bits: Int): InetAddress {
        val bytes = ByteArray(ip.address.size)
        val mask = maskForPrefix(bytes.size, bits)
        for (i in bytes.indices) {
            bytes[i] = (mask[i].toInt() and ip.address[i].toInt()).toByte()
        }
        return InetAddress.getByAddress(bytes)
    }
}