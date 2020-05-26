/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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

package org.openziti.sample.netcat

import kotlinx.coroutines.CompletableDeferred
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler

suspend fun suspendRead(buf: ByteBuffer, clt: AsynchronousSocketChannel): Int {
    val result = CompletableDeferred<Int>()
    clt.read(buf, result, object : CompletionHandler<Int, CompletableDeferred<Int>> {
        override fun completed(result: Int, c: CompletableDeferred<Int>) {
            c.complete(result)
        }
        override fun failed(exc: Throwable, c: CompletableDeferred<Int>) {
            c.completeExceptionally(exc)
        }
    })

    return result.await()
}