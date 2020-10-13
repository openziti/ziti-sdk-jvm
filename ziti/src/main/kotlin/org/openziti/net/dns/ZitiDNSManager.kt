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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.bouncycastle.util.IPAddress
import org.openziti.api.Service
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer


@ExperimentalCoroutinesApi
internal object ZitiDNSManager : DNSResolver, ServiceMapper {

    internal val PREFIX = byteArrayOf(0xa9.toByte(), 0xfe.toByte())

    internal val postfix = AtomicInteger(0x0101) // start with 1.1 postfix

    internal val host2Ip = mutableMapOf<String, InetAddress>()
    internal val addr2serviceId = mutableMapOf<InetSocketAddress, String>()
    internal val serviceId2addr = mutableMapOf<String, InetSocketAddress>()
    internal val dnsBroadCast = BroadcastChannel<DNSResolver.DNSEvent>(Channel.BUFFERED)

    internal fun registerService(service: Service): InetSocketAddress? {

        service.dns?.hostname?.toLowerCase(Locale.getDefault())?.let { hostname ->

            val ip = when {
                IPAddress.isValidIPv4(hostname) -> Inet4Address.getByName(hostname)
                IPAddress.isValidIPv6(hostname) -> Inet6Address.getByName(hostname)
                else -> host2Ip.getOrPut(hostname) { nextAddr(hostname) }
            }

            runBlocking {
                dnsBroadCast.send(DNSResolver.DNSEvent(hostname, ip, false))
            }

            service.dns?.port?.let { port ->
                val addr = InetSocketAddress(ip, port)

                addr2serviceId.put(addr, service.id)
                serviceId2addr.put(service.id, addr)

                return addr
            }
        }

        return null
    }

    internal fun unregisterService(service: Service) {
        val addr = serviceId2addr.get(service.id)
        if (addr != null) {
            addr2serviceId.remove(addr)
            runBlocking {
                GlobalScope.launch {
                    dnsBroadCast.send(DNSResolver.DNSEvent(service.dns?.hostname, addr.address, true))
                }
            }
        }
    }

    override fun resolve(hostname: String): InetAddress? = host2Ip.get(hostname.toLowerCase(Locale.getDefault()))

    override fun subscribe(sub: (DNSResolver.DNSEvent) -> Unit) {
        runBlocking {
            GlobalScope.launch {
                dnsBroadCast.asFlow().collect { sub(it) }
            }
        }
    }

    override fun subscribe(sub: Consumer<DNSResolver.DNSEvent>) = subscribe{sub.accept(it)}

    override fun getServiceIdByAddr(addr: InetSocketAddress): String? = addr2serviceId.get(addr)

    internal fun nextAddr(dnsname: String): InetAddress {
        var nextPostfix = postfix.incrementAndGet()

        if ((nextPostfix and 0xFF) == 0) {
            nextPostfix = postfix.incrementAndGet()
        }

        val ip = PREFIX + byteArrayOf(nextPostfix.shr(8).and(0xff).toByte(), (nextPostfix and 0xFF).toByte())
        return InetAddress.getByAddress(dnsname, ip)
    }
}