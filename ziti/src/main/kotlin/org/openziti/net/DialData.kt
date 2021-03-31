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

package org.openziti.net

import com.google.gson.annotations.SerializedName

enum class Protocol {
    @SerializedName("tcp") TCP,
    @SerializedName("udp") UDP,
    @SerializedName("sctp") SCTP,
}

data class DialData(
    @SerializedName("dst_protocol") val dstProtocol: Protocol? = null,
    @SerializedName("dst_hostname") val dstHostname: String? = null,
    @SerializedName("dst_ip") val dstIp: String? = null,
    @SerializedName("dst_port") val dstPort: String? = null,

    @SerializedName("src_protocol") val srcProtocol: Protocol? = null,
    @SerializedName("src_ip") val srcIp: String? = null,
    @SerializedName("src_port") val srcPort: String? = null,

    @SerializedName("source_addr") val sourceAddr: String? = null
)
