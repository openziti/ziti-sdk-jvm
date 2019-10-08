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

package io.netfoundry.ziti.identity

import io.netfoundry.ziti.util.AliasKeyManager
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

    var sessionToken: String?
}

internal class KeyStoreIdentity(private val ks: KeyStore, alias: String, pw: CharArray = charArrayOf()) : Identity {
    enum class Attributes(val oid: String) {
        Controller("2.5.29.29"),
        Name("1.2.840.113549.1.9.20"),
    }

    private val controller: String
    private val name: String
    private val ssl: SSLContext
    private val tm: X509TrustManager

    init {
        check(ks.isKeyEntry(alias)) { "alias entry is not of correct type" }
        val entry = ks.getEntry(alias, KeyStore.PasswordProtection(pw))
        controller = getAttr(entry, Attributes.Controller)
        name = getAttr(entry, Attributes.Name)

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(ks, pw)
        }
        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(ks)
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

    override var sessionToken: String? = null

    internal fun getAttr(entry: KeyStore.Entry, attr: Attributes): String =
        checkNotNull(entry.attributes.find { it.name == attr.oid }?.value) { "entry is not a ziti identity: attr[${attr.name}] is missing" }
}
