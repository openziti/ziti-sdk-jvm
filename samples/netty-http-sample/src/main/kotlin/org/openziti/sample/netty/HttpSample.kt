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

package org.openziti.sample.netty

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.handler.codec.http.*
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import org.openziti.netty.ZitiChannelFactory
import org.openziti.netty.ZitiServerChannelFactory
import java.nio.charset.StandardCharsets

object HttpSample {

    lateinit var ziti: ZitiContext

    class clientInit: ChannelInitializer<Channel>(){
        override fun initChannel(ch: Channel) {
            ch.pipeline()
                .addLast(HttpClientCodec())
                .addLast(HttpContentDecompressor())
                .addLast(object : SimpleChannelInboundHandler<HttpObject>(){
                    override fun channelRead0(ctx: ChannelHandlerContext, msg: HttpObject) {
                        when (msg) {
                            is HttpResponse -> println("================\n$msg\n================\n")
                            is HttpContent -> {
                                println(msg.content().toString(StandardCharsets.UTF_8))
                                ctx.close()
                            }
                        }
                    }
                })
        }
    }

    class client: CliktCommand("client") {
        val service by option("-s","--service", help = "ziti service to dial").required()
        val hostname by option( "-H", "--hostname")

        override fun run() {
            val clt = Bootstrap()
//                .group(NioEventLoopGroup())
//                .channel(NioSocketChannel::class.java)
                .group(DefaultEventLoopGroup())
                .channelFactory(ZitiChannelFactory(ziti))
                .handler(clientInit())
                .connect(ZitiAddress.Dial(service)).sync().channel()
                .writeAndFlush(
                    DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/json").apply {
                        headers().add("host", hostname ?: service)
                    }
                )
                .sync()
        }
    }
    class server: CliktCommand("server") {
        val service by argument("service", "ziti service to bind to")

        override fun run() {
            val server = ServerBootstrap()
                .group(DefaultEventLoopGroup())
                //.group(NioEventLoopGroup())
                .channelFactory(ZitiServerChannelFactory(ziti))
                //.channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<Channel>(){
                    override fun initChannel(ch: Channel) {
                        println("new child $ch")
                        ch.pipeline().addLast(
                            HttpResponseEncoder(),
                            HttpRequestDecoder(),
                            SampleServerHandler()
                        )
                    }
                })
                // .bind(13333)
                .bind(ZitiAddress.Bind(service))
                .sync().addListener {
                    if (!it.isSuccess) {
                        println("server failed to bind ${it.cause()}")
                        System.exit(1)
                    }
                }
                .channel()
            println("listening on ${server.localAddress()}")
            server.closeFuture().sync()
        }

    }

    object cli: CliktCommand("http-sample", name = "http-sample") {
        val idFile by option("-i", "--id").file(mustBeReadable = true).required()
        init {
            subcommands(server(), client())
        }
        override fun run() {
            ziti = Ziti.newContext(idFile, charArrayOf())
        }
    }


    @JvmStatic
    fun main(args: Array<String>) = cli.main(args)
}