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

package io.netfoundry.ziti.impl

import io.netfoundry.ziti.*
import io.netfoundry.ziti.api.*
import io.netfoundry.ziti.identity.Identity
import io.netfoundry.ziti.net.Channel
import io.netfoundry.ziti.net.ZitiServerSocketChannel
import io.netfoundry.ziti.net.ZitiSocketChannel
import io.netfoundry.ziti.net.dns.ZitiDNSManager
import io.netfoundry.ziti.net.internal.ZitiSocket
import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.ZitiLog
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URI
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

/**
 * Object maintaining current Ziti session.
 *
 */
internal class ZitiContextImpl(internal val id: Identity, enabled: Boolean) : ZitiContext, Identity by id,
    CoroutineScope, Logged by ZitiLog() {

    enum class Status {
        Active,
        Disabled,
        NotAuthorized,
        Unavailable,
        Impared
    }

    var enabled: Boolean by Delegates.observable(false) { _, _, isEnabled ->
        if (isEnabled) {
            status = Status.Active
            val session = login()
            runServiceUpdates(session)
        } else {
            status = Status.Disabled
            stop()
        }
    }

    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + supervisor

    private val controller: Controller

    private val servicesLoaded = CompletableDeferred<Unit>()
    private val servicesByName = mutableMapOf<String, Service>()
    private val servicesById = mutableMapOf<String, Service>()
    private val servicesByAddr = mutableMapOf<InetSocketAddress, Service>()
    private val addrById = mutableMapOf<String, InetSocketAddress>()

    var status: Status by Delegates.observable(Status.Disabled) { d, old, new ->
        d { "${d.name}: [$old] => [$new]" }
    }

    data class SessionKey (val serviceId: String, val type: SessionType)
    private val networkSessions = ConcurrentHashMap<SessionKey, NetworkSession>()

    init {
        controller = Controller(URI.create(id.controller()).toURL(), sslContext(), trustManager(), sessionToken)
        this.enabled = enabled
    }

    override fun dial(serviceName: String): ZitiConnection {
        val conn = open() as ZitiSocketChannel
        conn.connect(ZitiAddress.Service(serviceName)).get()
        return conn
    }

    internal fun dial(host: String, port: Int): ZitiConnection {
        val service = getService(host, port)
        return dial(service.name)
    }

    override fun connect(host: String, port: Int): Socket = ZitiSocket(dial(host, port))

    fun initialize() {
        if (!enabled) {
            stop()
            status = Status.Disabled
            processServiceUpdates(emptyArray())
            networkSessions.clear()
        } else if (enabled) {
            status = Status.Active

            val session = login()
            runServiceUpdates(session)
        }
    }

    override fun stop() {
        controller.shutdown()
        supervisor.cancelChildren()

        val copy = channels.values

        copy.forEach { ch ->
            ch.close()
        }
    }

    fun destroy() {
    }

    internal fun login() = async {
        controller.login().also {
            d("${name()} logged in successfully s[${it.token}] at ${controller()} t[${Thread.currentThread().name}]")
            sessionToken = it.token
        }
    }

    private fun runServiceUpdates(session: Deferred<Session>) = launch {
        try {
            session.await()
            while (true) {
                d("[${id.name()}] slept and restarting on t[${Thread.currentThread().name}]")
                val services = controller.getServices()
                processServiceUpdates(services)
                d("[${id.name()}] got ${services.size} services on t[${Thread.currentThread().name}]")
                delay(5 * 60000)
            }
        } catch (ze: ZitiException) {
            w("[${name()}] failed ${ze.localizedMessage}")
            if (ze.code == Errors.NotAuthorized) {
                sessionToken = null
                status = Status.NotAuthorized
            }
        } catch (ce: CancellationException) {
            d("[${name()}] refresh stopped")
        } catch (ex: Exception) {
            e("[${name()}] failed to load: ${ex.localizedMessage}", ex)
            status = Status.Impared
        }
    }

    fun logout() = runBlocking { controller.logout() }

    fun checkServicesLoaded() = runBlocking {
        try {
            withTimeout(30_000) {
                servicesLoaded.await()
            }
        } catch (tex: TimeoutCancellationException) {
            e("failed to load services: $tex")
        }
    }

    internal fun getNetworkSession(service: Service, st: SessionType): NetworkSession = runBlocking {
        d("getNetworkSession(${service.name})")

        enabled || throw ZitiException(Errors.ServiceNotAvailable)

        checkServicesLoaded()

        networkSessions.getOrPut(SessionKey(service.id, st)) {
            val netSession = controller.createNetSession(service, st)
            d("received net session[${netSession.id}] for service[${service.name}]")
            netSession
        }
    }

    internal fun getNetworkSessionByID(servId: String, st: SessionType): NetworkSession {
        checkServicesLoaded()

        servicesById.get(servId)?.let {
            if (it.permissions.contains(st))
                return getNetworkSession(it, st)
        }

        throw ZitiException(Errors.ServiceNotAvailable)
    }

    internal fun getNetworkSession(name: String, st: SessionType): NetworkSession {
        checkServicesLoaded()

        val service = servicesByName.get(name) ?: throw ZitiException(Errors.ServiceNotAvailable)
        return getNetworkSession(service, st)

    }

    internal fun getService(host: String, port: Int): Service {
        checkServicesLoaded()

        return servicesByName.values.find {
            it.dns?.hostname == host && it.dns?.port == port
        } ?: throw ZitiException(Errors.ServiceNotAvailable)
    }

    internal val channels: MutableMap<String, Channel> = mutableMapOf()

    internal fun getChannel(ns: NetworkSession): Channel {
        val addrList = ns.edgeRouters.map { it.urls["tls"] }.filterNotNull()
        for (addr in addrList) {
            channels[addr]?.let { return it }
        }

        for (addr in addrList) {
            try {
                val ch = Channel.Dial(addr, this)
                channels[addr] = ch
                ch.onClose {
                    channels.remove(addr)
                }
                return ch

            } catch (ex: Exception) {
                w("failed to dial channel ${ex.localizedMessage}")
            }
        }

        throw ZitiException(Errors.EdgeRouterUnavailable)
    }

    internal fun processServiceUpdates(services: Array<Service>) {
        val currentIds = services.map { it.id }.toCollection(mutableSetOf())
        // check removed access

        val removeIds = servicesById.keys.filter { !currentIds.contains(it) }
        for (rid in removeIds) {
            servicesById.remove(rid)?.let {
                servicesByName.remove(it.name)
                addrById.remove(it.id)?.let { addr ->
                    servicesByAddr.remove(addr)
                }
                ZitiDNSManager.unregisterService(it)
            }

            networkSessions.remove(SessionKey(rid, SessionType.Dial))
            networkSessions.remove(SessionKey(rid, SessionType.Bind))
        }

        // update
        for (s in services) {
            servicesByName.put(s.name, s)
            servicesById.put(s.id, s)

            ZitiDNSManager.registerService(s)?.let { addr ->
                addrById.put(s.id, addr)
                servicesByAddr.put(addr, s)

                d("[${name()}] service[${s.name}] => $addr")
            }
        }

        if (!servicesLoaded.isCompleted)
            servicesLoaded.complete(Unit)
    }

    override fun open(): AsynchronousSocketChannel {
        return ZitiSocketChannel(this)
    }

    override fun openServer(): AsynchronousServerSocketChannel {
        return ZitiServerSocketChannel(this)
    }
}