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
import java.lang.reflect.Field
import java.net.URL
import java.net.URLConnection
import java.net.URLStreamHandler
import java.net.URLStreamHandlerFactory

typealias useZitiPred = (String, Int) -> Boolean

internal object HTTPS : Logged by JULogged() {

    lateinit var useZiti: useZitiPred

    fun init(pred: useZitiPred) {
        if (::useZiti.isInitialized) {
            w { "already initialized" }
        } else {
            i { "HTTPS internals initialized" }
            useZiti = pred
        }
    }

    lateinit var defaultHandler: URLStreamHandler
    lateinit var facField: Field

    init {
        try {
            val handlerField = URL::class.java.getDeclaredField("handler").apply {
                isAccessible = true
            }
            defaultHandler = URL("https://google.com").let {
                handlerField.get(it) as URLStreamHandler
            }

            facField = URL::class.java.getDeclaredField("factory").apply { isAccessible = true }

        } catch (ex: Exception) {
            e(ex) { "HTTPS internals failed to initialize" }
        }

        try {
            URL.setURLStreamHandlerFactory(Factory())
        } catch (ex: Error) {
            e(ex) { "failed to install URL handler factory" }
        }
    }

    class Factory : URLStreamHandlerFactory, Logged by JULogged("https-factory") {
        override fun createURLStreamHandler(protocol: String): URLStreamHandler? {
            return when (protocol) {
                "https" -> Handler()
                else -> null
            }
        }
    }

    class Handler : URLStreamHandler() {
        override fun getDefaultPort() = 443

        override fun openConnection(u: URL?): URLConnection {
            if (u == null)
                throw IllegalArgumentException("url is null")

            val port = if (u.port == -1) u.defaultPort else u.port

            return if (useZiti(u.host, port)) {
                v { "using ZitiHttpsConnection for ${u}" }
                ZitiHTTPSConnection(u)
            } else {
                v { "using default HTTP connectioin for ${u}" }
                return URL(u, "", defaultHandler).openConnection()
            }
        }

    }
}