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

package org.openziti

import java.net.Socket
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel

/**
 * Object representing an instantiated Ziti identity.
 * It main purpose is to instantiate
 * - connections to Ziti services
 * - binding for hosting Ziti services in the app
 */
interface ZitiContext {

    /**
     * connect to Ziti service.
     */
    fun dial(serviceName: String): ZitiConnection

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
     * [AsynchronousSocketChannel.connect] method with [ZitiAddress.Service] address.
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
     * with [ZitiAddress.Service] address
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

    fun stop()
}