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

package org.openziti.net.nio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.openziti.Ziti
import org.openziti.impl.ZitiContextImpl
import org.openziti.net.ZitiSocketFactory
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.IOException
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.nio.channels.SocketChannel
import java.nio.channels.spi.SelectorProvider
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SyncSocketChannel(provider: SelectorProvider?) : SocketChannel(provider),
    Logged by ZitiLog() {

    private lateinit var channelImpl: AsynchronousSocketChannel

    private fun validateConnection() {
        if (!this::channelImpl.isInitialized || isConnected.not()) IOException("Channel is not connected")
    }

    override fun implCloseSelectableChannel() {
        if(this::channelImpl.isInitialized) {
            channelImpl.close()
        }
    }

    override fun implConfigureBlocking(block: Boolean) {
        TODO("Not yet implemented")
    }

    override fun read(dst: ByteBuffer?): Int {
        validateConnection()
        return channelImpl.read(dst).get()
    }

    override fun read(dsts: Array<out ByteBuffer>?, offset: Int, length: Int): Long {
        validateConnection()
        var response: Long

        runBlocking {
             response = suspendCoroutine { cont ->
                 channelImpl.read(
                     dsts,
                     offset,
                     length,
                     -1, // No read timeout set from here - delegate to underlying channel
                     TimeUnit.MILLISECONDS,
                     this,
                     object : CompletionHandler<Long, CoroutineScope> {
                         override fun completed(result: Long?, attachment: CoroutineScope?) {
                             cont.resume(result ?: -1)
                         }

                         override fun failed(exc: Throwable?, attachment: CoroutineScope?) {
                             if (null != exc) cont.resumeWithException(exc)
                             else cont.resumeWithException(IllegalStateException("Channel read failed without an exception"))
                         }
                     })
             }
        }
        return response
    }

    override fun write(src: ByteBuffer?): Int {
        validateConnection()
        return channelImpl.write(src).get()
    }

    override fun write(srcs: Array<out ByteBuffer>?, offset: Int, length: Int): Long {
        validateConnection()
        var response: Long

        runBlocking {
            response = suspendCoroutine { cont ->
                channelImpl.write(
                    srcs,
                    offset,
                    length,
                    -1,  // No read timeout set from here - delegate to underlying channel
                    TimeUnit.MILLISECONDS,
                    this,
                    object : CompletionHandler<Long, CoroutineScope> {
                        override fun completed(result: Long?, attachment: CoroutineScope?) {
                            cont.resume(result ?: -1)
                        }

                        override fun failed(exc: Throwable?, attachment: CoroutineScope?) {
                            if (null != exc) cont.resumeWithException(exc)
                            else cont.resumeWithException(IllegalStateException("Channel read failed without an exception"))
                        }
                    })
            }
        }
        return response
    }

    override fun bind(local: SocketAddress?): SocketChannel = error("only client sockets are supported")

    override fun getLocalAddress(): SocketAddress {
        validateConnection()
        return channelImpl.localAddress
    }

    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): SocketChannel {
        validateConnection()
        channelImpl.setOption(name, value)
        return this
    }

    override fun <T : Any?> getOption(name: SocketOption<T>?): T {
        validateConnection()
        return channelImpl.getOption(name)
    }

    override fun supportedOptions(): MutableSet<SocketOption<*>> {
        validateConnection()
        return channelImpl.supportedOptions()
    }

    override fun shutdownInput(): SocketChannel {
        validateConnection()
        channelImpl.shutdownInput()
        return this
    }

    override fun shutdownOutput(): SocketChannel {
        validateConnection()
        channelImpl.shutdownOutput()
        return this
    }

    override fun socket(): Socket {
        validateConnection()
        return AsychChannelSocket(channelImpl)
    }

    override fun isConnected(): Boolean = this::channelImpl.isInitialized && channelImpl.isOpen

    override fun isConnectionPending(): Boolean = this::channelImpl.isInitialized && !channelImpl.isOpen

    override fun connect(addr: SocketAddress?): Boolean {
        val sockAddr = addr as InetSocketAddress
        for (ctx in Ziti.getContexts()) {
            val ctxImpl = ctx as ZitiContextImpl
            try {
                val s = ctxImpl.getService(sockAddr)
                if (s != null) {
                    channelImpl = ctx.open()
                    channelImpl.connect(sockAddr).get()
                    break
                }
            } catch (ex: Exception) {
                w { "${ctx.name()}: $ex" }
            }
        }
        if (!isConnected) {
            i { "no ZitiContext provides service for $addr" }
            throw ConnectException()
        }
        return true
    }

    override fun finishConnect(): Boolean  = true // NOOP

    override fun getRemoteAddress(): SocketAddress {
        return channelImpl.remoteAddress
    }
}
