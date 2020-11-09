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
        val tls = SSLContext.getDefault().apply {
            init(null, tmf.trustManagers, SecureRandom())
        }
    }

    constructor(s: Socket, addr: InetAddress, port: Int): this(s, addr.hostName, port)
    constructor(s: Socket, host: String, port: Int): this(s, tls.createSSLEngine(host, port))

    inner class Output : OutputStream() {
        val buffer = ByteBuffer.allocate(32 * 1024)
        override fun write(b: Int) {
            buffer.put(b.toByte())
        }

        override fun write(b: ByteArray, off: Int, len: Int) {
            d{"writing $len bytes"}
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
        val sslBuffer = ByteBuffer.allocate(32 * 1024)
        val input = transport.getInputStream()

        override fun read(): Int {
            val buf = ByteArray(1)
            val read = read(buf, 0, 1)
            return if (read == 1) (buf[0].toInt() and 0xFF) else -1
        }

        override fun read(out: ByteArray, off: Int, len: Int): Int {
            val outBuffer = ByteBuffer.wrap(out, off, len)
            sslBuffer.compact()
            val b = ByteArray(sslBuffer.remaining())
            val read = input.read(b)
            d("read read=$read sslBuf=$sslBuffer")
            if (read > 0) {
                sslBuffer.put(b, 0, read)
                d("read read=$read sslBuf=$sslBuffer")
                sslBuffer.flip()

                while(sslBuffer.remaining() > 0 && outBuffer.remaining() > 0) {
                    val res = engine.unwrap(sslBuffer, outBuffer)
                    v("unwrap cons/prod=${res.bytesConsumed()}/${res.bytesProduced()} $sslBuffer $outBuffer")
                    if (res.bytesProduced() == 0)
                        break
                }
                d("sslBuf=$sslBuffer outBuffer=$outBuffer")
                return outBuffer.position()
            }
            else {
                return read
            }
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

    init {
        engine.useClientMode = true
    }
}