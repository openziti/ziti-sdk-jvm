/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.util

import java.util.logging.Level
import java.util.logging.Logger

typealias LogMsg = () -> String

internal interface Logged {
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

internal class JULogged(val name: String) : Logged {
    val logger: Logger = Logger.getLogger(name)

    constructor() :
            this(getCaller().split(".").last())

    override fun e(msg: LogMsg) = e(null, msg)

    override fun e(ex: Throwable?, msg: LogMsg) = logger.logp(Level.SEVERE, name, "", msg(), ex)

    override fun w(msg: LogMsg) = logger.logp(Level.WARNING, name, "", msg)

    override fun i(msg: LogMsg) = logger.logp(Level.INFO, name, "", msg)

    override fun d(msg: LogMsg) = logger.logp(Level.FINE, name, "", msg)

    override fun v(msg: LogMsg) = logger.logp(Level.FINER, name, "", msg)

    override fun t(msg: LogMsg) = logger.logp(Level.FINEST, name, "", msg)

    companion object {
        fun getCaller(): String {
            val ex = Exception().stackTrace
            return ex[2].className
        }
    }
}

