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

package io.netfoundry.ziti.impl

import io.netfoundry.ziti.ZitiContext
import io.netfoundry.ziti.identity.KeyStoreIdentity
import java.io.File
import java.security.KeyStore

internal object ZitiImpl {

    internal fun loadContext(idFile: File, pwd: CharArray, alias: String?): ZitiContext {
        val ks = KeyStore.getInstance("PKCS12").apply {
            load(idFile.inputStream(), pwd)
        }

        val idName = alias ?: findIdentity(ks)
        val id = KeyStoreIdentity(ks, idName)
        return ZitiContextImpl(id, true)
    }

    private fun findIdentity(ks: KeyStore): String {
        for (a in ks.aliases()) {
            if (ks.isKeyEntry(a)) {
                return a
            }
        }

        error("no suitable key entry")
    }
}