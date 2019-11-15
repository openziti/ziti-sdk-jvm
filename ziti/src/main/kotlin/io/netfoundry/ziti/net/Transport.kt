/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

import io.netfoundry.ziti.net.internal.Sockets
import io.netfoundry.ziti.util.ZitiLog
import io.netfoundry.ziti.util.Logged
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URI
import java.security.cert.Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket

internal interface Transport : Closeable {

    companion object {
        fun dial(address: String, ssl: SSLContext): Transport {
            val url = URI.create(address)
            return TLS(url.host, url.port, ssl)
        }
    }

    fun getPeerCerts(): Array<out Certificate>?
    fun getInput(): InputStream
    fun getOutput(): OutputStream
    fun isClosed(): Boolean

    fun write(buf: ByteArray) {
        getOutput().apply {
            write(buf)
            flush()
        }
    }

    fun read(buf: ByteArray): Int {
        val input = getInput()
        var count = 0
        while (count < buf.size) {
            val read = input.read(buf, count, buf.size - count)
            if (read < 0)
                return read

            count += read
        }

        return count
    }

    class TLS(host: String, port: Int, sslContext: SSLContext) : Transport, Logged by ZitiLog("ziti-tls") {
        val socket: SSLSocket

        init {
            d { "connecting to $host:$port on t[${Thread.currentThread().name}" }
            val s = Sockets.BypassSocket()
            s.connect(InetSocketAddress(InetAddress.getByName(host), port))
            socket = sslContext.socketFactory.createSocket(s, host, port, true) as SSLSocket
            socket.addHandshakeCompletedListener { e -> d { "handshake completed to ${e.socket.remoteSocketAddress}" } }
            socket.startHandshake()
        }

        override fun getInput() = socket.inputStream
        override fun getOutput() = socket.outputStream
        override fun close() = socket.close()
        override fun isClosed() = socket.isClosed
        override fun getPeerCerts() = socket.session.peerCertificates
    }

}
