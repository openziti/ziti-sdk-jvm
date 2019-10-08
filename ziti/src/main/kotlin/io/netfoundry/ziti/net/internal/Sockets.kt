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

import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import java.lang.reflect.Constructor
import java.net.InetAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.SocketImpl

/**
 *
 * @author eugene
 * @since 12/12/18
 */
internal object Sockets : Logged by JULogged() {

    val baseImplCls = SocketImpl::class.java

    val createMethod = baseImplCls.getDeclaredMethod("create", Boolean::class.java).apply { isAccessible = true }
    val connect1 = baseImplCls.getDeclaredMethod("connect", SocketAddress::class.java, Int::class.java)
        .apply { isAccessible = true }
    val inStream = baseImplCls.getDeclaredMethod("getInputStream").apply { isAccessible = true }
    val outStream = baseImplCls.getDeclaredMethod("getOutputStream").apply { isAccessible = true }
    val close = baseImplCls.getDeclaredMethod("close").apply { isAccessible = true }
    val getFD = baseImplCls.getDeclaredMethod("getFileDescriptor").apply { isAccessible = true }
    val bind =
        baseImplCls.getDeclaredMethod("bind", InetAddress::class.java, Int::class.java).apply { isAccessible = true }

    val defaultImplCls: Class<out SocketImpl>
    val defaultImplCons: Constructor<out SocketImpl>

    init {
        val s = Socket()
        val implField = s::class.java.getDeclaredField("impl").apply {
            isAccessible = true
        }

        val impl = implField.get(s) as SocketImpl
        defaultImplCls = impl::class.java.apply {
        }

        defaultImplCons = defaultImplCls.getDeclaredConstructor().apply {
            isAccessible = true
        }
    }

    fun init() {
        i { "internals initialized" }
    }

    class BypassSocket : Socket(defaultImplCons.newInstance())
}

