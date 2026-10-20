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

    /**
     * Terminator precedence for a hosted service binding.
     */
    enum class Precedence(val value: Byte) {
        Default(0), Required(1), Failed(2);
    }

    /**
     * Address representing a service to host. The resulting hosted service is backed by
     * multiple terminators -- up to [maxTerminators] (one per available edge router) --
     * to keep the service reachable if a router goes away.
     *
     * @param service name of the service
     * @param identity terminator identity. If not specified, the terminator is not
     *        addressable by identity
     * @param useEdgeId use the current identity name as terminator identity
     * @param cost terminator cost; defaults to the identity's hosting cost attribute
     *        (`serviceHostingCosts`/`defaultHostingCost`), 0 if not set. Only sent if > 0
     * @param precedence terminator precedence; defaults to the identity's hosting
     *        precedence attribute (`serviceHostingPrecedences`/`defaultHostingPrecedence`)
     * @param maxTerminators max number of terminators to establish, one per edge router
     */
    data class Bind @JvmOverloads constructor(
        val service: String,
        val identity: String? = null,
        val useEdgeId: Boolean = false,
        val cost: Int? = null,
        val precedence: Precedence? = null,
        val maxTerminators: Int = 3)
        : ZitiAddress()

    class Session internal constructor(
        internal val id: String,
        val service: String,
        val callerId: String?,
        val appData: ByteArray?
    ): ZitiAddress()
}