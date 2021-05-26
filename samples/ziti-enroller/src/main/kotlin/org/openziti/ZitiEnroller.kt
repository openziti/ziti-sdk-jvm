/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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

package org.openziti

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.future.asDeferred
import org.openziti.api.MFAType
import org.openziti.identity.Enroller
import java.io.FileNotFoundException
import java.net.InetAddress
import java.security.KeyStore
import java.util.concurrent.CompletableFuture

object ZitiEnroller {

    private class enroll: CliktCommand() {
        val jwt by option(help = "Enrollment token (JWT file). Required").file().required().validate {
            it.exists() || throw FileNotFoundException("jwt[${it.path}] not found")
        }
        val out by option(help = "Output configuration file.").file().required().validate {
            it.exists() && throw FileAlreadyExistsException(it, null, "identity file already exists")
        }

        override fun run() {
            val enroller = Enroller.fromJWT(jwt.readText())
            val store = KeyStore.getInstance("PKCS12").apply {
                load(null)
            }

            enroller.enroll(null, store, InetAddress.getLocalHost().hostName)

            store.store(out.outputStream(), charArrayOf())
        }
    }

    private class verify: CliktCommand(help = "verify identity file. OTP will be requested if identity is enrolled in MFA") {
        val idFile by option(help = "identity configuration file.").file().required().validate {
            it.exists()
        }

        val showCodes by option(help = "display recovery codes").flag(default = false)
        val newCodes by option(help = "generate new recovery codes").flag(default = false)

        fun readMFACode(type: MFAType, provider: String): Deferred<String> {
            println("Enter MFA code $type/$provider: ")
            return CompletableFuture.supplyAsync {
                var s: String? = null
                while(s == null) s = readLine()
                s
            }.asDeferred()
        }

        override fun run() = runBlocking {
            val ztx = Ziti.newContext(idFile, charArrayOf())

            val j = coroutineScope {
                launch  {
                    ztx.statusUpdates().collect {
                        println("status: $it")
                        when(it) {
                            ZitiContext.Status.Loading -> {}
                            is ZitiContext.Status.NeedsAuth -> {
                                println("identity is enrolled in MFA")
                                val code = readMFACode(it.type, it.provider).await()
                                ztx.authenticateMFA(code)
                            }
                            ZitiContext.Status.Active -> {
                                println("verification success!")
                                cancel()
                            }
                            is ZitiContext.Status.NotAuthorized -> {
                                cancel("verification failed!", it.ex)
                            }
                            else -> cancel("unexpected status")
                        }
                    }
                }
            }

            j.join()

            val mfaEnrolled = ztx.isMFAEnrolled()
            println("MFA enrolled: $mfaEnrolled")

            if (showCodes || newCodes) {
                if (mfaEnrolled) {
                    print("""enter OTP to ${if (newCodes) "generate" else "show"} recovery codes: """)
                    val code = readLine()
                    runCatching {
                        val recCodes = ztx.getMFARecoveryCodes(code!!, newCodes)
                        for (rc in recCodes) {
                            println(rc)
                        }
                    }.onFailure {
                        println("invalid code submitted")
                    }
                } else {
                    println("cannot show recovery codes: not enrolled in MFA")
                }
            }

            ztx.destroy()
        }
    }

    private class mfa: CliktCommand(help = "Enroll identity in MFA") {
        val idFile by option(help = "identity configuration file.").file().required().validate {
            it.exists()
        }

        override fun run() = runBlocking {
            val ztx = Ziti.newContext(idFile, charArrayOf())

            val j = launch {
                ztx.statusUpdates().takeWhile { it != ZitiContext.Status.Active }.collectLatest { println(it) }
                val mfa = ztx.enrollMFA()
                println(mfa)

                print("Enter OTP code: ")
                val code = readLine()
                ztx.verifyMFA(code!!.trim())
            }
            j.join()

            ztx.destroy()
        }

    }

    private class Cli : CliktCommand(name = "ziti-enroller") {

        init {
            subcommands(enroll(), verify(), mfa())
        }

        override fun run() {
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = Cli().main(args)
}