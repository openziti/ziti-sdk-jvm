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
import io.netfoundry.ziti.ZitiException
import io.netfoundry.ziti.api.NetworkSession
import io.netfoundry.ziti.impl.ZitiContextImpl
import io.netfoundry.ziti.impl.ZitiImpl
import io.netfoundry.ziti.net.internal.Sockets
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import java.io.FileDescriptor
import java.io.InputStream
import java.io.OutputStream
import java.net.*

/**
 *
 * @author eugene
 * @since 9/20/18
 */
internal class ZitiSocketImpl : SocketImpl(), Logged by JULogged(TAG) {

    var connected: Boolean = false
    var zitiConn: ZitiConn? = null
    val fallback = Sockets.defaultImplCons.newInstance()

    fun connect(ns: NetworkSession, ctx: ZitiContextImpl) {
        zitiConn = ctx.dial(ns) as ZitiConn
        connected = true
    }

    override fun getInputStream(): InputStream = zitiConn?.getInputStream()
        ?: Sockets.inStream.invoke(fallback) as InputStream

    override fun getOutputStream() = zitiConn?.getOutputStream()
        ?: Sockets.outStream.invoke(fallback) as OutputStream

    override fun available() = inputStream.available()
    fun isConnected() = connected

    override fun close() {
        zitiConn?.close()
        Sockets.close.invoke(fallback)
    }

    override fun connect(host: String?, port: Int) = connect(InetSocketAddress(host, port), DEFAULT_TIMEOUT)

    override fun connect(address: InetAddress?, port: Int) = connect(InetSocketAddress(address, port), DEFAULT_TIMEOUT)

    override fun connect(address: SocketAddress, timeout: Int) {
        i { "connecting to $address" }
        val addr = address as InetSocketAddress
        try {
            val ctx = ZitiImpl.contexts.first()
            zitiConn = ctx.dial(addr.hostName, addr.port) as ZitiConn
            connected = true
        } catch (realex: Exception) {
            e(realex) { "failed to connect" }
            // val realex = unwrapException(ex)
            if (realex is ZitiException) {
                when (realex.code) {
                    Errors.NotEnrolled, Errors.ServiceNotAvailable -> {
                        d("failed to connect to $address: ${realex.message}. Trying fallback")
                        fallbackConnect(address, timeout)
                    }
                    else -> throw realex
                }
            } else {
                throw realex
            }
        }

    }

    internal fun fallbackConnect(address: SocketAddress?, timeout: Int) {
        Sockets.connect1(fallback, address, timeout)
        fd = Sockets.getFD(fallback) as FileDescriptor
        connected = true
    }

    override fun getFileDescriptor(): FileDescriptor {
        if (fd != null) {
            return fd
        }
        val fd = Sockets.getFD.invoke(fallback) as FileDescriptor
        return fd
    }

    override fun create(stream: Boolean) {
        Sockets.createMethod.invoke(fallback, stream)
    }

    override fun getOption(optID: Int): Any {
        return when (optID) {
            SocketOptions.SO_TIMEOUT -> /*zitiConn?.timeout ?:*/ fallback.getOption(optID)
            else -> fallback.getOption(optID)
        }
    }

    override fun setOption(optID: Int, value: Any?) {
        when (optID) {
            SocketOptions.SO_TIMEOUT -> zitiConn?.apply {
                //timeout = (value as Number).toLong()
            }
            else -> {
                w("unhandled option $optID is being set to $value")
            }
        }
    }

    override fun bind(host: InetAddress?, port: Int) {
        Sockets.bind.invoke(fallback, host, port)
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
        val TAG = "ziti-socket-impl"
        val DEFAULT_TIMEOUT = 5000
    }

}