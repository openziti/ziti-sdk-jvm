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
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.pkcs.PKCS10CertificationRequest
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder
import org.openziti.Enrollment
import org.openziti.IdentityConfig
import org.openziti.edge.ApiClient
import org.openziti.edge.ApiException
import org.openziti.edge.api.AuthenticationApi
import org.openziti.edge.api.ControllersApi
import org.openziti.edge.api.InformationalApi
import org.openziti.edge.model.ApiErrorEnvelope
import org.openziti.edge.model.EnrollmentCertsEnvelope
import org.openziti.util.*
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.ECGenParameterSpec
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import javax.net.ssl.SSLContext

internal class Enroller(
    private val jwt: ZitiJWT
) : Enrollment, Logged by ZitiLog("ziti-enroll") {

    private val http: HttpClient.Builder = ApiClient.createDefaultHttpClientBuilder().apply {
        executor(Dispatchers.IO.asExecutor())
        sslContext(SSLContext.getInstance("TLSv1.2").apply {
            init(null, arrayOf(KeyTrustManager(jwt.serverKey)), SecureRandom())
        })
    }

    private val api: ApiClient =
        ApiClient(http, ApiClient.createDefaultObjectMapper(), jwt.controller.toString())

    val name = jwt.name
    val token = jwt.token

    private val caCerts: Collection<X509Certificate>

    init {
        val wellKnownCertsReq = HttpRequest.newBuilder(jwt.controller.resolve("/.well-known/est/cacerts")).build()
        val pkcs7 = http.build().send(wellKnownCertsReq, HttpResponse.BodyHandlers.ofString()).body()
        caCerts = Base64.getMimeDecoder().decode(pkcs7).inputStream().use {
                CertificateFactory.getInstance("X.509").generateCertificates(it)
        }.filterIsInstance<X509Certificate>()

        val path = with(InformationalApi(api).listVersion().join()) {
            data.apiVersions?.get("edge")?.get("v1")?.path
        }
        api.setBasePath(path!!)
    }

    companion object : Logged by ZitiLog("ziti-enroller") {
        val P256 = ECGenParameterSpec("secp256r1")

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
        val uri = URI.create(api.baseUri + "/enroll?method=${jwt.method}&token=$token")

        val req = HttpRequest.newBuilder(uri)
            .header("content-type", "application/x-pem-file")
            .header("accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(csr.toPEM()))

        val resp = http.build().send(req.build(), HttpResponse.BodyHandlers.ofString())

        val body = resp.body()
        if (resp.statusCode() != 200) {
            val err = api.objectMapper.readValue(body, ApiErrorEnvelope::class.java)
            throw ApiException(resp.statusCode(), err.error.message)
        }

        val cert = api.objectMapper.readValue(body, EnrollmentCertsEnvelope::class.java).data!!.cert!!

        val controllers = listControllers(api.baseUri, kp.private, readCerts(cert), caCerts)

        return IdentityConfig(
            controller = api.baseUri,
            controllers = controllers,
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

        val ssl = makeSSLContext(pk, certs, caCerts)
        http.sslContext(ssl)

        // TODO use generated enrollment API when it is fixed
        // https://github.com/openziti/ziti/issues/2796
        val uri = URI.create(api.baseUri + "/enroll?method=${jwt.method}&token=$token")

        val req = HttpRequest.newBuilder(uri)
            .header("accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofByteArray(byteArrayOf()))

        val resp = http.build().send(req.build(), HttpResponse.BodyHandlers.ofString())

        val body = resp.body()
        if (resp.statusCode() != 200) {
            val err = api.objectMapper.readValue(body, ApiErrorEnvelope::class.java)
            throw ApiException(resp.statusCode(), err.error.message)
        }

        val controllers = listControllers(api.baseUri, pk, certs, caCerts)

        return IdentityConfig(
            controller = api.baseUri,
            controllers = controllers,
            id = IdentityConfig.Id(
                key = key,
                cert = cert,
                ca = caCerts.joinToString(separator = "\n"){ it.toPEM() }),)
    }

    private fun listControllers(ctrl: String, key: PrivateKey, certs: Collection<X509Certificate>, ca: Collection<X509Certificate>): List<String> {
        val ssl = makeSSLContext(key, certs, ca)
        val cb = ApiClient.createDefaultHttpClientBuilder().sslContext(ssl)
        val api = ApiClient(cb, ApiClient.createDefaultObjectMapper(), ctrl)
        val resp  = AuthenticationApi(api).authenticate("cert", null).thenApply {
            api.requestInterceptor = Consumer { req ->
                req.header("zt-session", it.data.token)
            }
        }.thenCompose {
            ControllersApi(api).listControllers(10, 0, null)
        }.get(3, TimeUnit.SECONDS)

        if (resp.data.isEmpty())
            return listOf(ctrl)

        return resp.data.mapNotNull { it.apiAddresses?.get("edge-client") }.flatten()
            .filter {it.version == "v1" }.mapNotNull { it.url }
    }
}