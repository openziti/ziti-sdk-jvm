/*
 * Copyright (c) 2018-2023 NetFoundry Inc.
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

import java.net.SocketAddress

/**
 * Object representing Ziti address.
 * It can be used to initiate connections or host services on Ziti network.
 */
sealed class ZitiAddress: SocketAddress() {
    /**
     * Address representing a Ziti service.
     * @param name name of the service
     * @param identity target terminator
     * @param callerId caller identity
     */

    data class Dial @JvmOverloads constructor (
        val service: String,
        val appData: Any? = null,
        val identity: String? = null,
        val callerId: String? = null
    ) : ZitiAddress()

    data class Bind  @JvmOverloads constructor(
        val service: String, val identity: String? = null, val useEdgeId: Boolean = false)
        : ZitiAddress()

    data class Session internal constructor(
        internal val id: String,
        val service: String,
        val callerId: String?,
        val appData: ByteArray?
    ): ZitiAddress()
}