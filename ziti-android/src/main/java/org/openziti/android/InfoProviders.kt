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

package org.openziti.android

import android.os.Build
import org.openziti.util.DebugInfoProvider
import org.openziti.util.Version
import java.io.Writer
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.util.concurrent.CompletableFuture

class KeystoreInfoProvider: DebugInfoProvider {
    private val keyStore: KeyStore
    init {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
    }
    override fun names() = listOf("keystore.info")

    override fun dump(name: String, output: Writer) {
        for (a in keyStore.aliases()) {
            val entry = keyStore.getEntry(a, null)
            val cert = when(entry) {
                is KeyStore.TrustedCertificateEntry -> entry.trustedCertificate
                is KeyStore.PrivateKeyEntry -> entry.certificate
                else -> continue
            }

            output.appendLine()
            output.appendLine("""
                name: $a 
                type: ${entry.javaClass.simpleName}
                cert: ${cert.type} ${if (cert is X509Certificate)
                "Subject: ${cert.subjectDN} Issuer: ${cert.issuerDN}" else ""} 
                """.trimIndent())
        }
    }
}

class LogcatProvider: DebugInfoProvider {

    override fun names() = listOf("logcat")

    override fun dump(name: String, output: Writer) {
        val logcat = Runtime.getRuntime().exec("logcat -d -b crash,main")
        val log = CompletableFuture.supplyAsync { logcat.inputStream.bufferedReader().readText() }
        val err = CompletableFuture.supplyAsync { logcat.errorStream.bufferedReader().readText() }
        val logrc = CompletableFuture.supplyAsync { logcat.waitFor() }

        output.appendLine("logcat result: ${logrc.get()}")
        output.write(log.get())
        output.appendLine()
        output.appendLine("logcat ERROR:")
        output.appendLine(err.get())
    }
}

class AppInfoProvider: DebugInfoProvider {
    override fun names() = listOf("app.info")

    override fun dump(name: String, output: Writer) {
        output.appendLine("Device:          ${Build.MODEL} (${Build.MANUFACTURER})")
        output.appendLine("Android Version: ${Build.VERSION.RELEASE}")
        output.appendLine("Android-SDK:     ${Build.VERSION.SDK_INT}")
        output.appendLine("Ziti Version:    ${BuildConfig.ZITI_VERSION}(${Version.revision})")
        output.appendLine("App:             ${Ziti.app.packageName}")
        output.appendLine("App Version:     ${Ziti.app.packageManager.getPackageInfo(Ziti.app.packageName, 0).versionName}")
    }
}

