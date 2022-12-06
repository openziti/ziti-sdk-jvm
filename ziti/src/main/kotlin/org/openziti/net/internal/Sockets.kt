/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
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

import org.openziti.impl.ZitiImpl
import org.openziti.net.ZitiSocketFactory
import org.openziti.net.nio.AsyncSocketImpl
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

internal object Sockets : Logged by ZitiLog() {

    lateinit var provider: BypassProvider

    private val initialized = AtomicBoolean(false)
    private var isSeamless = false

    var defaultSoTimeout: Int = 0

    fun init(seamless: Boolean) {
        if (initialized.compareAndSet(false, true)) {
            isSeamless = seamless
            if (!seamless)
                provider = DefaultBypassProvider()
            else {

                defaultSoTimeout = Socket().soTimeout
                d { "internals initialized" }
                Socket.setSocketImplFactory { AsyncSocketImpl(ZitiSocketFactory.ZitiConnector.Default) }

                provider = findBypassProvider()

                if (ZitiImpl.onAndroid) {
                    HTTP.init()
                }
            }
        }
    }

    private fun findBypassProvider(): BypassProvider {
        val loader = ServiceLoader.load(BypassProvider::class.java)
        return loader.firstOrNull() ?: JavaBypassProvider()
    }

    fun isSeamless() = isSeamless

    class BypassSocketFactory: SocketFactory() {

        override fun createSocket(): Socket = SocketChannelSocket()

        override fun createSocket(host: String?, port: Int): Socket
                = createSocket().apply { connect(InetSocketAddress(host, port)) }

        override fun createSocket(host: String?, port: Int, localhost: InetAddress?, localport: Int): Socket
                = createSocket().apply { connect(InetSocketAddress(host, port)) }

        override fun createSocket(addr: InetAddress?, port: Int): Socket
                = createSocket().apply { connect(InetSocketAddress(addr, port)) }

        override fun createSocket(addr: InetAddress?, port: Int, localhost: InetAddress?, localport: Int): Socket
                = createSocket().apply { connect(InetSocketAddress(addr, port)) }
    }

    fun bypassSocketFactory(): SocketFactory = provider.getSocketFactory()
    fun bypassSSLSocketFactory(ssl: SSLContext): SSLSocketFactory = provider.getSSLSocketFactory(ssl)

    internal class DefaultBypassProvider: BypassProvider {
        override fun getSocketFactory(): SocketFactory = SocketFactory.getDefault()
        override fun getSSLSocketFactory(ssl: SSLContext): SSLSocketFactory = ssl.socketFactory
    }

    // this does not work on Android because it uses conscript native SSL
    internal class JavaBypassProvider: BypassProvider {
        override fun getSocketFactory(): SocketFactory = BypassSocketFactory()
        override fun getSSLSocketFactory(ssl: SSLContext): SSLSocketFactory = ssl.socketFactory
    }
}

