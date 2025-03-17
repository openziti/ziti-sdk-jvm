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

package org.openziti.net.nio

import java.io.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import java.nio.channels.AsynchronousSocketChannel

/**
 * Socket wrapper over AsynchronousSocketChannel.
 *
 * most of the work is done in [AsyncSocketImpl]
 *
 * @see AsyncSocketImpl
 */
internal
class AsychChannelSocket(internal val impl: AsyncSocketImpl = AsyncSocketImpl()): Socket(impl) {

    constructor(ch: AsynchronousSocketChannel): this(AsyncSocketImpl(ch))

    constructor(host: String?, port: Int) : this(InetAddress.getByName(host), port)

    constructor(address: InetAddress?, port: Int) : this() {
        connect(InetSocketAddress(address, port))
    }

    override fun isConnected(): Boolean {
        return impl.channel.remoteAddress != null
    }

    override fun isClosed(): Boolean = !impl.channel.isOpen

    override fun close() {
        impl.channel.close()
    }

    override fun getInputStream(): InputStream {
        if (isClosed())
            throw SocketException("Socket is closed")
        if (!isConnected)
            throw SocketException("Socket is not connected")

        return object : FilterInputStream(impl.inputStream) {
            override fun close() {
                this@AsychChannelSocket.close()
            }
        }
    }

    override fun getOutputStream(): OutputStream {
        if (isClosed())
            throw SocketException("Socket is closed")
        if (!isConnected)
            throw SocketException("Socket is not connected")

        return object : FilterOutputStream(impl.outputStream) {
            override fun close() {
                this@AsychChannelSocket.close()
            }
        }
    }
}