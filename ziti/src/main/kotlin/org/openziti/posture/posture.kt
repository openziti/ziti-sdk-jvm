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

package org.openziti.posture

import org.openziti.api.PostureQuery
import org.openziti.api.PostureResponse
import java.util.*

internal object PostureLoader {
    val provider: PostureServiceProvider
    init {
        val l = ServiceLoader.load(PostureServiceProvider::class.java)
        provider = l.iterator().let {
            if (it.hasNext()) it.next()
            else DefaultPostureService.Provider
        }
    }
}

interface PostureService {
    fun registerServiceCheck(serviceId: String, query: PostureQuery)
    fun getPosture(): Array<PostureResponse>
}

@FunctionalInterface
interface PostureServiceProvider {
    fun getPostureService(): PostureService
}

fun PostureService(): PostureService =
    PostureLoader.provider.getPostureService()






