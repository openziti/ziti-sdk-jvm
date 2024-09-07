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

package org.openziti.net.internal

import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*

class ZitiSSLSocket(val transport: Socket, val engine: SSLEngine) :
    SSLSocket(),
    Logged by ZitiLog("ziti-ssl-socket")
{
    companion object {
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(null as KeyStore?)
        }
        val tls = SSLContext.getInstance("TLSv1.3").apply {
            init(null, tmf.trustManagers, SecureRandom())
        }
    }

    constructor(s: Socket, addr: InetAddress, port: Int): this(s, addr.hostName, port)
    constructor(s: Socket, host: String, port: Int): this(s, tls.createSSLEngine(host, port))

    private val sslBuffer: ByteBuffer = ByteBuffer.allocate(32 * 1024)

    init {
        engine.useClientMode = true
        engine.beginHandshake()
    }

    inner class Output : OutputStream() {
        val buffer = ByteBuffer.allocate(32 * 1024)
        override fun write(b: Int) {
            buffer.put(b.toByte())
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            v{"writing $len bytes"}
            if (len + buffer.position() > buffer.capacity()) {
                flush()
            }
            buffer.put(b, off, len)
            flush()
        }

        override fun flush() {
            val wrapped = ByteBuffer.allocate(32 * 1024)
            buffer.flip()
            if (buffer.hasRemaining()) {
                val res = engine.wrap(buffer, wrapped)
                v{"flushing ${res.bytesProduced()} bytes"}
                transport.getOutputStream()?.apply {
                    write(wrapped.array(), 0, wrapped.position())
                    flush()
                }
            }
            buffer.clear()
        }
    }

    val output = Output()
    override fun getOutputStream(): OutputStream {
        doHandshake()
        return output
    }

    inner class Input : InputStream() {
        private val plainBuffer: ByteBuffer = ByteBuffer.allocate(32 * 1024).apply {
            flip()
        }
        private val input: InputStream = transport.getInputStream()

        override fun read(): Int {
            val buf = ByteArray(1)
            val read = read(buf, 0, 1)
            return if (read == 1) (buf[0].toInt() and 0xFF) else -1
        }

        private fun copyPlainText(out: ByteArray, off: Int, len: Int): Int {
            plainBuffer.flip()
            val count = if (len > plainBuffer.remaining()) plainBuffer.remaining() else len
            plainBuffer.get(out, off, count)
            plainBuffer.compact()
            return count
        }

        override fun read(out: ByteArray, off: Int, len: Int): Int {
            val count = copyPlainText(out, off, len)
            if (count > 0) return count

            sslBuffer.flip()
            with(engine.unwrap(sslBuffer, plainBuffer)) {
                sslBuffer.compact()
                if (bytesProduced() > 0) {
                    plainBuffer.flip()
                    return copyPlainText(out, off, len)
                }
            }

            while(plainBuffer.position() == 0) {
                val b = ByteArray(sslBuffer.remaining())
                val read = input.read(b)
                if (read < 0) {
                    return -1
                }
                sslBuffer.put(b, 0, read)
                sslBuffer.flip()
                engine.unwrap(sslBuffer, plainBuffer)
                sslBuffer.compact()
            }
            return copyPlainText(out, off, len)
        }
    }

    val input = Input()
    override fun getInputStream(): InputStream {
        doHandshake()
        return input
    }

    override fun startHandshake() {
        engine.beginHandshake()
    }

    private fun doHandshake() = synchronized(this) {

        if (engine.handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            return
        }

        val empty = ByteBuffer.wrap(ByteArray(0))

        val unwrapped = ByteBuffer.allocate(32 * 1024)
        val ssl_in = ByteArray(32 * 1024)

        while (engine.handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            v("continuing handshake status=${engine.handshakeStatus}")

            when (engine.handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    val wrapped = ByteBuffer.allocate(32 * 1024)
                    val res = engine.wrap(empty, wrapped)
                    v("res = $res")

                    transport.getOutputStream().apply {
                        write(wrapped.array(), 0, res.bytesProduced())
                        flush()
                    }
                }
                SSLEngineResult.HandshakeStatus.NEED_TASK -> {
                    engine.delegatedTask?.run()
                }
                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {

                    sslBuffer.flip()
                    v("unwrapping bytes $sslBuffer")
                    with(engine.unwrap(sslBuffer, unwrapped)) {
                        v("res = $this")
                        sslBuffer.compact()

                        // if there are not enough bytes to unwrap try reading and continue main loop
                        if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                            val r = transport.getInputStream().read(ssl_in, 0, sslBuffer.remaining())
                            if (r < 0) {
                                throw SSLHandshakeException("connection closed during SSL handshake")
                            }
                            sslBuffer.put(ssl_in, 0, r)
                        }
                    }
                }
                else -> { }
            }
        }

    }

    override fun getSession(): SSLSession {
        doHandshake()
        return engine.session
    }

    override fun getSoTimeout(): Int = transport.soTimeout
    override fun setSoTimeout(timeout: Int) {
        transport.soTimeout = timeout
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
}