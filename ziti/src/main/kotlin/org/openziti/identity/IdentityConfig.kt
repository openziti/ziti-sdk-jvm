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

package org.openziti.identity

import com.google.gson.Gson
import java.io.File
import java.io.Reader

internal class IdentityConfig(val ztAPI: String, val id: Id) {
    internal class Id(val key: String, val cert: String, val ca: String?)

    companion object {
        fun load(r: Reader): IdentityConfig = Gson().fromJson(r, IdentityConfig::class.java)

        fun load(f: File): IdentityConfig = load(f.reader());

        fun load(path: String): IdentityConfig = load(File(path))
    }
}