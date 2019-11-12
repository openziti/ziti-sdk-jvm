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

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.validate
import com.github.ajalt.clikt.parameters.types.file
import io.netfoundry.ziti.util.PrivateKeySigner
import io.netfoundry.ziti.util.getCACerts
import io.netfoundry.ziti.util.readCerts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PKCS12Attribute
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import java.util.*
import javax.net.ssl.*
import kotlin.text.Charsets.UTF_8

class Enroller(enrollUrl: String, val method: Method, val name: String, val caCerts: Collection<X509Certificate>) {

    enum class Method {
        ott,
        ottca,
        ca
    }

    val enrollmentURI = URI.create(enrollUrl)
    val controllerURI = enrollmentURI.resolve("/")

    companion object {
        val P256 = ECGenParameterSpec("secp256r1")

        @JvmStatic
        fun fromJWT(jwt: String): Enroller = runBlocking(Dispatchers.IO) {

            val zitiJwt = ZitiJWT.fromJWT(jwt)
            val enrollUrl = zitiJwt.enrollmentURL
            val controllerCA = getCACerts(URL(enrollUrl), zitiJwt.serverKey)

            val method = zitiJwt.method

            val name = zitiJwt.name ?: UUID.randomUUID().toString()

            Enroller(enrollUrl, Method.valueOf(method), name, controllerCA)
        }

        private class Cli : CliktCommand(name = "ziti-enroller") {
            val jwt by option(help = "Enrollment token (JWT file). Required").file().required().validate {
                it.exists() || throw FileNotFoundException("jwt[${it.path}] not found")
            }
            val out by option(help = "Output configuration file.").file().required().validate {
                it.exists() && throw FileAlreadyExistsException(it, null, "identity file already exists")
            }

            override fun run() {
                val enroller = fromJWT(jwt.readText())
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

    fun enroll(cert: KeyStore.Entry?, keyStore: KeyStore, name: String): String {

        val conn = enrollmentURI.toURL().openConnection() as HttpsURLConnection
        val ssl = getSSLContext(cert)

        val pke = when (method) {
            Method.ott ->
                enrollOtt(name, conn, keyStore, ssl)

            Method.ottca ->
                if (!(cert is KeyStore.PrivateKeyEntry)) throw Exception("client certificate is required for ottca enrollment")
                else enrollOttca(name, conn, cert, keyStore, ssl)

            else -> throw UnsupportedOperationException("method $method is not supported")
        }

        val alias = "ziti://${enrollmentURI.host}:${enrollmentURI.port}/${URLEncoder.encode(name, UTF_8.name())}"
        val protect = if (keyStore.type == "PKCS12") KeyStore.PasswordProtection(charArrayOf()) else null

        keyStore.setEntry(alias, pke, protect)

        for (ca in caCerts) {
            val caAlias = "ziti:${name}/${ca.serialNumber}"
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
        val body: ByteArray
        when {
            rc >= 400 -> {
                body = conn.errorStream.readBytes()
                val errors = JSONObject(body.toString(UTF_8))["errors"] as JSONArray
                val msg = (errors[0] as JSONObject).get("msg")?.toString()
                msg.let { throw IllegalArgumentException(it) }
            }
            else -> return cert
        }
    }

    private fun enrollOtt(alias: String, conn: HttpsURLConnection, keyStore: KeyStore, ssl: SSLContext): KeyStore.PrivateKeyEntry {
        val name = X500Name("CN=${alias}")

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
        conn.setRequestProperty("Content-Type", "text/plain")
        conn.setRequestProperty("Content-Length", pemBytes.size.toString())
        conn.outputStream.write(pemBytes)
        conn.outputStream.flush()


        val rc = conn.responseCode
        val body: ByteArray
        when {
            rc >= 400 -> {
                body = conn.errorStream.readBytes()
                val error = JSONObject(body.toString(UTF_8)).getJSONObject("error")
                val msg = error.get("message")?.toString()
                msg.let { throw IllegalArgumentException(it) }
            }
            else -> {
                val certs = readCerts(conn.inputStream.reader()).toTypedArray()
                return  KeyStore.PrivateKeyEntry(kp.private, certs, emptySet())
            }
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
                ks.setCertificateEntry("ca-${it.subjectX500Principal}", it)
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