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

package org.openziti.springboot

import org.apache.tomcat.util.ExceptionUtils
import org.apache.tomcat.util.net.*
import org.openziti.ZitiAddress
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.nio.channels.NetworkChannel

class ZitiEndpoint:  Nio2Endpoint() {
    init {
        acceptor = ZitiAcceptor()
    }

    inner class ZitiAcceptor: Acceptor<AsynchronousSocketChannel?>(this),
        CompletionHandler<AsynchronousSocketChannel?, Void?> {
        private var errorDelay = 0
        override fun run() {
            // The initial accept will be called in a separate utility thread
            if (!isPaused) {
                //if we have reached max connections, wait
                try {
                    countUpOrAwaitConnection()
                } catch (e: InterruptedException) {
                    // Ignore
                }
                if (!isPaused) {
                    // Note: as a special behavior, the completion handler for accept is
                    // always called in a separate thread.
                    server.accept(null, this)
                } else {
                    state = AcceptorState.PAUSED
                }
            } else {
                state = AcceptorState.PAUSED
            }
        }

        /**
         * Signals the Acceptor to stop.
         *
         * @param waitSeconds Ignored for NIO2.
         */
        override fun stop(waitSeconds: Int) {
            state = AcceptorState.ENDED
        }

        override fun completed(socket: AsynchronousSocketChannel?, attachment: Void?) {
            // Successful accept, reset the error delay
            errorDelay = 0
            // Continue processing the socket on the current thread
            // Configure the socket
            if (isRunning && !isPaused) {
                if (maxConnections == -1) {
                    server.accept(null, this)
                } else if (connectionCount < maxConnections) {
                    try {
                        // This will not block
                        countUpOrAwaitConnection()
                    } catch (e: InterruptedException) {
                        // Ignore
                    }
                    server.accept(null, this)
                } else {
                    // Accept again on a new thread since countUpOrAwaitConnection may block
                    executor.execute(this)
                }
                if (!setSocketOptions(socket)) {
                    closeSocket(socket)
                }
            } else {
                if (isRunning) {
                    state = AcceptorState.PAUSED
                }
                destroySocket(socket)
            }
        }

        override fun failed(t: Throwable, attachment: Void?) {
            if (isRunning) {
                if (!isPaused) {
                    if (maxConnections == -1) {
                        server.accept<Void>(null, this)
                    } else {
                        // Accept again on a new thread since countUpOrAwaitConnection may block
                        executor.execute(this)
                    }
                } else {
                    state = AcceptorState.PAUSED
                }
                // We didn't get a socket
                countDownConnection()
                // Introduce delay if necessary
                errorDelay = handleExceptionWithDelay(errorDelay)
                ExceptionUtils.handleThrowable(t)
                log.error(sm.getString("endpoint.accept.fail"), t)
            } else {
                // We didn't get a socket
                countDownConnection()
            }
        }
    }

    internal lateinit var server: AsynchronousServerSocketChannel
    internal lateinit var zitiAddress: ZitiAddress.Bind

    override fun getServerSocket(): NetworkChannel = server

    override fun bind() {
        server.bind(zitiAddress)
        initialiseSsl()
    }

    override fun doCloseServerSocket() {
        server.close()
    }

    override fun serverSocketAccept(): AsynchronousSocketChannel {
        log.info("starting accept")
        return server.accept().get()
    }

    override fun getAttribute(key: String?): Any {
        return super.getAttribute(key)
    }

    // need to override this to provide our own wrapper
    override fun setSocketOptions(socket: AsynchronousSocketChannel?): Boolean {
        var socketWrapper: Nio2SocketWrapper? = null
        try {
            // Allocate channel and wrapper
            var channel: Nio2Channel? = null
            if (nioChannels != null) {
                channel = nioChannels.pop()
            }
            if (channel == null) {
                val bufhandler = SocketBufferHandler(
                    socketProperties.appReadBufSize,
                    socketProperties.appWriteBufSize,
                    socketProperties.directBuffer
                )
                channel = if (isSSLEnabled) {
                    SecureNio2Channel(bufhandler, this)
                } else {
                    Nio2Channel(bufhandler)
                }
            }
            val newWrapper = ZitiSocketWrapper(channel, this)
            channel.reset(socket, newWrapper)
            connections[socket] = newWrapper
            socketWrapper = newWrapper

            // Set socket properties
            socketProperties.setProperties(socket)
            socketWrapper.readTimeout = connectionTimeout.toLong()
            socketWrapper.writeTimeout = connectionTimeout.toLong()
            socketWrapper.setKeepAliveLeft(maxKeepAliveRequests)
            // Continue processing on the same thread as the acceptor is async
            return processSocket(socketWrapper, SocketEvent.OPEN_READ, false)
        } catch (t: Throwable) {
            ExceptionUtils.handleThrowable(t)
            log.error(sm.getString("endpoint.socketOptionsError"), t)
            if (socketWrapper == null) {
                destroySocket(socket)
            }
        }
        // Tell to close the socket if needed
        return false
    }

    /**
     * Socket wrapper that sets `remoteAddr` to `caller_id`
     */
    class ZitiSocketWrapper(ch: Nio2Channel, endp: Nio2Endpoint): Nio2SocketWrapper(ch, endp) {
        override fun populateRemoteAddr() {
           socket.ioChannel?.let {
               it.runCatching { remoteAddress }.onSuccess { addr ->
                   if (addr is ZitiAddress.Session) {
                       remoteAddr = addr.callerId
                   }
               }
            }
        }
    }
}
