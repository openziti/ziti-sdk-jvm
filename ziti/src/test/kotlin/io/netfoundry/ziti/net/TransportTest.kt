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

package io.netfoundry.ziti.net

import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Test
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import javax.net.ssl.SSLContext

class TransportTest {

    @Test
    fun testHTTP() {
        val t = Transport.dial("tls://httpbin.org:443", SSLContext.getDefault())

        runBlocking {
            val req = """POST /anything HTTP/1.1
Accept: */*
Connection: keep-alive
Host: httpbin.org
User-Agent: ziti/1.0.2

"""
            t.write(StandardCharsets.UTF_8.encode(req))
            val respBuf = ByteBuffer.allocate(1024)
            t.read(respBuf, false)

            respBuf.flip()
            val resp = StandardCharsets.UTF_8.decode(respBuf)
            println(resp)
            val lines = resp.toString().reader().readLines()
            assertThat(lines[0], CoreMatchers.startsWith("HTTP/1.1 200 OK"))

        }
        t.close()
    }
}