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

package org.openziti.vertx.sample

import io.vertx.core.impl.VertxBuilder
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.vertx.ZitiTransport

object EchoServer {

    @JvmStatic
    fun main(args: Array<String>) {

        if (args.size != 2) {
            println("Usage: ${EchoServer.javaClass.simpleName} <identity> <service>")
            return
        }

        val ztx = Ziti.newContext(args[0], charArrayOf())
        val binding = mapOf(8080 to ZitiAddress.Bind(service = args[1]))

        val vertx = VertxBuilder()
            .findTransport(ZitiTransport(ztx, binding))
            .init()
            .vertx()

        vertx.createNetServer().apply {
            connectHandler { clt ->
                println("clt[${clt.remoteAddress()}] connected")
                clt.handler {
                    clt.write(it)
                }
                clt.endHandler {
                    println("clt[${clt.remoteAddress()}] disconnected")
                }
            }
            listen(8080)
        }
    }
}