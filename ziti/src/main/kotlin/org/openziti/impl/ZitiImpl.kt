/*
 * Copyright (c) 2018-2023 NetFoundry Inc.
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

package org.openziti.impl

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.openziti.*
import org.openziti.api.Service
import org.openziti.identity.Enroller
import org.openziti.identity.KeyStoreIdentity
import org.openziti.identity.findIdentityAlias
import org.openziti.identity.loadKeystore
import org.openziti.net.dns.ZitiDNSManager
import org.openziti.net.internal.Sockets
import org.openziti.util.Logged
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import java.io.File
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URI
import java.security.KeyStore

internal object ZitiImpl : Logged by ZitiLog() {
    internal val contexts = mutableListOf<ZitiContextImpl>()
    internal var appId = ""
    internal var appVersion = ""

    internal val serviceEvents = MutableSharedFlow<Pair<ZitiContext, ZitiContext.ServiceEvent>>()

    internal val onAndroid: Boolean by lazy {
        try {
            Class.forName("android.util.Log")
            true
        } catch (cnf: ClassNotFoundException) {
            false
        }
    }

    init {
        i("ZitiSDK version ${Version.version} @${Version.revision}(${Version.branch})")
    }

    internal fun loadContext(ks: KeyStore, alias: String?): ZitiContextImpl {
        val idName = alias ?: findIdentityAlias(ks)
        val id = KeyStoreIdentity(ks, idName)
        return ZitiContextImpl(id, true).also { ctx ->
            contexts.add(ctx)
            ctx.launch {
                ztxEvents.emit(Ziti.IdentityEvent(Ziti.IdentityEventType.Loaded, ctx))
                ctx.serviceUpdates().collect {
                    serviceEvents.emit(Pair(ctx, it))
                }
            }
        }
    }

    internal fun loadContext(idFile: File, pwd: CharArray, alias: String?): ZitiContextImpl {
        initInternalNetworking(false)
        val ks = loadKeystore(idFile, pwd)
        return loadContext(ks, alias)
    }

    internal fun loadContext(stream: InputStream, pwd: CharArray, alias: String?): ZitiContextImpl {
        initInternalNetworking(false)
        val ks = loadKeystore(stream, pwd)
        return loadContext(ks, alias)
    }

    internal fun loadContext(id: ByteArray): ZitiContextImpl {
        val ks = loadKeystore(id)
        return loadContext(ks, null)
    }

    fun init(c: ByteArray, seamless: Boolean) {
        initInternalNetworking(seamless)

        val ctx = loadContext(c)
        ctx.checkServicesLoaded()
    }

    fun init(file: File, pwd: CharArray, seamless: Boolean) {
        initInternalNetworking(seamless)

        val ctx = loadContext(file, pwd, null)
        ctx.checkServicesLoaded()
    }

    fun removeContext(ctx: ZitiContext) {
        contexts.remove(ctx)
        if(ctx is ZitiContextImpl) {
            runBlocking { ztxEvents.emit(Ziti.IdentityEvent(Ziti.IdentityEventType.Removed, ctx)) }
            ctx.destroy()
        }
    }

    fun init(ks: KeyStore, seamless: Boolean): List<ZitiContext> {
        initInternalNetworking(seamless)

        for (a in ks.aliases()) {
            if (isZitiIdentity(ks, a)) {
                loadContext(ks, a)
            }
        }

        return contexts
    }

    private fun isZitiIdentity(ks: KeyStore, alias: String): Boolean =
        ks.isKeyEntry(alias) && runCatching { URI.create(alias).scheme == "ziti" }.getOrElse { false }

    fun isSeamless(): Boolean = Sockets.isSeamless()

    private fun initInternalNetworking(seamless: Boolean) {
        Sockets.init(seamless)
    }

    fun enroll(ks: KeyStore, jwt: ByteArray, name: String): ZitiContext {
        val enroller = Enroller.fromJWT(String(jwt))
        val alias = enroller.enroll(null, ks, name)

        return loadContext(ks, alias)
    }

    fun getServiceFor(addr: InetSocketAddress): Pair<ZitiContext, Service>? {
        return ZitiDNSManager.lookup(addr.address)?.let { getServiceFor(it, addr.port) }
    }

    fun getServiceFor(host: String, port: Int): Pair<ZitiContext, Service>? = contexts.map { c ->
            c.getServiceForAddress(host, port)?.let { Pair(c, it) }
        }.filterNotNull().firstOrNull()

    fun connect(addr: SocketAddress): ZitiConnection {
        when (addr) {
            is InetSocketAddress -> {
                val (ztx, svc) = getServiceFor(addr) ?: throw ZitiException(Errors.ServiceNotAvailable)
                return ztx.dial(svc.name)
            }
            is ZitiAddress.Dial -> {
                for (c in contexts) {
                    c.getService(addr.service)?.let {
                        return c.dial(addr)
                    }
                }

                e { "service @[$addr] not available in any contexts" }
                throw ZitiException(Errors.ServiceNotAvailable)
            }

            else -> error("unsupported address type")
        }
    }

    fun setApplicationInfo(id: String, version: String) {
        appId = id
        appVersion = version
    }

    fun serviceUpdates(): Flow<Pair<ZitiContext, ZitiContext.ServiceEvent>> = serviceEvents.asSharedFlow()

    private val ztxEvents = MutableSharedFlow<Ziti.IdentityEvent>()
    internal fun getEvents(): Flow<Ziti.IdentityEvent> = flow {
        contexts.forEach {
            emit(Ziti.IdentityEvent(Ziti.IdentityEventType.Loaded, it))
        }
        emitAll(ztxEvents)
    }

    fun findDialInfo(addr: InetSocketAddress): Pair<ZitiContext, SocketAddress>? {
        for (c in contexts) {
            val dial = c.getDialAddress(addr)
            if (dial != null) {
                return c to dial
            }
        }
        return null
    }
}
