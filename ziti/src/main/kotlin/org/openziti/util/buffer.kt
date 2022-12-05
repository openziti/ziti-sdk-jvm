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

package org.openziti.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onSuccess
import java.nio.ByteBuffer
import java.util.concurrent.atomic.AtomicInteger

internal val EMPTY = ByteBuffer.wrap(byteArrayOf())

/**
 * Transfer `min(this.remaining(), dst.remaining())` bytes from this buffer
 * to `dst`
 * @param dst destination buffer
 * @return number of bytes copied over
 */
internal fun ByteBuffer.transferTo(dst: ByteBuffer): Int {
    val initPosition = dst.position()
    if (this.remaining() > dst.remaining()) {
        val sub = this.slice().limit(dst.remaining())
        dst.put(sub)
        this.position(this.position() + sub.limit())
    } else {
        dst.put(this)
    }
    return dst.position() - initPosition
}

internal fun ByteBuffer.transferTo (dsts: Array<out ByteBuffer>): Long  {
    var copied = 0L
    for (d in dsts) {
        copied += this.transferTo(d)
        if (!this.hasRemaining()) break
    }
    return copied
}

internal class BufferPool(capacity: Int, val bufferSize: Int, val direct: Boolean = false) {
    private val leftToAlloc = AtomicInteger(capacity)
    private val pool: Channel<ByteBuffer>
    init {
        require(capacity > 0)
        require(bufferSize > 0)

        pool = Channel(capacity)
    }

    suspend fun get(): ByteBuffer {
        pool.tryReceive().onSuccess {
            return it
        }

        if (leftToAlloc.decrementAndGet() >= 0) {
            return if (direct) ByteBuffer.allocateDirect(bufferSize) else ByteBuffer.allocate(bufferSize)
        }

        return pool.receive()
    }

    fun put(b: ByteBuffer) {
        require(b.capacity() == bufferSize){"wrong buffer returned"}
        b.clear()
        pool.trySend(b)
    }
}