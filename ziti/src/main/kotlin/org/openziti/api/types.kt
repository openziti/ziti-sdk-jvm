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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.openziti.edge.model.CurrentApiSessionDetail
import org.openziti.edge.model.PostureCheckType
import org.openziti.edge.model.ServiceDetail
import org.openziti.net.Protocol
import java.util.*

internal const val ClientV1Cfg = "ziti-tunneler-client.v1"
internal const val InterceptV1Cfg = "intercept.v1"
typealias SessionType = org.openziti.edge.model.DialBind

enum class InterceptProtocol {
    tcp,
    udp,
    sctp
}

internal class Login(val username: String, val password: String)
typealias ApiSession = CurrentApiSessionDetail

private data class ClientV1Config(val hostname: String, val port: Int)

data class PortRange(val low: Int, val high: Int): Comparable<PortRange> {
    fun contains(port: Int) = (port in low..high)

    override fun toString(): String = if (low == high) low.toString() else "$low-$high"
    override fun compareTo(other: PortRange): Int {
        val ord = low.compareTo(other.low)
        return if (ord == 0) high.compareTo(other.high) else ord
    }
}

fun <T> Array<T>.display() = joinToString(prefix = "[", postfix = "]")
fun <T> Set<T>.display() = joinToString(prefix = "[", postfix = "]")

data class InterceptConfig(
    val protocols: Set<Protocol>,
    val addresses: Set<InterceptAddress>,
    val portRanges: SortedSet<PortRange>,
    val dialOptions: Map<String,Any> = emptyMap(),
    val sourceIp: String? = null
) {
    override fun toString(): String {
        return """${protocols.display()}:${addresses.display()}:${portRanges.display()}"""
    }
}

typealias Service = ServiceDetail

fun Service.failingPostureChecks(): Map<PostureCheckType, Int> {
    val failing = postureQueries.flatMap {
        if (it.isPassing) emptyList()
        else it.postureQueries.filter { !it.isPassing }
    }.groupBy{ it.queryType }

    return failing.entries.fold(mapOf()){ m, e -> m + (e.key to e.value.size) }
}

fun <C> Service.getConfig(configType: String, cls: Class<out C>): C? {
    val tree = config[configType]

    tree?.let {
        val mapper = ObjectMapper().registerModule(kotlinModule())
        val str = mapper.writeValueAsString(tree)
        val v = mapper.readValue(str, cls)
        return v
    }
    return null
}

fun Service.interceptConfig(): InterceptConfig? =
    getConfig(InterceptV1Cfg, InterceptConfig::class.java) ?:
    getConfig(ClientV1Cfg, ClientV1Config::class.java)?.let {
        InterceptConfig(
            protocols = setOf(Protocol.TCP),
            addresses = setOf(it.hostname.asInterceptAddr()),
            portRanges = sortedSetOf(PortRange(it.port, it.port))
        )
    }

typealias Session = org.openziti.edge.model.SessionDetail

internal class OneTimeToken(val token: String, val jwt: String, val issuedAt: Date)
internal class Enrollment(val ott: OneTimeToken)
data class Identity internal constructor(val id: String, val name: String, internal val enrollment: Enrollment?)
