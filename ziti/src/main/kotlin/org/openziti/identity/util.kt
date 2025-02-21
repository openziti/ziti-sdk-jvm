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

import org.openziti.IdentityConfig
import org.openziti.util.ZitiLog
import org.openziti.util.fingerprint
import org.openziti.util.readCerts
import org.openziti.util.readKey
import java.io.File
import java.io.InputStream
import java.net.URI
import java.security.KeyStore
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

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

    val certs = readCerts(id.id.cert!!)
    val ztAPI = URI.create(id.controller)

    val name = certs[0].subjectX500Principal.name
    name.split(delimiters = arrayOf(","))
        .map{ it.trim() }
        .filter { it.startsWith("cn=", true) }
        .map { it.split("=", limit = 2)[1] }

    val alias = "ziti://${ztAPI.host}:${ztAPI.port}/${name}"

    val key = readKey(id.id.key!!)
    val keyEntry = KeyStore.PrivateKeyEntry(key, certs.toTypedArray())
    ks.setEntry(alias, keyEntry, KeyStore.PasswordProtection(charArrayOf()))

    val caCerts = readCerts(id.id.ca)
    for (ca in caCerts) {
        val caAlias = "${alias}-ca-${ca.serialNumber}"
        ks.setCertificateEntry(caAlias, ca)
    }

    return ks

}
internal fun loadKeystore(i: ByteArray): KeyStore {
    val log = ZitiLog()

    try {
        val id = IdentityConfig.load(i.inputStream())
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
    val bytes = stream.readNBytes(16 * 1024)
    val ks = loadKeystore(bytes.inputStream(), pwd, log)
    if (ks != null) {
        return ks
    }

    val id = IdentityConfig.load(bytes.inputStream())
    return keystoreFromConfig(id)
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

internal fun makeSSLContext(key: PrivateKey?, certs: Collection<X509Certificate>?, ca: Collection<X509Certificate>): SSLContext {
    val ks = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
        load(null, null)
        key?.let {
            setKeyEntry("identity", it, null, certs?.toTypedArray())
        }
        for (c in ca) {
            setCertificateEntry("${c.subjectX500Principal.name}-${c.fingerprint()}", c)
        }
    }

    val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
        init(ks, null)
    }

    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
        init(ks)
    }

    return SSLContext.getInstance("TLS").apply {
        init(kmf.keyManagers, tmf.trustManagers, SecureRandom())
    }
}
