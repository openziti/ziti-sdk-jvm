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

package org.openziti.sample.terminators

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.openziti.Ziti
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import org.openziti.api.Service
import org.openziti.net.nio.*
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel

/**
 * sample for demonstrating working with addressable service terminators.
 *
 * this app creates and manages both side -- server and client:
 *   - server binds to the given service with a terminator identity
 *   - client dials the given service with the same terminator identity
 *
 * the hosted service is established with one terminator per available edge
 * router (up to `maxTerminators`, 3 by default), all sharing the same
 * terminator identity, so the service stays reachable if a router goes away
 *
 * Prerequisites:
 *  - enrolled Ziti identity
 *  - a ziti service with both `Dial` and `Bind` permissions for the above identity
 */
object Main: CoroutineScope {

    val supervisor = SupervisorJob()
    override val coroutineContext = supervisor + Dispatchers.IO

    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val ziti = Ziti.newContext(args[0], charArrayOf())
        val serviceName = args[1]
        val service = ziti.serviceUpdates()
            .filter { it.service.name == serviceName }.map {it.service}.first()
        val termId = "this-is-my-terminator"

        val listener = ziti.openServer()
        listener.bind(ZitiAddress.Bind(service = service.name, identity = termId))

        runListener(listener)

        val client = runClient(ziti, service, termId)

        client.join()

        ziti.destroy()
        supervisor.cancelAndJoin()
    }

    fun runClient(ztx: ZitiContext, service: Service, terminator: String) = launch {

        // the controller may report the terminator identity as-is or with the hosting
        // router appended (`<identity>@<router>`): one terminator per router the server bound to
        fun ours() = ztx.getServiceTerminators(service).filter {
            it.identity == terminator || it.identity?.startsWith("$terminator@") == true
        }

        // the server establishes its terminators in the background: wait for the first
        // one to show up, more may follow as the server binds to the other edge routers
        val terminators = withTimeoutOrNull(30_000) {
            var terms = ours()
            while (terms.isEmpty()) {
                delay(1_000)
                terms = ours()
            }
            terms
        } ?: emptyList()

        require(terminators.isNotEmpty()) {
            "can't find terminator identity[$terminator] for service[${service.name}]"
        }
        println("service[${service.name}] terminators for identity[$terminator]: ${terminators.map { it.identity }}")

        val conn = ztx.open()
        conn.connectSuspend(ZitiAddress.Dial(service.name, terminator))

        val msgs = arrayOf("Hello Ziti!", "Goodbye")

        val readBuf = ByteBuffer.allocate(128)
        for (m in msgs) {
            println("-> $m")
            conn.writeCompletely(Charsets.UTF_8.encode(m))

            readBuf.clear()
            if (conn.readSuspend(readBuf) > 0) {
                readBuf.flip()
                val resp = Charsets.UTF_8.decode(readBuf)
                println("<- $resp")
            }
        }
    }

    fun runListener(listener: AsynchronousServerSocketChannel) = launch {
        val child = listener.acceptSuspend()
        runServer(child)
    }

    fun runServer(ch: AsynchronousSocketChannel) = launch {
        val buf = ByteBuffer.allocate(1024)

        do {
            buf.clear()
            val read = ch.readSuspend(buf)
            buf.flip()
            val resp = "${Charsets.UTF_8.decode(buf)}, eh"
            ch.writeSuspend(Charsets.UTF_8.encode(resp))
        } while(read > 0)
    }
}