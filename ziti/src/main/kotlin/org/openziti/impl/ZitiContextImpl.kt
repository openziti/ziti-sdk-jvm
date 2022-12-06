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

package org.openziti.impl

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.selects.select
import org.openziti.*
import org.openziti.api.*
import org.openziti.identity.Identity
import org.openziti.net.*
import org.openziti.net.dns.ZitiDNSManager
import org.openziti.net.nio.connectSuspend
import org.openziti.net.routing.RouteManager
import org.openziti.posture.PostureService
import org.openziti.util.IPUtil
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.Writer
import java.net.*
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates
import kotlin.random.Random
import org.openziti.api.Identity as ApiIdentity

/**
 * Object maintaining current Ziti session.
 *
 */
internal class ZitiContextImpl(internal val id: Identity, enabled: Boolean) : ZitiContext, Identity by id,
    CoroutineScope, Logged by ZitiLog() {

    private var _enabled: Boolean by Delegates.observable(enabled) { _, _, isEnabled ->
        if (isEnabled) {
            if (statusCh.value == ZitiContext.Status.Disabled)
                statusCh.value = ZitiContext.Status.Active
        } else {
            statusCh.value = ZitiContext.Status.Disabled
        }
    }

    private var refreshDelay = TimeUnit.MINUTES.toMillis(1)

    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + supervisor

    private val apiSession = MutableStateFlow<ApiSession?>(null)
    private val currentEdgeRouters = MutableStateFlow<Collection<EdgeRouter>>(emptyList())

    private val controller: Controller = Controller(URI.create(id.controller()).toURL(), sslContext(), trustManager())
    private val postureService = PostureService()

    private val statusCh: MutableStateFlow<ZitiContext.Status>
            = MutableStateFlow(if (enabled) ZitiContext.Status.Loading else ZitiContext.Status.Disabled)

    private val serviceCh: MutableSharedFlow<ZitiContext.ServiceEvent> = MutableSharedFlow()

    private val authCode = kotlinx.coroutines.channels.Channel<String>(capacity = 1, BufferOverflow.DROP_OLDEST)

    private val servicesLoaded = CompletableDeferred<Unit>()
    private val servicesByName = ConcurrentHashMap<String, Service>()
    private val servicesById = ConcurrentHashMap<String, Service>()

    private val servicesByAddr = mutableMapOf<InetAddress, MutableMap<PortRange,Service>>()

    data class SessionKey (val serviceId: String, val type: SessionType)
    private val networkSessions = ConcurrentHashMap<SessionKey, Session>()

    private val connCounter = AtomicInteger(0)

    private val connections = sortedMapOf<Int, ZitiConnection>()

    init {
        this._enabled = enabled

        launch {
            statusCh.collect {
                d { "${this@ZitiContextImpl} transitioned to $it" }
                if (it == ZitiContext.Status.Disabled) {
                    stop()
                }
            }
        }

        start()
    }

    override fun getIdentity() = apiSession.mapNotNull{ it?.identity }
    override fun getId(): ApiIdentity? = apiSession.value?.identity

    override fun getStatus() = statusCh.value
    override fun statusUpdates(): StateFlow<ZitiContext.Status> = statusCh

    override fun serviceUpdates(): Flow<ZitiContext.ServiceEvent> = flow {
        emitAll(servicesByName.values.map {
            ZitiContext.ServiceEvent(it, ZitiContext.ServiceUpdate.Available)
        }.asFlow())

        emitAll(serviceCh)
    }

    override fun getServiceTerminators(service: Service): Collection<ServiceTerminator> = runBlocking {
        controller.getServiceTerminators(service).toCollection(mutableListOf())
    }

    private fun updateStatus(s: ZitiContext.Status) {
        if (statusCh.value != ZitiContext.Status.Disabled) {
            statusCh.value = s
        }
    }

    override fun isEnabled() = _enabled
    override fun setEnabled(v: Boolean) { _enabled = v }
    private fun checkEnabled() {
        _enabled || throw ZitiException(Errors.ServiceNotAvailable)
    }

    override fun dial(serviceName: String): ZitiConnection {
        return dial(ZitiAddress.Dial(serviceName))
    }

    override fun dial(dialAddr: ZitiAddress.Dial): ZitiConnection {
        return runBlocking { dialSuspend(dialAddr) }
    }

    override suspend fun dialSuspend(dialAddr: ZitiAddress.Dial): ZitiConnection {
        checkEnabled()
        val conn = open() as ZitiSocketChannel
        conn.connectSuspend(dialAddr)
        return conn
    }

    internal fun dialById(serviceId: String): ZitiConnection =
        servicesById[serviceId]?.let {
            dial(it.name)
        } ?: throw ZitiException(Errors.ServiceNotAvailable)


    internal fun dial(host: String, port: Int): ZitiConnection {
        val service = getServiceForAddress(host, port) ?: throw ZitiException(Errors.ServiceNotAvailable)
        return dial(service.name)
    }

    internal fun dial(host: InetAddress, port: Int): ZitiConnection {
        val ports = servicesByAddr.get(host) ?: throw ZitiException(Errors.ServiceNotAvailable)

        for ((range, s) in ports) {
            if (range.contains(port)) return dial(s.name)
        }

        throw ZitiException(Errors.ServiceNotAvailable)
    }

    override fun connect(host: String, port: Int): Socket {
        checkEnabled()
        getService(host, port, 10000L) // throws timeout exception if no service loaded
        val sock = ZitiSocketFactory(this).createSocket()
        sock.connect(InetSocketAddress.createUnresolved(host, port))
        return sock
    }

    fun start(): Job = launch {
        runCatching { runner() }
            .onFailure { cancel("ziti context failed", it) }
    }

    private suspend fun runner() {
        while(true) {
            val session = runCatching { login() }.getOrElse {
                e("failed to login, cannot continue")
                updateStatus(ZitiContext.Status.NotAuthorized(it))
                throw it
            }

            apiSession.value = session
            val mfa = session.authQueries.find { it.typeId == MFAType.MFA && it.provider == "ziti" }
            if (mfa != null) {
                updateStatus(ZitiContext.Status.NeedsAuth(mfa.typeId ?: MFAType.CUSTOM, mfa.provider))
                val success = runCatching {
                    val code = authCode.receive()
                    controller.authMFA(code)
                    true
                }.getOrElse {
                    updateStatus(ZitiContext.Status.NotAuthorized(it))
                    false
                }

                if (!success) continue
            }

            updateStatus(ZitiContext.Status.Active)

            val apiSessionUpdate = maintainApiSession()
            val serviceUpdate = runServiceUpdates()

            val finisher = select<Job> {
                apiSessionUpdate.onJoin { apiSessionUpdate }
                serviceUpdate.onJoin { serviceUpdate }
            }

            finisher.invokeOnCompletion { ex ->
                w {"$finisher is completed due to $ex"}
            }

            listOf(apiSessionUpdate, serviceUpdate).forEach {
                it.cancelAndJoin()
            }

            networkSessions.clear()
        }
    }

    fun stop() {
        val copy = channels.values.toList()
        channels.clear()

        runBlocking {
            copy.forEach { ch ->
                runCatching {
                    d{"closing ${ch.name}"}
                    ch.close()
                }
            }
        }
    }

    override fun destroy() {
        d{"stopping networking"}
        stop()
        d{"stopping controller"}
        runCatching { controller.shutdown() }
        d{"shutting down"}
        runBlocking { supervisor.cancelAndJoin() }
        d{"ziti context is finished"}
    }

    internal suspend fun login(): ApiSession {
        var attempt = 0
        statusCh.value = ZitiContext.Status.Loading
        while(true) {
            if (attempt > 0) {
                val backoff = Random.nextInt(0, attempt) % 6
                val delaySeconds = (1 shl backoff) * 5
                d{ "retrying login() after ${delaySeconds}s (attempt=$attempt)"}
                delay(delaySeconds * 1000L)
            }

            try {
                return controller.login()
            } catch (ex: Exception) {
                if (ex is ZitiException && ex.code == Errors.NotAuthorized) {
                    throw ex
                }
                updateStatus(ZitiContext.Status.Unavailable(ex))
                w { "failed to login: ${ex.localizedMessage}" }
                attempt++
                continue
            }
        }
    }

    private fun maintainApiSession() = async {
        apiSession.collect {
            if (it == null) {
                cancel()
            } else {
                val refreshDelay = it.expiresAt.time - it.updatedAt.time - 10_000

                if (refreshDelay > 0) {
                    d { "waiting for refresh ${refreshDelay / 1000} seconds" }
                    delay(refreshDelay)
                }

                controller.runCatching { currentApiSession() }.onFailure { ex ->
                    w { "failed to get current session: $ex" }
                    apiSession.value = null
                    if (ex is ZitiException && ex.code == Errors.NotAuthorized) throw ex
                }.onSuccess {
                    apiSession.value = it
                }
            }
        }
    }

    private fun runServiceUpdates() = async {
        var lastUpdate = Date(0)
        while (true) {
            val oneUpdate = async {
                d("[${id.name()}] slept and restarting on t[${Thread.currentThread().name}]")
                val updt = controller.getServiceUpdates()

                if (updt.lastChangeAt.after(lastUpdate)) {
                    lastUpdate = updt.lastChangeAt

                    val services = controller.getServices().toList()
                    processServiceUpdates(services)
                    d("[${id.name()}] got ${services.size} services on t[${Thread.currentThread().name}]")
                }
            }

            controller.runCatching { getEdgeRouters() }
                .onSuccess {
                    d{"current edge routers = $it"}
                    currentEdgeRouters.value = it }
                .onFailure { w("failed to get current edge routers: $it") }

            oneUpdate.join()
            oneUpdate.invokeOnCompletion {
                when (it) {
                    is CancellationException -> { cancel(it) }
                    is ZitiException -> if (it.code == Errors.NotAuthorized) throw it
                    else -> {}
                }
            }
            d{"delaying service refresh for ${refreshDelay}ms"}
            delay(refreshDelay)
        }
    }

    fun logout() = runBlocking { controller.logout() }

    fun checkServicesLoaded() = runBlocking {
        checkEnabled()
        try {
            withTimeout(30_000) {
                servicesLoaded.await()
            }
        } catch (tex: TimeoutCancellationException) {
            e("failed to load services: $tex")
        }
    }

    internal suspend fun getNetworkSession(service: Service, st: SessionType): Session {
        checkEnabled()

        d("getNetworkSession(${service.name})")
        return networkSessions.getOrPut(SessionKey(service.id, st)) {
            val netSession = controller.createNetSession(service, st)
            t("received $netSession for service[${service.name}]")
            netSession
        }
    }

    internal suspend fun getNetworkSession(name: String, st: SessionType): Session {
        val service = servicesByName.get(name) ?: throw ZitiException(Errors.ServiceNotAvailable)
        if (!service.permissions.contains(st)) throw ZitiException(Errors.ServiceNotAvailable)

        return getNetworkSession(service, st)

    }
    private fun getDnsTarget(addr: InetSocketAddress): String? {
        if (addr.hostString != null ) {
            if (IPUtil.isValidIPv4(addr.hostString)) {
                val ip = addr.address ?: InetAddress.getByName(addr.hostString)
                return ZitiDNSManager.lookup(ip)
            } else {
                return addr.hostString
            }
        } else {
            return ZitiDNSManager.lookup(addr.address)
        }
    }

    private fun getIPtarget(addr: InetSocketAddress): InetAddress? {
        if (addr.address != null) return addr.address

        if (IPUtil.isValidIPv4(addr.hostString)) return Inet4Address.getByName(addr.hostString)
        if (IPUtil.isValidIPv6(addr.hostString)) return Inet6Address.getByName(addr.hostString)
        return null
    }

    internal fun getDialAddress(addr: InetSocketAddress, proto: Protocol = Protocol.TCP): ZitiAddress.Dial? {
        isEnabled() || return null

        val targetAddr = getDnsTarget(addr) ?: getIPtarget(addr) ?: return null

        val service = servicesById.values.firstOrNull { s ->
            s.permissions.contains(SessionType.Dial) &&
            s.interceptConfig?.let { cfg ->
                cfg.protocols.contains(proto) &&
                        cfg.portRanges.any { it.contains(addr.port) } &&
                        cfg.addresses.any { it.matches(targetAddr) }
            } ?: false
        } ?: return null

        return ZitiAddress.Dial(
            service = service.name,
            callerId = name(),
            appData = DialData(
                dstProtocol = proto,
                dstHostname = if (targetAddr is String) targetAddr else null,
                dstIp = if (targetAddr is InetAddress) targetAddr.hostAddress else null,
                dstPort = addr.port.toString()
            ))
    }

    override fun getService(addr: InetSocketAddress): Service? = getServiceForAddress(addr.hostString, addr.port)

    override fun getService(name: String): Service? {
        return servicesByName.get(name)
    }

    override fun getService(name: String, timeout: Long): Service {
        return getService(name) ?: runCatching {
            runBlocking {
                withTimeout(timeout) {
                    serviceUpdates().filter {
                        it.type == ZitiContext.ServiceUpdate.Available && it.service.name == name
                    }.first().service
                }
            }
        }.getOrElse { throw TimeoutException("failed to get service[$name] in ${timeout}ms") }
    }

    override fun getService(addr: InetSocketAddress, timeout: Long): Service = getService(addr.hostString, addr.port, timeout)

    internal fun getServiceForAddress(host: String, port: Int): Service? {
        return servicesByName.values.find { svc ->
            svc.interceptConfig?.let {
                it.addresses.any { it.matches(host) } && it.portRanges.any { r -> port in r.low..r.high }
            } ?: false
        }
    }

    internal fun getService(host: String, port: Int, timeout: Long): Service {
        return getServiceForAddress(host, port) ?: runCatching {
            runBlocking {
                withTimeout(timeout) {
                    serviceUpdates().filter {
                        it.type == ZitiContext.ServiceUpdate.Available &&
                            servicesByName.get(it.service.name)?.let { svc ->
                                svc.interceptConfig?.let {
                                    it.addresses.any { it.matches(host) } && it.portRanges.any { r -> port in r.low..r.high }
                                } ?: false
                            } ?: false
                    }.first().service
                }
            }
        }.getOrElse { throw TimeoutException("failed to get service[$host:$port] in ${timeout}ms") }
    }

    internal fun nextConnId() = connCounter.incrementAndGet()

    internal val channels = ConcurrentHashMap<String, Channel>()

    internal suspend fun getChannel(ns: Session): Channel {
        val ers = ns.edgeRouters ?: throw ZitiException(Errors.EdgeRouterUnavailable)

        val addrList = ers.map { it.supportedProtocols["tls"] }.filterNotNull()

        val chMap = sortedMapOf<Long,Channel>()
        val unconnected = mutableListOf<Channel>()

        for (addr in addrList) {
            val c = getChannel(addr)

            when(val s = c.state) {
                is Channel.State.Connected -> chMap[s.latency] = c
                else -> unconnected.add(c)
            }
        }

        if (chMap.isEmpty()) {
            val selected = connectAll(unconnected) ?: throw ZitiException(Errors.EdgeRouterUnavailable)
            d{"selected $selected"}
            return selected
        } else {
            unconnected.forEach { it.tryConnect() }
            return chMap.entries.first().value
        }
    }

    internal fun getChannel(addr: String): Channel {
        return channels.computeIfAbsent(addr) {
            Channel(it, id) { apiSession.value }
        }
    }

    internal fun connectAll(routers: Collection<EdgeRouter>) = launch {
        routers.forEach {
            it.supportedProtocols["tls"]?.let {
                getChannel(it)
            }
        }
    }

    internal suspend fun connectAll(chList: List<Channel>): Channel? {
        if (chList.isEmpty()) return null

        return select {
            for (ch in chList) {
                ch.connectAsync().onAwait { ch }
            }
        }
    }

    // can't just replace old with new as updates do not contain session token
    // do update list of edge routers for this session
    private fun updateSession(s: Session): Session? {
        val sessionKey = SessionKey(s.service.id, s.type)
        networkSessions.containsKey(sessionKey) || return null
        return networkSessions.merge(sessionKey, s) { old, new ->
            old.apply { edgeRouters = new.edgeRouters }
        }
    }

    internal fun processServiceUpdates(services: Collection<Service>) {
        val events = mutableListOf<ZitiContext.ServiceEvent>()

        val currentIds = services.map { it.id }.toCollection(mutableSetOf())
        // check removed access
        val rtMgr = RouteManager()

        val removeIds = servicesById.keys.filter { !currentIds.contains(it) }
        for (rid in removeIds) {
            servicesById.remove(rid)?.let {
                servicesByName.remove(it.name)
                it.interceptConfig?.addresses?.forEach {
                    when(it) {
                        is CIDRBlock -> rtMgr.removeRoute(it)
                        is DNSName -> ZitiDNSManager.unregisterHostname(it.name)
                        is DomainName -> ZitiDNSManager.unregisterDomain(it.name)
                    }
                }
                events.add(ZitiContext.ServiceEvent(it, ZitiContext.ServiceUpdate.Unavailable))
            }

            networkSessions.remove(SessionKey(rid, SessionType.Dial))
            networkSessions.remove(SessionKey(rid, SessionType.Bind))
        }

        // update
        for (s in services) {
            val oldV = servicesByName.put(s.name, s)
            if (oldV == null) {
                events.add(ZitiContext.ServiceEvent(s, ZitiContext.ServiceUpdate.Available))
            } else {
                if (oldV.permissions != s.permissions ||
                    oldV.config != s.config) {
                    events.add(ZitiContext.ServiceEvent(s, ZitiContext.ServiceUpdate.ConfigurationChange))
                }
            }
            servicesById.put(s.id, s)

            val addresses = s.interceptConfig?.addresses ?: emptySet()
            val oldAddresses = oldV?.interceptConfig?.addresses ?: emptySet()

            val removedAddresses = oldAddresses - addresses
            removedAddresses.forEach {
                when(it) {
                    is CIDRBlock -> rtMgr.removeRoute(it)
                    is DNSName -> ZitiDNSManager.unregisterHostname(it.name)
                    is DomainName -> ZitiDNSManager.unregisterDomain(it.name)
                }
            }

            val addedAddresses = addresses - oldAddresses
            addedAddresses.forEach {
                val route = when(it) {
                    is DNSName -> CIDRBlock(ZitiDNSManager.registerHostname(it.name), 32)
                    is CIDRBlock -> it
                    is DomainName -> {
                        ZitiDNSManager.registerDomain(it.name)
                        null
                    }
                }

                route?.let { rtMgr.addRoute(it) }
            }

            s.postureSets?.forEach {
                it.postureQueries.forEach {
                    postureService.registerServiceCheck(s.id, it)
                }
            }
        }

        runBlocking {
            postureService.getPosture().forEach {
                controller.sendPostureResp(it)
            }

            serviceCh.emitAll(events.asFlow())
        }

        servicesLoaded.complete(Unit)
    }

    override fun open(): AsynchronousSocketChannel {
        checkEnabled()
        return ZitiSocketChannel(this).also {
            connections[it.connId] = it
        }
    }

    internal fun close(conn: ZitiSocketChannel) {
        connections.remove(conn.connId)
    }

    override fun openServer(): AsynchronousServerSocketChannel {
        checkEnabled()
        return ZitiServerSocketChannel(this)
    }

    override fun toString(): String {
        val id = getId()
        return "${id?.name ?: name()}[${id?.id}]@${controller()}"
    }

    suspend internal fun waitForActive() {
        checkEnabled()
        // wait for active
        statusCh.takeWhile { it != ZitiContext.Status.Active }.collect()
    }

    override fun isMFAEnrolled(): Boolean {
        return apiSession.value?.authQueries?.isNotEmpty() ?: false
    }

    override fun authenticateMFA(code: String) {
        authCode.trySend(code)
    }

    override suspend fun enrollMFA(): MFAEnrollment {

        val mfaEnrollment = runCatching { controller.getMFAEnrollment() }.getOrThrow()
        if (mfaEnrollment != null) return mfaEnrollment

        controller.postMFA()
        return controller.getMFAEnrollment()!!
    }
    override fun enrollMFAAsync() = async { enrollMFA() }.asCompletableFuture()

    override suspend fun verifyMFA(code: String) {
        return controller.verifyMFA(code)
    }
    override fun verifyMFAAsync(code: String) = async { verifyMFA(code) }.asCompletableFuture()

    override suspend fun removeMFA(code: String) {
        controller.removeMFA(code)
    }
    override fun removeMFAAsync(code: String) = async { removeMFA(code) }.asCompletableFuture()

    override suspend fun getMFARecoveryCodes(code: String, newCodes: Boolean): Array<String> {
        return controller.getMFARecoveryCodes(code, newCodes)
    }

    override fun getMFARecoveryCodesAsync(code: String, newCodes: Boolean) =
        async { getMFARecoveryCodes(code, newCodes) }.asCompletableFuture()

    override fun dump(writer: Writer) {
        writer.appendLine("""
            id:         ${id.name()}
            controller: ${id.controller()}
            status:     ${getStatus()}
            """.trimIndent())

        writer.appendLine()
        writer.appendLine("=== Services ===")
        servicesByName.forEach { (name, s) ->
            writer.appendLine("name: $name id: ${s.id} permissions: ${s.permissions.joinToString()} intercept: ${s.interceptConfig}")
        }
        writer.flush()

        writer.appendLine()
        writer.appendLine("=== Available Edge Routers[${currentEdgeRouters.value.size}] ===")
        currentEdgeRouters.value.forEach {
            writer.appendLine(it.toString())
        }
        writer.appendLine("=== Channels[${channels.size}] ===")
        channels.forEach { (name, ch) ->
            writer.appendLine("ER: $name status: ${ch.state}")
        }
        writer.appendLine("=== Connections[${connections.size}] ===")
        connections.forEach { (id, conn) ->
            writer.appendLine("conn[$id]: $conn")
        }
    }
}