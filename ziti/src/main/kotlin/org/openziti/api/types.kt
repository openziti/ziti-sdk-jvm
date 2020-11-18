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
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.openziti.util.SystemInfo
import java.util.*

internal const val InterceptConfig = "ziti-tunneler-client.v1"
internal enum class SessionType {
    Dial,
    Bind
}

enum class PostureQueryType {
    OS,
    MAC,
    DOMAIN,
    PROCESS
}

internal data class ClientInfo(val sdkInfo: Map<*, *>, val envInfo: SystemInfo, val configTypes: Array<String>)

internal class Response<T>(val meta: Meta, val data: T?, val error: Error?)
internal data class Error(val code: String, val message: String, val cause: JsonObject, val field: String?)
internal class Meta(val pagination: Pagination?)
internal class Pagination(val limit: Int, val offset: Int, val totalCount: Int)
internal data class Id(val id: String)

internal class SessionReq(val serviceId: String, val type: SessionType = SessionType.Dial)

data class ControllerVersion(val buildDate: String, val revision: String, val runtimeVersion: String, val version: String)
internal class Login(val username: String, val password: String)
internal class ApiSession(val id: String, val token: String, val identity: Identity?)

data class ServiceDNS(val hostname: String, val port: Int)
class Service internal constructor(
    val id: String, val name: String,
    val encryptionRequired: Boolean,
    internal val permissions: Set<SessionType>,

    @SerializedName("postureQueries")
    internal val postureSets: Array<PostureSet>?,

    internal val config: Map<String,JsonObject>) {

    val dns: ServiceDNS?
        get() = getConfig(InterceptConfig, ServiceDNS::class.java)

    fun <C> getConfig(configType: String, cls: Class<out C>): C? = Gson().fromJson(config[configType],cls)
}

data class PostureQueryProcess (
    val osType: String,
    val path: String,
)

data class PostureQuery (
    val queryType: PostureQueryType,
    val id: String,
    val isPassing: Boolean,
    val process: PostureQueryProcess?
)

internal data class PostureSet (
    val isPassing: Boolean,
    val policyId: String,
    val postureQueries: Array<PostureQuery>?
)

@JsonAdapter(PostureResponseAdapter::class)
class PostureResponse (val id: String, val typeId: PostureQueryType, val data: Data) {
    abstract class Data
    class OS(val type: String, val version: String, val build: String) : Data()
    class Domain(val domain: String): Data()
    class MAC(val macAddresses: Array<String>): Data()

    companion object {
        fun OS(id: String, name: String, version: String, build: String) =
            PostureResponse(id, PostureQueryType.OS, OS(name, version, build))
        fun MAC(id: String, macs: Array<String>) =
            PostureResponse(id, PostureQueryType.MAC, MAC(macs))
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

class PostureResponseAdapter: TypeAdapter<PostureResponse>() {
    override fun write(out: JsonWriter, value: PostureResponse) {
        out.beginObject()
        out.name("typeId")
        out.value(value.typeId.name)
        out.name("id")
        out.value(value.id)

        val dataTree = Gson().toJsonTree(value.data) as JsonObject
        dataTree.entrySet().forEach {
            out.name(it.key)
            out.jsonValue(Gson().toJson(it.value))
        }

        out.endObject()
    }

    override fun read(`in`: JsonReader?): PostureResponse {
        TODO("Not yet implemented")
    }

}