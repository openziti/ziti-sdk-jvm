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

package org.openziti.net

import org.openziti.net.ZitiProtocol.ContentType
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class Message(
    val content: ContentType,
    val body: ByteArray = ByteArray(0),
    val headers: MutableMap<Int, ByteArray> = mutableMapOf()
) {
    var seqNo: Int = -1
    var repTo: Int = -1

    companion object {
        fun newHello(token: String): Message {
            return Message(ContentType.HelloType, token.toByteArray())
        }

        internal suspend
        fun readMessage(input: Transport): Message? {
            val header = ByteBuffer.allocate(ZitiProtocol.HEADER_LENGTH).order(ByteOrder.LITTLE_ENDIAN)

            val read = input.read(header)
            if (read == -1)
                return null
            check(!header.hasRemaining()) { "could not read complete message header, read=${header.position()} expected=${header.capacity()}" }
            header.flip()

            ByteArray(ZitiProtocol.VERSION.size).apply {
                header.get(this)
                if (!ZitiProtocol.VERSION.contentEquals(this)) {
                    throw IllegalStateException("message prefix mismatch")
                }
            }

            val ct = ContentType.fromInt(header.int)
            val seq = header.int
            val headersLen = header.int
            val bodyLen = header.int

            val headersBuf = ByteBuffer.allocate(headersLen).order(ByteOrder.LITTLE_ENDIAN)
            input.read(headersBuf)
            check(!headersBuf.hasRemaining()) { "failed to read headers data" }
            headersBuf.flip()
            val headers = parseHeaders(headersBuf)

            val body = ByteArray(bodyLen)
            if (bodyLen > 0) {
                input.read(ByteBuffer.wrap(body))
            }

            return Message(ct, body, headers).apply {
                seqNo = seq
                repTo = getIntHeader(ZitiProtocol.Header.ReplyFor) ?: -1
            }
        }

        fun parseHeaders(buf: ByteBuffer) = mutableMapOf<Int, ByteArray>().apply {
            while (buf.remaining() >= 8) {
                val key = buf.int
                val vlen = buf.int
                val b = ByteArray(vlen).apply { buf.get(this) }
                put(key, b)
            }
            if (buf.remaining() > 0) throw IllegalStateException("header parse failed")
        }
    }

    internal suspend fun write(t: Transport) {
        val headersLength = headers.map { (_, v) -> v.size + 8 }.fold(0) { l, r -> l + r }
        val size = ZitiProtocol.HEADER_LENGTH + body.size + headersLength

        val out = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN)
        out.put(ZitiProtocol.VERSION)
        out.putInt(content.id)
        out.putInt(seqNo)
        out.putInt(headersLength)
        out.putInt(body.size)
        headers.forEach {
            out.putInt(it.key)
            out.putInt(it.value.size)
            out.put(it.value)
        }
        out.put(body)
        out.flip()

        t.write(out)
    }

    override fun toString(): String {
        return "ct: %s, seq: %d, repTo: %d, connId: %d, body %d bytes"
            .format(content, seqNo, repTo, getIntHeader(ZitiProtocol.Header.ConnId), body.size)
    }

    fun setHeader(headerId: Int, v: String) = this.apply {
        headers.put(headerId, v.toByteArray())
    }

    fun setHeader(headerId: Int, v: Int): Message = this.apply {
        val b = ByteArray(4)
        ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).putInt(v)
        headers.put(headerId, b)
    }

    fun setHeader(headerId: Int, b: ByteArray) = this.apply {
        headers.put(headerId, b)
    }

    fun setHeader(headerId: Int, v: Boolean) = this.apply {
        val b: Byte = if (v) 1 else 0
        headers.put(headerId, byteArrayOf(b))
    }

    fun getStringHeader(headerId: Int): String? {
        return headers[headerId]?.toString(StandardCharsets.UTF_8)
    }

    fun getIntHeader(headerId: Int): Int? {
        return headers[headerId]?.let {
            ByteBuffer.wrap(it).order(ByteOrder.LITTLE_ENDIAN).int
        }
    }

    fun getBoolHeader(headerId: Int): Boolean {
        return headers[headerId]?.let { it[0].toInt() != 0 } ?: false
    }

    fun getHeader(headerId: Int): ByteArray? = headers[headerId]
}