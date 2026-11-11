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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.openapitools.jackson.nullable.JsonNullableModule
import org.openziti.edge.RFC3339JavaTimeModule

/**
 * Mapper for the generated `org.openziti.edge.model` classes. Configured exactly like the generated
 * `ApiClient.createDefaultObjectMapper()`, which is not used directly because that class references
 * `java.net.http`, and that package does not exist on Android.
 */
internal val edgeMapper: ObjectMapper = ObjectMapper().apply {
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
    disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
    registerModule(JavaTimeModule())
    registerModule(JsonNullableModule())
    registerModule(RFC3339JavaTimeModule())
}
