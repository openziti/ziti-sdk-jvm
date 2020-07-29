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

package org.openziti.net.internal

import org.openziti.impl.ZitiImpl
import org.openziti.net.ZitiSocketFactory
import org.openziti.net.nio.AsyncSocketImpl
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

internal object Sockets : Logged by ZitiLog() {

    private val initialized = AtomicBoolean(false)
    var defaultSoTimeout: Int = 0

    fun init() {
        if (initialized.compareAndSet(false, true)) {
            defaultSoTimeout = Socket().soTimeout
            d { "internals initialized" }
            Socket.setSocketImplFactory { -> AsyncSocketImpl(ZitiSocketFactory.ZitiConnector) }

            if (ZitiImpl.onAndroid) {
                HTTP.init()
            }
        }
    }
}

