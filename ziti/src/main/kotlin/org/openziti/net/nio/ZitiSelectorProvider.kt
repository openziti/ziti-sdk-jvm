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

package org.openziti.net.nio

import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.ProtocolFamily
import java.nio.channels.DatagramChannel
import java.nio.channels.Pipe
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.nio.channels.spi.AbstractSelector
import java.nio.channels.spi.SelectorProvider

/**
 * <p>
 * Selector implementation that can be used to create NIO socket channels.
 * </p>
 * <p>
 * Datagram channels, server channels, and pipes are not supported at this time.
 * </p>
 * <p>
 * To use, set the JVM system property <code>java.nio.channels.spi.SelectorProvider</code> to
 * <code>org.openziti.net.nio.ZitiSelectorProvider</code>
 * </p>
 */
class ZitiSelectorProvider: SelectorProvider(), Logged by ZitiLog() {
    override fun openDatagramChannel(family: ProtocolFamily?): DatagramChannel {
        error("Datagram channels are not supported yet")
    }

    override fun openDatagramChannel(): DatagramChannel {
        error("Datagram channels are not supported yet")
    }

    override fun openServerSocketChannel(): ServerSocketChannel {
        error("Server socket channels are not supported")
    }

    override fun openPipe(): Pipe {
        error("Pipes are not supported yet")
    }

    override fun openSelector(): AbstractSelector {
        error("Selectors are not supported yet")
    }

    override fun openSocketChannel(): SocketChannel {
        return SyncSocketChannel(this)
    }
}
