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

package org.openziti.netty

import io.netty.resolver.AddressResolver
import io.netty.resolver.AddressResolverGroup
import io.netty.util.concurrent.DefaultPromise
import io.netty.util.concurrent.EventExecutor
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.Promise
import org.openziti.ZitiAddress
import org.openziti.ZitiContext
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.UnresolvedAddressException
import java.nio.channels.UnsupportedAddressTypeException

class ZitiResolverGroup(val ztx: ZitiContext): AddressResolverGroup<SocketAddress>() {

    inner class Resolver(val executor: EventExecutor?): AddressResolver<SocketAddress> {
        override fun close() = Unit

        override fun isSupported(address: SocketAddress?): Boolean =
            address is ZitiAddress || address is InetSocketAddress

        override fun isResolved(address: SocketAddress?): Boolean  {
            return runCatching { zitiResolve(address) }.isSuccess
        }

        override fun resolve(address: SocketAddress?): Future<SocketAddress> =
            resolve(address, DefaultPromise(executor))

        override fun resolve(address: SocketAddress?, promise: Promise<SocketAddress>) = promise.apply {
            runCatching { zitiResolve(address) }
                .onSuccess { setSuccess(it) }
                .onFailure { setFailure(it) }
        }

        override fun resolveAll(address: SocketAddress?): Future<MutableList<SocketAddress>> =
            resolveAll(address, DefaultPromise(executor))

        override fun resolveAll(
            address: SocketAddress?,
            promise: Promise<MutableList<SocketAddress>>
            ): Future<MutableList<SocketAddress>> = promise.apply {
            runCatching { zitiResolve(address) }
                    .onSuccess { setSuccess(mutableListOf(it)) }
                .onFailure { setFailure(it) }
        }

        private fun zitiResolve(addr: SocketAddress?): SocketAddress = when(addr) {
            is ZitiAddress.Dial -> addr
            is InetSocketAddress -> {
                val service =  ztx.getService(addr)
                service?.let {
                    ZitiAddress.Dial(service.name)
                } ?: throw UnresolvedAddressException()
            }
            else -> throw UnsupportedAddressTypeException()
        }
    }

    override fun newResolver(executor: EventExecutor?) = Resolver(executor)
}