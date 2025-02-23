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

package org.openziti.util

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.bouncycastle.util.io.pem.PemWriter
import java.io.OutputStream
import java.io.Reader
import java.io.StringWriter
import java.net.Socket
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.net.ssl.*

internal class KeyTrustManager(val key: Key) : X509TrustManager {
    override fun checkServerTrusted(certs: Array<out X509Certificate>, authType: String) {
        check(certs[0].publicKey.equals(key)) {
            throw CertificateException("untrusted server")
        }
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    override fun checkClientTrusted(p0: Array<out X509Certificate>, p1: String) = TODO("not used")
}

/**
 * [KeyManager] that narrows key store to a single identity
 */
internal class AliasKeyManager(val alias: String, delegate: KeyManager) : X509ExtendedKeyManager() {
    val x509 = delegate as X509KeyManager
    override fun getClientAliases(keyType: String?, issuers: Array<out Principal>?): Array<String> = arrayOf(alias)

    override fun getServerAliases(keyType: String?, issuers: Array<out Principal>?): Array<String> =
        TODO("not implemented")

    override fun chooseServerAlias(keyType: String?, issuers: Array<out Principal>?, socket: Socket?) =
        TODO("not implemented")

    override fun getCertificateChain(alias: String?): Array<X509Certificate> = x509.getCertificateChain(alias)

    override fun getPrivateKey(alias: String?): PrivateKey? = x509.getPrivateKey(alias)

    override
    fun chooseEngineClientAlias(keyType: Array<out String>?, issuers: Array<out Principal>?, engine: SSLEngine?) =
        alias

    override
    fun chooseClientAlias(keyType: Array<out String>?, issuers: Array<out Principal>?, socket: Socket?) = alias

    companion object {
        fun from(alias: String, kms: Array<KeyManager>): Array<X509KeyManager> =
            kms.map { AliasKeyManager(alias, it) }.toTypedArray()
    }
}


internal val cf = CertificateFactory.getInstance("X.509")

internal fun readCerts(pem: String) = readCerts(pem.removePrefix("pem:").reader())

internal fun readCerts(pemInput: Reader) = PemReader(pemInput).use { reader ->
    generateSequence { reader.readPemObject() }
        .filter { it.type == "CERTIFICATE" }
        .map { cf.generateCertificate(it.content.inputStream()) as X509Certificate }
        .toList()
}

internal fun readKey(pem: String) = readKey(pem.removePrefix("pem:").reader())

fun readKey(input: Reader): PrivateKey {
    val parser = PEMParser(input)
    val pk = when(val po = parser.readObject()) {// as PEMKeyPair
        is PEMKeyPair -> JcaPEMKeyConverter().getKeyPair(po).private
        is PrivateKeyInfo -> JcaPEMKeyConverter().getPrivateKey(po)
        else -> error("unsupported key format")
    }

    return pk
}

internal class PrivateKeySigner(val key: PrivateKey, val sigAlg: String) : ContentSigner {
    val sig = Signature.getInstance(sigAlg)

    init {
        sig.initSign(key)
    }

    override fun getAlgorithmIdentifier() = DefaultSignatureAlgorithmIdentifierFinder().find(sigAlg)

    override fun getOutputStream() = object : OutputStream() {
        override fun write(b: Int) = sig.update(b.toByte())
        override fun write(b: ByteArray) = sig.update(b)
        override fun write(b: ByteArray, off: Int, len: Int) = sig.update(b, off, len)
    }

    override fun getSignature(): ByteArray = sig.sign()
}

internal fun parsePKCS7(bundle: ByteArray) =
    Base64.getMimeDecoder().decode(bundle).inputStream().use { input ->
        cf.generateCertificates(input)
    }.filterIsInstance<X509Certificate>()

internal fun ByteArray.toHEXString(): String = joinToString("") { "%02x".format(it) }

internal fun X509Certificate.fingerprint() =
    encoded.inputStream().use { stream ->
        DigestInputStream(stream, MessageDigest.getInstance("SHA-1")).use {
            it.readAllBytes().toHEXString()
        }
    }

internal fun ByteArray.toPEM(name: String): String = StringWriter().use { out ->
    PemWriter(out).use {
        it.writeObject(PemObject(name, this))
    }
    out.toString()
}

internal fun PrivateKey.toPEM() = PKCS8EncodedKeySpec(this.encoded).encoded.toPEM("PRIVATE KEY")
internal fun X509Certificate.toPEM() = encoded.toPEM("CERTIFICATE")
internal fun PKCS10CertificationRequest.toPEM() = encoded.toPEM("CERTIFICATE REQUEST")

