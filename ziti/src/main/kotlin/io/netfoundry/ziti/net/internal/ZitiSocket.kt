/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.net.internal

import io.netfoundry.ziti.ZitiConnection
import io.netfoundry.ziti.net.ZitiSocketImpl
import java.net.Socket

internal class ZitiSocket internal constructor(internal val zitiImpl: ZitiSocketImpl) : Socket(zitiImpl) {

    constructor(zc: ZitiConnection) : this(ZitiSocketImpl(zc))

    override fun isConnected(): Boolean {
        return zitiImpl.isConnected()
    }
}