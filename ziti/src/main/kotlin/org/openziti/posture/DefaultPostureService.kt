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

package org.openziti.posture

import org.openziti.api.PostureQueryType
import org.openziti.edge.model.PostureCheckType
import org.openziti.edge.model.PostureQuery
import org.openziti.edge.model.PostureResponseCreate
import org.openziti.edge.model.PostureResponseMacAddressCreate
import org.openziti.edge.model.PostureResponseOperatingSystemCreate
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.NetworkInterface

internal class DefaultPostureService: PostureService, Logged by ZitiLog() {
    internal val queries = mutableMapOf<String,PostureQuery>()

    object Provider: PostureServiceProvider {
        val service = DefaultPostureService()
        override fun getPostureService() = service
    }

    val osName: String by lazy {
        System.getProperty("os.name")
    }

    val osVersion: String by lazy {
        System.getProperty("os.version")
    }

    val macs: Array<String> by lazy {
        NetworkInterface.getNetworkInterfaces().toList()
            .map { it.hardwareAddress }.filterNotNull().map {
                it.joinToString(":"){"%02x".format(it)}
            }.toTypedArray()
    }

    override fun registerServiceCheck(serviceId: String, query: PostureQuery) {
        queries.put(query.id, query)
    }

    override fun getPosture(): List<PostureResponseCreate> =
        queries.map { processQuery(it.value) }.filterNotNull()

    internal fun processQuery(q: PostureQuery): PostureResponseCreate? {
        return when(q.queryType) {
            PostureCheckType.OS -> PostureResponseOperatingSystemCreate().apply {
                id(q.id)
                typeId(PostureCheckType.OS)
                type(osName)
                version(osVersion)
                build("")
            }
            PostureCheckType.MAC -> PostureResponseMacAddressCreate().apply {
                id(q.id)
                typeId(PostureCheckType.MAC)
                macAddresses(macs.toList())
            }
            else -> {
                w{"unsupported posture type: $q"}
                null
            }
        }
    }
}