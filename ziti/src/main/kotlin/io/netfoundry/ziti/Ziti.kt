/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti

import io.netfoundry.ziti.impl.ZitiImpl
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import io.netfoundry.ziti.util.Version
import java.io.File

object Ziti : Logged by JULogged() {
    @JvmStatic
    fun newContext(idFile: File, pwd: CharArray): ZitiContext = ZitiImpl.loadContext(idFile, pwd, null)

    @JvmStatic
    fun newContext(fname: String, pwd: CharArray): ZitiContext = newContext(File(fname), pwd)

    @JvmStatic
    fun init(fname: String, pwd: CharArray, seamless: Boolean) = ZitiImpl.init(File(fname), pwd, seamless)

    init {
        i("ZitiSDK version ${Version.version} @${Version.revision}(${Version.branch})")
    }
}