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

package io.netfoundry.ziti.netty

import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.ZitiLog
import io.netty.channel.AbstractServerChannel
import io.netty.channel.ChannelConfig
import io.netty.channel.DefaultChannelConfig
import io.netty.channel.EventLoop
import java.net.SocketAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

class ZitiServerChannel(val ch: AsynchronousServerSocketChannel): AbstractServerChannel(), Logged by ZitiLog() {

    override fun doBeginRead() {
        d("starting accept")
        ch.accept(this, object : CompletionHandler<AsynchronousSocketChannel, ZitiServerChannel> {
            override fun completed(peer: AsynchronousSocketChannel, serv: ZitiServerChannel) {
                d("accepted $peer")
                pipeline().fireChannelRead(ZitiChannel(this@ZitiServerChannel, peer))
                serv.ch.accept(serv, this)
            }
            override fun failed(exc: Throwable?, s: ZitiServerChannel) {
                s.pipeline().fireExceptionCaught(exc)
            }
        })
    }

    override fun isActive(): Boolean {
        return ch.isOpen && (ch.localAddress != null)
    }

    override fun isCompatible(loop: EventLoop?): Boolean {
        return true
    }

    override fun doBind(localAddress: SocketAddress) {
        ch.bind(localAddress)
    }

    override fun doClose() {
        ch.close()
        pipeline().fireChannelInactive()
    }

    override fun config(): ChannelConfig = DefaultChannelConfig(this)

    override fun localAddress0(): SocketAddress {
        return ch.localAddress
    }

    override fun isOpen(): Boolean {
        return ch.isOpen
    }
}