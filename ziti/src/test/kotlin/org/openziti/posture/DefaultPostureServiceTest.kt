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

package org.openziti.posture

import org.junit.Before
import org.junit.Test
import org.openziti.api.PostureQuery
import org.openziti.api.PostureQueryType
import org.openziti.api.PostureResponse
import kotlin.test.assertEquals

class DefaultPostureServiceTest {

    lateinit var postureService: PostureService
    @Before
    fun setup() {
        postureService = PostureService()
    }

    @Test
    fun testOSposture() {
        postureService.registerServiceCheck("some-service-id", PostureQuery(PostureQueryType.OS, "query-id", false, null))

        val posture = postureService.getPosture()
        assertEquals(1, posture.size)
        val osResp = posture[0].data as PostureResponse.OS
        assertEquals(System.getProperty("os.name"), osResp.type)
    }
}