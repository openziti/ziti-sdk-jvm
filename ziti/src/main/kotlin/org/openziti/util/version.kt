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


object Version {
    val VersionInfo: Properties by lazy {
        Properties().apply {
            val inputStream = Version.javaClass.getResourceAsStream("ziti-version.properties")
            try {
                load(inputStream)
            } catch (ex: Exception) {
                setProperty("version", "local")
                setProperty("revision", "local-build")
                setProperty("branch", "local")
            }
        }
    }

    val version = VersionInfo["version"].toString()
    val revision = VersionInfo["revision"].toString()
    val branch = VersionInfo["branch"].toString()
}
