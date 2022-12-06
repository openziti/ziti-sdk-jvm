/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
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

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.sync.Mutex
import org.openziti.net.nio.FutureHandler
import org.openziti.util.Logged
import org.openziti.util.transferTo
import java.nio.ByteBuffer
import java.nio.channels.*
import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

interface InputChannel<A : InputChannel<A>>: Logged, CoroutineScope {
    companion object {
        object SHUTDOWN: CancellationException("shutdownInput")
        object CLOSE: CancellationException("close")
    }
    val inputSupport: InputSupport

    class InputSupport(val queue: ReceiveChannel<ByteArray>) {
        val mut = Mutex()
        var leftover: ByteBuffer? = null
        var readOp: Job? = null

        fun cancelRead() {
            val op = readOp
            readOp = null
            mut.unlock()
            op?.cancel("read cancelled")
        }
    }

    fun isClosed(): Boolean
    fun isConnected(): Boolean

    fun shutdownInput(): A {
        isConnected() || throw NotYetConnectedException()
        inputSupport.leftover = null
        inputSupport.queue.cancel(SHUTDOWN)
        return this as A
    }

    fun close() {
        inputSupport.leftover = null
        inputSupport.queue.cancel(CLOSE)
    }

    fun <A : Any?> read(dst: ByteBuffer, timeout: Long, unit: TimeUnit,
                        att: A, handler: CompletionHandler<Int, in A>
    ) {
        read(arrayOf(dst), 0, 1, timeout, unit, att, object : CompletionHandler<Long, A> {
            override fun completed(result: Long, a: A) = handler.completed(result.toInt(), a)
            override fun failed(exc: Throwable?, a: A) = handler.failed(exc, a)
        })
    }

    fun read(dst: ByteBuffer): Future<Int> {
        require(!dst.isReadOnly){"Read-only buffer"}
        val result = CompletableFuture<Int>()
        result.handle { _, ex ->
            if (ex is java.util.concurrent.CancellationException) {
                inputSupport.cancelRead()
            }
        }
        read(dst, 0, TimeUnit.MILLISECONDS, result, FutureHandler())
        return result
    }

    fun <A : Any?> read(dsts: Array<out ByteBuffer>, offset: Int, length: Int,
                                 to: Long, unit: TimeUnit,
                                 att: A, handler: CompletionHandler<Long, in A>
    ) {
        !isClosed() || throw ClosedChannelException()
        isConnected() || throw NotYetConnectedException()
        inputSupport.mut.tryLock() || throw ReadPendingException()

        t{"reading"}

        val slice = dsts.sliceArray(offset until offset + length)
        var copied = 0L

        inputSupport.leftover?.let {
            copied = it.transferTo(slice)
            if (!it.hasRemaining()) { // no more leftover
                inputSupport.leftover = null
            }
        }

        // see if we can read more without blocking
        while(inputSupport.leftover == null) {
            val data = inputSupport.queue.tryReceive().getOrNull() ?: break

            val dataBuf = ByteBuffer.wrap(data)
            copied += dataBuf.transferTo(slice)
            if (dataBuf.hasRemaining()) {
                t { "saving ${dataBuf.remaining()} for later" }
                inputSupport.leftover = dataBuf
            }
        }

        if (copied > 0) {
            inputSupport.mut.unlock()
            handler.completed(copied, att)
            return
        }

        val rop = async {
            var count = 0L
            var data: ByteArray? = if (to > 0) withTimeout(unit.toMillis(to)) {inputSupport.queue.receive()} else inputSupport.queue.receive()
            while (data != null) {
                val dataBuf = ByteBuffer.wrap(data)
                count += dataBuf.transferTo(slice)
                if (dataBuf.hasRemaining()) {
                    t { "saving ${dataBuf.remaining()} for later" }
                    inputSupport.leftover = dataBuf
                    break
                }
                data = inputSupport.queue.tryReceive().getOrNull()
            }

            t { "transferred $count" }
            count
        }

        inputSupport.readOp = rop

        rop.invokeOnCompletion {
            t{ "read completed ${it}"}
            inputSupport.readOp = null
            inputSupport.mut.runCatching { unlock() }

            when (it) {
                null -> handler.completed(rop.getCompleted(), att)
                is TimeoutCancellationException -> handler.failed(InterruptedByTimeoutException(), att)
                is SHUTDOWN -> handler.completed(-1, att)
                is CLOSE -> {
                    w{"closed"}
                    handler.failed(AsynchronousCloseException(), att)
                }
                is ClosedReceiveChannelException -> handler.completed(-1, att)
                is CancellationException -> {
                    e(it){"cancellation <<< ${it.cause}"}
                    handler.failed(it.cause ?: it, att)
                }
                else -> {
                    e(it){"failed"}
                    handler.failed(it, att)
                }
            }
        }
    }
}