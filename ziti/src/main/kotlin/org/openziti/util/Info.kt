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

package org.openziti.util

import org.openziti.Ziti
import java.io.Writer

class ZtxInfoProvider: DebugInfoProvider {

    override fun names(): Iterable<String> = Ziti.getContexts().map{ it.name() }

    override fun dump(name: String, output: Writer) {
        val ztx = Ziti.getContexts().find { it.name() == name }
        ztx?.dump(output) ?: output.appendLine("ziti context[$name] not found")
    }

}

class DnsInfoProvider: DebugInfoProvider {

    override fun names() = listOf("ziti_dns.info")

    override fun dump(name: String, output: Writer) {
        Ziti.getDNSResolver().dump(output)
    }
}

class ThreadDumpProvider: DebugInfoProvider {
    override fun names() = listOf("thread_dump.txt")

    override fun dump(name: String, output: Writer) {
        Thread.getAllStackTraces().forEach { t, trace ->
            output.appendLine("Thread: ${t}")
            trace.forEach {
                output.appendLine("  $it")
            }
            output.appendLine()
        }
    }
}