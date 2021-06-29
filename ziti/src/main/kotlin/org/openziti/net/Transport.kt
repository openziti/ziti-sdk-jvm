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

package org.openziti.net

import org.openziti.net.nio.AsyncTLSChannel
import org.openziti.net.nio.connectSuspend
import org.openziti.net.nio.readSuspend
import org.openziti.net.nio.writeCompletely
import java.io.Closeable
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URI
import java.nio.ByteBuffer
import javax.net.ssl.SSLContext

internal interface Transport : Closeable {

    companion object {
        suspend fun dial(address: String, ssl: SSLContext, timeout: Long): Transport {
            val url = URI.create(address)
            val tls = TLS(url.host, url.port, ssl)
            tls.connect(timeout)
            return tls
        }
    }

    fun isClosed(): Boolean
    suspend fun connect(timeout: Long)

    suspend fun write(buf: ByteBuffer)
    suspend fun read(buf: ByteBuffer, full: Boolean = true): Int

    class TLS(host: String, port: Int, sslContext: SSLContext) : Transport {
        val socket = AsyncTLSChannel(sslContext)
        val addr = InetSocketAddress(InetAddress.getByName(host), port)

        override suspend fun connect(timeout: Long) = socket.connectSuspend(addr, timeout)

        override suspend fun write(buf: ByteBuffer) {
            socket.writeCompletely(buf)
        }

        override suspend fun read(buf: ByteBuffer, full: Boolean): Int {
            var res = socket.readSuspend(buf)
            if (res == -1) return res

            while (full && buf.hasRemaining()) {
                val rc = socket.readSuspend(buf)
                if (rc == -1) break
                res += rc
            }
            return res
        }

        override fun close() = socket.close()

        override fun isClosed(): Boolean = !socket.isOpen

        override fun toString(): String = "TLS:${socket.remoteAddress}"
    }
}
