/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.openziti.Errors
import org.openziti.ZitiException
import org.openziti.getZitiError
import org.openziti.net.nio.AsychChannelSocket
import org.openziti.net.nio.AsyncTLSSocketFactory
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
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

internal class Controller(endpoint: URL, sslContext: SSLContext, trustManager: X509TrustManager) :
    Logged by ZitiLog() {
    val SdkInfo = mapOf("type" to "ziti-sdk-java") + Version.VersionInfo

    internal interface API {
        @GET("version")
        fun version(): Deferred<Response<ControllerVersion>>

        @GET("current-api-session")
        fun currentApiSession(): Deferred<Response<ApiSession>>

        @POST("authenticate?method=password")
        fun authenticate(@Body login: Login): Deferred<Response<ApiSession>>

        @POST("authenticate?method=cert")
        fun authenticateCert(@Body req: ClientInfo): Deferred<Response<ApiSession>>

        @DELETE("current-api-session")
        fun logout(): Deferred<Unit>

        @GET("services")
        fun getServicesAsync(@Query("offset") offset: Int = 0, @Query("limit") limit: Int? = null)
                : Deferred<Response<Collection<Service>>>

        @GET("services/{service_id}/terminators")
        fun getServiceTerminators(
            @Path("service_id") service_id: String,
            @Query("offset") offset: Int = 0,
            @Query("limit") limit: Int = 100
        ): Deferred<Response<Collection<ServiceTerminator>>>

        @POST("identities")
        fun createIdentity(@Body req: CreateIdentity): Call<Response<Id>>

        @GET("identities/{id}")
        fun getIdentity(@Path("id") id: String): Call<Response<Identity>>

        @GET("sessions")
        fun getSessionsAsync(@Query("filter") filter: String? = null,
                             @Query("offset") offset: Int = 0,
                             @Query("limit") limit: Int? = null): Deferred<Response<Collection<Session>>>

        @POST("sessions")
        fun createNetworkSession(@Body req: SessionReq): Deferred<Response<Session>>

        @POST("posture-response")
        fun sendPosture (@Body pr: PostureResponse): Deferred<Response<JsonObject>>

        @DELETE("{p}")
        fun delete(@Header("zt-session") session: String, @Path("p", encoded = true) path: String): Call<Response<Unit>>
    }

    internal val api: API
    internal var apiSession: ApiSession? = null

    internal val errorConverter: Converter<ResponseBody, Response<Unit>>
    internal val loggingInterceptor = HttpLoggingInterceptor().apply {
        val level = HttpLoggingInterceptor.Level.valueOf(System.getProperty("ZitiControllerDebug", "NONE"))
        setLevel(level)
    }

    val clt: OkHttpClient
    init {
        clt = OkHttpClient.Builder().apply {
            socketFactory(AsychChannelSocket.Factory())
            sslSocketFactory(AsyncTLSSocketFactory(sslContext), trustManager)
            cache(null)
            addInterceptor(SessionInterceptor())
            addInterceptor(loggingInterceptor)
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(clt)
            .build()
        api = retrofit.create(API::class.java)
        errorConverter = retrofit.responseBodyConverter(Response::class.java, emptyArray())
    }

    suspend fun version() = api.version().await().data

    suspend internal fun login(login: Login? = null): ApiSession {
        // validate current session
        if (apiSession != null) {
            try {
                apiSession = api.currentApiSession().await().data
            } catch (ex: HttpException) {
                val err = getError(ex.response())
                w("current-session: ${ex.code()} ${err}")
                apiSession = null
            }
        }

        if (apiSession == null) {

            val call = if (login == null)
                api.authenticateCert(getClientInfo()) else
                api.authenticate(login)

            try {
                val resp = call.await()
                apiSession = resp.data!!
            } catch (ex: Exception) {
                convertError(ex)
            }
        }
        return apiSession!!
    }

    suspend fun logout() {
        apiSession?.let {
            api.logout().await()
        }
        apiSession = null
    }

    fun shutdown() {
        clt.dispatcher().executorService().shutdown()
    }

    internal fun createIdentity(name: String, type: String = "Device", enrollment: String = "ott"): Id {
        val resp = api.createIdentity(CreateIdentity(name, type, enrollment)).execute()
        return resp.body()?.data ?: throw Exception(getError(resp))
    }

    internal fun getIdentity(id: String): Identity? {
        val req = api.getIdentity(id)
        return req.execute().body()?.data
    }

    internal fun getServices() = pagingRequest {
        offset -> api.getServicesAsync(offset = offset, limit = 25)
    }

    internal fun getSessions(): Flow<Session> {
        val sessionFilter = apiSession?.let {
            "apiSession=\"${it.id}\""
        }
        return pagingRequest { offset ->  api.getSessionsAsync(sessionFilter, offset = offset) }
    }

    internal suspend fun createNetSession(s: Service, t: SessionType): Session {
        try {
            val response = api.createNetworkSession(SessionReq(s.id, t)).await()
            return response.data!!
        } catch (ex: Exception) {
            convertError(ex)
        }
    }

    internal fun getServiceTerminators(s: Service): Flow<ServiceTerminator> = pagingRequest {
            offset ->  api.getServiceTerminators(s.id, offset = offset)
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

        pages.awaitAll().forEach { page ->
            page.data?.let {
                it.forEach { el -> emit(el) }
            }
        }
    }

    internal suspend fun sendPostureResp(pr: PostureResponse) {
        api.sendPosture(pr).await()
    }

    private fun convertError(t: Throwable): Nothing {
        e("error $t", t)
        when (t) {
            is HttpException -> throw ZitiException(getZitiError(getError(t.response())))
            is IOException -> throw ZitiException(Errors.ControllerUnavailable, t)
            else -> throw ZitiException(Errors.WTF(t.toString()), t)
        }
    }

    private fun getError(resp: retrofit2.Response<*>?): String {
        if (resp == null)
            return "no response available"

        val errorBody = resp.errorBody()

        return if (errorBody != null) {
            val body = errorConverter.convert(errorBody)
            w{"request failed with ${body?.error}"}
            body?.error?.code.toString()
        }
        else resp.message()
    }

    inner class SessionInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val origReq = chain.request()
            val rb = origReq.newBuilder()
            if (origReq.header("Accept") == null) {
                rb.header("Accept", "application/json")
            }
            apiSession?.let { rb.header("zt-session", it.token) }

            val req = rb.build()
            d("${req.method()} ${req.url()} session=${apiSession?.id} t[${Thread.currentThread().name}]")
            return chain.proceed(req)
        }
    }

    private fun getClientInfo(): ClientInfo = ClientInfo(
        sdkInfo = SdkInfo,
        envInfo = SystemInfoProvider().getSystemInfo(),
        configTypes = arrayOf(InterceptConfig)
    )
}