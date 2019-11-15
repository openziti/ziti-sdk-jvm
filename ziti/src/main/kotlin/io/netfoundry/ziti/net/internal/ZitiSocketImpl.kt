/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.net

import io.netfoundry.ziti.Errors
import io.netfoundry.ziti.ZitiConnection
import io.netfoundry.ziti.ZitiException
import io.netfoundry.ziti.impl.ZitiImpl
import io.netfoundry.ziti.net.internal.Sockets
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import java.io.FileDescriptor
import java.net.*

internal class ZitiSocketImpl(zc: ZitiConnection? = null): SocketImpl(), Logged by JULogged("ziti.socket.impl") {

    var connected: Boolean = false
    var closed: Boolean = false
    val fallback: Socket?
    var zitiConn: ZitiConn?

    init {
        zitiConn = zc as ZitiConn?
        if (zitiConn != null) {
            connected = true
            fallback = null
        } else {
            fallback = Sockets.BypassSocket()
        }
    }

    override fun getInputStream() = zitiConn?.getInputStream()
        ?: fallback?.getInputStream()

    override fun getOutputStream() = zitiConn?.getOutputStream()
        ?: fallback?.getOutputStream()

    override fun available() = inputStream!!.available()
    fun isConnected() = connected

    override fun close() {
        zitiConn?.close()
        fallback?.close()
    }

    override fun connect(host: String?, port: Int) = connect(InetSocketAddress(host, port), DEFAULT_TIMEOUT)

    override fun connect(address: InetAddress?, port: Int) = connect(InetSocketAddress(address, port), DEFAULT_TIMEOUT)

    override fun connect(address: SocketAddress, timeout: Int) {
        if (isConnected()) throw SocketException("already connected")
        if (closed) throw SocketException("socket closed")

        d { "connecting to $address" }
        val addr = address as InetSocketAddress
        for (ctx in ZitiImpl.contexts) {
            try {
                zitiConn = ctx.dial(addr.hostName, addr.port) as ZitiConn
                setOption(SocketOptions.SO_TIMEOUT, fallback?.soTimeout ?: timeout)
                connected = true
            } catch (zex: ZitiException) {
                when (zex.code) {
                    Errors.NotEnrolled, Errors.ServiceNotAvailable -> {
                        w("failed to connect to $address using ctx[${ctx.name()}]: ${zex.message}. Trying fallback")
                    }
                    else -> throw zex
                }
            } catch (ex: Exception) {
                e(ex) { "failed to connect" }
                // val ex = unwrapException(ex)
                throw ex
            }
        }

        if (connected) {
            return;
        }

        fallbackConnect(address, timeout);
        connected = true;
    }

    private fun fallbackConnect(address: SocketAddress?, timeout: Int) {
        fallback!!.connect(address, timeout)
        connected = true
    }

    override fun getFileDescriptor(): FileDescriptor {
        if (fd != null) {
            return fd
        }

//        val fd = Sockets.getFD.invoke(fallback) as FileDescriptor
        return fd
    }

    override fun create(stream: Boolean) {

            //Sockets.createMethod.invoke(it, stream)
    }

    override fun getOption(optID: Int): Any {
        return when (optID) {
            SocketOptions.SO_TIMEOUT -> zitiConn?.timeout ?: fallback?.soTimeout
            else -> null// fallback?.getOption(optID)
        } ?: false
    }

    override fun setOption(optID: Int, value: Any?) {
        when (optID) {
            SocketOptions.SO_TIMEOUT -> zitiConn?.apply {
                timeout = (value as Number).toLong()
            }
            else -> {
                w("unhandled option $optID is being set to $value")
            }
        }
    }

    override fun bind(host: InetAddress?, port: Int) {
        fallback?.bind(InetSocketAddress(host, port))
        // Sockets.bind.invoke(fallback, host, port)
    }

    override fun listen(backlog: Int): Unit {
        TODO("listen($backlog): server sockets are not supported")
    }

    override fun accept(s: SocketImpl?) {
        TODO("accept(): server sockets are not supported")
    }

    override fun sendUrgentData(data: Int) {
        TODO("sendUrgentData(): not implemented")
    }

    companion object {
        const val DEFAULT_TIMEOUT = 60000
    }

}