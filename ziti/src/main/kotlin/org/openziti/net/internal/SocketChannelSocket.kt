/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

import java.io.InputStream
import java.io.OutputStream
import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel

/**
 * Bypass Socket.
 *
 * Wraps NIO SocketChannel and does not use SocketImpl.
 * Implements all socket methods.
 */
class SocketChannelSocket(val ch: SocketChannel): Socket(null as SocketImpl?) {
    private var inputShut = false
    private var outputShut = false
    private val sel: Selector
    private val selKey: SelectionKey
    constructor(): this(SocketChannel.open())

    init {
        ch.configureBlocking(false)
        sel = ch.provider().openSelector()
        selKey = ch.register(sel, SelectionKey.OP_CONNECT)
    }

    override fun bind(bindpoint: SocketAddress?) {
        ch.bind(bindpoint)
    }

    inner class Output: OutputStream() {
        override fun write(b: Int) {
            write(byteArrayOf(b.toByte()))
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            val buf = ByteBuffer.wrap(b, off, len)
            val timeout = soTimeout
            val start = System.currentTimeMillis()
            while (buf.hasRemaining()) {
                if (ch.write(buf) == 0) {
                    val toRemaining = timeout - (System.currentTimeMillis() - start)
                    if (toRemaining <= 0) {
                        close()
                        throw SocketTimeoutException()
                    }

                    sel.select(toRemaining)
                }
            }
        }
    }

    inner class Input: InputStream() {
        override fun read(): Int {
            val b = ByteArray(1)
            val rc = read(b)
            if (rc == -1) return rc
            return b[0].toInt()
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            if (isInputShutdown) return -1

            val buf = ByteBuffer.wrap(b, off, len)
            val start = System.currentTimeMillis()
            val timeout = soTimeout

            synchronized(selKey) {
                val rc = ch.read(buf)
                if (rc != 0) {
                    return rc
                }

                while(true) {
                    var toRemaining = 0L
                    if (timeout > 0) {
                        val elapsed = System.currentTimeMillis() - start
                        if (elapsed >= timeout) {
                            throw SocketTimeoutException()
                        }
                        toRemaining = timeout - elapsed
                    }

                    sel.select(toRemaining)
                    for (k in sel.selectedKeys()) {
                        if (k.isReadable) {
                            val count = ch.read(buf)
                            if (count != 0)
                                return count
                        }
                    }
                }
            }
        }

        override fun close() {
            shutdownInput()
            sel.close()
        }
    }

    override fun close() {
        sel.runCatching { close() }
        ch.runCatching { close() }
    }

    override fun connect(endpoint: SocketAddress?) {
        connect(endpoint, 0)
    }

    override fun connect(endpoint: SocketAddress?, timeout: Int) {
        if (timeout == 0) {
            ch.configureBlocking(true)
            ch.connect(endpoint)
            ch.configureBlocking(false)
            selKey.interestOps(SelectionKey.OP_READ)
            return
        }

        ch.configureBlocking(false)

        if (ch.connect(endpoint)) {
            selKey.interestOps(SelectionKey.OP_READ)
            return
        }

        val start = System.currentTimeMillis()
        while (true) {
            sel.select(timeout.toLong())
            if (selKey.isConnectable) {
                if (ch.finishConnect()) {
                    selKey.interestOps(SelectionKey.OP_READ)
                } else {
                    close()
                }
                return
            }

            if (System.currentTimeMillis() - start > timeout) {
                close()
                throw SocketTimeoutException()
            }
        }

    }

    override fun getInetAddress(): InetAddress? = remoteSocketAddress.address
    override fun getLocalAddress(): InetAddress? = (ch.localAddress as InetSocketAddress).address
    override fun getPort(): Int = remoteSocketAddress.port
    override fun getLocalPort(): Int = localSocketAddress.port
    override fun getRemoteSocketAddress(): InetSocketAddress = ch.remoteAddress as InetSocketAddress
    override fun getLocalSocketAddress(): InetSocketAddress = ch.localAddress as InetSocketAddress

    override fun getChannel(): SocketChannel = ch
    override fun getInputStream(): InputStream = Input()
    override fun getOutputStream(): OutputStream = Output()

    override fun setTcpNoDelay(on: Boolean) { ch.setOption(StandardSocketOptions.TCP_NODELAY, on) }
    override fun getTcpNoDelay(): Boolean = ch.getOption(StandardSocketOptions.TCP_NODELAY)

    override fun setSoLinger(on: Boolean, linger: Int) { ch.setOption(StandardSocketOptions.SO_LINGER, linger) }
    override fun getSoLinger(): Int = ch.getOption(StandardSocketOptions.SO_LINGER)

    override fun sendUrgentData(data: Int) {
        error("not supported")
    }

    override fun setOOBInline(on: Boolean) {
        error("not supported")
    }

    override fun getOOBInline(): Boolean {
        error("not supported")
    }

    override fun setSendBufferSize(size: Int) { ch.setOption(StandardSocketOptions.SO_SNDBUF, size) }
    override fun getSendBufferSize(): Int = ch.getOption(StandardSocketOptions.SO_SNDBUF)

    override fun setReceiveBufferSize(size: Int) { ch.setOption(StandardSocketOptions.SO_RCVBUF, size) }
    override fun getReceiveBufferSize(): Int = ch.getOption(StandardSocketOptions.SO_RCVBUF)

    override fun setKeepAlive(on: Boolean) { ch.setOption(StandardSocketOptions.SO_KEEPALIVE, on) }

    override fun getKeepAlive(): Boolean = ch.getOption(StandardSocketOptions.SO_KEEPALIVE)

    override fun setTrafficClass(tc: Int) { ch.setOption(StandardSocketOptions.IP_TOS, tc) }
    override fun getTrafficClass(): Int = ch.getOption(StandardSocketOptions.IP_TOS)

    override fun setReuseAddress(on: Boolean) { ch.setOption(StandardSocketOptions.SO_REUSEADDR, on) }
    override fun getReuseAddress(): Boolean = ch.getOption(StandardSocketOptions.SO_REUSEADDR)

    override fun shutdownInput() {
        ch.shutdownInput()
        inputShut = true
    }
    override fun shutdownOutput() {
        ch.shutdownOutput()
        outputShut = true
    }

    override fun isConnected(): Boolean = ch.isConnected

    override fun isBound(): Boolean = ch.localAddress != null

    override fun isClosed(): Boolean = !ch.isOpen

    override fun isInputShutdown(): Boolean = ch.isConnected && inputShut
    override fun isOutputShutdown(): Boolean = ch.isConnected && outputShut

    override fun <T : Any?> setOption(name: SocketOption<T>?, value: T): Socket =
        this.apply {
            ch.setOption(name, value)
        }

    override fun <T : Any?> getOption(name: SocketOption<T>?): T = ch.getOption(name)
    override fun supportedOptions(): MutableSet<SocketOption<*>> = ch.supportedOptions()
}