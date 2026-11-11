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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.openziti.Enrollment
import org.openziti.IdentityConfig
import org.openziti.api.ZitiAuthenticator
import org.openziti.api.http.ControllerHttp
import org.openziti.api.http.EdgeClient
import org.openziti.api.http.ZitiHttpException
import org.openziti.api.http.await
import org.openziti.api.http.edgeMapper
import org.openziti.api.http.queryOf
import org.openziti.edge.model.ApiErrorEnvelope
import org.openziti.edge.model.CurrentApiSessionDetailEnvelope
import org.openziti.edge.model.EnrollmentCertsEnvelope
import org.openziti.edge.model.ListControllersEnvelope
import org.openziti.edge.model.ListVersionEnvelope
import org.openziti.util.*
import java.io.File
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import java.time.Duration
import java.util.*
import javax.net.ssl.SSLContext

internal class Enroller(
    private val jwt: ZitiJWT
) : Enrollment, Logged by ZitiLog("ziti-enroll") {

    // Until a certificate is issued the controller is only known by the key in the JWT.
    private var http: OkHttpClient = ControllerHttp.client(
        SSLContext.getInstance("TLSv1.2").apply {
            init(null, arrayOf(KeyTrustManager(jwt.serverKey)), SecureRandom())
        }
    )

    private var edge = EdgeClient(http, jwt.controller.toString())

    val name = jwt.name
    val token = jwt.token

    private val caCerts: Collection<X509Certificate>

    init {
        // no `Accept` header, as before: the answer is not JSON
        val wellKnownCertsReq = Request.Builder()
            .url(jwt.controller.resolve("/.well-known/est/cacerts").toString().toHttpUrl())
            .build()
        val pkcs7 = runBlocking {
            http.newCall(wellKnownCertsReq).await().use { resp ->
                val text = resp.body.string()
                if (!resp.isSuccessful) throw ZitiHttpException(resp.code, text)
                text
            }
        }
        caCerts = Base64.getMimeDecoder().decode(pkcs7).inputStream().use {
                CertificateFactory.getInstance("X.509").generateCertificates(it)
        }.filterIsInstance<X509Certificate>()

        val path = runBlocking { edge.get<ListVersionEnvelope>("/version") }
            .data.apiVersions?.get("edge")?.get("v1")?.path
        edge.setBasePath(path!!)
    }

    companion object : Logged by ZitiLog("ziti-enroller") {
        val P256 = ECGenParameterSpec("secp256r1")

        // enrolling must not hang on a controller that stops answering
        private val LIST_CONTROLLERS_TIMEOUT = Duration.ofSeconds(3)

        @JvmStatic
        fun fromJWT(jwt: String): Enroller = runBlocking(Dispatchers.IO) {

            val f = File(jwt)

            val zitiJwt = ZitiJWT.fromJWT(if(f.exists()) f.readText() else jwt)
            d("enrolling ctrl[${zitiJwt.controller}] name[${zitiJwt.name}] method[${zitiJwt.method}]")

            Enroller(zitiJwt)
        }

        private fun generateKeyPair() = KeyPairGenerator.getInstance("EC").let {
            it.initialize(P256)
            it.generateKeyPair()
        }

        private fun createCsr(name: String, keyPair: KeyPair): PKCS10CertificationRequest {
            val csrBuildr = JcaPKCS10CertificationRequestBuilder(X500Name("CN=$name"), keyPair.public)
            return csrBuildr.build(PrivateKeySigner(keyPair.private, "SHA256withECDSA"))
        }
    }

    override fun getMethod(): Enrollment.Method = jwt.method

    override fun requiresClientCert(): Boolean = when (jwt.method){
        Enrollment.Method.ca, Enrollment.Method.ottca -> true
        else -> false
    }

    override fun enroll(): IdentityConfig {
        require(!requiresClientCert()) {
            "cannot be used with `${jwt.method}' enrollment method"
        }

        val kp = generateKeyPair()
        val csr = createCsr(token, kp)

        // TODO use generated enrollment API when it is fixed
        // https://github.com/openziti/ziti/issues/2796
        val cert = enrolling {
            edge.post<EnrollmentCertsEnvelope>(
                "/enroll",
                csr.toPEM(),
                queryOf("method" to jwt.method, "token" to token),
                mapOf("Content-Type" to "application/x-pem-file")
            )
        }.data!!.cert!!

        val controllers = listControllers(edge.baseUri, kp.private, readCerts(cert), caCerts)

        return IdentityConfig(
            controller = edge.baseUri,
            apiEndpoints = controllers,
            id = IdentityConfig.Id(
                key = kp.private.toPEM(),
                cert = cert,
                ca = caCerts.joinToString(separator = "\n"){ it.toPEM() }),)
    }

    override fun enroll(key: String, cert: String): IdentityConfig {
        require(requiresClientCert()) {
            "cannot be used with `${jwt.method}' enrollment method"
        }

        val pk = readKey(key)
        val certs = readCerts(cert)

        // from here on the controller is validated by the network CA and the new identity is presented
        http = ControllerHttp.client(makeSSLContext(pk, certs, caCerts))
        edge = edge.withClient(http)

        // TODO use generated enrollment API when it is fixed
        // https://github.com/openziti/ziti/issues/2796
        enrolling {
            edge.send("POST", "/enroll", queryOf("method" to jwt.method, "token" to token))
        }

        val controllers = listControllers(edge.baseUri, pk, certs, caCerts)

        return IdentityConfig(
            controller = edge.baseUri,
            apiEndpoints = controllers,
            id = IdentityConfig.Id(
                key = key,
                cert = cert,
                ca = caCerts.joinToString(separator = "\n"){ it.toPEM() }),)
    }

    // A rejected enrollment carries the controller's explanation in the error envelope.
    private fun <T> enrolling(call: suspend () -> T): T = try {
        runBlocking { call() }
    } catch (e: ZitiHttpException) {
        val explanation = runCatching {
            edgeMapper.readValue(e.responseBody, ApiErrorEnvelope::class.java).error.message
        }.getOrNull()
        throw ZitiHttpException(e.code, e.responseBody, explanation)
    }

    private fun listControllers(ctrl: String, key: PrivateKey, certs: Collection<X509Certificate>, ca: Collection<X509Certificate>): List<String> {
        val ssl = makeSSLContext(key, certs, ca)
        val api = EdgeClient(ControllerHttp.client(ssl, LIST_CONTROLLERS_TIMEOUT), ctrl)
        val resp = runBlocking {
            val session = api.post<CurrentApiSessionDetailEnvelope>("/authenticate", query = queryOf("method" to "cert")).data
            api.token = ZitiAuthenticator.ZitiAccessToken(
                ZitiAuthenticator.TokenType.API_SESSION, session.token, session.expiresAt
            )
            api.get<ListControllersEnvelope>("/controllers", queryOf("limit" to 10, "offset" to 0))
        }

        if (resp.data.isEmpty())
            return listOf(ctrl)

        return resp.data.mapNotNull { it.apiAddresses?.get("edge-client") }.flatten()
            .filter {it.version == "v1" }.mapNotNull { it.url }
    }
}
