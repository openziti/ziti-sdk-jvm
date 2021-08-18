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

import kotlinx.coroutines.*
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.net.nio.connectSuspend
import org.openziti.net.nio.readSuspend
import org.openziti.net.nio.writeCompletely
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

object HalfCloseTest {
    class Server(val sock: AsynchronousServerSocketChannel) : CompletionHandler<AsynchronousSocketChannel, Any?>, CoroutineScope {

        override val coroutineContext = SupervisorJob() + Dispatchers.IO

        fun start() {
            sock.accept(null, this)
        }

        fun stop() {
            kotlin.runCatching { sock.close() }
            cancel()
        }

        fun runClient(clt: AsynchronousSocketChannel) = launch {
            val buf = ByteBuffer.allocate(1024)
            var done = false
            println("client[${clt.remoteAddress}] connected")
            try {
                while (!done) {
                    buf.clear()
                    val result = clt.readSuspend(buf)
                    if (result > 0) {
                        buf.flip()
                        println("received ${Charsets.UTF_8.decode(buf)}")
                        val msg = "you send ${result} bytes\n"
                        clt.writeCompletely(ByteBuffer.wrap(msg.toByteArray()))
                    } else if (result == -1) {
                        val msg = "you send EOF!!\n"
                        println("client sent EOF")
                        clt.writeCompletely(ByteBuffer.wrap(msg.toByteArray()))
                        clt.shutdownOutput()
                        done = true
                    }
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
                clt.runCatching { close() }
            }
        }

        override fun completed(clt: AsynchronousSocketChannel, attachment: Any?) {
            runClient(clt)
        }

        override fun failed(exc: Throwable?, attachment: Any?) {
            println("accept failed $exc")
            stop()
        }
    }

    class Client(val sock: AsynchronousSocketChannel): CoroutineScope {
        override val coroutineContext = SupervisorJob() + Dispatchers.IO

        fun run(addr: SocketAddress) = runBlocking(coroutineContext) {
            sock.connectSuspend(addr)

            val reader = launch {
                val readBuf = ByteBuffer.allocate(1024)
                while(true) {
                    readBuf.clear()
                    val read = sock.readSuspend(readBuf)
                    if (read > 0) {
                        readBuf.flip()
                        println("< " + Charsets.UTF_8.newDecoder().decode(readBuf).toString())
                    } else if (read == -1) {
                        println("server send FIN")
                        sock.close()
                        break
                    }
                }
            }

            for (i in 0..5) {
                val msg = "Hello this is $i!"
                sock.writeCompletely(ByteBuffer.wrap(msg.toByteArray()))
                delay(300)
            }

            sock.runCatching { shutdownOutput() }

            reader.join()
            println("reader joined")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {

        val ztx = Ziti.newContext(args[0], charArrayOf())

        try {
            val serverSocket = ztx.openServer()
            val a = ZitiAddress.Bind(args[1], "test-terminator")

            serverSocket.bind(a)
            val addr = serverSocket.localAddress

            val s = Server(serverSocket)
            s.start()
            println("server is listening on $addr")

            val clientSock = ztx.open()
            kotlin.runCatching {
                val clientAddr = ZitiAddress.Dial(a.service, identity = "test-terminator")
                Client(clientSock).run(clientAddr)
            }

            println("stopping server side")
            s.stop()
            runBlocking {
                s.coroutineContext.job.join()
            }
        } finally {
            println("stopping ziti context")
            ztx.destroy()
        }
    }
}