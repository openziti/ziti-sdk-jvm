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

package org.openziti

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.file
import org.openziti.identity.Enroller
import java.io.FileNotFoundException
import java.net.InetAddress
import java.security.KeyStore

object ZitiEnroller {

    private class Cli : CliktCommand(name = "ziti-enroller") {
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

    @JvmStatic
    fun main(args: Array<String>) = Cli().main(args)
}