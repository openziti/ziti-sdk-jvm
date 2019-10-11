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
import java.net.Socket
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*
import kotlin.math.min

class ZitiSSLSocket(val transport: Socket, val host: String?, val pport: Int) :
    SSLSocket(host, pport), Logged by JULogged() {

    val engine: SSLEngine

    init {
        val ssl = SSLContext.getInstance("TLSv1.2")
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(null as KeyStore?)
        ssl.init(null, tmf.trustManagers, SecureRandom())

        engine = ssl.createSSLEngine(host, pport)
        engine.useClientMode = true
    }

    private val output = Output()
    override fun getOutputStream(): OutputStream = output

    private val inbound = transport.getInputStream()
    private val unwrapped = ByteBuffer.allocate(engine.session.applicationBufferSize)
    private val input = Input()
    override fun getInputStream(): InputStream = input

    override fun startHandshake() {
        doHandshake()
    }

    override fun getSession(): SSLSession {
        doHandshake()
        return engine.session
    }

    private fun doHandshake() {
        if (engine.session.isValid)
            return

        engine.beginHandshake();

        val empty = ByteBuffer.wrap(ByteArray(0))
        val output = transport.getOutputStream()
        val input = transport.getInputStream()

        val ssl_in = ByteArray(engine.session.packetBufferSize)
        var read = 0
        var processed = 0

        v { "starting handshake status=${engine.handshakeStatus}" }
        while (engine.handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
            v { "continuing handshake status=${engine.handshakeStatus}" }

            when (engine.handshakeStatus) {
                SSLEngineResult.HandshakeStatus.NEED_WRAP -> {
                    val wrapped = ByteBuffer.allocate(32 * 1024)
                    val res = engine.wrap(empty, wrapped)
                    v("wrapped $res")
                    if (res.bytesProduced() > 0) {
                        output.apply {
                            write(wrapped.array(), 0, res.bytesProduced())
                            flush()
                        }
                    }
                }

                SSLEngineResult.HandshakeStatus.NEED_TASK -> engine.delegatedTask?.run()

                SSLEngineResult.HandshakeStatus.NEED_UNWRAP -> {
                    if (input.available() > 0 || processed == read)
                        read += input.read(ssl_in, read, ssl_in.size - read)

                    val res = engine.unwrap(ByteBuffer.wrap(ssl_in, processed, read - processed), unwrapped)
                    check(res.status == SSLEngineResult.Status.OK) { "invalid status ${res}" }
                    processed += res.bytesConsumed()
                }

                SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING -> {
                }

                else -> error("unexpected SSL handshake status")
            }
        }

        unwrapped.flip()
        v { "handshake complete: ${engine.handshakeStatus} ${unwrapped.position()}" }
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeHandshakeCompletedListener(listener: HandshakeCompletedListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setEnableSessionCreation(flag: Boolean) {
        engine.enableSessionCreation = flag
    }

    inner class Input : InputStream() {
        override fun read(): Int {
            return transport.getInputStream().read()
        }

        override fun read(out: ByteArray, off: Int, len: Int): Int {
            if (unwrapped.remaining() > 0) {
                val l = min(len, unwrapped.remaining())
                unwrapped.get(out, off, l)
                return l
            }

            val b = ByteArray(engine.session.packetBufferSize)
            val read = inbound.read(b)
            if (read > 0) {
                unwrapped.compact()
                val res = engine.unwrap(ByteBuffer.wrap(b, 0, read), unwrapped)
                check(res.status == SSLEngineResult.Status.OK) { "invalid status" }
                unwrapped.flip()
                val l = min(len, unwrapped.remaining())
                unwrapped.get(out, off, l)
                return l
            } else {
                return read
            }
        }
    }

    inner class Output : OutputStream() {
        val buffer = ByteBuffer.allocate(32 * 1024)
        override fun write(b: Int) {
            buffer.put(b.toByte())
        }

        override fun write(b: ByteArray?, off: Int, len: Int) {
            d { "writing $len bytes (${String(b!!, off, len)})" }
            if (len + buffer.position() > buffer.capacity()) {
                flush()
            }
            buffer.put(b, off, len)
        }

        override fun flush() {
            val wrapped = ByteBuffer.allocate(32 * 1024)
            buffer.flip()
            val res = engine.wrap(buffer, wrapped)
            check(res.status == SSLEngineResult.Status.OK)
            transport.getOutputStream().apply {
                write(wrapped.array(), 0, wrapped.position())
                flush()
            }
            buffer.clear()
        }
    }
}