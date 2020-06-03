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

package org.openziti.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.openziti.Errors
import org.openziti.ZitiException
import org.openziti.getZitiError
import org.openziti.net.internal.Sockets
import org.openziti.net.nio.AsychChannelSocket
import org.openziti.net.nio.AsyncTLSSocketFactory
import org.openziti.util.Logged
import org.openziti.util.Version
import org.openziti.util.ZitiLog
import retrofit2.Call
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.URL
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class Controller(endpoint: URL, sslContext: SSLContext, trustManager: X509TrustManager, sessToken: String? = null) :
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

        @GET("services?limit=100")
        fun services(): Deferred<Response<Array<Service>>>

        @POST("identities")
        fun createIdentity(@Body req: CreateIdentity): Call<Response<Id>>

        @GET("identities/{id}")
        fun getIdentity(@Path("id") id: String): Call<Response<Identity>>

        @POST("sessions")
        fun createNetworkSession(@Body req: SessionReq): Deferred<Response<Session>>

        @DELETE("{p}")
        fun delete(@Header("zt-session") session: String, @Path("p", encoded = true) path: String): Call<Response<Unit>>
    }

    internal val api: API
    internal var apiSession: ApiSession? = sessToken?.let { ApiSession(it, null) }

    internal val errorConverter: Converter<ResponseBody, Response<Unit>>


    val socketFactory = object : SocketFactory() {
        override fun createSocket(): Socket = Sockets.BypassSocket()

        override fun createSocket(host: String?, port: Int): Socket {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun createSocket(host: InetAddress?, port: Int): Socket {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun createSocket(
            address: InetAddress?,
            port: Int,
            localAddress: InetAddress?,
            localPort: Int
        ): Socket {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    val clt: OkHttpClient
    init {
        clt = OkHttpClient.Builder().apply {
            socketFactory(AsychChannelSocket.Factory())
            sslSocketFactory(AsyncTLSSocketFactory(sslContext), trustManager)
            cache(null)
            addInterceptor(SessionInterceptor())
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(HttpUrl.get(endpoint)!!)
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
                return convertError(ex)
            }
        }
        return apiSession!!
    }

    suspend fun logout() {
        api.logout().await()
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

    internal suspend fun getServices(): Array<Service> {
        try {
            return api.services().await().data!!
        } catch (ex: Exception) {
            return convertError(ex)
        }
    }

    internal suspend fun createNetSession(s: Service, t: SessionType): Session {
        try {
            val response = api.createNetworkSession(SessionReq(s.id, t)).await()
            return response.data!!
        } catch (ex: Exception) {
            return convertError(ex)
        }
    }

    private fun <T> convertError(t: Throwable): T {
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
            val r = chain.request()
            d("${r.method()} ${r.url()} session=${apiSession} t[${Thread.currentThread().name}]")

            apiSession?.let {
                val request = chain.request().newBuilder()
                    .header("zt-session", it.token)
                    .build()
                return chain.proceed(request)
            }

            return chain.proceed(chain.request())
        }
    }

    private fun getClientInfo(): ClientInfo = ClientInfo(
        sdkInfo = SdkInfo,
        envInfo = mapOf(
            "os" to System.getProperty("os.name"),
            "osRelease" to System.getProperty("java.vm.version"),
            "osVersion" to System.getProperty("os.version"),
            "arch" to System.getProperty("os.arch")
        ),
        configTypes = arrayOf(InterceptConfig)
    )
}