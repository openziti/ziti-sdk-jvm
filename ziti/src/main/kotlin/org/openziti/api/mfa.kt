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

package org.openziti.api

import org.openziti.edge.model.DetailMfa

enum class MFAType {
    MFA,
    CUSTOM,
    Invalid
}

data class AuthQueryMFA (
    val typeId: MFAType?,
    val provider: String,
    val httpMethod: String,
    val httpUrl: String,
    val minLength: Int,
    val maxLength: Int,
    val format: String
)

typealias MFAEnrollment = DetailMfa

data class MFACode (
    val code: String
)

data class MFARecoveryCodes (
    val recoveryCodes: Array<String>
)