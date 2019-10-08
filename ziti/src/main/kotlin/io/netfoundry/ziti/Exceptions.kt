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

package io.netfoundry.ziti

enum class Errors {
    NotEnrolled,
    ControllerUnavailable,
    NotAuthorized,
    GatewayUnavailable,
    ServiceNotAvailable,
    WTF
}

private val errorMap = mapOf(
    "NO_ROUTABLE_INGRESS_NODES" to Errors.GatewayUnavailable,
    "INVALID_AUTHENTICATION" to Errors.NotAuthorized,
    "REQUIRES_CERT_AUTH" to Errors.NotAuthorized,
    "UNAUTHORIZED" to Errors.NotAuthorized,
    "INVALID_AUTH" to Errors.NotAuthorized
)

fun getZitiError(err: String): Errors = errorMap.getOrElse(err) { Errors.WTF }

class ZitiException(val code: Errors, cause: Throwable? = null) : Exception(code.name, cause) {

    override fun getLocalizedMessage(): String {
        cause?.let {
            return code.name + ": " + it.localizedMessage
        }

        return code.name
    }
}