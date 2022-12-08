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

package org.openziti.netty

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.ClosedChannelException
import java.nio.channels.CompletionHandler
import java.nio.channels.ReadPendingException


class ZitiChannel(parent: ServerChannel?, val peer: AsynchronousSocketChannel):
    AbstractChannel(parent),
    Logged by ZitiLog() {

    private var active = false
    init {
        if (parent != null && peer.isOpen) {
            active = true
        }
    }

    private class Reader(val c: ZitiChannel): CompletionHandler<Int, ByteBuffer> {
        override fun completed(len: Int, buf: ByteBuffer) {
            c.t("received $len bytes")
            if (len > 0) {
                buf.flip()
                val b = Unpooled.copiedBuffer(buf)
                buf.clear()
                c.pipeline().fireChannelRead(b)
            } else if (len == -1) {
                c.eventLoop().execute {
                    c.unsafe().close(c.voidPromise())
                }
                return
            }
            c.pipeline().fireChannelReadComplete()
        }

        override fun failed(exc: Throwable, buf: ByteBuffer) {
            c.pipeline().fireExceptionCaught(exc)
        }
    }

    override fun doBeginRead() {
        v("starting read")
        val buf = ByteBuffer.allocate(READ_BUFFER_SIZE)

        try {
            peer.read(buf, buf, Reader(this))
        } catch (rpe: ReadPendingException) { // already reading
            v("readOp is already active")
        } catch(cce: ClosedChannelException) {
            d{"channel is closed"}
            pipeline().fireChannelInactive()
        }
        catch (ex: Exception) {
            e(ex){"readOp failed"}
            pipeline().fireExceptionCaught(ex)
        }
    }

    override fun isActive(): Boolean {
        return active && peer.isOpen
    }

    override fun isCompatible(loop: EventLoop?): Boolean {
        return true
    }

    override fun remoteAddress0(): SocketAddress = peer.remoteAddress

    override fun metadata(): ChannelMetadata = META

    override fun doBind(localAddress: SocketAddress?) {
        error("unsupported operation")
    }

    override fun doClose() {
        active = false
        runCatching { peer.close() }
    }

    override fun newUnsafe(): AbstractUnsafe = AsyncUnsafe()

    override fun config(): ChannelConfig {
        return DefaultChannelConfig(this)
    }

    override fun localAddress0(): SocketAddress = peer.localAddress

    override fun isOpen(): Boolean = peer.isOpen

    override fun doWrite(buf: ChannelOutboundBuffer) {
        val spinCount = config().writeSpinCount
        for(i in 0 until spinCount) {
            val msg: Any? = buf.current()
            when (msg) {
                is ByteBuf -> {
                    if (!msg.isReadable)
                        buf.remove()
                    else {
                        val b = ByteBuffer.allocate(msg.readableBytes())
                        msg.readBytes(b)
                        b.flip()
                        val flushed = peer.write(b).get()
                        buf.progress(flushed.toLong())
                        if (!msg.isReadable)
                            buf.remove()
                    }
                }
                null -> return
            }
        }

        if (buf.current() != null) {
            t("need to flush again")
            eventLoop().execute {
                unsafe().flush()
            }
        }
    }

    override fun doDisconnect() {
        peer.close()
    }

    private inner class AsyncUnsafe: AbstractChannel.AbstractUnsafe() {
        override fun connect(remoteAddress: SocketAddress?, localAddress: SocketAddress?, promise: ChannelPromise) {
            promise.addListener {
                if (it.isSuccess) {
                    active = true
                    pipeline().fireChannelActive()
                }
            }
            peer.connect(remoteAddress, promise, object : CompletionHandler<Void, ChannelPromise>{
                override fun completed(result: Void?, attachment: ChannelPromise) { promise.trySuccess() }
                override fun failed(exc: Throwable, attachment: ChannelPromise) { promise.tryFailure(exc) }
            })
        }
    }

    companion object {
        val META = ChannelMetadata(false)
        const val READ_BUFFER_SIZE = 64 * 1024
    }
}