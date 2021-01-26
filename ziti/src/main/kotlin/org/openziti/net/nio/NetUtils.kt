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

import kotlinx.coroutines.*
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

suspend fun AsynchronousSocketChannel.readSuspend(b: ByteBuffer) = suspendCoroutine<Int> {
    this.read(b, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.readSuspend(b: ByteBuffer, timeout: Long, unit: TimeUnit?) = suspendCoroutine<Int> {
    this.read(b, timeout, unit, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.connectSuspend(addr: SocketAddress) = suspendCoroutine<Void> {
    this.connect(addr, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.connectSuspend(addr: SocketAddress, timeout: Long): Void {
    val d = GlobalScope.async(Dispatchers.IO) { connectSuspend(addr) }
    try {
        return withTimeout(timeout) { d.await() }
    } catch (tox: TimeoutCancellationException) {
        this.runCatching { close() }
        throw tox
    }
}

suspend fun AsynchronousSocketChannel.writeSuspend(b: ByteBuffer) = suspendCoroutine<Int> {
    this.write(b, it, ContinuationHandler())
}

suspend fun AsynchronousSocketChannel.writeCompletely(b: ByteBuffer): Int {
    var res = 0
    while (b.hasRemaining()) {
        res += this.writeSuspend(b)
    }
    return res
}