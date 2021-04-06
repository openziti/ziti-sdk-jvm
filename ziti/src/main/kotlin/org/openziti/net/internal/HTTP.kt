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

import org.openziti.Ziti
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.lang.reflect.Field
import java.net.*

internal object HTTP: Logged by ZitiLog() {

    const val HTTP_PORT = 80
    const val HTTPS_PORT = 443

    lateinit var defaultHTTPSHandler: URLStreamHandler
    lateinit var defaultHTTPHandler: URLStreamHandler
    lateinit var facField: Field

    fun init() {
        try {
            val handlerField =
                URL::class.java.getDeclaredField("handler").apply { isAccessible = true }
            defaultHTTPSHandler = URL("https://google.com").let {
                handlerField.get(it) as URLStreamHandler
            }
            defaultHTTPHandler = URL("http://google.com").let {
                handlerField.get(it) as URLStreamHandler
            }

            facField = URL::class.java.getDeclaredField("factory").apply { isAccessible = true }
        } catch (ex: Exception) {
            e("$ex")
        }

        try {
            i("setting Ziti URLStreamFactory")
            URL.setURLStreamHandlerFactory(Factory())
        } catch (ex: Error) {
            w("$ex")
        }
    }

    class Factory : URLStreamHandlerFactory {
        override fun createURLStreamHandler(protocol: String?): URLStreamHandler? {
            return when (protocol) {
                "https" -> Handler(HTTPS_PORT, defaultHTTPSHandler)
                "http" -> Handler(HTTP_PORT, defaultHTTPHandler)
                else -> null
            }
        }
    }

    class Handler(val defPort: Int, val defHandler: URLStreamHandler) : URLStreamHandler() {

        override fun getDefaultPort() = defPort

        override fun openConnection(u: URL): URLConnection {
            val port = if (u.port == -1) u.defaultPort else u.port

            Ziti.getServiceFor(u.host, port)?.let {
                when(u.protocol) {
                    "https" -> return ZitiHTTPSConnection(u)
                    "http" -> return ZitiHTTPConnection(u)

                    // should not be here
                    else -> throw IllegalArgumentException("invalid scheme")
                }
            }

            return URL(u, "", defHandler).openConnection()
        }

    }
}
