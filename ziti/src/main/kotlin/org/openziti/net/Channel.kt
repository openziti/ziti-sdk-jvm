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

package org.openziti.net

import kotlinx.coroutines.Deferred
import org.openziti.api.ApiSession
import org.openziti.impl.ChannelImpl
import java.io.Closeable
import javax.net.ssl.SSLContext

internal fun Channel(addr: String, ssl: SSLContext, apiSession: () -> ApiSession?): Channel
        = ChannelImpl(addr, ssl, apiSession).apply { start() }

internal interface Channel: Closeable {

    sealed class State {
        data object Initial : State()
        data object Connecting: State()
        data class Connected(val latency: Long): State()
        data class Disconnected(val err: Throwable?): State()
        data object Closed: State()
    }

    interface MessageReceiver {
        suspend fun receive(msg: Result<Message>)
    }

    fun tryConnect()
    fun connectAsync(): Deferred<State>

    val name: String
    val state: State

    fun deregisterReceiver(id: Int)
    fun registerReceiver(id: Int, rec: MessageReceiver)

    suspend fun Send(msg: Message)
    suspend fun SendSynch(msg: Message)
    suspend fun SendAndWait(msg: Message): Message

    fun getCurrentLatency(): Long


}
