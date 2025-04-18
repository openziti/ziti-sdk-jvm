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

package org.openziti.integ

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.openziti.Enrollment
import org.openziti.IdentityConfig
import org.openziti.Ziti

abstract class BaseTest {
    protected lateinit var info: TestInfo

    @BeforeEach
    fun initTests(testInfo: TestInfo) {
        info = testInfo
    }

    fun createIdentity(name: String = "id-${info.displayName}-${System.nanoTime()}"): IdentityConfig {
        val token = ManagementHelper.createIdentity(name)

        val enrollment = Ziti.createEnrollment(token)
        Assertions.assertEquals(enrollment.getMethod(), Enrollment.Method.ott)

        return enrollment.enroll()
    }
}