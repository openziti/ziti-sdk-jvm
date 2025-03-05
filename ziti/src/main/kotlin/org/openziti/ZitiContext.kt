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
import kotlinx.coroutines.flow.StateFlow
import org.openziti.api.MFAEnrollment
import org.openziti.api.Service
import org.openziti.edge.model.AuthQueryType
import org.openziti.edge.model.IdentityDetail
import org.openziti.edge.model.TerminatorClientDetail
import java.io.Closeable
import java.io.Writer
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.CompletionStage
import java.util.concurrent.Future
import java.util.function.Consumer

/**
 * Object representing an instantiated Ziti identity.
 * Its main purpose is to instantiate
 * - connections to Ziti services
 * - binding for hosting Ziti services in the app
 */
interface ZitiContext {

    enum class ServiceUpdate {
        Available,
        Unavailable,
        ConfigurationChange
    }

    data class ServiceEvent(val service: Service, val type: ServiceUpdate)

    sealed class Status {
        object Loading: Status()
        class NeedsAuth(val type: AuthQueryType, val provider: String): Status()
        object Active: Status()
        object Disabled: Status()
        class NotAuthorized(val ex: Throwable): Status()
        class Unavailable(val ex: Throwable): Status()

        override fun toString(): String = this::class.java.simpleName
    }

    fun name(): String
    fun isEnabled(): Boolean
    fun setEnabled(v: Boolean)

    /**
     * register for status updates.
     *
     * The resulting future never completes, unless cancelled or the context is destroyed.
     *
     * @param consumer callback to be called on status change
     * @return future that can be cancelled to stop receiving updates
     */
    fun onStatus(consumer: Consumer<Status>): Future<Unit>
    fun getStatus(): Status
    fun statusUpdates(): StateFlow<Status>

    /**
     * register for service updates.
     *
     * The resulting future never completes, unless cancelled or the context is destroyed.
     *
     * @param consumer callback to be called on service change
     * @return future that can be cancelled to stop receiving updates
     */
    fun onServiceEvent(consumer: Consumer<ServiceEvent>): Future<Unit>
    fun serviceUpdates(): Flow<ServiceEvent>

    fun getIdentity(): Flow<IdentityDetail>
    fun getId(): IdentityDetail?

    fun getService(addr: InetSocketAddress): Service?
    fun getService(name: String): Service?
    fun getService(name: String, timeout: Long): Service
    fun getService(addr: InetSocketAddress, timeout: Long): Service
    fun getServiceTerminators(service: Service): Collection<TerminatorClientDetail>

    /**
     * connect to Ziti service.
     */
    fun dial(serviceName: String): ZitiConnection
    fun dial(dialAddr: ZitiAddress.Dial): ZitiConnection

    suspend fun dialSuspend(dialAddr: ZitiAddress.Dial): ZitiConnection

    /**
     * connect to Ziti service identified by intercept host and port.
     * @param host intercept hostname
     * @param port intercept port
     * @return connection adapted to standard [Socket]
     */
    fun connect(host: String, port: Int): Socket

    /**
     * creates unconnected [AsynchronousSocketChannel].
     *
     * before it can be used it has to be connected, via any standard
     * [AsynchronousSocketChannel.connect] method with [ZitiAddress.Dial] address.
     * ```kotlin
     *    val con = zitiCtx.open()
     *    con.connect(ZitiAddress.Service(serviceName)).get()
     * ```
     * @return unconnected connection adapted to [AsynchronousSocketChannel]
     */
    fun open(): AsynchronousSocketChannel

    /**
     * creates unbound [AsynchronousServerSocketChannel].
     *
     * before it can be used to accept ziti client connections
     * it has to be bound to Ziti service via any standard [AsynchronousServerSocketChannel.bind] method
     * with [ZitiAddress.Bind] address
     * all standard [AsynchronousServerSocketChannel.accept] methods are supported.
     *
     * Example:
     * ```kotlin
     * val server = zitiCtx.openServer()
     * server.bind(ZitiAddress.Service(serviceName))
     *
     * // start accepting
     * val clt = service.accept().get()
     *
     * ```
     * @return unbound [AsynchronousServerSocketChannel]
     */
    fun openServer(): AsynchronousServerSocketChannel

    fun destroy()

    fun isMFAEnrolled(): Boolean
    fun authenticateMFA(code: String)

    suspend fun enrollMFA(): MFAEnrollment
    fun enrollMFAAsync(): CompletionStage<MFAEnrollment>

    suspend fun verifyMFA(code: String)
    fun verifyMFAAsync(code: String): CompletionStage<Unit>

    suspend fun removeMFA(code: String)
    fun removeMFAAsync(code: String): CompletionStage<Unit>

    suspend fun getMFARecoveryCodes(code: String, newCodes: Boolean): Array<String>
    fun getMFARecoveryCodesAsync(code: String, newCodes: Boolean): CompletionStage<Array<String>>

    fun dump(writer: Writer)
}