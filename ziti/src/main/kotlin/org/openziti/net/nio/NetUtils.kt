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

package org.openziti.net.nio

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketAddress
import java.net.SocketTimeoutException
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousByteChannel
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

internal class FutureHandler<A>: CompletionHandler<A, CompletableFuture<A>> {
    override fun completed(result: A?, f: CompletableFuture<A>) { f.complete(result) }
    override fun failed(exc: Throwable, f: CompletableFuture<A>) { f.completeExceptionally(exc) }
}

internal class DeferredHandler<A>: CompletionHandler<A, CompletableDeferred<A>> {
    override fun completed(result: A, deferred: CompletableDeferred<A>) {
        deferred.complete(result)
    }

    override fun failed(exc: Throwable, deferred: CompletableDeferred<A>) {
        deferred.completeExceptionally(exc)
    }
}

internal class ContinuationHandler<A>: CompletionHandler<A, Continuation<A>> {
    override fun completed(result: A, cont: Continuation<A>) = cont.resume(result)
    override fun failed(exc: Throwable, cont: Continuation<A>) = cont.resumeWithException(exc)
}

suspend fun AsynchronousServerSocketChannel.acceptSuspend() = suspendCoroutine<AsynchronousSocketChannel> {
    this.accept(it, ContinuationHandler())
}

suspend fun AsynchronousByteChannel.readSuspend(b: ByteBuffer) = suspendCoroutine<Int> {
    this.read(b, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.readSuspend(b: ByteBuffer, timeout: Long, unit: TimeUnit?) = suspendCoroutine<Int> {
    this.read(b, timeout, unit, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.connectSuspend(addr: SocketAddress) = connectSuspend(addr, Long.MAX_VALUE)

suspend fun AsynchronousSocketChannel.connectSuspend(addr: SocketAddress, timeout: Long) {
    val ch = this

    return withContext(coroutineContext) {
        val result = CompletableDeferred<Void?>()
        ch.connect(addr, result, DeferredHandler())
        val timeoutDelay = launch {
            delay(timeout)
            if (!result.isCompleted) {
                val ex = SocketTimeoutException("failed to connect to $addr in $timeout millis")
                if (result.completeExceptionally(ex)) {
                    ch.runCatching { close() }
                }
            }
        }

        try {
            result.await()
        } finally {
            timeoutDelay.cancel()
        }
    }
}

suspend fun AsynchronousByteChannel.writeSuspend(b: ByteBuffer) = suspendCoroutine<Int> {
    this.write(b, it, ContinuationHandler())
}

suspend fun AsynchronousByteChannel.writeCompletely(b: ByteBuffer): Int {
    var res = 0
    while (b.hasRemaining()) {
        res += this.writeSuspend(b)
    }
    return res
}