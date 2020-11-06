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

package org.openziti.util

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class BufferPoolTest {
    @Test
    fun testAlloc() {
        runBlocking {
            val pool = BufferPool(2, 128)

            val b1 = withTimeout(10) { pool.get() }
            assertEquals(b1.capacity(), pool.bufferSize)

            val b2 = withTimeout(10) { pool.get() }
            assertEquals(b2.capacity(), pool.bufferSize)

            try {
                val b3 = withTimeout(10) { pool.get() }
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