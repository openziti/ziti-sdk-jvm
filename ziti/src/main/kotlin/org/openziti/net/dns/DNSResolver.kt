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

package org.openziti.net.dns

import java.io.Writer
import java.net.InetAddress
import java.util.function.Consumer

interface DNSResolver {
    fun resolve(hostname: String): InetAddress?
    fun lookup(addr: InetAddress): String?

    data class DNSEvent(val hostname: String?, val ip: InetAddress, val removed: Boolean)
    fun subscribe(sub: (DNSEvent) -> Unit)
    fun subscribe(sub: Consumer<DNSEvent>)

    fun dump(writer: Writer)
}
