/*
 * Copyright (c) 2018-2025 NetFoundry Inc.
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

package org.openziti.integ

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.openziti.management.ApiClient
import org.openziti.management.JSON
import org.openziti.management.api.AuthenticationApi
import org.openziti.management.api.ConfigApi
import org.openziti.management.api.EnrollmentApi
import org.openziti.management.api.IdentityApi
import org.openziti.management.api.InformationalApi
import org.openziti.management.api.ServiceApi
import org.openziti.management.api.ServicePolicyApi
import org.openziti.management.model.*
import org.openziti.util.fingerprint
import org.openziti.util.parsePKCS7
import java.net.InetAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.time.Duration
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

internal object ManagementHelper {

    private val defaultURL = "https://${InetAddress.getLocalHost().hostName}:1280"

    private object DefaultCredentials: Authenticate() {
        init {
            username = "admin"
            password = "admin"
        }
    }

    private val trustAllssl: SSLContext = SSLContext.getInstance("TLS").apply {
        init(null, arrayOf(TrustAll), SecureRandom())
    }


    internal val sslContext: SSLContext by lazy {
        val clt = HttpClient.newBuilder().sslContext(trustAllssl).build()
        val req = HttpRequest.newBuilder(URI.create("$defaultURL/.well-known/est/cacerts")).build()
        val pkcs7 = clt.send(req, HttpResponse.BodyHandlers.ofByteArray()).body()
        parsePKCS7(pkcs7).makeSSL()
    }

    private val identityApi by lazy { IdentityApi(api) }
    private val enrollmentApi by lazy { EnrollmentApi(api) }
    private val serviceApi by lazy { ServiceApi(api) }
    private val spApi by lazy { ServicePolicyApi(api) }

    private object TrustAll : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    private fun List<X509Certificate>.makeSSL(): SSLContext {
        val ks = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            this@makeSSL.forEach {
                setCertificateEntry("${it.subjectX500Principal.name}-${it.fingerprint()}", it)
            }
        }

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(ks)
        }

        return SSLContext.getInstance("TLS").apply {
            init(null, tmf.trustManagers, SecureRandom())
        }
    }


    internal val api: ApiClient by lazy {
        ApiClient().apply {
            updateBaseUri(defaultURL)
            setHttpClientBuilder(
                HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .executor(Dispatchers.IO.asExecutor())
            )

            val mgmtUrl = InformationalApi(this@apply)
                .listVersion().get()
                .data.apiVersions?.get("edge-management")?.get("v1")?.path!!
            setBasePath(mgmtUrl)

            val token = AuthenticationApi(this@apply)
                .authenticate("password", DefaultCredentials)
                .get().data.token
            setRequestInterceptor { it.header("zt-session", token) }
        }
    }

    internal fun getIdentity(id: String) = identityApi.detailIdentity(id).waitFor()
    /**
     * returns enrollment JWT
     */
    internal fun createIdentity(name: String = "test-${System.nanoTime()}"): String {
        val identity = identityApi.createIdentity(
            IdentityCreate()
                .name(name)
                .isAdmin(false)
                .type(IdentityType.DEVICE)
        ).get().data!!


        val exp = OffsetDateTime.now().plusDays(1)
        val id = identity.id!!
        val enrollReq = EnrollmentCreate()
            .identityId(id)
            .method(EnrollmentCreate.MethodEnum.OTT)
            .expiresAt(exp)
        enrollmentApi.createEnrollment(enrollReq).waitFor()

        return identityApi.getIdentityEnrollments(id).waitFor().data.first().jwt!!
    }

    internal fun <T> CompletableFuture<T>.waitFor(timeout: Duration = Duration.of(5, ChronoUnit.SECONDS)): T =
        this.get(timeout.toMillis(), TimeUnit.MILLISECONDS)

    internal fun createService(
        srvName: String = "service-${System.nanoTime()}",
        dialRoles: List<String> = listOf("#all"),
        bindRoles: List<String> = listOf("#all"),
        configs: Map<String, Any>,
    ): String {

        val cfgIds = mutableListOf<String>()
        configs.forEach { (type, v) ->
            val typeId = ConfigApi(api).listConfigTypes(1,0, """name = "$type" """).waitFor().data.first().id
            val data = JSON.getDefault().mapper.convertValue(v, Map::class.java) as Map<String, *>
            val id = ConfigApi(api).createConfig(ConfigCreate().apply {
                name("$srvName-$type")
                configTypeId(typeId)
                data(data)
            }).waitFor().data!!.id!!
            cfgIds.add(id)
        }

        val srvId = serviceApi.createService(ServiceCreate().apply {
            name = srvName
            encryptionRequired = true
            configs(cfgIds)
        }).waitFor().data!!.id

        // dial policy
        spApi.createServicePolicy(ServicePolicyCreate().apply{
            name("$srvName-dial")
            type(DialBind.DIAL)
            serviceRoles(listOf("@$srvId"))
            identityRoles(dialRoles)
            semantic(Semantic.ANY_OF)
        }).waitFor()

        // dial policy
        spApi.createServicePolicy(ServicePolicyCreate().apply{
            name("$srvName-bind")
            type(DialBind.BIND)
            serviceRoles(listOf("@$srvId"))
            identityRoles(bindRoles)
            semantic(Semantic.ANY_OF)
        }).waitFor()
        return srvName
    }
}
