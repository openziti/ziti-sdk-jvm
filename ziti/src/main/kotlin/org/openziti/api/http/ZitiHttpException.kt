/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.api.http

/**
 * A controller answered with a non-success HTTP status.
 *
 * Deliberately NOT an [java.io.IOException]: transient-failure detection treats every `IOException` as
 * "controller unreachable, retry", and a 4xx/5xx answer from a reachable controller is not that.
 */
class ZitiHttpException(
    val code: Int,
    val responseBody: String?,
    message: String? = null,
) : Exception(message ?: "HTTP $code")
