/*
 * Copyright (c) 2018-2026 NetFoundry Inc.
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

package org.openziti.api.http

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.openziti.api.ZitiAuthenticator
import java.io.IOException
import java.net.URI

/**
 * Query parameters for [EdgeClient]: null values are dropped and an [Iterable] value becomes the same
 * parameter repeated once per element (the `multi` collection format of the generated clients).
 */
internal fun queryOf(vararg params: Pair<String, Any?>): List<Pair<String, String>> =
    params.flatMap { (name, value) ->
        when (value) {
            null -> emptyList()
            is Iterable<*> -> value.filterNotNull().map { name to it.toString() }
            else -> listOf(name to value.toString())
        }
    }

/**
 * JSON-over-HTTP client for the controller's edge API, in place of the generated `ApiClient`, which is built
 * on `java.net.http` and therefore does not work on Android. Request and response types are the generated
 * `org.openziti.edge.model` classes; the generated API classes remain the reference for paths and parameters.
 */
internal class EdgeClient(
    client: OkHttpClient,
    uri: String,
    @PublishedApi internal val mapper: ObjectMapper = edgeMapper,
) {
    private var client = client
    private var scheme = "https"
    private var host = ""
    private var port = -1
    private var basePath = ""

    /** When set, authenticates every request. */
    var token: ZitiAuthenticator.ZitiAccessToken? = null

    init {
        updateBaseUri(uri)
    }

    /** `scheme://host[:port]` followed by the base path, without a trailing slash. */
    val baseUri: String
        get() = "$scheme://$host" + (if (port == -1) "" else ":$port") + basePath

    /** Takes scheme, host, port and base path from [uri]. */
    fun updateBaseUri(uri: String) {
        val parsed = URI.create(uri)
        scheme = checkNotNull(parsed.scheme) { "controller address has no scheme: $uri" }
        host = checkNotNull(parsed.host) { "controller address has no host: $uri" }
        port = parsed.port
        basePath = normalized(parsed.rawPath ?: "")
    }

    /** [path] is either empty or starts with `/`. */
    fun setBasePath(path: String) {
        basePath = normalized(path)
    }

    /** A copy with the same address and token that talks through [client]. */
    fun withClient(client: OkHttpClient): EdgeClient =
        EdgeClient(client, baseUri, mapper).also { it.token = token }

    /**
     * Sends the request and returns the response body text (empty if the answer has no body).
     *
     * [body]: a `String` is sent as is, with the `Content-Type` found in [headers] (default `application/json`);
     * any other object is serialized as JSON; `null` sends an empty body for POST/PUT/PATCH and none otherwise.
     *
     * @throws ZitiHttpException for any non-2xx answer (redirects are not followed)
     * @throws IOException if the controller cannot be reached
     */
    suspend fun send(
        method: String,
        path: String,
        query: List<Pair<String, String>> = emptyList(),
        headers: Map<String, String> = emptyMap(),
        body: Any? = null,
    ): String {
        val url = (baseUri + path).toHttpUrl().newBuilder().apply {
            query.forEach { (name, value) -> addQueryParameter(name, value) }
        }.build()

        val request = Request.Builder().url(url).header("Accept", "application/json")
        token?.let {
            when (it.type) {
                ZitiAuthenticator.TokenType.BEARER -> request.header("Authorization", "Bearer ${it.token}")
                ZitiAuthenticator.TokenType.API_SESSION -> request.header("zt-session", it.token)
            }
        }

        var contentType = JSON
        for ((name, value) in headers) {
            if (name.equals("Content-Type", ignoreCase = true)) contentType = value else request.header(name, value)
        }

        request.method(method, requestBody(method, body, contentType))

        client.newCall(request.build()).await().use { response ->
            val text = response.body.string()
            if (!response.isSuccessful) throw ZitiHttpException(response.code, text)
            return text
        }
    }

    suspend inline fun <reified T : Any> get(
        path: String,
        query: List<Pair<String, String>> = emptyList(),
        headers: Map<String, String> = emptyMap(),
    ): T = parse(send("GET", path, query, headers), T::class.java)

    suspend inline fun <reified T : Any> post(
        path: String,
        body: Any? = null,
        query: List<Pair<String, String>> = emptyList(),
        headers: Map<String, String> = emptyMap(),
    ): T = parse(send("POST", path, query, headers, body), T::class.java)

    suspend fun delete(path: String, headers: Map<String, String> = emptyMap()) {
        send("DELETE", path, headers = headers)
    }

    // Anything but a well-formed body is an IOException, so that it is retried like an outage.
    @PublishedApi
    internal fun <T> parse(text: String, type: Class<T>): T {
        if (text.isBlank()) throw IOException("empty response body")
        return mapper.readValue(text, type)
    }

    private fun requestBody(method: String, body: Any?, contentType: String): RequestBody? = when {
        body is String -> body.toByteArray().toRequestBody(contentType.toMediaType())
        body != null -> mapper.writeValueAsBytes(body).toRequestBody(contentType.toMediaType())
        method == "POST" || method == "PUT" || method == "PATCH" -> ByteArray(0).toRequestBody(null)
        else -> null
    }

    private fun normalized(path: String): String {
        val p = path.trimEnd('/')
        require(p.isEmpty() || p.startsWith("/")) { "base path must start with '/': $path" }
        return p
    }

    private companion object {
        const val JSON = "application/json"
    }
}
