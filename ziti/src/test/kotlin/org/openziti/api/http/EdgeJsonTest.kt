/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.api.http

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.openziti.edge.model.DialBind
import org.openziti.edge.model.ListServicesEnvelope
import org.openziti.edge.model.SessionCreate
import org.openziti.impl.isTransientFailure
import java.io.IOException
import java.time.Instant

// The shared mapper must (de)serialize the generated edge models exactly like ApiClient.createDefaultObjectMapper().
class EdgeJsonTest {

    @Test
    fun ignoresUnknownProperties() {
        val json = """{"data":[{"id":"s1","name":"svc","extra":1}],"meta":{}}"""

        val envelope = edgeMapper.readValue(json, ListServicesEnvelope::class.java)

        assertEquals("s1", envelope.data[0].id)
    }

    @Test
    fun readsRfc3339Dates() {
        val json = """{"data":[{"id":"s1","createdAt":"2024-05-01T10:15:30.123Z"}]}"""

        val envelope = edgeMapper.readValue(json, ListServicesEnvelope::class.java)

        assertEquals(Instant.parse("2024-05-01T10:15:30.123Z"), envelope.data[0].createdAt.toInstant())
    }

    @Test
    fun serializesEnumsViaToStringAndOmitsNulls() {
        val json = edgeMapper.writeValueAsString(SessionCreate().type(DialBind.DIAL).serviceId("s1"))

        assertTrue(json, json.contains("\"type\":\"Dial\""))
        assertTrue(json, json.contains("\"serviceId\":\"s1\""))
        assertFalse(json, json.contains("null"))
    }

    @Test
    fun httpExceptionIsNotTransient() {
        val ex: Throwable = ZitiHttpException(502, "<html/>")

        assertFalse(ex is IOException)
        assertFalse(isTransientFailure(ex))
    }
}
