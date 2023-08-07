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

import org.openziti.util.IPUtil
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.Writer
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicInteger

internal object ZitiDNSManager : DNSResolver, Logged by ZitiLog() {

    internal data class Domain(val name: String)

    internal data class Entry(val name: String, val addr: InetAddress, val domain: Domain? = null) {
        private val repr by lazy {
            domain?.let { "$addr [${it.name}]" } ?: addr.toString()
        }

        override fun toString() = repr
    }

    internal val PREFIX = byteArrayOf(100.toByte(), 64.toByte())

    const val startPostfix = 0x0101
    internal val postfix = AtomicInteger(startPostfix) // start with 1.1 postfix

    internal val host2Ip = mutableMapOf<String, Entry>()
    internal val ip2host = mutableMapOf<InetAddress, Entry>()
    internal val domains = mutableMapOf<String, Domain>()

    internal fun registerHostname(hostname: String): InetAddress {
        val ip = when {
            IPUtil.isValidIPv4(hostname) -> Inet4Address.getByName(hostname)
            IPUtil.isValidIPv6(hostname) -> Inet6Address.getByName(hostname)
            else -> {
                val dnsName = hostname.lowercase()
                val entry = host2Ip.getOrPut(dnsName) {
                    val e = nextAddr(dnsName)
                    ip2host[e.addr] = e
                    e
                }
                i{ "registered: ${hostname} => ${entry}" }
                entry.addr
            }
        }
        return ip
    }

    internal fun unregisterHostname(hostname: String) {
        val e = host2Ip.remove(hostname.lowercase())
        e?.let {
            i{ "removed entry=$it" }
            ip2host.remove(e.addr)
        }
    }

    internal fun registerDomain(domainName: String) {
        val key = when {
            domainName.startsWith("*.") -> domainName.substring(2)
            domainName.startsWith(".") -> domainName.substring(1)
            else -> domainName
        }.lowercase()

        val domain = Domain("*.$key")
        if (domains.putIfAbsent(key, domain)== null) {
            i{ "registered $domain"}
        }
    }

    internal fun unregisterDomain(domainName: String) {
        val key = when {
            domainName.startsWith("*.") -> domainName.substring(2)
            domainName.startsWith(".") -> domainName.substring(1)
            else -> domainName
        }.lowercase()

        val domain = domains.remove(key)

        if (domain != null) {
            val entries = host2Ip.values.filter { it.domain === domain }
            entries.forEach {
                unregisterHostname(it.name)
            }

            i{ "domain[${domain.name}] removed" }
        }
    }

    override fun resolve(hostname: String): InetAddress? = resolveOrAssign(hostname.lowercase())

    private fun resolveOrAssign(hostname: String): InetAddress? {
        var name = hostname.lowercase()
        host2Ip[name]?.let { return it.addr }

        do {
            domains[name]?.let {
                // found matching
                val entry = nextAddr(hostname, it)
                ip2host[entry.addr] = entry
                host2Ip[hostname] = entry
                return entry.addr
            }

            name = name.substringAfter('.', "")
        } while (name.isNotEmpty())

        return null
    }

    override fun lookup(addr: InetAddress): String?  = ip2host[addr]?.name

    private fun nextAddr(dnsname: String, domain: Domain? = null): Entry {
        var nextPostfix = postfix.incrementAndGet()

        if ((nextPostfix and 0xFF) == 0) {
            nextPostfix = postfix.incrementAndGet()
        }

        val ip = PREFIX + byteArrayOf(nextPostfix.shr(8).and(0xff).toByte(), (nextPostfix and 0xFF).toByte())
        val addr = InetAddress.getByAddress(dnsname, ip)

        i { """assigned $dnsname => $addr [${domain?.name ?: ""}]""" }
        return Entry(dnsname, addr, domain)
    }

    internal fun reset() {
        host2Ip.clear()
        ip2host.clear()
        postfix.set(startPostfix)
    }

    override fun dump(writer: Writer) {
        for ((h,ip) in host2Ip) {
            writer.appendLine("$h -> $ip")
        }
        writer.appendLine()
        writer.appendLine("== Wildcard Domains ==")
        domains.forEach { _, domain ->
            writer.appendLine(domain.name)
        }
    }
}