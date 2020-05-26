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

package org.openziti.net

import org.openziti.net.dns.ZitiDNSManager
import org.openziti.net.internal.ZitiSSLSocket
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.InetAddress
import java.net.Socket
import java.security.KeyStore
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 *
 */
class ZitiSSLSocketFactory: SSLSocketFactory(), Logged by ZitiLog() {
    val TrustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).let {
        it.init(null as KeyStore?)
        it.trustManagers[0] as X509TrustManager
    }

    val defaultFactory = SSLSocketFactory.getDefault() as SSLSocketFactory;

    override fun getDefaultCipherSuites(): Array<String> {
        return defaultFactory.defaultCipherSuites;
    }

    override fun createSocket(s: Socket?, host: String, port: Int, autoClose: Boolean): Socket {

        try {
            checkNotNull(s) { "transport socket == null" }
            val addr = ZitiDNSManager.resolve(host) ?: InetAddress.getByName(host)
            d("resolved $host => $addr")
            return ZitiSSLSocket(s, addr!!, port)
        } catch (ex: Exception) {
            e(ex.localizedMessage, ex)
            throw ex
        }
    }

    override fun createSocket(host: String?, port: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        error("not implemented")
    }

    override fun createSocket(a: InetAddress?, p: Int, la: InetAddress?, lp: Int): Socket {
        error("not implemented")
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return defaultFactory.supportedCipherSuites;
    }

}