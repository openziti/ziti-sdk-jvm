/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.api.http

import org.openziti.net.internal.BypassProvider
import org.openziti.net.internal.Sockets
import java.net.InetAddress
import java.net.Socket
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

/**
 * Socket factory for the SDK's own controller traffic: in seamless mode it must reach the real network
 * rather than be intercepted. The bypass provider is looked up on every call because [Sockets] may not be
 * initialized yet (or may be initialized later); until then the platform default factory is used.
 */
internal class LazyBypassSocketFactory(
    private val provider: () -> BypassProvider? = Sockets::providerOrNull,
) : SocketFactory() {

    private fun delegate(): SocketFactory = provider()?.getSocketFactory() ?: SocketFactory.getDefault()

    override fun createSocket(): Socket = delegate().createSocket()

    override fun createSocket(host: String?, port: Int): Socket = delegate().createSocket(host, port)

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket =
        delegate().createSocket(host, port, localHost, localPort)

    override fun createSocket(host: InetAddress?, port: Int): Socket = delegate().createSocket(host, port)

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket =
        delegate().createSocket(address, port, localAddress, localPort)
}

/** TLS counterpart of [LazyBypassSocketFactory]: sockets come from the bypass provider, TLS from [ssl]. */
internal class LazyBypassSSLSocketFactory(
    private val ssl: SSLContext,
    private val provider: () -> BypassProvider? = Sockets::providerOrNull,
) : SSLSocketFactory() {

    private fun delegate(): SSLSocketFactory = provider()?.getSSLSocketFactory(ssl) ?: ssl.socketFactory

    override fun getDefaultCipherSuites(): Array<String> = delegate().defaultCipherSuites

    override fun getSupportedCipherSuites(): Array<String> = delegate().supportedCipherSuites

    override fun createSocket(): Socket = delegate().createSocket()

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket =
        delegate().createSocket(s, host, port, autoClose)

    override fun createSocket(host: String?, port: Int): Socket = delegate().createSocket(host, port)

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket =
        delegate().createSocket(host, port, localHost, localPort)

    override fun createSocket(host: InetAddress?, port: Int): Socket = delegate().createSocket(host, port)

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket =
        delegate().createSocket(address, port, localAddress, localPort)
}
