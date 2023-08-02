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

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import org.openziti.util.*
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import java.net.URLEncoder
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import javax.net.ssl.*
import kotlin.text.Charsets.UTF_8

class Enroller(
    val enrollmentURL: URL,
    val method: Method,
    val name: String,
    val caCerts: Collection<X509Certificate>
) : Logged by ZitiLog("ziti-enroler") {

    enum class Method {
        ott,
        ottca,
        ca
    }

    companion object : Logged by ZitiLog("ziti-enroller") {
        val P256 = ECGenParameterSpec("secp256r1")

        @JvmStatic
        fun fromJWT(jwt: String): Enroller = runBlocking(Dispatchers.IO) {

            val zitiJwt = ZitiJWT.fromJWT(jwt)
            d("enrolling ctrl[${zitiJwt.controller}] name[${zitiJwt.name}] method[${zitiJwt.method}]")

            val controllerCA = getCACerts(zitiJwt.controller, zitiJwt.serverKey)
            d("received ${controllerCA.size} certificates")

            val method = zitiJwt.method
            val name = zitiJwt.name

            Enroller(zitiJwt.enrollmentURL, Method.valueOf(method), name, controllerCA)
        }
    }

    fun enroll(cert: KeyStore.Entry?, keyStore: KeyStore, n: String): String {

        val conn = enrollmentURL.openConnection() as HttpsURLConnection
        val ssl = getSSLContext(cert)

        val pke = when (method) {
            Method.ott ->
                enrollOtt(n, conn, keyStore, ssl)

            Method.ottca ->
                if (!(cert is KeyStore.PrivateKeyEntry)) throw Exception("client certificate is required for ottca enrollment")
                else enrollOttca(n, conn, cert, keyStore, ssl)

            else -> throw UnsupportedOperationException("method $method is not supported")
        }

        val alias = "ziti://${enrollmentURL.host}:${enrollmentURL.port}/${URLEncoder.encode(this.name, UTF_8.name())}"
        val protect = if (keyStore.type == "PKCS12") KeyStore.PasswordProtection(charArrayOf()) else null

        keyStore.setEntry(alias, pke, protect)

        for (ca in caCerts) {
            val caAlias = "ziti:${this.name}/${ca.serialNumber}"
            keyStore.setCertificateEntry(caAlias, ca)
        }
        return alias
    }

    private fun enrollOttca(
        alias: String,
        conn: HttpsURLConnection,
        cert: KeyStore.PrivateKeyEntry,
        keyStore: KeyStore,
        ssl: SSLContext
    ): KeyStore.PrivateKeyEntry {

        conn.doInput = true
        conn.doOutput = true
        conn.setRequestProperty("Content-Type", "text/plain")
        conn.setRequestProperty("Content-Length", "0")

        conn.sslSocketFactory = ssl.socketFactory

        conn.outputStream.write(byteArrayOf())
        conn.outputStream.flush()

        val rc = conn.responseCode
        when {
            rc >= 400 -> {
                val json = Gson().fromJson(conn.errorStream.reader(UTF_8), JsonObject::class.java)

                val errors = json.getAsJsonArray("errors")

                for (e in errors) {
                    e.asJsonObject.get("msg")?.let {
                        throw IllegalArgumentException(it.toString())
                    }
                }

                throw IllegalArgumentException(errors.asString)
            }
            else -> return cert
        }
    }

    private fun enrollOtt(
        alias: String,
        conn: HttpsURLConnection,
        keyStore: KeyStore,
        ssl: SSLContext
    ): KeyStore.PrivateKeyEntry {
        val name = X500Name("CN=${name}")

        val kpg = KeyPairGenerator.getInstance("EC")
        kpg.initialize(P256)
        val kp = kpg.generateKeyPair()

        val csrBuildr = JcaPKCS10CertificationRequestBuilder(name, kp.public)
        val csr = csrBuildr.build(PrivateKeySigner(kp.private, "SHA256withECDSA"))
        val csrPem = PemObject("CERTIFICATE REQUEST", csr.encoded)
        val pem = ByteArrayOutputStream()
        val w = PemWriter(OutputStreamWriter(pem))
        w.writeObject(csrPem)
        w.flush()

        val pemBytes = pem.toByteArray()

        conn.sslSocketFactory = ssl.socketFactory
        conn.doInput = true
        conn.doOutput = true
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("Content-Type", "text/plain")
        conn.setRequestProperty("Content-Length", pemBytes.size.toString())
        conn.outputStream.write(pemBytes)
        conn.outputStream.flush()

        if (conn.responseCode >= 400) {
            val error = Gson().fromJson(conn.errorStream.reader(UTF_8), JsonObject::class.java).getAsJsonObject("error")
            throw IllegalArgumentException(error.get("message")?.toString() ?: "unknown error" )
        } else {
            val ct = conn.getHeaderField("Content-Type").lowercase()
            val certs = when (ct) {
                "application/x-pem-file" -> {
                    readCerts(conn.inputStream.reader()).toTypedArray()
                }
                "application/json" -> {
                    val data = Gson().fromJson(conn.inputStream.reader(UTF_8), JsonObject::class.java).getAsJsonObject("data")
                    readCerts(data.getAsJsonPrimitive("cert").asString).toTypedArray()
                }
                else -> error("Invalid content-type: $ct")
            }
            return KeyStore.PrivateKeyEntry(kp.private, certs)
        }
    }

    fun getSSLContext(clientCert: KeyStore.Entry?): SSLContext {
        val ks = KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null) }
        clientCert?.let {
            ks.setEntry("client-cert", clientCert, null)
        }
        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
            init(ks, charArrayOf())
        }

        var tms: Array<TrustManager>? = null

        if (!caCerts.isEmpty()) {
            caCerts.forEach {
                ks.setCertificateEntry("ca-${it.serialNumber}", it)
            }

            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(ks)
            }

            tms = tmf.trustManagers
        }

        val sc = SSLContext.getInstance("TLSv1.2").apply {
            init(kmf.keyManagers, tms, SecureRandom())
        }

        return sc
    }
}