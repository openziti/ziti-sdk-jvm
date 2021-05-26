/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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

package org.openziti.sample.netcat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import java.lang.System.exit
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import kotlin.text.Charsets.UTF_8

object NetcatHost {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 2) {
            println("Usage: NetcatHost <config> <service-name>")
            exit(1)
        }

        val cfg = args[0]
        val service = args[1]

        Ziti.setApplicationInfo("org.openziti.sample.NetCatHost", "v1.0")
        val ziti = Ziti.newContext(cfg, charArrayOf())

        runBlocking {
            ziti.statusUpdates().takeWhile { it !is ZitiContext.Status.Active }.collect { println(it) }
        }
        while(true) {
            try {
                val server = ziti.openServer()
                server.bind(ZitiAddress.Bind(service))

                processClients(server)

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun processClients(server: AsynchronousServerSocketChannel) {
        while (true) {
            println("waiting for clients")
            val clt = server.accept().get()
            println("client connected")

            val writer = Thread {
                while (true) {
                    print("> ")
                    val l = readLine() + "\n"
                    clt.write(ByteBuffer.wrap(l.toByteArray()))
                }
            }
            writer.start()

            runBlocking {
                val reader = launch(Dispatchers.IO) {
                    val readBuf = ByteBuffer.allocate(1024)
                    while (true) {
                        val rc = suspendRead(readBuf, clt)
                        if (rc == -1) {
                            clt.close()
                            break
                        } else {
                            readBuf.flip()
                            val text = UTF_8.decode(readBuf).toString()
                            print(text)
                            readBuf.compact()
                        }
                    }
                }
                reader.invokeOnCompletion { writer.interrupt() }
                reader.join()
            }
            println("client disconnected")
        }
    }
}