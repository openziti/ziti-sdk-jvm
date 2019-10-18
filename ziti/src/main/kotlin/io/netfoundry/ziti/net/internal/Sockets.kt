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

import io.netfoundry.ziti.net.ZitiSocketImpl
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import java.lang.reflect.Constructor
import java.net.Socket
import java.net.SocketImpl

internal object Sockets : Logged by JULogged() {

    val defaultImplCls: Class<out SocketImpl>
    val defaultImplCons: Constructor<out SocketImpl>

    init {
        val impl1 = Class.forName("java.net.SocksSocketImpl") as Class<SocketImpl>
        defaultImplCls = impl1

        defaultImplCons = defaultImplCls.getDeclaredConstructor().apply {
            isAccessible = true
        }
    }

    fun init() {
        d { "internals initialized" }
        Socket.setSocketImplFactory { ZitiSocketImpl() }
    }

    class BypassSocket : Socket(defaultImplCons.newInstance())
}

