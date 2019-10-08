/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.api

import java.util.*

internal data class ClientInfo(val sdkInfo: Map<*, *>, val envInfo: Map<*, *>)

internal class Response<T>(val meta: Meta, val data: T?, val error: Error?)
internal class Error(val code: String, val msg: String)
internal class Meta(val location: String?)
internal class Id(val id: String)
internal class NetSessionReq(val serviceId: String)

internal class Login(val username: String, val password: String)
internal class Session(val token: String, val identity: Identity?)
internal class SessionResponse(val session: Session)

internal class ServiceDNS(val hostname: String, val port: Int)
internal class Service(var id: String, var name: String?, var dns: ServiceDNS?)

internal class Gateway(val name: String, val hostname: String, val urls: Map<String, String>)
internal class NetworkSession(val id: String, val token: String, val gateways: Array<Gateway>)

internal class OneTimeToken(val token: String, val jwt: String, val issuedAt: Date)
internal class Enrollment(val ott: OneTimeToken)
internal class Identity(val id: String, val name: String, val enrollment: Enrollment?)

internal class EnrollmentType(val ott: Boolean)
internal class CreateIdentity(val name: String, val type: String, enrollmentType: String) {
    val enrollment = EnrollmentType(enrollmentType.equals("ott"))
}