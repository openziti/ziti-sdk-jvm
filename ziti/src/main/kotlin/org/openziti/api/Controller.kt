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

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.future.await
import org.openziti.ZitiException
import org.openziti.ZitiException.Errors
import org.openziti.edge.ApiClient
import org.openziti.edge.ApiException
import org.openziti.edge.api.*
import org.openziti.edge.model.*
import org.openziti.impl.ZitiImpl
import org.openziti.util.*
import java.io.IOException
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import javax.net.ssl.SSLContext
import kotlin.reflect.full.memberFunctions

internal class Controller internal constructor(
    ep: String,
    sslContext: SSLContext,
    val capabilities: Set<Capabilities> = emptySet()) :
    Logged by ZitiLog() {

    private val pageSize = 100

    private val http = HttpClient.newBuilder()
        .sslContext(sslContext)
        .executor(Dispatchers.IO.asExecutor())

    private val edgeApi: ApiClient = ApiClient().apply {
        setHttpClientBuilder(http)
        updateBaseUri(ep)
        setBasePath("")
        setRequestInterceptor(ReqInterceptor())
    }
    private val infoClient: InformationalApi
        get() = InformationalApi(edgeApi)


    internal val endpoint: String
        get() = edgeApi.baseUri

    internal suspend fun listControllers(): List<ControllerDetail> {
        return runCatching {
            ControllersApi(edgeApi).listControllers(100, 0, null).await().data
        }.getOrElse {
            convertError(it)
        }
    }

    suspend fun version(): org.openziti.edge.model.Version {
        val version = infoClient.listVersion().await().data
        i { "controller[${edgeApi.baseUri}] version(${version.version}/${version.revision})"}
        val prefix = version.apiVersions?.run { get("edge-client") ?: get("edge") }?.get("v1")?.path

        prefix?.let {
            edgeApi.setBasePath(it)
        } ?: w {"did not receive expected apiVersions mapping"}
        return version
    }

    internal suspend fun currentApiSession(): ApiSession = runCatching {
        CurrentApiSessionApi(edgeApi).currentAPISession.await().data
    }.getOrElse {
        convertError(it)
    }

    internal suspend fun login(login: Login? = null): ApiSession {
        runCatching { version() }.getOrElse { convertError(it) }

        val method = if (login == null) "cert" else "password"
        val auth = getClientInfo()
        if (login != null) {
            auth.username(login.username)
            auth.password(login.password)
        }

        val apiSession = runCatching {
            AuthenticationApi(edgeApi).authenticate(method, auth).await().data
        }.getOrElse {
            convertError(it)
        }
        edgeApi.requestInterceptor = ReqInterceptor(apiSession)

        return apiSession
    }

    suspend fun logout() {
        CurrentApiSessionApi(edgeApi).currentApiSessionDelete().runCatching {
            await()
        }
    }

    fun shutdown() = Unit

    internal suspend fun getServiceUpdates() =
        CurrentApiSessionApi(edgeApi).listServiceUpdates().runCatching {
            await().data
        }.getOrElse {
            convertError(it)
        }

    internal fun getServices(): Flow<Service> {
        val serviceApi = ServiceApi(edgeApi)

        return pagingApiRequest {
                limit, offset -> serviceApi.listServices(limit,  offset,
            null, ALL_CONFIGS, null, null)
        }
    }

    internal suspend fun createNetSession(s: Service, t: SessionType): Session  = runCatching {
        val req = SessionCreate().type(t).serviceId(s.id)
        SessionApi(edgeApi).createSession(req).await().data!!
    }.getOrElse { convertError(it) }


    internal fun getServiceTerminators(s: Service): Flow<TerminatorClientDetail> {
        val serviceApi = ServiceApi(edgeApi)
        return pagingApiRequest {
                limit, offset -> serviceApi.listServiceTerminators(s.id, limit, offset, null)
        }
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

    private inline fun <reified Env, reified T> pagingApiRequest(
        crossinline req: (limit: Int, offset: Int) -> CompletableFuture<Env>
    ): Flow<T> {

        val metaFunc = Env::class.memberFunctions.first { it.name == "getMeta" }
        val dataFunc = Env::class.memberFunctions.first { it.name == "getData" }

        return flow {
            var offset = 0
            val p0 = req(pageSize, offset)
            val resp = p0.await()
            val meta = metaFunc.call(resp) as Meta
            val pagination = meta.pagination
            val pages = mutableListOf(p0)
            pagination?.let {
                while (it.totalCount.toInt() > offset + it.limit.toInt()) {
                    offset += pagination.limit.toInt()
                    pages.add(req(pageSize, offset))
                }
            }

            pages.forEach {
                val r = it.await()
                val d = (dataFunc.call(r) as Collection<*>).filterIsInstance<T>()
                emitAll(d.asFlow())
            }
        }
    }

    internal suspend fun sendPostureResp(responses: List<PostureResponseCreate>) {
        runCatching {
            PostureChecksApi(edgeApi).createPostureResponseBulk(responses).await()
        }.getOrNull()
    }

    internal suspend fun getEdgeRouters() = runCatching {
        EdgeRouterApi(edgeApi).currentIdentityEdgeRouters.await().data
    }.getOrElse {
        convertError(it)
    }

    private fun convertError(t: Throwable): Nothing {
        val errCode = when (t) {
            is CancellationException -> throw t
            is ApiException -> ZitiException.getZitiError(getError(t.responseBody))
            is IOException -> Errors.ControllerUnavailable
            else -> Errors.WTF(t.toString())
        }

        throw ZitiException(errCode,  t)
    }

    private fun getError(resp: String): String {

        val err = edgeApi.objectMapper.readValue(resp, ApiErrorEnvelope::class.java).error

        return err.code ?: err.message!!
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
        configTypes = ALL_CONFIGS
    }

    internal inner class ReqInterceptor(val session: ApiSession? = null): Consumer<HttpRequest.Builder> {
        override fun accept(req: HttpRequest.Builder) {
            session?.let { req.header("zt-session", session.token) }
            val r = req.build()
            d {"${r.method()} ${r.uri()} session=${session?.id}"}
        }
    }

    class NotAvailableException(msg: String = "Controller is not available") : Exception(msg)

    companion object: Logged by ZitiLog(Controller.TAG) {
        private const val TAG = "Controller"
        val ALL_CONFIGS = listOf("all")

        suspend fun getActiveController(endpoints: Collection<String>, sslContext: SSLContext): Controller {

            val edgeApiClient = ApiClient().apply {
                setHttpClientBuilder(
                    HttpClient.newBuilder()
                        .sslContext(sslContext)
                        .executor(Dispatchers.IO.asExecutor()))
            }

            val eps = endpoints.shuffled()
            for (ep in eps) {
                edgeApiClient.apply {
                    updateBaseUri(ep)
                    setBasePath("/")
                }

                InformationalApi(edgeApiClient).runCatching {
                    val capabilities = listEnumeratedCapabilities().await().data
                    val version = listVersion().await().data
                    val url = version.apiVersions!!["edge"]!!["v1"]!!.apiBaseUrls!![0]
                    Controller(url, sslContext, capabilities.toSet())
                }.onFailure {
                    e(it) { "failed to connect to controller $ep" }
                }.onSuccess {
                    return it
                }
            }

            throw NotAvailableException()
        }
    }
}