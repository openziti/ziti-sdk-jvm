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

package org.openziti.identity

import org.openziti.util.AliasKeyManager
import java.net.URI
import java.net.URLDecoder
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


interface Identity {
    fun controller(): String
    fun name(): String
    fun sslContext(): SSLContext
    fun trustManager(): X509TrustManager
}

internal class KeyStoreIdentity(private val ks: KeyStore, alias: String, pw: CharArray = charArrayOf()) : Identity {

    private val controller: String
    private val name: String
    private val ssl: SSLContext
    private val tm: X509TrustManager

    init {
        check(ks.isKeyEntry(alias)) { "alias entry is not of correct type" }
        // alias is in URI form -> ziti://controller_host:controller_port/name
        val aliasURI = URI.create(alias)

        controller = "https://${aliasURI.host}:${aliasURI.port}"
        name = URLDecoder.decode(aliasURI.rawPath.substring(1), Charsets.UTF_8.name()) // remove leading slash and decode

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(ks, pw)
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            // if we don't have CA certs in the store use system-wide trust
            val certEntry = ks.aliases().asSequence().find { ks.isCertificateEntry(it) }
            if (certEntry != null)
                init(ks)
            else
                init(null as KeyStore?)
        }
        tm = tmf.trustManagers[0] as X509TrustManager

        ssl = SSLContext.getInstance("TLSv1.2").apply {
            init(AliasKeyManager.from(alias, kmf.keyManagers), tmf.trustManagers, SecureRandom())
        }
    }

    override fun controller() = controller

    override fun name() = name

    override fun sslContext(): SSLContext = ssl

    override fun trustManager(): X509TrustManager = tm
}
