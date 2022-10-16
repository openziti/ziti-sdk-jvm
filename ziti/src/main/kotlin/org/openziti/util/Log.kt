/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory

typealias LogMsg = () -> String

interface Logged {
    fun e(msg: LogMsg)
    fun e(msg: String) = e(null) { msg }
    fun e(msg: String, t: Throwable) = e(t) { msg }
    fun e(ex: Throwable?, msg: LogMsg)

    fun w(msg: LogMsg)
    fun w(msg: String) = w { msg }

    fun i(msg: LogMsg)
    fun i(msg: String) = i { msg }

    fun d(msg: LogMsg)
    fun d(msg: String) = d { msg }

    fun v(msg: LogMsg)
    fun v(msg: String) = v { msg }

    fun t(msg: LogMsg)
    fun t(msg: String) = t { msg }
}

class ZitiLog(name: String, private val delegate: Logged = SLF4JLoggedImpl(name)) : Logged by delegate {
    constructor() :
            this(getCaller())

    companion object {
        fun getCaller(): String {
            val ex = Exception().stackTrace
            return ex[2].className
        }
    }
}

val TRACE = MarkerFactory.getMarker("TRACE")
internal class SLF4JLoggedImpl(val name: String) : Logged {
    private val logger = LoggerFactory.getLogger(name)

    override fun e(msg: LogMsg) = e(null, msg)

    override fun e(ex: Throwable?, msg: LogMsg) = logger.error(msg(), ex)

    override fun w(msg: LogMsg) = logger.warn(msg())

    override fun i(msg: LogMsg) = logger.info(msg())

    override fun d(msg: LogMsg) = logger.debug(msg())

    override fun v(msg: LogMsg) = logger.trace(msg())

    override fun t(msg: LogMsg) = logger.trace(TRACE, msg())
}

