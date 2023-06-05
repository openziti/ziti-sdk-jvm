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

import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import java.nio.ByteBuffer
import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.text.Charsets.US_ASCII
import kotlin.text.Charsets.UTF_8

class ZitiSocketChannelTest {

    lateinit var ctx: ZitiContext

    @Before
    fun setup() {
        val path = System.getProperty("test.identity")
        Assume.assumeNotNull(path)

        ctx = Ziti.newContext(path, charArrayOf())

    }

    @Test
    fun connect() {
        val ch = ctx.open() as ZitiSocketChannel
        ch.connect(ZitiAddress.Dial("httpbin")).get()
        val req = """GET /json HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate
Connection: close
Host: httpbin.org
User-Agent: HTTPie/1.0.2


"""
        val wc = ch.write(US_ASCII.encode(req)).get(5, TimeUnit.SECONDS)
        assertEquals(req.length, wc)

        println("state = ${ch.state}")

        val resp = ByteBuffer.allocate(16 * 1024)
        val rf = CompletableFuture<Int>()
        ch.read(resp, 10, TimeUnit.SECONDS, resp, object : CompletionHandler<Int, ByteBuffer>{
            override fun completed(result: Int, attachment: ByteBuffer?) {
                rf.complete(result)
            }

            override fun failed(exc: Throwable?, attachment: ByteBuffer?) {
                rf.completeExceptionally(exc)
            }
        })

        val rc = rf.get(5, TimeUnit.SECONDS)

        val eof = ch.read(resp).get(5,TimeUnit.SECONDS)
        assertEquals(-1, eof)

        assertEquals(rc, resp.position())
        resp.flip()

        val lines = UTF_8.decode(resp).toString().reader().readLines()
        assertThat(lines[0], startsWith("HTTP/1.1"))

        for (l in lines) {
            println(l)
        }

    }
}