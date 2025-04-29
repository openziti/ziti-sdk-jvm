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

package org.openziti.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.future.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.openziti.edge.ApiClient
import org.openziti.edge.api.AuthenticationApi
import org.openziti.edge.api.CurrentApiSessionApi
import org.openziti.edge.model.Authenticate
import org.openziti.edge.model.EnvInfo
import org.openziti.edge.model.SdkInfo
import org.openziti.impl.ZitiImpl
import org.openziti.util.Logged
import org.openziti.util.SystemInfoProvider
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.OffsetDateTime
import java.util.Base64
import java.util.function.Consumer
import javax.net.ssl.SSLContext
import kotlin.random.Random


interface ZitiAuthenticator {
    enum class TokenType {
        BEARER, API_SESSION
    }

    data class ZitiAccessToken(
        val type: TokenType,
        val token: String,
        val expiration: OffsetDateTime
    )
    suspend fun login(): ZitiAccessToken
    suspend fun refresh(): ZitiAccessToken
}

internal fun authenticator(ep: String, ssl: SSLContext, oidc: Boolean): ZitiAuthenticator =
    if (oidc)
        InternalOIDC(ep, ssl)
    else
        LegacyAuth(ep, ssl)

class LegacyAuth(val ep: String, val ssl: SSLContext) : ZitiAuthenticator, Logged by ZitiLog() {

    private val auth = Authenticate().apply {
        val info = SystemInfoProvider().getSystemInfo()
        sdkInfo = SdkInfo()
            .type("ziti-sdk-java")
            .version(Version.version)
            .branch(Version.branch)
            .revision(Version.revision)
            .appId(ZitiImpl.appId)
            .appVersion(ZitiImpl.appVersion)
        envInfo = EnvInfo()
            .arch(info.arch)
            .os(info.os)
            .osRelease(info.osRelease)
            .osVersion(info.osVersion)
        configTypes = listOf("all")
    }

    private val http = HttpClient.newBuilder()
        .sslContext(ssl)
        .executor(Dispatchers.IO.asExecutor())

    private val api = ApiClient().apply {
        setHttpClientBuilder(http)
        updateBaseUri(ep)
    }

    override suspend fun login(): ZitiAuthenticator.ZitiAccessToken {
        val authApi = AuthenticationApi(api)
        val session = authApi.authenticate("cert", auth).await()
        api.requestInterceptor = Consumer {
            req -> req.header("zt-session", session.data.token)
        }
        return ZitiAuthenticator.ZitiAccessToken(
            ZitiAuthenticator.TokenType.API_SESSION,
            session.data.token,
            session.data.expiresAt
        )
    }

    override suspend fun refresh(): ZitiAuthenticator.ZitiAccessToken {
        val currentApiSessionApi = CurrentApiSessionApi(api)
        val session = currentApiSessionApi.currentAPISession.await()
        return ZitiAuthenticator.ZitiAccessToken(
            ZitiAuthenticator.TokenType.API_SESSION,
            session.data.token,
            session.data.expiresAt
        )
    }
}

class InternalOIDC(val ep: String, ssl: SSLContext): ZitiAuthenticator, Logged by ZitiLog() {

    companion object {
        const val CLIENT_ID = "openziti"
        const val internalRedirect = "http://localhost:8080/auth/callback"
        val Encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()
        const val DISCOVERY = "/oidc/.well-known/openid-configuration"
        const val TOKEN_EXCHANGE_GRANT = "urn:ietf:params:oauth:grant-type:token-exchange"
    }


    private val http: HttpClient = HttpClient.newBuilder()
        .sslContext(ssl)
        .followRedirects(HttpClient.Redirect.NEVER)
        .executor(Dispatchers.IO.asExecutor())
        .build()
    lateinit var tokens: JsonObject

    private val config by lazy {
        loadConfig()
    }

    private fun formatForm(params: Map<String, String>): String = params.entries.joinToString("&") {
        "${it.key}=${URLEncoder.encode(it.value, StandardCharsets.UTF_8)}"
    }

    private suspend fun startAuth(authEndpoint: String, challenge: String, state: String): URI {
        val form = mapOf(
            "response_type" to "code",
            "client_id" to CLIENT_ID,
            "redirect_uri" to internalRedirect,
            "scope" to "openid offline_access",
            "state" to state,
            "code_challenge" to challenge,
            "code_challenge_method" to "S256"
        )

        val body = formatForm(form)
        i("body: $body")
        val uri = URI.create(authEndpoint)
        val req = HttpRequest.newBuilder(uri)
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()

        i{"sending auth request $req"}
        val resp = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).await()

        if (resp.statusCode() / 100 != 3 && resp.headers().firstValue("Location").isEmpty) {
            throw Exception("Unexpected login auth response: ${resp.statusCode()} ${resp.body()}")
        }

        val path = resp.headers().firstValue("Location").get()
        return URI.create(ep).resolve(path)
    }

    private suspend fun login(loginURI: URI): URI {
        val req = HttpRequest.newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .uri(loginURI).POST(HttpRequest.BodyPublishers.noBody()).build()

        val resp = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).await()
        return URI.create(resp.headers().firstValue("Location").get())
    }

    private suspend fun getCode(codeURI: URI): Pair<String, String> {
        val req = HttpRequest.newBuilder()
            .header("Accept", "application/json")
            .uri(codeURI).GET().build()

        val resp = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).await()

        val redirectUri = resp.headers().firstValue("Location").get().run { URI.create(this) }

        val query = redirectUri.query.split("&").associate {
            val (k, v) = it.split("=", limit = 2)
            k to v
        }
        return query["code"]!! to query["state"]!!
    }

    private suspend fun getTokens(ep: URI, code: String, codeVerifier: String): JsonObject {
        val body = formatForm(
            mapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "client_id" to CLIENT_ID,
                "redirect_uri" to internalRedirect,
                "code_verifier" to codeVerifier
            )
        )
        val req = HttpRequest.newBuilder().header("Accept", "application/json")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .uri(ep)
            .POST(HttpRequest.BodyPublishers.ofString(body)).build()

        val tokenResp = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).await()
        return Json.parseToJsonElement(tokenResp.body()).jsonObject
    }

    override suspend fun login(): ZitiAuthenticator.ZitiAccessToken {
        val codeVerifier = Encoder.encodeToString(Random.Default.nextBytes(40))
        val challenge = Encoder.encodeToString(
            MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
        )
        val state = Encoder.encodeToString(Random.Default.nextBytes(30))


        val authEndpoint = config["authorization_endpoint"]?.jsonPrimitive?.content
            ?: throw Exception("Missing authorization endpoint in OIDC config")
        val tokenEndpoint = config["token_endpoint"]?.jsonPrimitive?.content
            ?: throw Exception("Missing token endpoint in OIDC config")

        val loginURI = startAuth(authEndpoint, challenge, state)
        val codeURI = login(loginURI)
        val (code, st) = getCode(codeURI)

        require(st == state){ "OIDC state mismatch" }

        tokens = getTokens(URI.create(tokenEndpoint), code, codeVerifier)
        d{ "OIDC tokens: $tokens" }

        val accessToken = tokens["access_token"]?.jsonPrimitive?.content
            ?: throw Exception("Missing access token in OIDC response")
        val exp = OffsetDateTime.now().plusSeconds(tokens["expires_in"]?.jsonPrimitive?.content?.toLong() ?: 600)
        return ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.BEARER, accessToken, exp)
    }

    override suspend fun refresh(): ZitiAuthenticator.ZitiAccessToken {
        val refreshToken = tokens.get("refresh_token")?.jsonPrimitive?.content

        if (refreshToken == null) return login()

        val form = mapOf(
            "grant_type" to TOKEN_EXCHANGE_GRANT,
            "requested_token_type" to "urn:ietf:params:oauth:token-type:refresh_token",
            "subject_token_type" to   "urn:ietf:params:oauth:token-type:refresh_token",
            "subject_token" to  refreshToken,
        )

        val req = HttpRequest.newBuilder()
            .uri(config["token_endpoint"]?.jsonPrimitive?.content?.let { URI.create(it) })
            .header("Accept", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(formatForm(form)))
            .build()

        val resp = http.sendAsync(req, HttpResponse.BodyHandlers.ofString()).await()

        if (resp.statusCode() != 200) {
            return login()
        }

        tokens = Json.parseToJsonElement(resp.body()).jsonObject
        val accessToken = tokens["access_token"]?.jsonPrimitive?.content
            ?: throw Exception("Missing access token in OIDC response")
        val exp = OffsetDateTime.now().plusSeconds(tokens["expires_in"]?.jsonPrimitive?.content?.toLong() ?: 600)
        return ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.BEARER, accessToken, exp)
    }

    private fun loadConfig(): JsonObject {
        val url = URI.create(ep).resolve(DISCOVERY)

        val request = HttpRequest.newBuilder(url)
            .GET()
            .build()

        val response = http.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() != 200) {
            throw Exception("Failed to get OIDC config: ${response.statusCode()}")
        }

        i("OIDC config response: ${response.body()}")
        return Json.parseToJsonElement(response.body()).jsonObject
    }
}