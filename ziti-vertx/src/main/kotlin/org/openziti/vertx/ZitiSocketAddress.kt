/*
 * Copyright (c) 2018-2025 NetFoundry Inc.
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

package org.openziti.vertx

import io.vertx.core.net.SocketAddress
import org.openziti.Identity
import org.openziti.ZitiAddress

class ZitiSocketAddress(val service: String,
                        val identity: String? = null,
                        val useEdgeId: Boolean = false): SocketAddress {
    override fun host() = null
    override fun hostName() = null
    override fun hostAddress() = null
    override fun port() = -1

    override fun path(): String  = "ziti://$service"

    override fun isInetSocket() = false
    override fun isDomainSocket() = true

    fun toBind() = ZitiAddress.Bind(service = service, identity = identity, useEdgeId = useEdgeId)
}