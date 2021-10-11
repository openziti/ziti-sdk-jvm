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

package org.openziti.net.nio

import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.SocketOptions.SO_TIMEOUT
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.CompletableFuture
import javax.net.ssl.*

/**
 * [SSLSocket] adapter of [AsyncTLSChannel]
 */
class AsyncTLSChannelSocket(transport: AsynchronousSocketChannel, host: String, port: Int, sslContext: SSLContext): SSLSocket() {

    internal val asyncTls: AsyncTLSChannel = AsyncTLSChannel(transport, sslContext)
    internal val impl: AsyncSocketImpl = AsyncSocketImpl(asyncTls)
    internal val listeners = mutableMapOf<HandshakeCompletedListener, CompletableFuture<Unit>>()

    constructor(addr: InetSocketAddress, localAddr: InetSocketAddress?, ssl: SSLContext):
            this(AsynchronousSocketChannel.open(), addr.hostString, addr.port, ssl) {
        localAddr?.let {
            asyncTls.bind(it)
        }

        runBlocking { asyncTls.connectSuspend(addr) }
    }

    override fun startHandshake() = asyncTls.startHandshake()
    override fun getSession(): SSLSession = asyncTls.getSession()

    override fun addHandshakeCompletedListener(l: HandshakeCompletedListener) {
        val f = asyncTls.handshake.asCompletableFuture().thenApplyAsync {
            l.handshakeCompleted(HandshakeCompletedEvent(this, it))
        }
        listeners.put(l, f)
    }

    override fun removeHandshakeCompletedListener(l: HandshakeCompletedListener) {
        listeners.remove(l)?.cancel(false)
    }

    override fun setSSLParameters(params: SSLParameters) = asyncTls.setSSLParameters(params)

    override fun getEnabledCipherSuites(): Array<String>? = asyncTls.getEnabledCipherSuites()
    override fun getSupportedCipherSuites(): Array<String>? = asyncTls.getSupportedCipherSuites()
    override fun setEnabledCipherSuites(p: Array<String>) = asyncTls.setEnabledCipherSuites(p)

    override fun getSupportedProtocols(): Array<String>? = asyncTls.getSupportedProtocols()
    override fun setEnabledProtocols(p: Array<String>) = asyncTls.setEnabledProtocols(p)
    override fun getEnabledProtocols(): Array<String>? = asyncTls.getEnabledProtocols()
    override fun getApplicationProtocol(): String? = asyncTls.getApplicationProtocol()

    override fun getSoTimeout() = impl.getOption(SO_TIMEOUT) as Int
    override fun setSoTimeout(timeout: Int) = impl.setOption(SO_TIMEOUT, timeout)

    override fun getInputStream(): InputStream = impl.inputStream
    override fun getOutputStream(): OutputStream = impl.outputStream
    override fun close() {
        impl.channel.close()
    }

    override fun setEnableSessionCreation(enable: Boolean) {
        asyncTls.engine.enableSessionCreation = enable
    }

    override fun getEnableSessionCreation() = asyncTls.engine.enableSessionCreation
    // override fun getApplicationProtocol() = null

    override fun getUseClientMode(): Boolean = true
    override fun setUseClientMode(mode: Boolean) {
        require(mode){"only client mode is supported"}
    }

    /////////// Server Socket methods //////////////
    override fun getNeedClientAuth(): Boolean = false
    override fun setNeedClientAuth(need: Boolean): Unit = error("Not yet implemented")
    override fun getWantClientAuth(): Boolean = false
    override fun setWantClientAuth(want: Boolean) = require(!want){ "not supported" }

}