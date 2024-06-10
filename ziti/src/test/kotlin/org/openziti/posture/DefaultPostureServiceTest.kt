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

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.openziti.api.PortRange
import org.openziti.edge.model.PostureCheckType
import org.openziti.edge.model.PostureQuery
import org.openziti.edge.model.PostureResponseOperatingSystemCreate
import kotlin.test.assertEquals

class DefaultPostureServiceTest {

    lateinit var postureService: PostureService
    @Before
    fun setup() {
        postureService = PostureService()
    }

    @Test
    fun testOSposture() {
        postureService.registerServiceCheck("some-service-id",
            PostureQuery().queryType(PostureCheckType.OS).id("query-id").isPassing(false)
        )

        val posture = postureService.getPosture()
        assertEquals(1, posture.size)
        val osResp = posture[0] as PostureResponseOperatingSystemCreate
        assertEquals(System.getProperty("os.name"), osResp.type)
    }

    data class U(val low: Int) {
        @JsonCreator
        fun fromJson(v: Any): U {
            println(v)
            return U(v as Int)
        }
    }
    data class S(val s: String)
    @Test
    fun testSer() {
        val p1 = PortRange(22,22)

        val mapper = ObjectMapper().registerModule(kotlinModule())

        val foo = mapper.writeValueAsString(U(3))

        val f = mapper.readValue<U>(foo)

        val s1 = mapper.writeValueAsString(p1)
        val str = Gson().toJson(p1)
        val u: U = mapper.readValue(str)
        val range  = mapper.readValue<PortRange>(str)
        println(range)
    }

}