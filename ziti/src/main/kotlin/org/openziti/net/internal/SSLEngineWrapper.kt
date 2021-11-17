/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

import java.nio.ByteBuffer
import java.util.function.BiFunction
import javax.net.ssl.SSLEngine
import javax.net.ssl.SSLEngineResult
import javax.net.ssl.SSLSession

class SSLEngineWrapper(val delegate: SSLEngine): SSLEngine(delegate.peerHost, delegate.peerPort) {

    override fun wrap(srcs: Array<out ByteBuffer>?, offset: Int, length: Int, dst: ByteBuffer?) =
        delegate.wrap(srcs, offset, length, dst)

    override fun unwrap(src: ByteBuffer?, dsts: Array<out ByteBuffer>?, offset: Int, length: Int)
            = delegate.unwrap(src, dsts, offset, length)

    override fun getDelegatedTask(): Runnable = delegate.delegatedTask

    override fun closeInbound() = delegate.closeInbound()

    override fun isInboundDone(): Boolean = delegate.isInboundDone

    override fun closeOutbound() = delegate.closeOutbound()

    override fun isOutboundDone(): Boolean = delegate.isOutboundDone

    override fun getSupportedCipherSuites(): Array<String> = delegate.supportedCipherSuites

    override fun getEnabledCipherSuites(): Array<String> = delegate.enabledCipherSuites
    override fun setEnabledCipherSuites(ciphers: Array<out String>?)  { delegate.enabledCipherSuites = ciphers }

    override fun getSupportedProtocols(): Array<String> = delegate.supportedProtocols
    override fun getEnabledProtocols(): Array<String> = delegate.enabledProtocols
    override fun setEnabledProtocols(protocols: Array<out String>?) { delegate.enabledProtocols = protocols }

    override fun getSession(): SSLSession = delegate.session

    // fixes non-spec behavior of Conscript SSLEngine on older Android versions
    override fun beginHandshake() {
        if (delegate.handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)
            delegate.beginHandshake()
    }

    override fun getHandshakeStatus(): SSLEngineResult.HandshakeStatus = delegate.handshakeStatus

    override fun setUseClientMode(mode: Boolean) { delegate.useClientMode = mode }
    override fun getUseClientMode(): Boolean = delegate.useClientMode

    override fun setNeedClientAuth(need: Boolean) { delegate.needClientAuth = need }
    override fun getNeedClientAuth(): Boolean = delegate.needClientAuth

    override fun setWantClientAuth(want: Boolean) { delegate.wantClientAuth = want }
    override fun getWantClientAuth(): Boolean = delegate.wantClientAuth

    override fun setEnableSessionCreation(flag: Boolean) { delegate.enableSessionCreation = flag }
    override fun getEnableSessionCreation(): Boolean = delegate.enableSessionCreation

    override fun getApplicationProtocol(): String = delegate.applicationProtocol
    override fun getHandshakeApplicationProtocol(): String = delegate.handshakeApplicationProtocol
    override fun getHandshakeSession(): SSLSession = delegate.handshakeSession

    override fun setHandshakeApplicationProtocolSelector(selector: BiFunction<SSLEngine, MutableList<String>, String>?) {
        delegate.handshakeApplicationProtocolSelector = selector
    }
    override fun getHandshakeApplicationProtocolSelector(): BiFunction<SSLEngine, MutableList<String>, String> =
        delegate.handshakeApplicationProtocolSelector
}