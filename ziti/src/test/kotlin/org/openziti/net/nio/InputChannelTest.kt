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

package org.openziti.net.nio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.openziti.net.InputChannel
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.nio.ByteBuffer
import java.nio.channels.*
import java.security.MessageDigest
import java.util.concurrent.*
import kotlin.random.Random
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class InputChannelTest {
    class TestInputChannel(var connected: Boolean = true) : InputChannel<TestInputChannel>, Logged by ZitiLog(),
        CoroutineScope by CoroutineScope(Dispatchers.IO) {
        val inbound = Channel<ByteArray>(16)
        override val inputSupport = InputChannel.InputSupport(inbound)
        override fun isClosed(): Boolean = closed
        override fun isConnected(): Boolean = connected

        override fun close() {
            closed = true
            super.close()
        }

        var closed: Boolean = false
    }

    @JvmField
    @Rule
    val timeout = org.junit.rules.Timeout(5, TimeUnit.SECONDS)

    @Test(expected = NotYetConnectedException::class)
    fun testReadNotConnected() {
        val ch = TestInputChannel(false)
        ch.read(ByteBuffer.allocate(128))
    }

    @Test(expected = ClosedChannelException::class)
    fun testReadClosed() {
        val ch = TestInputChannel()
        ch.closed = true
        ch.read(ByteBuffer.allocate(1024))
    }

    @Test
    fun testShutdownInput() {
        val ch = TestInputChannel()
        val read1 = ch.read(ByteBuffer.allocate(128))

        assert(ch.shutdownInput() == ch)
        assert(read1.get() == -1)

        assert(ch.read(ByteBuffer.allocate(128)).get() == -1)
    }

    @Test
    fun testClose() {
        val ch = TestInputChannel()
        val read1 = ch.read(ByteBuffer.allocate(128))

        ch.close()
        val ex = assertThrows<ExecutionException> {read1.get()}
        assertIs<AsynchronousCloseException>(ex.cause)
        assertThrows<ClosedChannelException> { ch.read(ByteBuffer.allocate(128)) }
    }

    @Test(expected = ReadPendingException::class)
    fun testReadPending() {
        val ch = TestInputChannel()

        assertDoesNotThrow {
            ch.read(ByteBuffer.allocate(1024))
        }
        ch.read(ByteBuffer.allocate(1024))
    }

    @Test
    fun testReadTimeout() {
        val ch = TestInputChannel()

        val f = CompletableFuture<Boolean>()

        ch.read(ByteBuffer.allocate(1024), 1, TimeUnit.SECONDS, this, object :
            CompletionHandler<Int, InputChannelTest> {
            override fun completed(result: Int?, attachment: InputChannelTest) {
                f.completeExceptionally(IllegalStateException("should not complete"))
            }

            override fun failed(exc: Throwable?, attachment: InputChannelTest) {
                f.completeAsync {
                    assertIs<InterruptedByTimeoutException>(exc, "should get timeout")
                    true
                }
            }
        })

        f.get()
    }

    @Test
    fun testPartialReads() {
        val ch = TestInputChannel()
        val digest = MessageDigest.getInstance("MD5")
        var expectedByteCount = 0
        for (i in 0 until 10) {
            val b = Random.nextBytes(20)
            digest.update(b)
            expectedByteCount += b.size
            ch.inbound.trySend(b).getOrThrow()
        }
        ch.inbound.close() // mark EOF
        val expectedChecksum = digest.digest()

        digest.reset()
        val b = ByteBuffer.allocate(15)
        var readCount = 0
        var byteCount = 0
        while(true) {
            val f = ch.read(b)
            f.get(10, TimeUnit.MILLISECONDS) != -1 || break

            byteCount += f.get()
            readCount++
            digest.update(b.flip())
            b.clear()
        }

        assertEquals(expectedByteCount, byteCount)
        assertContentEquals(expectedChecksum, digest.digest())
    }

    @Test
    fun testSmallReads() {
        val ch = TestInputChannel()
        val payload = Random.nextBytes(128)
        ch.inbound.trySend(payload).getOrThrow()
        ch.inbound.close() // EOF

        val readBuf = ByteBuffer.allocate(payload.size)

        val b = ByteBuffer.allocate(10)
        while(true) {
            val read = ch.read(b).get()
            if (read == -1) break
            b.flip()
            readBuf.put(b)
            b.clear()
        }

        assertContentEquals(payload, readBuf.array())
    }

    @Test
    fun testReadWaiting() {
        val ch = TestInputChannel()
        val buf = ByteBuffer.allocate(128)
        val f = ch.read(buf)

        assertThrows<TimeoutException> { f.get(100, TimeUnit.MILLISECONDS)  }

        val msg = "Hello Ziti!!"
        ch.inbound.trySend(msg.encodeToByteArray()).getOrThrow()

        val count = f.get(100, TimeUnit.MILLISECONDS)
        assertEquals(count, msg.length)
        buf.flip()
        assertEquals(count, buf.remaining())
        val readMsg = Charsets.UTF_8.decode(buf).toString()
        assertEquals(msg, readMsg)
    }

    @Test
    fun testReadFutureCancel() {
        val ch = TestInputChannel()

        val f = ch.read(ByteBuffer.allocate(128))

        assertThrows<TimeoutException> { f.get(100, TimeUnit.MILLISECONDS) }
        assertThrows<ReadPendingException> { ch.read(ByteBuffer.allocate(128)) }

        f.cancel(true)
        assertThrows<CancellationException> { f.get() }

        val f2 = ch.read(ByteBuffer.allocate(128))
        ch.inbound.trySend("hello again".encodeToByteArray()).getOrThrow()
        f2.get()
    }


    @Test
    fun testEOF() {
        val ch = TestInputChannel()
        ch.inbound.close()

        assertEquals(-1, ch.read(ByteBuffer.allocate(128)).get(100, TimeUnit.MILLISECONDS))
    }
}