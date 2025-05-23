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

package org.openziti.api

import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openziti.IdentityConfig
import org.openziti.Ziti
import org.openziti.integ.BaseTest
import org.openziti.integ.ManagementHelper.getIdentity
import org.openziti.util.Version


class ControllerTests: BaseTest() {

    lateinit var cfg: IdentityConfig
    lateinit var idName: String
    lateinit var appVersion: String

    @BeforeEach
    fun initIdentity() {
        appVersion = System.nanoTime().toString()
        Ziti.setApplicationInfo(info.displayName, appVersion)

        idName = "id-${info.displayName}-${System.nanoTime()}"
        cfg = createIdentity(idName)
    }

    @Test
    fun testOttEnrollment() = runTest {
        val ctrl = Controller.getActiveController(cfg.controllers(), cfg.sslContext())
        val session = ctrl.login()
        assertEquals(idName, session.identity.name)
        ctrl.logout()

        val identity = getIdentity(session.identityId)
        val idInfo = identity.data.sdkInfo
        assertEquals("ziti-sdk-java", idInfo.type)
        assertEquals(Version.version, idInfo.version)
        assertEquals(appVersion, idInfo.appVersion)
        assertEquals(info.displayName, idInfo.appId)
    }

}