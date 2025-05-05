/*
 * Copyright (c) 2018-2025 NetFoundry Inc.
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

package org.openziti.impl

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openziti.IdentityConfig
import org.openziti.Ziti
import org.openziti.identity.keystoreFromConfig
import org.openziti.integ.BaseTest
import java.io.ByteArrayOutputStream

class LoadTests: BaseTest() {
    lateinit var cfg: IdentityConfig
    @BeforeEach
    fun before() {
        cfg = createIdentity()
    }

    @AfterEach
    fun after() {
        Ziti.getContexts().forEach {
            it.destroy()
        }
    }

    @Test
    fun testLoadConfigByteArray() {
        val cfgBytes = Json.encodeToString(cfg).toByteArray(Charsets.UTF_8)

        Ziti.init(cfgBytes, false)

        val contexts = Ziti.getContexts()
        assertEquals(1, contexts.size)
    }

    @Test
    fun testLoadKeyStoreByteArray() {
        val ks = keystoreFromConfig(cfg)
        val output = ByteArrayOutputStream()
        ks.store(output, charArrayOf())

        val storeBtes = output.toByteArray()
        Ziti.init(storeBtes, false)

        val contexts = Ziti.getContexts()
        assertEquals(1, contexts.size)
    }
}