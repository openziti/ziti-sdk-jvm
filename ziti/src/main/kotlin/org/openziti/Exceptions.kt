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

package org.openziti


sealed class Errors {
    object NotEnrolled : Errors()
    object ControllerUnavailable: Errors()
    object NotAuthorized: Errors()
    object EdgeRouterUnavailable: Errors()
    object ServiceNotAvailable: Errors()
    object InsufficientSecurity: Errors()
    data class WTF(val err: String): Errors()

    override fun toString(): String = javaClass.simpleName
}

private val errorMap = mapOf(
    "NO_EDGE_ROUTERS_AVAILABLE" to Errors.EdgeRouterUnavailable,
    "INVALID_AUTHENTICATION" to Errors.NotAuthorized,
    "REQUIRES_CERT_AUTH" to Errors.NotAuthorized,
    "UNAUTHORIZED" to Errors.NotAuthorized,
    "INVALID_AUTH" to Errors.NotAuthorized,
    "INVALID_POSTURE" to Errors.InsufficientSecurity,
    "MFA_INVALID_TOKEN" to Errors.InsufficientSecurity
)

fun getZitiError(err: String): Errors = errorMap.getOrElse(err) { Errors.WTF(err) }

class ZitiException(val code: Errors, cause: Throwable? = null) : Exception(code.toString(), cause) {

    override fun getLocalizedMessage(): String {
        cause?.let {
            return code.toString() + ": " + it.localizedMessage
        }

        return code.toString()
    }
}