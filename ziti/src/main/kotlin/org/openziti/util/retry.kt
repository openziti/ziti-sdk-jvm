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

package org.openziti.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Retry {
    class RetryException(ex: Throwable? = null, msg: String = "Max retry exceeded") : Exception(msg, ex)

    companion object {
        suspend fun <T> withExponentialBackoff(
            delay: Duration = 1.seconds, maxBackoff: Int = 5, maxRetry: Int = Int.MAX_VALUE, proc: suspend () -> T
        ): T {

            for (i in 0 until maxRetry) {
                runCatching { proc() }
                    .onFailure { if (it is CancellationException ) throw it }
                    .onSuccess { return it }


                val backoff = min(i, maxBackoff)
                val d = Random.nextLong((1 shl backoff) * delay.inWholeMilliseconds)
                delay(d)
            }

            throw RetryException()
        }
    }
}