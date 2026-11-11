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

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.security.KeyStore
import java.time.Duration
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** OkHttp clients for the SDK's own traffic to the controller. */
internal object ControllerHttp {

    private val CONNECT_TIMEOUT = Duration.ofSeconds(10)
    private val READ_TIMEOUT = Duration.ofSeconds(60)
    private val WRITE_TIMEOUT = Duration.ofSeconds(30)

    // OkHttp insists on an X509TrustManager next to a custom SSLSocketFactory, but only uses it for
    // certificate pinning (not used here) and for cleaning the chain it reports in `Handshake`.
    // Certificates are validated by the trust managers inside the caller's SSLContext during the
    // handshake, so the platform default is a safe stand-in. Do not read `Handshake.peerCertificates`.
    private val placeholderTrustManager: X509TrustManager by lazy {
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).run {
            init(null as KeyStore?)
            trustManagers.filterIsInstance<X509TrustManager>().first()
        }
    }

    /**
     * @param ssl validates the controller and supplies the client certificate, if any
     * @param callTimeout overall limit for one call; [Duration.ZERO] means no limit
     */
    fun client(ssl: SSLContext, callTimeout: Duration = Duration.ZERO): OkHttpClient =
        OkHttpClient.Builder()
            .socketFactory(LazyBypassSocketFactory())
            .sslSocketFactory(LazyBypassSSLSocketFactory(ssl), placeholderTrustManager)
            // the old java.net.http client never followed redirects, and OIDC depends on seeing them
            .followRedirects(false)
            .followSslRedirects(false)
            .cookieJar(CookieJar.NO_COOKIES)
            .connectTimeout(CONNECT_TIMEOUT)
            .readTimeout(READ_TIMEOUT)
            .writeTimeout(WRITE_TIMEOUT)
            .callTimeout(callTimeout)
            .build()
}

/** Runs the call without blocking the caller; cancelling the coroutine cancels the call. */
internal suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
    cont.invokeOnCancellation { cancel() }
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if (cont.isCancelled) return
            cont.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            // if the coroutine was cancelled in the meantime nobody will read the response
            cont.resume(response) { response.close() }
        }
    })
}
