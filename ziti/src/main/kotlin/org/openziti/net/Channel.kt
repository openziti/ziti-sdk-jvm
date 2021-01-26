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

package org.openziti.net

import com.codahale.metrics.Meter
import com.codahale.metrics.Timer
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.openziti.Errors
import org.openziti.ZitiException
import org.openziti.api.ApiSession
import org.openziti.identity.Identity
import org.openziti.impl.ChannelImpl
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.io.Closeable
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.channels.Channel as Chan

internal fun Channel(addr: String, id: Identity, apiSession: () -> ApiSession?): Channel {
    val ch = ChannelImpl(addr, id, apiSession)
    ch.start()
    return ch
}

internal interface Channel: Closeable {

    sealed class State {
        object Initial : State() {
            override fun toString() = "Initial"
        }
        object Connecting: State() {
            override fun toString() = "Connecting"
        }
        data class Connected(val latency: Long): State()
        data class Disconnected(val err: Throwable?): State()
        object Closed: State()
    }

    interface MessageReceiver {
        suspend fun receive(msg: Result<Message>)
    }

    fun connectNow(): Deferred<State>

    val name: String
    val state: State

    fun deregisterReceiver(id: Int)
    fun registerReceiver(id: Int, rec: MessageReceiver)

    suspend fun Send(msg: Message)
    suspend fun SendSynch(msg: Message)
    suspend fun SendAndWait(msg: Message): Message

    fun getCurrentLatency(): Long


}
