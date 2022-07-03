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

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x500.style.BCStyle
import org.openziti.util.ZitiLog
import org.openziti.util.readCerts
import org.openziti.util.readKey
import java.io.File
import java.io.InputStream
import java.net.URI
import java.security.KeyStore

/**
 *
 */
internal fun findIdentityAlias(ks: KeyStore): String {
    for (a in ks.aliases()) {
        if (ks.isKeyEntry(a)) {
            return a
        }
    }

    error("no suitable key entry")
}

internal fun keystoreFromConfig(id: IdentityConfig): KeyStore {
    val ks = KeyStore.getInstance("PKCS12")
    ks.load(null)

    val certs = readCerts(id.id.cert.replace("pem:", ""))
    val ztAPI = URI.create(id.ztAPI)
    val name = X500Name(certs[0].subjectDN.name).getRDNs(BCStyle.CN)[0].first.value.toASN1Primitive().toString()
    val alias = "ziti://${ztAPI.host}:${ztAPI.port}/${name}"

    val key = readKey(id.id.key.replace("pem:", "").reader())
    val keyEntry = KeyStore.PrivateKeyEntry(key, certs.toTypedArray())
    ks.setEntry(alias, keyEntry, KeyStore.PasswordProtection(charArrayOf()))

    id.id.ca?.let {
        val cacerts = readCerts(it.replace("pem:", ""))
        for (ca in cacerts) {
            val caAlias = "${alias}-ca-${ca.serialNumber}"
            ks.setCertificateEntry(caAlias, ca)
        }
    }

    return ks

}
internal fun loadKeystore(i: ByteArray): KeyStore {
    val log = ZitiLog()

    try {
        val id = IdentityConfig.load(i.inputStream().reader())
        return keystoreFromConfig(id)
    } catch (ex: Exception) {
        log.w("failed to load identity config: ${ex.localizedMessage}")
    }

    throw IllegalArgumentException("unsupported format")
}

internal fun loadKeystore(f: File, pwd: CharArray): KeyStore {
    val log = ZitiLog()

    if (!f.exists() || !f.canRead()) {
        throw IllegalArgumentException("Failed to parse keystore.  ${f.absolutePath} does not exist or can not be read")
    }

    val ks = loadKeystore(f.inputStream(), pwd, log)
    if (ks != null) {
        return ks
    }

    log.t(
        "Trying to load it as a plain identity config"
    )
    try {
        val id = IdentityConfig.load(f)
        return keystoreFromConfig(id)
    } catch (ex: Exception) {
        log.w("failed to load identity config: ${ex.localizedMessage}")
    }

    throw IllegalArgumentException("unsupported format")
}

internal fun loadKeystore(stream: InputStream, pwd: CharArray): KeyStore {
    val log = ZitiLog()
    val ks = loadKeystore(stream, pwd, log)
    if (ks != null) {
        return ks
    }

    throw IllegalArgumentException("unsupported format")
}

internal fun loadKeystore(stream: InputStream, pwd: CharArray, log: ZitiLog): KeyStore? {
    val ks = KeyStore.getInstance("PKCS12")
    return try {
        ks.load(stream, pwd)
        ks
    } catch (ex: Exception) {
        log.t(
            "Failed to parse identity file as a keystore: ${ex.localizedMessage}"
        )
        //w("failed to load $f as PKCS12 key store: $ex")
        null
    }
}
