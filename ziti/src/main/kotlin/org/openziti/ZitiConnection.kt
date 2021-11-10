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

package org.openziti

import kotlinx.coroutines.runBlocking
import java.io.Closeable

/**
 * Object representing established Ziti connection.
 */
interface ZitiConnection: Closeable {
    var timeout: Long

    suspend fun send(data: ByteArray)
    suspend fun receive(out: ByteArray, off: Int, len: Int): Int

    fun write(data: ByteArray) = runBlocking { send(data) }
    fun read(out: ByteArray, off: Int, len: Int): Int = runBlocking { receive(out, off, len) }

    fun isClosed(): Boolean
}