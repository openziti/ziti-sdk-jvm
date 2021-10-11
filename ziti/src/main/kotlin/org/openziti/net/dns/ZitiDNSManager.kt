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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bouncycastle.util.IPAddress
import java.io.Writer
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer


internal object ZitiDNSManager : DNSResolver, CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    internal val PREFIX = byteArrayOf(100.toByte(), 64.toByte())

    const val startPostfix = 0x0101
    internal val postfix = AtomicInteger(startPostfix) // start with 1.1 postfix

    internal val host2Ip = mutableMapOf<String, InetAddress>()
    internal val ip2host = mutableMapOf<InetAddress, String>()

    internal val dnsBroadCast = MutableSharedFlow<DNSResolver.DNSEvent>()

    internal fun registerHostname(hostname: String): InetAddress {
        val ip = when {
            IPAddress.isValidIPv4(hostname) -> Inet4Address.getByName(hostname)
            IPAddress.isValidIPv6(hostname) -> Inet6Address.getByName(hostname)
            else -> {
                host2Ip.getOrPut(hostname) { nextAddr(hostname) }.also {
                    ip2host[it] = hostname
                }
            }
        }
        launch {
            dnsBroadCast.emit(DNSResolver.DNSEvent(hostname, ip, false))
        }
        return ip
    }
    override fun resolve(hostname: String): InetAddress? = host2Ip[hostname.lowercase()]

    override fun lookup(addr: InetAddress): String?  = ip2host[addr] ?: addr.hostAddress

    override fun subscribe(sub: (DNSResolver.DNSEvent) -> Unit) {
        launch {
            host2Ip.forEach { (h, ip) ->
                sub(DNSResolver.DNSEvent(h, ip, false))
            }
            dnsBroadCast.collect { sub(it) }
        }
    }

    override fun subscribe(sub: Consumer<DNSResolver.DNSEvent>) = subscribe{sub.accept(it)}

    private fun nextAddr(dnsname: String): InetAddress {
        var nextPostfix = postfix.incrementAndGet()

        if ((nextPostfix and 0xFF) == 0) {
            nextPostfix = postfix.incrementAndGet()
        }

        val ip = PREFIX + byteArrayOf(nextPostfix.shr(8).and(0xff).toByte(), (nextPostfix and 0xFF).toByte())
        return InetAddress.getByAddress(dnsname, ip)
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
    }
}