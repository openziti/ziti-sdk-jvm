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

package io.netfoundry.ziti.util

import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import java.lang.reflect.Method
import java.util.logging.Level
import java.util.logging.Logger

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

internal fun getDelegate(name: String): Logged {

    try {
        Class.forName("android.util.Log")
        return AndroidLogged(name)
    }
    catch (ex: Throwable) {
        return JULoggedImpl(name)
    }
}

class ZitiLog(name: String, private val delegate: Logged = SLF4JLoggedImpl(name)) : Logged by delegate {
    constructor() :
            this(getCaller().split(".").last())

    companion object {
        fun getCaller(): String {
            val ex = Exception().stackTrace
            return ex[2].className
        }
    }
}

internal class JULoggedImpl(val name: String) : Logged {
    private val logger: Logger = Logger.getLogger(name)

    override fun e(msg: LogMsg) = e(null, msg)

    override fun e(ex: Throwable?, msg: LogMsg) = logger.logp(Level.SEVERE, name, "", msg(), ex)

    override fun w(msg: LogMsg) = logger.logp(Level.WARNING, name, "", msg)

    override fun i(msg: LogMsg) = logger.logp(Level.INFO, name, "", msg)

    override fun d(msg: LogMsg) = logger.logp(Level.FINE, name, "", msg)

    override fun v(msg: LogMsg) = logger.logp(Level.FINER, name, "", msg)

    override fun t(msg: LogMsg) = logger.logp(Level.FINEST, name, "", msg)
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

internal class AndroidLogged(val tag: String): Logged {

    override fun e(msg: LogMsg) {
        err(null, tag, msg(), null)
    }

    override fun e(ex: Throwable?, msg: LogMsg) {
        err(null, tag, msg(), ex)
    }

    override fun w(msg: LogMsg) {
        warn(null, tag, msg(), null)

    }

    override fun i(msg: LogMsg) {
        info(null, tag, msg(), null)
    }

    override fun d(msg: LogMsg) {
        dbg(null, tag, msg(), null)
    }

    override fun v(msg: LogMsg) {
        verb(null, tag, msg(), null)
    }

    override fun t(msg: LogMsg) {
        trace(null, tag, msg(), null)
    }

    companion object {
        lateinit var err: Method
        lateinit var warn: Method
        lateinit var info: Method
        lateinit var dbg: Method
        lateinit var verb: Method
        lateinit var trace: Method

        init {
            try {
                val logCls = Class.forName("android.util.Log")

                err = logCls.getDeclaredMethod("e", String::class.java, String::class.java, Throwable::class.java)
                warn = logCls.getDeclaredMethod("w", String::class.java, String::class.java, Throwable::class.java)
                info = logCls.getDeclaredMethod("w", String::class.java, String::class.java, Throwable::class.java)
                dbg = logCls.getDeclaredMethod("d", String::class.java, String::class.java, Throwable::class.java)
                verb = logCls.getDeclaredMethod("v", String::class.java, String::class.java, Throwable::class.java)
                trace = verb
            } catch (ex: ClassNotFoundException) {
                // ignore as we should not be here
            }
        }
    }
}

