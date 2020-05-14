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

package io.netfoundry.ziti.sample.netcat

import io.netfoundry.ziti.Ziti
import io.netfoundry.ziti.ZitiAddress
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.System.exit
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import kotlin.text.Charsets.UTF_8

object NetcatHost {

    suspend fun suspendRead(buf: ByteBuffer, clt: AsynchronousSocketChannel): Int {
        val result = CompletableDeferred<Int>()
        clt.read(buf, result, object : CompletionHandler<Int, CompletableDeferred<Int>>{
            override fun completed(result: Int, c: CompletableDeferred<Int>) {
                c.complete(result)
            }
            override fun failed(exc: Throwable, c: CompletableDeferred<Int>) {
                c.completeExceptionally(exc)
            }
        })

        return result.await()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 2) {
            println("Usage: NetcatHost <config> <service-name>")
            exit(1)
        }

        val cfg = args[0]
        val service = args[1]

        val ziti = Ziti.newContext(cfg, charArrayOf())

        val server = ziti.openServer()
        server.bind(ZitiAddress(service))

        while(true) {
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