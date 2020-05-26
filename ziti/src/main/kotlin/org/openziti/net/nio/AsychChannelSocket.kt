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

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.channels.AsynchronousSocketChannel
import javax.net.SocketFactory

/**
 * Socket wrapper over AsynchronousSocketChannel.
 *
 * most of the work is done in [AsyncSocketImpl]
 *
 * @see AsyncSocketImpl
 */
internal
class AsychChannelSocket(internal val impl: AsyncSocketImpl = AsyncSocketImpl()): Socket(impl) {

    class Factory: SocketFactory() {
        override fun createSocket(): Socket  = AsychChannelSocket()
        override fun createSocket(host: String?, port: Int): Socket  = AsychChannelSocket(host, port)
        override fun createSocket(host: InetAddress?, port: Int): Socket  = AsychChannelSocket(host, port)

        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket  {
            TODO("Not yet implemented")
        }

        override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
            TODO("Not yet implemented")
        }
    }

    constructor(ch: AsynchronousSocketChannel): this(AsyncSocketImpl(ch))

    constructor(host: String?, port: Int) : this(InetAddress.getByName(host), port)

    constructor(address: InetAddress?, port: Int) : this() {
        connect(InetSocketAddress(address, port))
    }

}