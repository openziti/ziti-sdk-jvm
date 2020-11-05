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
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun AsynchronousSocketChannel.asyncRead(b: ByteBuffer) = suspendCoroutine<Int> {
    this.read(b, it, object : CompletionHandler<Int, Continuation<Int>>{
        override fun completed(result: Int, cont: Continuation<Int>) = cont.resume(result)
        override fun failed(exc: Throwable, cont: Continuation<Int>) = cont.resumeWithException(exc)
    })
}

suspend fun AsynchronousSocketChannel.asyncConnect(addr: SocketAddress) = suspendCoroutine<Void?> {
    this.connect(addr, it, object : CompletionHandler<Void?, Continuation<Void?>>{
        override fun completed(result: Void?, cont: Continuation<Void?>) = cont.resume(result)
        override fun failed(exc: Throwable, cont: Continuation<Void?>) = cont.resumeWithException(exc)
    })
}

object HalfCloseTest {
    class Server(val sock: AsynchronousServerSocketChannel) : CompletionHandler<AsynchronousSocketChannel, Any?> {
        val deferred = CompletableDeferred<Unit>()

        fun start() {
            sock.accept(null, this)
        }

        fun stop() {
            sock.close()
            runBlocking { deferred.complete(Unit) }
        }

        fun runClient(clt: AsynchronousSocketChannel) = GlobalScope.launch(Dispatchers.IO) {
            val buf = ByteBuffer.allocate(1024)
            var done = false
            try {
                while (!done) {
                    buf.clear()
                    val result = clt.asyncRead(buf)
                    if (result > 0) {
                        buf.flip()
                        println("received ${Charsets.UTF_8.decode(buf)}")
                        val msg = "you send ${result} bytes\n"
                        clt.write(ByteBuffer.wrap(msg.toByteArray()))
                    } else if (result == -1) {
                        val msg = "you send EOF!!\n"
                        println("client sent EOF")
                        clt.write(ByteBuffer.wrap(msg.toByteArray())).get()
                        clt.shutdownOutput()
                        done = true
                    }
                }
            } catch(ex: Exception) {
                ex.printStackTrace()
                clt.close()
            }
        }

        override fun completed(clt: AsynchronousSocketChannel, attachment: Any?) {
            runClient(clt)
        }

        override fun failed(exc: Throwable?, attachment: Any?) {
            stop()
        }
    }

    class Client(val sock: AsynchronousSocketChannel) {
        suspend fun run(addr: SocketAddress) {
            sock.asyncConnect(addr)

            val reader = GlobalScope.launch(Dispatchers.IO) {
                val readBuf = ByteBuffer.allocate(1024)
                while(true) {
                    readBuf.clear()
                    val read = sock.asyncRead(readBuf)
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
                sock.write(ByteBuffer.wrap(msg.toByteArray()))
                delay(300)
            }

            sock.shutdownOutput()

            reader.join()
            println("reader joined")
        }
    }
    @JvmStatic
    fun main(args: Array<String>) {

        val ztx = Ziti.newContext(args[0], charArrayOf())

        val serverSocket = ztx.openServer()
        val a = ZitiAddress.Service(args[1])

        serverSocket.bind(a)
        val addr = serverSocket.localAddress

        val s = Server(serverSocket)
        s.start()
        println("server is listening on $addr")

        val clientSock = ztx.open()
        runBlocking {
            Client(clientSock).run(addr)
        }
        s.stop()
        //ztx.stop()
    }
}