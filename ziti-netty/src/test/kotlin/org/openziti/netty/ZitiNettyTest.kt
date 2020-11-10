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

package org.openziti.netty

import com.google.gson.Gson
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.handler.codec.UnsupportedMessageTypeException
import io.netty.handler.codec.http.*
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ZitiNettyTest {
    class TestServerHandler : ChannelInboundHandlerAdapter() {
        lateinit var resp: DefaultFullHttpResponse
        lateinit var req: HttpRequest
        val body = StringBuilder()
        @Throws(Exception::class)
        override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
            when(msg) {
                is HttpRequest -> {
                    println("req ${msg.method()} ${msg.uri()}")
                    req = msg
                    resp = DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK)
                    resp.headers().set(HttpHeaderNames.SERVER, this::class.java.simpleName)
                }
                is HttpContent -> {
                    body.append(
                        msg.content().readCharSequence(msg.content().readableBytes(), Charsets.UTF_8)
                    )

                    if(msg is LastHttpContent) {
                        req.headers().fold(mutableMapOf<String,String>()){ m, h ->
                            m.put(h.key, h.value)
                            m
                        }

                        val r = mutableMapOf(
                            "body" to body.toString(),
                            "headers" to req.headers().map{it.key to it.value}.toMap()
                        )
                        val respBody = Gson().toJson(r)
                        resp.content().writeCharSequence(respBody, Charsets.UTF_8)
                        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE)
                    }
                }
                else -> throw UnsupportedMessageTypeException("msg is ${msg::class}")
            }
        }

        @Throws(Exception::class)
        override fun channelReadComplete(ctx: ChannelHandlerContext) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE)
        }

        @Throws(Exception::class)
        override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
            cause.printStackTrace()
            ctx.close()
        }
    }

    lateinit var ziti: ZitiContext
    lateinit var service: String
    @Before
    fun setup() {
        val path = System.getProperty("test.identity")
        Assume.assumeNotNull(path)
        try {
            ziti = Ziti.newContext(path, charArrayOf())
        } catch (ex: Exception) {
            Assume.assumeNoException(ex)
        }

        val serviceName = System.getProperty("test.service")
        Assume.assumeNotNull(serviceName)
        service = serviceName
    }

    @Test
    fun testHttpService() {
        val group = DefaultEventLoopGroup()

        val server = ServerBootstrap().group(group)
            .channelFactory(ZitiServerChannelFactory(ziti))
            .localAddress(ZitiAddress.Bind(service))
            .childHandler(object : ChannelInitializer<Channel>(){
                override fun initChannel(ch: Channel) {
                    println("new child $ch")
                    ch.pipeline().addLast(
                        HttpResponseEncoder(),
                        HttpRequestDecoder(),
                        TestServerHandler()
                    )
                }
            }).bind().sync()

        val client = ziti.open()
        client.connect(ZitiAddress.Dial(service)).get(1, TimeUnit.SECONDS)

        val reqBody = Base64.getEncoder().encodeToString(Random.nextBytes(32))

        val req = """GET /json HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: close
Host: httpbin.org
Content-Length: ${reqBody.length}
User-Agent: HTTPie/1.0.2

$reqBody
"""

        client.write(ByteBuffer.wrap(req.toByteArray())).get(1, TimeUnit.SECONDS)

        val resp = ByteBuffer.allocate(16 * 1024)

        try {
            for (i in 0 until 5) {
                val rc = client.read(resp).get(1, TimeUnit.SECONDS)
                println("read $rc bytes")
                if (rc == -1)
                    break
            }
        } catch(ex: TimeoutException) {
            // ignore
        }

        resp.flip()
        val lines = Charsets.UTF_8.decode(resp).toString().lines()
        assertEquals("HTTP/1.1 200 OK", lines.first())
        val respHeaders = lines.takeWhile { it != "" }.drop(1)
            .map { it.split(": ")}
            .map { it[0] to it[1] }.toMap()
        val serverHeader = respHeaders.get(HttpHeaderNames.SERVER.toString())
        assertEquals("TestServerHandler", serverHeader)

        val respBody = Gson().fromJson(lines.last(), Map::class.java)
        assertTrue(respBody.containsKey("headers"))

        val myHeaders = respBody["headers"] as Map<String,String>
        assertEquals("httpbin.org", myHeaders["Host"])

        assertEquals(reqBody, respBody["body"])
        group.shutdownGracefully()
    }
}