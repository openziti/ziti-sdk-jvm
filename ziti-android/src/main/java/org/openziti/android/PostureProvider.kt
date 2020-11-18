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

package org.openziti.android

import android.os.Build
import org.openziti.api.PostureQuery
import org.openziti.api.PostureQueryType
import org.openziti.api.PostureResponse
import org.openziti.posture.PostureService
import org.openziti.posture.PostureServiceProvider
import java.net.NetworkInterface

class PostureProvider: PostureServiceProvider {
    override fun getPostureService(): PostureService = AndroidPostureService

    object AndroidPostureService: PostureService {
        val androidVersion: String by lazy {
            Build.VERSION.RELEASE.split(".").plus(arrayOf("0","0")).take(3).joinToString(".")
        }
        val androidBuild: String by lazy {
            Build.VERSION.SECURITY_PATCH.replace("-", "") // turn date into an int
        }

        val macAddresses: Array<String> by lazy {
            NetworkInterface.getNetworkInterfaces().toList()
                .map { it.hardwareAddress }
                .filterNotNull()
                .map { it.joinToString(":"){ b -> "%02x".format(b) } }
                .toTypedArray()
        }

        val queries = mutableMapOf<String, PostureQuery>()

        override fun registerServiceCheck(serviceId: String, query: PostureQuery) {
            queries.put(query.id, query)
        }

        override fun getPosture(): Array<PostureResponse> = queries.values
            .map{ processQuery(it) }
            .filterNotNull().toTypedArray()

        internal fun processQuery(q: PostureQuery): PostureResponse? = when(q.queryType) {
            PostureQueryType.OS -> PostureResponse.OS(q.id, "android", androidVersion, androidBuild)
            PostureQueryType.MAC -> PostureResponse.MAC(q.id, macAddresses)
            else -> null
        }
    }
}