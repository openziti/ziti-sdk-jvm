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

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.*
import org.junit.Test
import java.nio.ByteBuffer

class BufferTests {
    @Test
    fun testTransfer() {
        val b1 = ByteBuffer.allocate(16)
        val b2 = ByteBuffer.allocate(8)

        for (i in 0 until b1.limit()) {
            b1.put(i, i.toByte())
        }

        var count = b1.transferTo(b2)
        assertEquals(b2.limit(), count)
        assertEquals(b1.limit() - b2.limit(), b1.remaining())

        b2.clear()
        val b3 = ByteBuffer.allocate(b1.capacity())
        count = b2.transferTo(b3)
        assertEquals(b2.limit(), count)
        assertEquals(b3.limit() - b2.limit(), b3.remaining())
        for (i in 0 until b3.limit()) {
            if (i < b2.limit())
                assertEquals(i, b3[i].toInt())
            else
                assertEquals(0, b3[i].toInt())
        }
    }

    @Test
    fun testTransferToArray() {
        val src = ByteBuffer.wrap(ByteArray(15){it.toByte()})
        val dst1 = ByteBuffer.allocate(10)
        val dst2 = ByteBuffer.allocate(10)

        val count = src.transferTo(arrayOf(dst1, dst2))
        assertEquals(src.limit().toLong(), count)
        assert(!src.hasRemaining())
        assert(!dst1.hasRemaining())
        assertEquals(5, dst2.remaining())
        assertArrayEquals(ByteArray(10){it.toByte()}, dst1.array())
        assertArrayEquals(ByteArray(10){ (if (it < 5) it + 10 else 0).toByte()}, dst2.array())

        src.flip()
        val count2 = src.transferTo(arrayOf(dst1,dst2))
        assertEquals(5L, count2)
        assertEquals(10, src.remaining())
        assert(!dst1.hasRemaining())
        assert(!dst2.hasRemaining())
    }
    @Test
    fun testBufferPool() {
        runBlocking {
            val pool = BufferPool(2, 128)

            val b1 = withTimeout(10) { pool.get() }
            assertEquals(b1.capacity(), pool.bufferSize)

            val b2 = withTimeout(10) { pool.get() }
            assertEquals(b2.capacity(), pool.bufferSize)

            try {
                withTimeout(10) { pool.get() }
                fail("exception expected")
            } catch (ex: TimeoutCancellationException) {
                // ok
            }

            pool.put(b2)
            val b3 = withTimeout(10) { pool.get() }
            assertEquals(b3.capacity(), pool.bufferSize)
        }
    }
}