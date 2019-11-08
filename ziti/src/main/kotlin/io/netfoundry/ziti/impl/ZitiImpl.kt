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
import io.netfoundry.ziti.identity.KeyStoreIdentity
import io.netfoundry.ziti.net.internal.Sockets
import io.netfoundry.ziti.util.*
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.jce.PrincipalUtil
import java.io.File
import java.security.KeyStore
import java.security.PKCS12Attribute

internal object ZitiImpl : Logged by JULogged() {

    init {
        i("ZitiSDK version ${Version.version} @${Version.revision}(${Version.branch})")
    }

    internal fun loadContext(idFile: File, pwd: CharArray, alias: String?): ZitiContextImpl {
        val ks = loadKeystore(idFile, pwd)
        val idName = alias ?: findIdentity(ks)
        val id = KeyStoreIdentity(ks, idName)
        return ZitiContextImpl(id, true)
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
        contexts.add(ctx)
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
            val alias = PrincipalUtil.getSubjectX509Principal(certs[0]).getValues(BCStyle.CN)[0].toString().toLowerCase()

            val key = readKey(id.id.key.replace("pem:", "").reader())
            val keyEntry = KeyStore.PrivateKeyEntry(
                key, certs.toTypedArray(), setOf(
                    PKCS12Attribute(KeyStoreIdentity.Attributes.Controller.oid, id.ztAPI)
                )
            )
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
}