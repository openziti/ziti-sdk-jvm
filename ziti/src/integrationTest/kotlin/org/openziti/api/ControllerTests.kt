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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openziti.integ.ManagementHelper
import org.openziti.integ.ManagementHelper.enrollmentApi
import org.openziti.integ.ManagementHelper.identityApi
import org.openziti.integ.ManagementHelper.sslContext
import org.openziti.management.model.EnrollmentCreate
import org.openziti.management.model.IdentityCreate
import org.openziti.management.model.IdentityType
import java.net.URL
import java.time.OffsetDateTime


class ControllerTests {

    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            val identity = identityApi.createIdentity(
                IdentityCreate().name("test-${System.nanoTime()}").isAdmin(false).type(IdentityType.DEVICE)
            ).get().data!!

            val exp = OffsetDateTime.now().plusDays(1)
            val enrollReq = EnrollmentCreate()
                .identityId(identity.id!!)
                .method(EnrollmentCreate.MethodEnum.OTT)
                .expiresAt(exp)
            val enrollment = enrollmentApi.createEnrollment(enrollReq).get().data
            val token = identityApi.getIdentityEnrollments(identity.id).get().data.first().jwt
            println(token)
        }

    }

    @Test
    fun testCreateController() = runTest {

//        assertThrows<Controller.NotAvailableException> {
//            Controller.getActiveController(listOf(), sslContext)
//        }
//
//        assertThrows<Controller.NotAvailableException> {
//            Controller.getActiveController(listOf(URL("https://google.com")), sslContext)
//        }
//
//        val ctrl = assertDoesNotThrow {
//            Controller.getActiveController(listOf(
//                URL("https://google.com"),
//                URL("https://localhost:1280")
//            ), sslContext)
//        }
//
//        Assertions.assertEquals("/edge/client/v1", ctrl.endpoint.path)

        val ctrl = Controller(URL(ManagementHelper.api.baseUri), sslContext)
        ctrl.version()
    }
}