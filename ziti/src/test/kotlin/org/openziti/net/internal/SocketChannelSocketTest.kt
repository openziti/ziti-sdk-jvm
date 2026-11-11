/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.net.internal

import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.DataInputStream
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.random.Random

// Writes to a peer that is not reading fill the (small) socket buffers, so some writes come up short and the
// socket has to wait until the peer takes the data. Like java.net.Socket, SO_TIMEOUT only limits reads:
// OkHttp sets it to 0 for HTTP/2 connections and enforces write timeouts itself by closing the socket.
class SocketChannelSocketTest {

    private val loopback = InetAddress.getByName("127.0.0.1")
    private val data = Random(42).nextBytes(1024 * 1024)
    private val closeables = mutableListOf<AutoCloseable>()

    @After
    fun tearDown() {
        closeables.forEach { runCatching { it.close() } }
    }

    private fun listen(): ServerSocket = ServerSocket().apply {
        receiveBufferSize = 4096
        bind(InetSocketAddress(loopback, 0))
        closeables += this
    }

    private fun connect(server: ServerSocket, soTimeout: Int): Pair<SocketChannelSocket, Socket> {
        val client = SocketChannelSocket().apply {
            sendBufferSize = 4096
            connect(InetSocketAddress(loopback, server.localPort), 5_000)
            this.soTimeout = soTimeout
        }
        val peer = server.accept()
        closeables += client
        closeables += peer
        return client to peer
    }

    // writes [data] on another thread; the future completes with the error, or null on success
    private fun writeAsync(s: Socket): CompletableFuture<Throwable?> {
        val done = CompletableFuture<Throwable?>()
        thread(isDaemon = true) {
            done.complete(runCatching { s.getOutputStream().write(data) }.exceptionOrNull())
        }
        return done
    }

    private fun readAll(peer: Socket): ByteArray =
        ByteArray(data.size).also { DataInputStream(peer.getInputStream()).readFully(it) }

    @Test(timeout = 10_000)
    fun write_withSoTimeoutZero_waitsForThePeer() {
        val (client, peer) = connect(listen(), soTimeout = 0)

        val write = writeAsync(client)
        Thread.sleep(300)
        assertTrue("the write gave up instead of waiting: ${write.getNow(null)}", !write.isDone)

        assertArrayEquals(data, readAll(peer))
        assertNull(write.get(5, TimeUnit.SECONDS))
    }

    @Test(timeout = 10_000)
    fun write_outlastsSoTimeout() {
        val (client, peer) = connect(listen(), soTimeout = 100)

        val write = writeAsync(client)
        Thread.sleep(500)
        assertTrue("the write gave up instead of waiting: ${write.getNow(null)}", !write.isDone)

        assertArrayEquals(data, readAll(peer))
        assertNull(write.get(5, TimeUnit.SECONDS))
    }

    // HTTP/2: one thread waits in read for the next frame while another writes
    @Test(timeout = 10_000)
    fun write_whileAnotherThreadWaitsInRead() {
        val (client, peer) = connect(listen(), soTimeout = 0)
        val read = CompletableFuture<Int>()
        thread(isDaemon = true) { read.complete(runCatching { client.getInputStream().read() }.getOrDefault(-2)) }
        Thread.sleep(100)

        val write = writeAsync(client)
        Thread.sleep(300)
        assertTrue("the write gave up instead of waiting: ${write.getNow(null)}", !write.isDone)

        assertArrayEquals(data, readAll(peer))
        assertNull(write.get(5, TimeUnit.SECONDS))

        peer.getOutputStream().write(7)
        assertEquals(7, read.get(5, TimeUnit.SECONDS))
    }

    // a write that waits for a peer that never reads is ended by closing the socket
    @Test(timeout = 10_000)
    fun close_endsAWaitingWrite() {
        val (client, _) = connect(listen(), soTimeout = 0)

        val write = writeAsync(client)
        Thread.sleep(300)
        assertTrue("the write gave up instead of waiting: ${write.getNow(null)}", !write.isDone)

        client.close()
        val error = write.get(5, TimeUnit.SECONDS)
        assertTrue("expected an IOException, got $error", error is IOException)
    }
}
