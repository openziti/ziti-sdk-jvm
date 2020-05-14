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

package io.netfoundry.ziti.net.nio

import java.io.InputStream
import java.io.OutputStream
import java.net.*
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

/**
 * SocketImpl wrapper over AsyncSocketChannel
 */
internal class AsyncSocketImpl(ch: AsynchronousSocketChannel? = null): SocketImpl() {
    internal lateinit var channel: AsynchronousSocketChannel

    internal val inputLock = Semaphore(1)
    internal val input = ByteBuffer.allocate(32 * 1024).apply {
        (this as Buffer).flip()
    }
    internal val timeout = AtomicInteger(Socket().soTimeout)

    init {
        ch?.let {
            channel = it
        }
    }

    override fun bind(host: InetAddress?, port: Int) = error("only client sockets are supported")
    override fun accept(s: SocketImpl?) = error("only client sockets are supported")
    override fun listen(backlog: Int) = error("only client sockets are supported")

    override fun getInetAddress() = (channel.remoteAddress as InetSocketAddress?)?.address
    override fun getPort() = (channel.remoteAddress as InetSocketAddress?)?.port ?: 0
    override fun getLocalPort() = (channel.localAddress as InetSocketAddress?)?.port ?: 0

    override fun shutdownInput() {
        channel.shutdownInput()
    }

    override fun shutdownOutput() {
        channel.shutdownOutput()
    }

    override fun create(stream: Boolean) {
        require(stream){"only stream sockets are supported"}
        if (!::channel.isInitialized) {
            channel = AsynchronousSocketChannel.open()
        }
    }

    override fun connect(host: String, port: Int) =
        connect(InetSocketAddress(host, port), 0)

    override fun connect(address: InetAddress, port: Int) = connect(
        InetSocketAddress(address, port), 0)

    override fun connect(address: SocketAddress, timeout: Int) {
        val cf = channel.connect(address)
        if (timeout > 0) {
            cf.get(timeout.toLong(), TimeUnit.MILLISECONDS)
        } else {
            cf.get()
        }
    }

    public
    override fun getInputStream(): InputStream {
        return object : InputStream(){
            override fun read(): Int {
                val oneByte = ByteArray(1)
                val rc = read(oneByte, 0, 1)
                if (rc < 0)
                    return -1
                return oneByte[0].toInt()
            }

            override fun read(b: ByteArray, off: Int, len: Int): Int {
                synchronized(input) {
                    inputLock.acquire() // protects async read operation
                    val count = min(len, input.remaining())
                    if (count > 0) {
                        input.get(b, off, count)
                        inputLock.release()
                        return count
                    }

                    input.compact()
                    val rf = CompletableFuture<Int>()
                    val to = getOption(SocketOptions.SO_TIMEOUT) as Number
                    channel.read(input, 0, TimeUnit.MILLISECONDS, rf,
                        object : CompletionHandler<Int, CompletableFuture<Int>> {

                            override fun completed(result: Int, f: CompletableFuture<Int>) {
                                if (!f.isDone) {
                                    f.complete(result)
                                }
                                input.flip()
                                inputLock.release()
                            }

                            override fun failed(exc: Throwable, f: CompletableFuture<Int>) {
                                if (!f.isDone) {
                                    f.completeExceptionally(exc)
                                }
                                inputLock.release()
                            }
                        })

                    try {
                        val read = rf.get(to.toLong(), TimeUnit.MILLISECONDS)
                        if (read == -1) {
                            return -1
                        }
                        inputLock.acquire()
                        val count1 = min(len, input.remaining())
                        input.get(b, off, count1)
                        inputLock.release()
                        return count1
                    } catch (toex: TimeoutException) {
                        throw SocketTimeoutException(toex.message)
                    } catch (exx: ExecutionException) {
                        exx.cause?.let { throw it }
                        throw exx
                    }
                }
            }

            override fun available(): Int {
                return input.remaining()
            }
        }
    }

    override fun close() {
        channel.close()
    }

    override fun getOption(optID: Int): Any? {
        if (optID == SocketOptions.SO_TIMEOUT) {
            return timeout.get()
        }
        return null
    }

    override fun setOption(optID: Int, value: Any?) {
        if (optID == SocketOptions.SO_TIMEOUT) {
            timeout.set((value as Number).toInt())
        }
    }

    public
    override fun getOutputStream(): OutputStream = object : OutputStream() {
        override fun write(b: Int) {
            write(byteArrayOf(b.toByte()))
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            val wf = channel.write(ByteBuffer.wrap(b, off, len))
            wf.get()
        }
    }

    override fun available(): Int {
        return input.remaining()
    }

    override fun sendUrgentData(data: Int) {
        outputStream.write(data)
    }
}