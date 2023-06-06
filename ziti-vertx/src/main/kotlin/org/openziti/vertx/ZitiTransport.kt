/*
 * Copyright (c) 2018-2023 NetFoundry Inc.
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

package org.openziti.vertx

import io.netty.channel.*
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.InternetProtocolFamily
import io.vertx.core.spi.transport.Transport
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import org.openziti.netty.ZitiChannelFactory
import org.openziti.netty.ZitiServerChannelFactory
import java.net.SocketAddress
import java.util.concurrent.ThreadFactory

class ZitiTransport(val ztx: ZitiContext, val binding: Map<Int, ZitiAddress.Bind>): Transport {

    constructor(ztx: ZitiContext): this(ztx, emptyMap())

    override fun channelFactory(domainSocket: Boolean): ChannelFactory<out Channel> = ZitiChannelFactory(ztx)

    override fun serverChannelFactory(domainSocket: Boolean): ChannelFactory<out ServerChannel> =
        ZitiServerChannelFactory(ztx, binding)

    override fun eventLoopGroup(type: Int, nThreads: Int, threadFactory: ThreadFactory?, ioRatio: Int): EventLoopGroup =
        DefaultEventLoopGroup(nThreads, threadFactory)

    override fun datagramChannel(): DatagramChannel {
        error("Not supported")
    }

    override fun datagramChannel(family: InternetProtocolFamily?): DatagramChannel {
        error("Not supported")
    }

    override fun convert(address: io.vertx.core.net.SocketAddress?) = address?.let {
        binding[it.port()]
    }

    override fun convert(address: SocketAddress?): io.vertx.core.net.SocketAddress? {
        return if (address is ZitiAddress.Session) {
            io.vertx.core.net.SocketAddress.domainSocketAddress("${address.service}/${address.callerId}")
        } else {
            super.convert(address)
        }
    }
}