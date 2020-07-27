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

package org.openziti.net

import org.openziti.Ziti
import org.openziti.impl.ZitiContextImpl
import org.openziti.net.nio.AsychChannelSocket
import org.openziti.net.nio.AsyncSocketImpl
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.*
import java.nio.channels.AsynchronousSocketChannel
import javax.net.SocketFactory

/**
 *
 */
internal class ZitiSocketFactory: SocketFactory() {
    object ZitiConnector: AsyncSocketImpl.Connector, Logged by ZitiLog() {
        override fun connect(addr: SocketAddress, timeout: Int): AsynchronousSocketChannel {
            for (ctx in Ziti.getContexts()) {
                val ctxImpl = ctx as ZitiContextImpl
                val sockAddr = addr as InetSocketAddress
                try {
                    val s = ctxImpl.getService(addr.hostName, addr.port)
                    return doConnect(ctx.open(), addr, timeout)
                } catch (ex: Exception) {}
            }

            i{"no ZitiContext provides service for $addr"}
            throw ConnectException()
        }
    }

    override fun createSocket(): Socket = AsychChannelSocket(AsyncSocketImpl(ZitiConnector))

    override fun createSocket(p0: String?, p1: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: String?, p1: Int, p2: InetAddress?, p3: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: InetAddress?, p1: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(p0: InetAddress?, p1: Int, p2: InetAddress?, p3: Int): Socket {
        error("not implemented")
    }
}