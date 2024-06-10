/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.openziti.Errors
import org.openziti.ZitiException
import org.openziti.edge.ApiClient
import org.openziti.edge.ApiException
import org.openziti.edge.api.*
import org.openziti.edge.model.*
import org.openziti.edge.model.Meta
import org.openziti.getZitiError
import org.openziti.impl.ZitiImpl
import org.openziti.net.internal.Sockets
import org.openziti.util.Logged
import org.openziti.util.SystemInfoProvider
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import retrofit2.Call
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.URL
import java.net.http.HttpClient
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import kotlin.reflect.full.memberFunctions

internal class Controller(endpoint: URL, sslContext: SSLContext, trustManager: X509TrustManager) :
    Logged by ZitiLog() {
    val SdkInfo = mapOf("type" to "ziti-sdk-java") + Version.VersionInfo

    internal interface API {
        @GET("services")
        fun getServicesAsync(@Query("offset") offset: Int = 0, @Query("limit") limit: Int? = null)
                : Deferred<Response<Collection<Service>>>
    }

    private val pageSize = 100

    internal var apiPrefix: String = ""
    internal val api: API
    internal var apiSession: ApiSession? = null

    // API features

    private val errorConverter: Converter<ResponseBody, Response<Unit>>
    internal val loggingInterceptor = HttpLoggingInterceptor().apply {
        val level = HttpLoggingInterceptor.Level.valueOf(System.getProperty("ZitiControllerDebug", "NONE"))
        setLevel(level)
    }

    private val edgeApi: ApiClient
    private val infoClient: InformationalApi

    val clt: OkHttpClient
    val retrofit: Retrofit

    init {
        clt = OkHttpClient.Builder().apply {
            socketFactory(Sockets.bypassSocketFactory())
            sslSocketFactory(Sockets.bypassSSLSocketFactory(sslContext), trustManager)
            cache(null)
            addInterceptor(ZitiInterceptor())
            addInterceptor(loggingInterceptor)
        }.build()

        retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(clt)
            .build()
        api = retrofit.create(API::class.java)
        errorConverter = retrofit.responseBodyConverter(Response::class.java, emptyArray())

        edgeApi = ApiClient().apply {
            setHttpClientBuilder(
                HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .executor(Dispatchers.IO.asExecutor()))
            updateBaseUri(endpoint.toString())
            setBasePath("/")
        }

        edgeApi.requestInterceptor = Consumer { req ->
            apiSession?.let {
                req.header("zt-session", it.token)
            }
        }

        // always uses root
        infoClient = InformationalApi(edgeApi)
    }

    suspend fun version(): org.openziti.edge.model.Version {
        val ver = infoClient.listVersion().await().data
        i { "controller[${edgeApi.baseUri}] version(${ver.version}/${ver.revision})"}
        val prefix = ver.apiVersions?.run { get("edge-client") ?: get("edge") }?.get("v1")?.path

        prefix?.let { apiPrefix = it } ?: w {"did not receive expected apiVersions mapping"}
        return ver
    }

    internal suspend fun currentApiSession(): ApiSession {
        return runCatching {
            CurrentApiSessionApi(edgeApi).currentAPISession.await().data
        }.getOrElse {
            convertError(it)
        }
    }

    suspend internal fun login(login: Login? = null): ApiSession {
        // validate current session
        if (apiSession != null) {
            apiSession = runCatching {  currentApiSession() }.getOrNull()
        }

        if (apiSession == null) {
            runCatching { version() }.getOrElse { convertError(it) }

            val method = if (login == null) "cert" else "password"
            val auth = getClientInfo()
            if (login != null) {
                auth.username(login.username)
                auth.password(login.password)
            }

            apiSession = runCatching {
                AuthenticationApi(edgeApi).authenticate(method, auth).await().data
            }.getOrElse {
                convertError(it)
            }
        }
        return apiSession!!
    }

    suspend fun logout() {
        CurrentApiSessionApi(edgeApi).currentApiSessionDelete().runCatching {
            await()
        }
        apiSession = null
    }

    fun shutdown() {
        clt.dispatcher().executorService().shutdown()
        clt.connectionPool().evictAll()
    }

    internal suspend fun getServiceUpdates() =
        CurrentApiSessionApi(edgeApi).listServiceUpdates().runCatching {
            await().data
        }.getOrElse {
            convertError(it)
        }

    internal fun getServices(): Flow<ServiceDetail> {
        val serviceApi = ServiceApi(edgeApi)

        return pagingApiRequest {
                offset -> serviceApi.listServices(25,  offset, null, null, null, null)
        }
    }

    internal fun getSessions(): Flow<Session> {
        val sessionFilter = apiSession?.let {
            "apiSession=\"${it.id}\""
        }
        val sessionApi = SessionApi(edgeApi)
        return pagingApiRequest { offset ->
            sessionApi.listSessions(pageSize, offset, sessionFilter)
        }
    }

    internal suspend fun createNetSession(s: ServiceDetail, t: SessionType): Session {
        val req = SessionCreate().type(t).serviceId(s.id)
        return SessionApi(edgeApi).createSession(req).runCatching {
            await().data!!
        }.getOrElse { convertError(it) }
    }

    internal fun getServiceTerminators(s: Service): Flow<TerminatorClientDetail> = pagingApiRequest {
            offset -> ServiceApi(edgeApi).listServiceTerminators(s.id, pageSize, offset, null)
    }

    internal suspend fun postMFA() =
        CurrentIdentityApi(edgeApi).enrollMfa().await().data

    internal suspend fun getMFAEnrollment(): DetailMfa? =
        runCatching {
            CurrentIdentityApi(edgeApi).detailMfa().await().data
        }.getOrElse {
            if (it is ApiException && it.code == 404) {
                null
            } else {
                convertError(it)
            }
        }

    internal suspend fun verifyMFA(code: String) {
        runCatching {
            CurrentIdentityApi(edgeApi).verifyMfa(MfaCode().code(code)).await()
        }.getOrElse { convertError(it) }
    }

    internal suspend fun authMFA(code: String) {
        runCatching {
            AuthenticationApi(edgeApi).authenticateMfa(MfaCode().code(code)).await()
        }.getOrElse { convertError(it) }
    }

    internal suspend fun removeMFA(code: String) {
        runCatching {
            CurrentIdentityApi(edgeApi).deleteMfa(code).await()
        }.getOrElse { convertError(it) }
    }

    internal suspend fun getMFARecoveryCodes(code: String, newCodes: Boolean): Array<String> {
        if (newCodes) {
            val c = MfaCode().code(code)
            runCatching {
                MfaApi(edgeApi).createMfaRecoveryCodes(c).await()
            }.getOrElse {
                convertError(it)
            }
        }

        return CurrentIdentityApi(edgeApi).detailMfa().await().data.recoveryCodes?.toTypedArray() ?: emptyArray()
    }

    private fun <T> pagingRequest(req: (offset: Int) -> Deferred<Response<Collection<T>>>) = flow {
        var offset = 0

        val p0 = req(offset)
        val resp = p0.await()
        val pagination = resp.meta.pagination!!

        val pages = mutableListOf(p0)

        while (pagination.totalCount > offset + pagination.limit) {
            offset += pagination.limit
            pages.add(req(offset))
        }

        pages.forEach { p ->
            p.await().data?.let {
                emitAll(it.asFlow())
            }
        }
    }

    private inline fun <reified Env, T> pagingApiRequest(
        crossinline req: (offset: Int) -> CompletableFuture<Env>
    ): Flow<T> {

        val metaFunc = Env::class.memberFunctions.first { it.name == "getMeta" }
        val dataFunc = Env::class.memberFunctions.first { it.name == "getData" }

        return flow {
            var offset = 0
            val p0 = req(offset)
            val resp = p0.await()
            val meta = metaFunc.call(resp) as Meta
            val pagination = meta.pagination
            val pages = mutableListOf(p0)
            pagination?.let {
                while (it.totalCount.toInt() > offset + it.limit.toInt()) {
                    offset += pagination.limit.toInt()
                    pages.add(req(offset))
                }
            }

            pages.forEach {
                val r = it.await()
                val d = dataFunc.call(r) as Collection<T>
                emitAll(d.asFlow())
            }
        }
    }

    internal suspend fun sendPostureResp(responses: List<PostureResponseCreate>) {
        runCatching {
            PostureChecksApi(edgeApi).createPostureResponseBulk(responses).await()
        }.getOrNull()
    }

    internal suspend fun getEdgeRouters() =
        EdgeRouterApi(edgeApi).currentIdentityEdgeRouters.runCatching {
            await().data
        }.getOrElse {
            convertError(it)
        }

    private fun convertError(t: Throwable): Nothing {
        val errCode = when (t) {
            is CancellationException -> throw t
            is HttpException -> getZitiError(getError(t.response()))
            is ApiException -> getZitiError(getError(t.responseBody))
            is IOException -> Errors.ControllerUnavailable
            else -> Errors.WTF(t.toString())
        }
        if (errCode is Errors.NotAuthorized) {
            apiSession = null
        }

        throw ZitiException(errCode,  t)
    }

    private fun getError(resp: String): String {
        val apiError = edgeApi.objectMapper.convertValue(resp, ApiError::class.java)
        return apiError.code ?: apiError.message!!
    }

    private fun getError(resp: retrofit2.Response<*>?): String {
        if (resp == null)
            return "no response available"

        val errorBody = resp.errorBody()!!.string()
        return getError(errorBody)
    }

    inner class ZitiInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val origReq = chain.request()
            val rb = origReq.newBuilder()
            if (origReq.header("Accept") == null) {
                rb.header("Accept", "application/json")
            }
            apiSession?.let { rb.header("zt-session", it.token) }
            val newPath = "${apiPrefix}${origReq.url().encodedPath()}"
            val newUrl = origReq.url().newBuilder()
                .encodedPath(newPath)
                .build()
            rb.url(newUrl)

            val req = rb.build()
            d("${req.method()} ${req.url()} session=${apiSession?.id} t[${Thread.currentThread().name}]")
            return chain.proceed(req)
        }
    }

    private fun getClientInfo(): Authenticate = Authenticate().apply {
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
        configTypes = listOf(InterceptV1Cfg, ClientV1Cfg)
    }
}