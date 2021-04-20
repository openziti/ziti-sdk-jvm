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

package org.openziti.impl

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.openziti.*
import org.openziti.api.Service
import org.openziti.identity.Enroller
import org.openziti.identity.KeyStoreIdentity
import org.openziti.identity.findIdentityAlias
import org.openziti.identity.loadKeystore
import org.openziti.net.internal.Sockets
import org.openziti.util.Logged
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import java.io.File
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URI
import java.security.KeyStore

internal object ZitiImpl : Logged by ZitiLog() {
    internal val contexts = mutableListOf<ZitiContextImpl>()
    internal var appId = ""
    internal var appVersion = ""
    internal var authHandler: Ziti.AuthHandler? = null

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

    internal fun loadContext(ks: KeyStore, alias: String?, authHandler: Ziti.AuthHandler?): ZitiContextImpl {
        val idName = alias ?: findIdentityAlias(ks)
        val id = KeyStoreIdentity(ks, idName)
        return ZitiContextImpl(id, true, authHandler).also { ctx ->
            contexts.add(ctx)
            GlobalScope.launch {
                ctx.serviceUpdates().collect {
                    serviceEvents.emit(Pair(ctx, it))
                }
            }
        }
    }

    internal fun loadContext(idFile: File, pwd: CharArray, alias: String?, auth: Ziti.AuthHandler?): ZitiContextImpl {
        val ks = loadKeystore(idFile, pwd)
        return loadContext(ks, alias, auth)
    }

    fun init(file: File, pwd: CharArray, seamless: Boolean, authHandler: Ziti.AuthHandler?): Unit {
        if (seamless) {
            initInternalNetworking()
        }

        val ctx = loadContext(file, pwd, null, authHandler)
        ctx.checkServicesLoaded()
    }

    fun removeContext(ctx: ZitiContext) {
        contexts.remove(ctx)
        if(ctx is ZitiContextImpl) {
            ctx.destroy()
        }
    }

    fun init(ks: KeyStore, seamless: Boolean, authHandler: Ziti.AuthHandler?): List<ZitiContext> {
        this.authHandler = authHandler
        if (seamless) {
            initInternalNetworking()
        }

        for (a in ks.aliases()) {
            if (isZitiIdentity(ks, a)) {
                loadContext(ks, a, authHandler)
            }
        }

        return contexts
    }

    private fun isZitiIdentity(ks: KeyStore, alias: String): Boolean {
        if (!ks.isKeyEntry(alias))
            return false

        try {
            return URI.create(alias).scheme == "ziti"
        }
        catch (ex: IllegalArgumentException) {
            return false
        }
    }

    private fun initInternalNetworking() {
        Sockets.init()
    }

    fun enroll(ks: KeyStore, jwt: ByteArray, name: String): ZitiContext {
        val enroller = Enroller.fromJWT(String(jwt))
        val alias = enroller.enroll(null, ks, name)

        return loadContext(ks, alias, authHandler)
    }

    fun getServiceFor(host: String, port: Int): Pair<ZitiContext, Service>? = contexts.map { c ->
            c.getService(host, port)?.let { Pair(c, it) }
        }.filterNotNull().firstOrNull()

    fun connect(addr: SocketAddress): ZitiConnection {
        when (addr) {
            is InetSocketAddress -> {
                for (c in contexts) {
                    try {
                        return c.dial(addr.address, addr.port)
                    } catch (ex: Exception) {
                        d { "service @[$addr] not available for ${c.name()}" }
                    }
                }


                e { "service @[$addr] not available in any contexts" }
                throw ZitiException(Errors.ServiceNotAvailable)
            }
            is ZitiAddress.Dial -> {
                for (c in contexts) {
                    try {
                        return c.open().apply { connect(addr).get() } as ZitiConnection
                    } catch (ex: Exception) {
                        i { "service @[$addr] not available for ${c.name()}" }
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
}