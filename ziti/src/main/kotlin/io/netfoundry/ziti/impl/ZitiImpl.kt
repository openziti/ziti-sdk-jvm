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

package io.netfoundry.ziti.impl

import com.google.gson.Gson
import io.netfoundry.ziti.ZitiContext
import io.netfoundry.ziti.identity.Enroller
import io.netfoundry.ziti.identity.KeyStoreIdentity
import io.netfoundry.ziti.net.internal.Sockets
import io.netfoundry.ziti.util.*
import java.io.File
import java.net.URI
import java.security.KeyStore

internal object ZitiImpl : Logged by ZitiLog() {

    internal val onAndroid: Boolean by lazy {
        try {
            Class.forName("android.util.Log")
            true
        } catch (cnf: ClassNotFoundException) {
            false
        }
    }

    init {
        i("ZitiSDK version ${Version.version} @${Version.revision}(${Version.branch})")
    }

    internal fun loadContext(ks: KeyStore, alias: String?): ZitiContextImpl {
        val idName = alias ?: findIdentity(ks)
        val id = KeyStoreIdentity(ks, idName)
        return ZitiContextImpl(id, true).also {
            contexts.add(it)
        }
    }
    internal fun loadContext(idFile: File, pwd: CharArray, alias: String?): ZitiContextImpl {
        val ks = loadKeystore(idFile, pwd)
        return loadContext(ks, alias)
    }

    private fun findIdentity(ks: KeyStore): String {
        for (a in ks.aliases()) {
            if (ks.isKeyEntry(a)) {
                return a
            }
        }

        error("no suitable key entry")
    }

    internal val contexts = mutableListOf<ZitiContextImpl>()
    fun init(file: File, pwd: CharArray, seamless: Boolean): Unit {
        if (seamless) {
            initInternalNetworking()
        }

        val ctx = loadContext(file, pwd, null) as ZitiContextImpl
        ctx.checkServicesLoaded()
    }

    fun init(ks: KeyStore, seamless: Boolean): List<ZitiContext> {
        if (seamless) {
            initInternalNetworking()
        }

        for (a in ks.aliases()) {
            if (isZitiIdentity(ks, a)) {
                loadContext(ks, a)
            }
        }

        return contexts
    }

    private fun isZitiIdentity(ks: KeyStore, alias: String): Boolean {
        if (!ks.isKeyEntry(alias))
            return false

        try {
            return URI.create(alias).scheme == "ziti"
        }
        catch (ex: IllegalArgumentException) {
            return false
        }
    }

    private fun initInternalNetworking() {
        Sockets.init()
    }

    internal class Id(val key: String, val cert: String, val ca: String?)
    internal class Identity(val ztAPI: String, val id: Id)

    internal fun loadKeystore(f: File, pwd: CharArray): KeyStore {
        val ks = KeyStore.getInstance("PKCS12")
        try {
            ks.load(f.inputStream(), pwd)
            return ks
        } catch (ex: Exception) {
            //w("failed to load $f as PKCS12 key store: $ex")
        }

        try {
            val id = Gson().fromJson(f.reader(), Identity::class.java)
            ks.load(null)
            val certs = readCerts(id.id.cert.replace("pem:", ""))
            val ztAPI = URI.create(id.ztAPI)
            val alias = "ziti://${ztAPI.host}:${ztAPI.port}/${f.name}"

            val key = readKey(id.id.key.replace("pem:", "").reader())
            val keyEntry = KeyStore.PrivateKeyEntry(key, certs.toTypedArray(), emptySet())
            ks.setEntry(alias, keyEntry, KeyStore.PasswordProtection(charArrayOf()))

            id.id.ca?.let {
                val cacerts = readCerts(it.replace("pem:", ""))
                for (ca in cacerts) {
                    val caAlias = "${alias}-ca-${ca.serialNumber}"
                    ks.setCertificateEntry(caAlias, ca)
                }
            }

            return ks
        } catch (ex: Exception) {
            println(ex)
            ex.printStackTrace()
        }

        throw IllegalArgumentException("unsupported format")
    }

    fun enroll(ks: KeyStore, jwt: ByteArray, name: String) {
        val enroller = Enroller.fromJWT(String(jwt))
        val alias = enroller.enroll(null, ks, name)

        loadContext(ks, alias)
    }
}