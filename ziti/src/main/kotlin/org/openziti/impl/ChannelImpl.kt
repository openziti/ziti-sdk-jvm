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

package org.openziti.impl

import com.codahale.metrics.Timer
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.openziti.api.ApiSession
import org.openziti.identity.Identity
import org.openziti.net.Channel
import org.openziti.net.Message
import org.openziti.net.Transport
import org.openziti.net.ZitiProtocol
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.math.min
import kotlin.random.Random

internal class ChannelImpl(val addr: String, val id: Identity, val apiSession: () -> ApiSession?) : Channel,
    CoroutineScope, Logged by ZitiLog("Channel[$addr]") {

    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + supervisor

    private val sequencer = AtomicInteger(1)
    private val txChan = kotlinx.coroutines.channels.Channel<Message>(16)
    private val waiters = ConcurrentHashMap<Int, CompletableDeferred<Message>>()
    private val synchers = ConcurrentHashMap<Int, CompletableDeferred<Unit>>()

    private val recMutex = Mutex()
    private val receivers = mutableMapOf<Int, Channel.MessageReceiver>()
    private val chState = MutableStateFlow<Channel.State>(Channel.State.Initial)
    private val reconnectSignal = kotlinx.coroutines.channels.Channel<Unit>()

    internal val latencyMeter: Timer = Timer()

    init {
        launch {
            chState.collect {
                d{"transitioned to $it"}
            }
        }
    }

    override val name: String
        get() = addr

    override val state: Channel.State
        get() = chState.value


    override fun registerReceiver(id: Int, rec: Channel.MessageReceiver) = runBlocking{
        recMutex.withLock {
            receivers[id] = rec
        }
    }

    override fun deregisterReceiver(id: Int): Unit = runBlocking {
        recMutex.withLock { receivers.remove(id) }
    }

    override fun tryConnect() {
        reconnectSignal.trySend(Unit).onSuccess {
            d{"forced re-connect"}
        }
    }

    override fun connectAsync(): Deferred<Channel.State> {
        tryConnect()
        return async { chState.filter { it is Channel.State.Connected }.first() }
    }

    @ExperimentalCoroutinesApi
    internal fun start() {
        launch {
            var retryCount = 0
            while (true) {
                if (retryCount > 0) {
                    val backoff = Random.nextInt(0, min(retryCount,6))
                    val backoffDelay = 3_000L * (1 shl backoff)

                    d{"delaying connect $backoffDelay ms (retry=$retryCount)"}
                    select<Unit> {
                        async { delay(backoffDelay) }.onAwait { d("reconnecting after timeout") }
                        async { reconnectSignal.receive() }.onAwait{ d("reconnecting on-demand") }
                    }
                }
                retryCount++
                val session = apiSession()
                if (session == null) {
                    d{"no session can't connect"}
                    continue
                }

                val helloMsg = Message.newHello(id.name()).apply {
                    setHeader(ZitiProtocol.Header.SessionToken, session.token)
                }

                var peer: Transport? = null
                chState.value = Channel.State.Connecting
                val jobs = mutableListOf<Deferred<Unit>>()
                try {
                    peer = Transport.dial(addr, id.sslContext(), CONNECT_TIMEOUT)
                    jobs += async { txer(peer) }
                    jobs += async { rxer(peer) }

                    val latencyStart = System.currentTimeMillis()
                    val reply = SendAndWait(helloMsg)

                    check(reply.content == ZitiProtocol.ContentType.ResultType) { "Invalid response type" }
                    val success = reply.getBoolHeader(ZitiProtocol.Header.ResultSuccess)
                    check(success) {reply.body.toString(Charsets.UTF_8)}
                    retryCount = 0 // successful connect -- reset backoff

                    val latency = System.currentTimeMillis() - latencyStart
                    chState.value = Channel.State.Connected(latency)

                    v{"starting latency check"}
                    jobs += startLatencyCheckAsync(Duration.ofMinutes(1), latencyMeter)

                    v{"channel is active"}
                    val j = select<Deferred<Unit>> {
                        jobs.onEach { it.onJoin{ it } }
                    }

                    onDisconnect(j.getCompletionExceptionOrNull())
                } catch (ex: Exception) {
                    onDisconnect(ex)
                } finally {
                    jobs.forEach { it.cancelAndJoin() }
                    peer?.runCatching { close() }
                }
            }
        }
    }

    private suspend fun onDisconnect(ex: Throwable?) {
        ex.takeIf { it !is CancellationException }?.let { err ->
            e(err){ "channel disconnected" }
        }
        chState.value = Channel.State.Disconnected(ex?.cause)

        for (v in waiters.values) v.cancel()
        waiters.clear()

        for (v in synchers.values) v.cancel()
        synchers.clear()

        recMutex.withLock {
            for((_,r) in receivers) {
                r.receive(Result.failure(CancellationException()))
            }
            receivers.clear()
        }

        var count = 0
        while(txChan.tryReceive().isSuccess) {
            count++
        }
        if (count > 0) d{"dropped $count undelivered messages"}
    }

    override fun close() {
        supervisor.cancel()
    }

    override suspend fun Send(msg: Message) {
        msg.seqNo = sequencer.getAndIncrement()
        txChan.send(msg)
    }

    /**
     * Sends the message and returns after the message has been written-n-flushed to underlying transport
     */
    override suspend fun SendSynch(msg: Message) {
        CompletableDeferred<Unit>().apply {
            msg.seqNo = sequencer.getAndIncrement()
            synchers[msg.seqNo] = this
            txChan.send(msg)
            await()
        }
    }

    override suspend fun SendAndWait(msg: Message): Message = CompletableDeferred<Message>().let {
        msg.seqNo = sequencer.getAndIncrement()
        waiters[msg.seqNo] = it
        txChan.send(msg)
        it.await()
    }

    private suspend fun txer(peer: Transport) {
        try {
            for (m in txChan) {
                v{"sending m = $m"}
                val syncher = synchers.remove(m.seqNo)
                try {
                    m.write(peer)
                    syncher?.complete(Unit)
                } catch (ex: Throwable) {
                    waiters.remove(m.seqNo)?.completeExceptionally(ex)
                    syncher?.completeExceptionally(ex)
                }
            }
        } catch (ce: CancellationException) {
            v("txer(): cancelled")
        } catch (ex: Exception) {
            w{"txer(): ${ex.localizedMessage}"}
        } finally {
            v("txer() is done")
        }
    }

    private suspend fun rxer(peer: Transport) {
        kotlin.runCatching {
            rx(peer).collect { m ->
                v{"got m = $m"}
                val waiter = waiters.remove(m.repTo)
                if (waiter != null) {
                    waiter.complete(m)
                } else {
                    val recId = m.getIntHeader(ZitiProtocol.Header.ConnId)
                    recId?.let {
                        val receiver = recMutex.withLock { receivers[it] }

                        receiver?.runCatching {
                            receive(Result.success(m))
                        }?.onFailure {
                            w("failed to dispatch: ${it.localizedMessage}")
                        } ?: d{"receiver[connId=$recId] not found for $m"}
                    }
                }
            }
        }.onFailure {
            coroutineScope { cancel("rxer() is done: ${it.localizedMessage}", it) }
        }
    }

    internal fun rx(peer: Transport): Flow<Message> = flow {
        while(true) {
            Message.readMessage(peer).onSuccess {
                emit(it)
            }.onFailure {
                when(it) {
                    is ZitiProtocol.ContentType.UnknownContent -> {
                        w { "ignoring ${it.message}"  }
                    }
                    Message.EOF -> return@flow
                    else -> throw it
                }
            }
        }
    }

    override fun getCurrentLatency() = state.let {
            if (it is Channel.State.Connected) it.latency
            else Long.MAX_VALUE
        }

    private fun startLatencyCheckAsync(interval: Duration, timer: Timer): Deferred<Unit> = async {
        try {
            while (true) {
                val start = Instant.now()

                val q = Message(ZitiProtocol.ContentType.LatencyType)
                val r = SendAndWait(q)
                check(r.content == ZitiProtocol.ContentType.ResultType) {"invalid response"}
                check(r.getBoolHeader(ZitiProtocol.Header.ResultSuccess)) {"not successful"}

                val latency = Duration.between(start, Instant.now())
                timer.update(latency.toNanos(), TimeUnit.NANOSECONDS)

                t { "latency[${this@ChannelImpl}] is now ${getCurrentLatency()}" }
                chState.value = Channel.State.Connected(latency.toMillis())

                delay(interval.toMillis())
            }
        } catch (cex: CancellationException) {
            d{"latency check cancelled"}
        } catch (ex: Exception) {
            e(ex){"latency check"}
        }
    }

    override fun toString(): String = "Channel[$addr]"

    companion object {
        const val CONNECT_TIMEOUT: Long = 20_000
    }
}
