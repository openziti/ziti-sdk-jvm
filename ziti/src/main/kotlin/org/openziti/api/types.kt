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

package org.openziti.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*

internal data class ClientInfo(val sdkInfo: Map<*, *>, val envInfo: Map<*, *>, val configTypes: Array<String>)

internal class Response<T>(val meta: Meta, val data: T?, val error: Error?)
internal data class Error(val code: String, val message: String, val cause: JsonObject, val field: String?)
internal class Meta(val pagination: Pagination?)
internal class Pagination(val limit: Int, val offset: Int, val totalCount: Int)
internal data class Id(val id: String)

internal enum class SessionType {
    Dial,
    Bind
}
internal class SessionReq(val serviceId: String, val type: SessionType = SessionType.Dial)

data class ControllerVersion(val buildDate: String, val revision: String, val runtimeVersion: String, val version: String)
internal class Login(val username: String, val password: String)
internal class ApiSession(val id: String, val token: String, val identity: Identity?)

data class ServiceDNS(val hostname: String, val port: Int)
data class Service internal constructor(
    val id: String, val name: String,
    val encryptionRequired: Boolean,
    internal val permissions: Set<SessionType>,
    internal val config: Map<String,JsonObject>) {

    val dns: ServiceDNS?
        get() = getConfig(InterceptConfig, ServiceDNS::class.java)

    fun <C> getConfig(configType: String, cls: Class<out C>): C? {
        return Gson().fromJson(config[configType],cls)
    }
}

internal data class EdgeRouter(val name: String, val hostname: String, val urls: Map<String, String>)

internal data class Session(val id: String, val token: String, val service: Id, val type: SessionType,
                            var edgeRouters: Array<EdgeRouter>?) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Session

        if (id != other.id) return false
        if (token != other.token) return false
        if (service != other.service) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + token.hashCode()
        result = 31 * result + service.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}

internal class OneTimeToken(val token: String, val jwt: String, val issuedAt: Date)
internal class Enrollment(val ott: OneTimeToken)
data class Identity internal constructor(val id: String, val name: String, internal val enrollment: Enrollment?)


internal class EnrollmentType(val ott: Boolean)
internal class CreateIdentity(val name: String, val type: String, enrollmentType: String) {
    val enrollment = EnrollmentType(enrollmentType.equals("ott"))
}

internal const val InterceptConfig = "ziti-tunneler-client.v1"