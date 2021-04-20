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

import org.openziti.api.PostureQuery
import org.openziti.api.PostureQueryType
import org.openziti.api.PostureResponse
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

    override fun getPosture(): Array<PostureResponse> {
        return queries.map{ processQuery(it.value) }.filterNotNull().toTypedArray()
    }

    internal fun processQuery(q: PostureQuery): PostureResponse? {
        return when(q.queryType) {
            PostureQueryType.OS -> PostureResponse.OS(q.id, osName, osVersion, "")
            PostureQueryType.MAC -> PostureResponse.MAC(q.id, macs)
            else -> {
                w{"unsupported posture type: $q"}
                null
            }
        }
    }
}