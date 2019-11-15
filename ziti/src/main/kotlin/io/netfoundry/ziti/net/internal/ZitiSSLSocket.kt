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

package io.netfoundry.ziti.net.internal

import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*

class ZitiSSLSocket(val transport: Socket, val host: InetAddress, val pport: Int) :
    SSLSocket(host, pport),
    Logged by JULogged("ziti-ssl-socket")
{

    inner class Output : OutputStream() {
        val buffer = ByteBuffer.allocate(32 * 1024)
        override fun write(b: Int) {
            buffer.put(b.toByte())
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            d{"writing $len bytes (${String(b, off, len)})"}
            if (len + buffer.position() > buffer.capacity()) {
                flush()
            }
            buffer.put(b, off, len)
        }

        override fun flush() {
            val wrapped = ByteBuffer.allocate(32 * 1024)
            buffer.flip()
            if (buffer.hasRemaining()) {
                val res = engine.wrap(buffer, wrapped)
                d{"flushing ${res.bytesProduced()} bytes"}
                transport.getOutputStream()?.apply {
                    write(wrapped.array(), 0, wrapped.position())
                    flush()
                }
            }
            buffer.clear()
        }
    }

    val output = Output()
    override fun getOutputStream(): OutputStream = output

    inner class Input : InputStream() {
        override fun read(): Int {
            return transport.getInputStream().read()
        }

        override fun read(out: ByteArray?, off: Int, len: Int): Int {
            transport.getInputStream()?.let {
                val buffer = ByteBuffer.allocate(32 * 1024)
                val b = ByteArray(32 * 1024)
                val read = it.read(b)
                if (read > 0) {
                    buffer.put(b, 0, read)
                    buffer.flip()

                    val res = engine.unwrap(buffer, ByteBuffer.wrap(out, off, len))
                    d{"read read=$read consumed=${res.bytesConsumed()} produced=${res.bytesProduced()} (${String(out!!, off, res.bytesProduced())})"}
                    return res.bytesProduced()
                }
                else {
                    return read
                }
            }

            return -1
        }
    }

    val input = Input()
    override fun getInputStream(): InputStream = input

    override fun startHandshake() {
        engine.beginHandshake()
    }


    override fun getSession(): SSLSession {

        val empty = ByteBuffer.wrap(ByteArray(0))

        val unwrapped = ByteBuffer.allocate(32 * 1024)
        val ssl_in = ByteArray(32 * 1024)
        var read = 0
        var processed = 0


        while (engine.handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            d("continuing handshake status=${engine.handshakeStatus}")

            when (engine.handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    val wrapped = ByteBuffer.allocate(32 * 1024)
                    val res = engine.wrap(empty, wrapped)
                    d("res = $res")
                    transport.getOutputStream().apply {
                        write(wrapped.array(), 0, res.bytesProduced())
                        flush()
                    }
                }
                SSLEngineResult.HandshakeStatus.NEED_TASK -> {
                    engine.delegatedTask?.run()
                }
                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {
                    var res: SSLEngineResult

                    v("unwrapping bytes $processed..$read")
                    res = engine.unwrap(ByteBuffer.wrap(ssl_in, processed, read - processed), unwrapped)
                    v("res = $res")
                    processed += res.bytesConsumed()

                    // if there are not enough bytes to unwrap try reading and continue main loop
                    if (res.status == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                        val r = transport.getInputStream().read(ssl_in, read, ssl_in.size - read)
                        if (r > 0) {
                            read += r
                        }

                        v("read $r/$read")
                    }
                }

                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING -> return engine.session

                else -> { }
            }
        }
        return engine.session
    }

    override fun setUseClientMode(mode: Boolean) {
        engine.useClientMode = mode
    }

    override fun setEnabledProtocols(protocols: Array<out String>?) {
        engine.enabledProtocols = protocols
    }

    override fun setEnabledCipherSuites(suites: Array<out String>?) {
        engine.enabledCipherSuites = suites
    }

    override fun getUseClientMode(): Boolean = engine.useClientMode

    override fun getEnableSessionCreation(): Boolean = engine.enableSessionCreation

    override fun getEnabledProtocols(): Array<String> = engine.enabledProtocols

    override fun getWantClientAuth(): Boolean = engine.wantClientAuth

    override fun getNeedClientAuth(): Boolean = engine.needClientAuth

    override fun getEnabledCipherSuites(): Array<String> = engine.enabledCipherSuites

    override fun setNeedClientAuth(need: Boolean) {
        engine.needClientAuth = need
    }

    override fun getSupportedCipherSuites(): Array<String> = engine.supportedCipherSuites

    override fun setWantClientAuth(want: Boolean) {
        engine.wantClientAuth = want
    }

    override fun getSupportedProtocols(): Array<String> = engine.supportedProtocols

    override fun addHandshakeCompletedListener(listener: HandshakeCompletedListener?) {
        error("not implemented")
    }

    override fun removeHandshakeCompletedListener(listener: HandshakeCompletedListener?) {
        error("not implemented")
    }

    override fun setEnableSessionCreation(flag: Boolean) {
        engine.enableSessionCreation = flag
    }

    val engine: SSLEngine

    init {
        val ssl = SSLContext.getInstance("TLSv1.2")
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(null as KeyStore?)
        ssl.init(null, tmf.trustManagers, SecureRandom())

        engine = ssl.createSSLEngine(host.hostName, pport)
        engine.useClientMode = true
    }
}