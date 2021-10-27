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

package org.openziti.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import org.openziti.util.IPUtil
import java.lang.reflect.Type
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress


class InterceptAddressDeserializer: JsonDeserializer<InterceptAddress> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): InterceptAddress = json.asString.asInterceptAddr()
}

fun String.asInterceptAddr(): InterceptAddress {
    val addr = this
    if (addr[0] == '*') return DomainName(addr)

    if (addr.contains('/')) { // must be CIDR block
        val (prefix, bits) = addr.split("/", limit = 2)

        val ip = if (prefix.contains('.')) { // IPv4
            Inet4Address.getByName(prefix)
        } else { // IPv6
            Inet6Address.getByName(prefix)
        }
        return CIDRBlock(ip, bits.toInt())
    } else if (IPUtil.isValidIPv4(addr)) {
        val addrBytes = IPUtil.toIPv4(addr)
        return CIDRBlock(Inet4Address.getByAddress(addrBytes), 32)
    } else {
        return DNSName(addr)
    }
}

@JsonAdapter(InterceptAddressDeserializer::class)
sealed class InterceptAddress {
    abstract fun matches(addr: Any): Boolean
}

data class CIDRBlock(val ip: InetAddress, val bits: Int): InterceptAddress() {
    val cidrAddress = IPUtil.toCanonicalCIDR(ip, bits)
    val mask = IPUtil.maskForPrefix(ip.address.size, bits)

    override fun matches(addr: Any): Boolean {
        val addrBytes =
            if (addr is InetAddress) addr.address
            else if (addr is String) {
                if (IPUtil.isValidIPv4(addr)) IPUtil.toIPv4(addr)
                else return false
            }
            else return false

        if (addrBytes.size != mask.size) return false

        for (i in 0 until mask.size) {
            if ((mask[i].toInt() and addrBytes[i].toInt()) != cidrAddress.address[i].toInt()) return false
        }
        return true
    }

    fun includes(other: CIDRBlock): Boolean =
        bits <= other.bits && matches(other.ip)
}

data class DNSName(val name: String): InterceptAddress() {
    override fun matches(addr: Any): Boolean =  (addr is String) && addr.equals(name, ignoreCase = true)
}

data class DomainName(val name: String): InterceptAddress() {
    private val domainSuffix = name.substring(1).lowercase()
    override fun matches(addr: Any): Boolean =  (addr is String) && addr.endsWith(domainSuffix, ignoreCase = true)
}