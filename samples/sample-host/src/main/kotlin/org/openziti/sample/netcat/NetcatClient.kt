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

package org.openziti.sample.netcat

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.openziti.Ziti
import org.openziti.ZitiAddress
import java.lang.System.exit
import java.nio.ByteBuffer
import kotlin.text.Charsets.UTF_8

object NetcatClient {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 2) {
            println("Usage: NetcatClient <config> <service-name>")
            exit(1)
        }

        val cfg = args[0]
        val service = args[1]

        val ziti = Ziti.newContext(cfg, charArrayOf())

        val svc = ziti.getService(service, 1000L)
        val clt = ziti.open()
        try {
            clt.connect(ZitiAddress.Dial(service)).get()
        } catch (ex: Exception) {
            ex.printStackTrace()
            exit(1)
        }

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
    }
}