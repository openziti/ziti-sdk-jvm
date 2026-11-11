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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.openziti.api.http.ControllerHttp
import org.openziti.api.http.EdgeClient
import org.openziti.api.http.ZitiHttpException
import org.openziti.api.http.await
import org.openziti.api.http.queryOf
import org.openziti.edge.model.CurrentApiSessionDetail
import org.openziti.edge.model.CurrentApiSessionDetailEnvelope
import org.openziti.impl.ZitiImpl
import org.openziti.util.Logged
import org.openziti.util.ZitiLog
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.time.OffsetDateTime
import java.util.*
import javax.net.ssl.SSLContext
import kotlin.random.Random


interface ZitiAuthenticator {
    enum class TokenType {
        BEARER, API_SESSION
    }

    class AuthException(cause: Throwable? = null, msg: String = "not authorized")
        : RuntimeException(cause?.message ?: msg, cause)

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

class LegacyAuth(val ep: String, val ssl: SSLContext) : ZitiAuthenticator, Logged by ZitiLog("legacyAuth[$ep]") {

    lateinit var apiSession: CurrentApiSessionDetail

    private val api = EdgeClient(ControllerHttp.client(ssl), ep)

    override suspend fun login(): ZitiAuthenticator.ZitiAccessToken = runCatching {
        d { "starting authentication" }
        apiSession = api.post<CurrentApiSessionDetailEnvelope>(
            "/authenticate", ZitiImpl.loginInfo, queryOf("method" to "cert")
        ).data
        d { "authenticated successfully session.expiresAt[${apiSession.expiresAt}]" }
        ZitiAuthenticator.ZitiAccessToken(
            ZitiAuthenticator.TokenType.API_SESSION,
            apiSession.token,
            apiSession.expiresAt
        )
    }.getOrElse { ex ->
        e(ex) { "failed to authenticate with error: ${ex.message}" }
        if (ex is ZitiHttpException && ex.code == HTTP_UNAUTHORIZED) {
            throw ZitiAuthenticator.AuthException(ex)
        }
        throw ex
    }

    override suspend fun refresh(): ZitiAuthenticator.ZitiAccessToken = runCatching {
        d { "refreshing API session" }
        apiSession = api.get<CurrentApiSessionDetailEnvelope>(
            "/current-api-session", headers = mapOf("zt-session" to apiSession.token)
        ).data
        ZitiAuthenticator.ZitiAccessToken(
            ZitiAuthenticator.TokenType.API_SESSION,
            apiSession.token,
            apiSession.expiresAt
        )
    }.getOrElse { ex ->
        e(ex) { "failed to authenticate with error: ${ex.message}" }
        if (ex is ZitiHttpException && ex.code == HTTP_UNAUTHORIZED) {
            throw ZitiAuthenticator.AuthException(ex)
        }
        throw ex
    }
}

class InternalOIDC(val ep: String, ssl: SSLContext): ZitiAuthenticator, Logged by ZitiLog("oidc[$ep]") {

    companion object {
        const val CLIENT_ID = "openziti"
        const val internalRedirect = "http://localhost:8080/auth/callback"
        val Encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()
        const val DISCOVERY = "/oidc/.well-known/openid-configuration"
        const val TOKEN_EXCHANGE_GRANT = "urn:ietf:params:oauth:grant-type:token-exchange"
        val json: ObjectMapper = ObjectMapper().registerModule(kotlinModule())
        val CLIENT_ID_BASIC_AUTH = "Basic ${Encoder.encodeToString("$CLIENT_ID:".toByteArray())}"
        private val FORM = "application/x-www-form-urlencoded".toMediaType()
    }


    // the login is a chain of redirects whose Location headers are read here, so redirects are not followed
    private val http: OkHttpClient = ControllerHttp.client(ssl)
    lateinit var tokens: JsonNode

    private val config by lazy {
        loadConfig()
    }

    private fun formatForm(params: Map<String, String>): String = params.entries.joinToString("&") {
        "${it.key}=${URLEncoder.encode(it.value, StandardCharsets.UTF_8)}"
    }

    // sent as bytes: OkHttp would add "; charset=utf-8" to the content type of a String body
    private fun formBody(body: String): RequestBody = body.toByteArray().toRequestBody(FORM)

    private fun Response.location(): String =
        header("Location") ?: throw Exception("Unexpected response from ${request.url}: $code, no Location header")

    private suspend fun startAuth(authEndpoint: String, challenge: String, state: String): URI {
        d { "starting auth" }
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
        val req = Request.Builder().url(authEndpoint.toHttpUrl())
            .header("Accept", "application/json")
            .post(formBody(body))
            .build()

        d{"sending auth request $req"}
        http.newCall(req).await().use { resp ->
            if (resp.code / 100 != 3 && resp.header("Location") == null) {
                throw Exception("Unexpected login auth response: ${resp.code} ${resp.body.string()}")
            }

            return URI.create(ep).resolve(resp.location())
        }
    }

    private suspend fun login(loginURI: URI): URI {
        val req = Request.Builder().url(loginURI.toString().toHttpUrl())
            .header("Accept", "application/json")
            .post(formBody(""))
            .build()

        http.newCall(req).await().use { resp ->
            return URI.create(resp.location())
        }
    }

    private suspend fun getCode(codeURI: URI): Pair<String, String> {
        val req = Request.Builder().url(codeURI.toString().toHttpUrl())
            .header("Accept", "application/json")
            .get().build()

        val redirectUri = http.newCall(req).await().use { resp -> URI.create(resp.location()) }

        val query = redirectUri.query.split("&").associate {
            val (k, v) = it.split("=", limit = 2)
            k to v
        }
        return query["code"]!! to query["state"]!!
    }

    private suspend fun getTokens(ep: URI, code: String, codeVerifier: String): JsonNode {
        // the code and its verifier must not travel in clear text
        require(ep.scheme == "https") { "OIDC token endpoint must use HTTPS: $ep" }

        val body = formatForm(
            mapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "client_id" to CLIENT_ID,
                "redirect_uri" to internalRedirect,
                "code_verifier" to codeVerifier
            )
        )
        val req = Request.Builder().url(ep.toString().toHttpUrl())
            .header("Accept", "application/json")
            .post(formBody(body)).build()

        http.newCall(req).await().use { tokenResp ->
            return json.readTree(tokenResp.body.string())
        }
    }

    override suspend fun login(): ZitiAuthenticator.ZitiAccessToken {
        val codeVerifier = Encoder.encodeToString(Random.Default.nextBytes(40))
        val challenge = Encoder.encodeToString(
            MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
        )
        val state = Encoder.encodeToString(Random.Default.nextBytes(30))


        val authEndpoint = config["authorization_endpoint"]?.textValue()
            ?: throw Exception("Missing authorization endpoint in OIDC config")
        val tokenEndpoint = config["token_endpoint"]?.textValue()
            ?: throw Exception("Missing token endpoint in OIDC config")

        val loginURI = startAuth(authEndpoint, challenge, state)
        val codeURI = login(loginURI)
        val (code, st) = getCode(codeURI)

        require(st == state){ "OIDC state mismatch" }

        tokens = getTokens(URI.create(tokenEndpoint), code, codeVerifier)

        val accessToken = tokens["access_token"]?.textValue()
            ?: throw Exception("Missing access token in OIDC response")

        val exp = OffsetDateTime.now().plusSeconds(tokens["expires_in"]?.longValue() ?: 600)
        return ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.BEARER, accessToken, exp)
    }

    override suspend fun refresh(): ZitiAuthenticator.ZitiAccessToken {
        d { "starting refresh" }
        val refreshToken = tokens.get("refresh_token")?.textValue()

        refreshToken ?: throw ZitiAuthenticator.AuthException(msg = "no refresh_token")

        val form = mapOf(
            "client_id"     to CLIENT_ID,
            "grant_type"    to "refresh_token",
            "refresh_token" to refreshToken,
        )

        val tokenEndpoint = config["token_endpoint"]?.textValue()
            ?: throw Exception("Missing token endpoint in OIDC config")
        val req = Request.Builder().url(tokenEndpoint.toHttpUrl())
            .header("Authorization", CLIENT_ID_BASIC_AUTH)
            .post(formBody(formatForm(form)))
            .build()

        http.newCall(req).await().use { resp ->
            if (resp.code == HTTP_UNAUTHORIZED) {
                throw ZitiAuthenticator.AuthException()
            }
            if (resp.code != 200) {
                throw Exception("unexpected refresh response ${resp.code}")
            }

            tokens = json.readTree(resp.body.string())
        }
        val accessToken = tokens["access_token"]?.textValue()
            ?: throw Exception("Missing access token in OIDC response")
        val exp = OffsetDateTime.now().plusSeconds(tokens["expires_in"]?.longValue() ?: 600)
        return ZitiAuthenticator.ZitiAccessToken(ZitiAuthenticator.TokenType.BEARER, accessToken, exp)
    }

    private fun loadConfig(): JsonNode {
        val url = URI.create(ep).resolve(DISCOVERY)

        val request = Request.Builder().url(url.toString().toHttpUrl()).get().build()

        http.newCall(request).execute().use { response ->
            if (response.code != 200) {
                throw Exception("Failed to get OIDC config: ${response.code}")
            }

            val body = response.body.string()
            v {"OIDC config response: $body"}
            return ObjectMapper().readTree(body)
        }
    }
}
