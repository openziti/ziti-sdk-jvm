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

import java.util.*

data class SystemInfo(val os: String, val osRelease: String, val osVersion: String, val arch: String)

interface SystemInfoProvider {
    fun getSystemInfo(): SystemInfo
}

internal class DefaultSystemInfoProvider: SystemInfoProvider {
    override fun getSystemInfo(): SystemInfo = SystemInfo(
        System.getProperty("os.name", ""),
        System.getProperty("java.vm.version", ""),
        System.getProperty("os.version", ""),
        System.getProperty("os.arch", "")

    )
}

fun SystemInfoProvider(): SystemInfoProvider {
    val l = ServiceLoader.load(SystemInfoProvider::class.java).toList()
    return if (l.isEmpty()) {
        DefaultSystemInfoProvider()
    } else {
        l.first()
    }
}
