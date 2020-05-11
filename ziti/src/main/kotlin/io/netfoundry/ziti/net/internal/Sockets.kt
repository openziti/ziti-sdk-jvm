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

package io.netfoundry.ziti.net.internal

import io.netfoundry.ziti.impl.ZitiImpl
import io.netfoundry.ziti.net.ZitiSocketImpl
import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.ZitiLog
import java.lang.reflect.Constructor
import java.net.Socket
import java.net.SocketImpl
import java.security.PrivilegedAction
import java.util.concurrent.atomic.AtomicBoolean

internal object Sockets : Logged by ZitiLog() {

    private val initialized = AtomicBoolean(false)

    val defaultImplCls: Class<out SocketImpl>
    val defaultImplCons: Constructor<out SocketImpl>
    val defaultImplConsArgs: Array<Any?>

    init {
        val impl1 = Class.forName("java.net.SocksSocketImpl") as Class<SocketImpl>
        defaultImplCls = impl1

        var cons: Constructor<SocketImpl>
        var args: Array<Any?>
        try { // v8 contructor
            cons = defaultImplCls.getDeclaredConstructor()
            args = arrayOf()
        } catch(ex: NoSuchMethodException) {
            cons = defaultImplCls.getDeclaredConstructor(Boolean::class.java)
            args = arrayOf(false)
        }

        defaultImplCons = defaultImplCls.getDeclaredConstructor()

        java.security.AccessController.doPrivileged(
            PrivilegedAction<Any> {
                defaultImplCons.setAccessible(true)
            })

    }

    fun init() {
        if (initialized.compareAndSet(false, true)) {
            d { "internals initialized" }
            Socket.setSocketImplFactory { ZitiSocketImpl() }

            if (ZitiImpl.onAndroid) {
                HTTP.init()
            }
        }
    }

    class BypassSocket : Socket(defaultImplCons.newInstance())
}

