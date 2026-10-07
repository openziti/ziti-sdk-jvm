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

package org.openziti.impl

import org.junit.Assert.assertEquals
import org.junit.Test
import org.openziti.ZitiException
import org.openziti.ZitiException.Errors
import org.openziti.api.ZitiAuthenticator
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.CancellationException

// Only transient failures may send the context's runner back to retry; anything else must still end it.
class TransientFailureTest {

    private val cases = listOf(
        "network error" to (IOException("connection reset") to true),
        "connect error" to (ConnectException("refused") to true),
        "controller unavailable" to (ZitiException(Errors.ControllerUnavailable) to true),
        "api error caused by network error" to (ZitiException(Errors.WTF("api"), IOException("eof")) to true),
        "rejected credentials" to (ZitiException(Errors.NotAuthorized) to false),
        "authentication failure" to (ZitiAuthenticator.AuthException() to false),
        "programming error" to (IllegalStateException("bug") to false),
        "cancellation" to (CancellationException("cancelled") to false),
    )

    @Test
    fun classifiesFailures() {
        val actual = cases.associate { (name, case) -> name to isTransientFailure(case.first) }
        val expected = cases.associate { (name, case) -> name to case.second }
        assertEquals(expected, actual)
    }
}
