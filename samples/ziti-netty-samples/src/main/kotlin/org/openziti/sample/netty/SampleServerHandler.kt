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

package org.openziti.sample.netty

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.UnsupportedMessageTypeException
import io.netty.handler.codec.http.*

class SampleServerHandler : ChannelInboundHandlerAdapter() {
    lateinit var resp: DefaultFullHttpResponse
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        when(msg) {
            is HttpRequest -> {
                println("req ${msg.method()} ${msg.uri()}")
                resp = DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK)
                resp.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
                resp.headers().set(HttpHeaderNames.SERVER, this.javaClass.simpleName)
                resp.content().writeCharSequence("${msg.protocolVersion()} ${msg.method()} ${msg.uri()}\n", Charsets.UTF_8)
                msg.headers().forEach {
                    resp.content().writeCharSequence("${it.key}: ${it.value}\n", Charsets.UTF_8)
                }
            }
            is HttpContent -> {
                println("got content $msg")
                resp.content().writeCharSequence("body is ${msg.content().readableBytes()} bytes\n", Charsets.UTF_8)

                if (msg is LastHttpContent) {
                    resp.headers().set(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes())
                    ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE)
                }
            }
            else -> throw UnsupportedMessageTypeException("msg is ${msg::class}")

        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        println("inactive $ctx")
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }

}
