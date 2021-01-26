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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import java.net.InetSocketAddress
import java.nio.channels.AsynchronousSocketChannel

class NetUtilsTest {
    @Test(expected = TimeoutCancellationException::class,timeout = 2000)
    fun test_connectSuspendWithTimeout(): Unit = runBlocking {
        val sock = AsynchronousSocketChannel.open()
        sock.connectSuspend(InetSocketAddress("1.2.3.4", 4444), 1000)
    }
}