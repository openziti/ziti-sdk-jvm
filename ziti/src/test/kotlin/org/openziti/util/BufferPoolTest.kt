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

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.take
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

    class PostureJsonAdapter: TypeAdapter<PostureResp>() {
        val gson = Gson()
        override fun write(out: JsonWriter, value: PostureResp) {

            out.beginObject()
            out.name("id")
            out.value(value.id)
            out.name("type")
            out.value(value.kind)


            val ad = gson.getAdapter(value.data.javaClass)
            val dO = ad.toJsonTree(value.data).asJsonObject
            dO.entrySet().forEach { e ->
                out.name(e.key)
                gson.getAdapter(e.value.javaClass).write(out, e.value)
            }

            out.endObject()
        }

        override fun read(`in`: JsonReader?): PostureResp {
            TODO("Not yet implemented")
        }
    }

    data class PostureRespOs(val name: String, val version: String)
    @JsonAdapter(PostureJsonAdapter::class)
    data class PostureResp(val id: String, val kind: String, val data: Any)
    @Test
    fun testOS() {

        runBlocking {

            val f = channelFlow {
                println("starting flow")
                for (i in 0..100) {
                    delay(100)
                    send(i)
                }
            }.conflate()
            println("taking 10")
            f.take(10).collect {
                println(it)
            }

            delay(2000)

            f.collect { println(it) }
        }
    }
}