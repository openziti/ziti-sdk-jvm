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

package org.openziti

import kotlinx.coroutines.flow.Flow
import org.openziti.api.Service
import org.openziti.impl.ZitiImpl
import org.openziti.net.ZitiSocketFactory
import org.openziti.net.dns.DNSResolver
import org.openziti.net.dns.ZitiDNSManager
import org.openziti.net.nio.AsyncTLSSocketFactory
import java.io.File
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.security.KeyStore
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

/**
 * Main API entry point.
 */
object Ziti {

    enum class IdentityEventType {
        Loaded,
        Removed
    }

    data class IdentityEvent(val type: IdentityEventType, val ztx: ZitiContext)

    /**
     * Load Ziti identity from the file.
     * The following formats of ziti identity files are supported:
     * * JSON file produced by `ziti-enroller`
     * * JKS or PKCS#12 file produced by [enroll] method
     *
     * @param idFile file containing identity information (key, certificate, controller url)
     * @param pwd password to access the file (only needed for .jks or .pfx/.p12 if they are protected by password)
     */
    @JvmStatic
    fun newContext(idFile: File, pwd: CharArray): ZitiContext = ZitiImpl.loadContext(idFile, pwd, null)

    /**
     * Load Ziti identity from the file.
     * The following formats of ziti identity files are supported:
     * @param fname path to file
     * @param pwd password to access the file (only needed for .jks or .pfx/.p12 if they are protected by password)
     */
    @JvmStatic
    fun newContext(fname: String, pwd: CharArray): ZitiContext = newContext(File(fname), pwd)

    /**
     * Load Ziti identity from the input stream.
     * The following formats of ziti identity files are supported:
     * @param stream input stream with the ziti identity
     * @param pwd password to access the keystore (only needed for .jks or .pfx/.p12 if they are protected by password)
     */
    @JvmStatic
    fun newContext(stream: InputStream, pwd: CharArray): ZitiContext = ZitiImpl.loadContext(stream, pwd, null)

    @JvmStatic
    fun removeContext(ctx: ZitiContext) = ZitiImpl.removeContext(ctx)

    @JvmStatic
    fun init(fname: String, pwd: CharArray, seamless: Boolean) = ZitiImpl.init(File(fname), pwd, seamless)

    @JvmStatic
    fun init(ks: KeyStore, seamless: Boolean) =  ZitiImpl.init(ks, seamless)

    @JvmStatic
    fun init(identity: ByteArray, seamless: Boolean) =  ZitiImpl.init(identity, seamless)

    @JvmStatic
    fun isSeamless() = ZitiImpl.isSeamless()

    fun identityEvents(): Flow<IdentityEvent> = ZitiImpl.getEvents()

    @JvmStatic
    fun enroll(ks: KeyStore, jwt: ByteArray, name: String): ZitiContext = ZitiImpl.enroll(ks, jwt, name)

    @JvmStatic
    fun getSocketFactory(): SocketFactory = ZitiSocketFactory()

    @JvmStatic
    fun getSSLSocketFactory(): SSLSocketFactory = AsyncTLSSocketFactory()

    @JvmStatic
    fun getSSLSocketFactory(ctx: SSLContext): SSLSocketFactory = AsyncTLSSocketFactory(ctx)

    @JvmStatic
    fun getDNSResolver(): DNSResolver = ZitiDNSManager

    @JvmStatic
    fun connect(addr: SocketAddress): ZitiConnection = ZitiImpl.connect(addr)

    @JvmStatic
    fun getContexts(): Collection<ZitiContext> = ZitiImpl.contexts

    @JvmStatic
    fun setApplicationInfo(id: String, version: String) = ZitiImpl.setApplicationInfo(id, version)

    @JvmStatic
    fun getServiceFor(host: String, port: Int): Pair<ZitiContext, Service>? = ZitiImpl.getServiceFor(host, port)

    fun getServiceFor(addr: InetSocketAddress): Pair<ZitiContext, Service>? = ZitiImpl.getServiceFor(addr)

    fun findDialInfo(addr: InetSocketAddress): Pair<ZitiContext, SocketAddress>? = ZitiImpl.findDialInfo(addr)

    fun serviceUpdates(): Flow<Pair<ZitiContext, ZitiContext.ServiceEvent>> = ZitiImpl.serviceUpdates()
}
