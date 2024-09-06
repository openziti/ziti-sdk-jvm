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

import com.fasterxml.jackson.module.kotlin.treeToValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.future.await
import org.openziti.Errors
import org.openziti.ZitiException
import org.openziti.edge.ApiClient
import org.openziti.edge.ApiException
import org.openziti.edge.api.*
import org.openziti.edge.model.*
import org.openziti.getZitiError
import org.openziti.impl.ZitiImpl
import org.openziti.util.Logged
import org.openziti.util.SystemInfoProvider
import org.openziti.util.Version
import org.openziti.util.ZitiLog
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

    private val pageSize = 100

    internal var apiPrefix: String = ""
    internal var apiSession: ApiSession? = null

    private val edgeApi: ApiClient = ApiClient().apply {
        setHttpClientBuilder(
            HttpClient.newBuilder()
                .sslContext(sslContext)
                .executor(Dispatchers.IO.asExecutor()))
        updateBaseUri(endpoint.toString())
        setBasePath("/")
    }
    private val infoClient: InformationalApi

    init {

        edgeApi.requestInterceptor = Consumer { req ->
            apiSession?.let {
                req.header("zt-session", it.token)
            }
            val r = req.build()
            i("${r.method()} ${r.uri()} session=${apiSession?.id} t[${Thread.currentThread().name}]")
        }

        // always uses root
        infoClient = InformationalApi(edgeApi)
    }

    suspend fun version(): org.openziti.edge.model.Version {
        val ver = infoClient.listVersion().await().data
        i { "controller[${edgeApi.baseUri}] version(${ver.version}/${ver.revision})"}
        val prefix = ver.apiVersions?.run { get("edge-client") ?: get("edge") }?.get("v1")?.path

        prefix?.let {
            apiPrefix = it
            edgeApi.setBasePath(it)
        } ?: w {"did not receive expected apiVersions mapping"}
        return ver
    }

    internal suspend fun currentApiSession(): ApiSession {
        return runCatching {
            CurrentApiSessionApi(edgeApi).currentAPISession.await().data
        }.getOrElse {
            convertError(it)
        }
    }

    internal suspend fun login(login: Login? = null): ApiSession {
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
    }

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
            null, null, null, null)
        }
    }

    internal fun getSessions(): Flow<Session> {
        val sessionFilter = apiSession?.let {
            "apiSession=\"${it.id}\""
        }
        val sessionApi = SessionApi(edgeApi)
        return pagingApiRequest { limit, offset ->
            sessionApi.listSessions(limit, offset, sessionFilter)
        }
    }

    internal suspend fun createNetSession(s: Service, t: SessionType): Session {
        val req = SessionCreate().type(t).serviceId(s.id)
        return SessionApi(edgeApi).createSession(req).runCatching {
            await().data!!
        }.getOrElse { convertError(it) }
    }

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

    private inline fun <reified Env, T> pagingApiRequest(
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

    internal suspend fun getEdgeRouters() = runCatching {
        EdgeRouterApi(edgeApi).currentIdentityEdgeRouters.await().data
    }.getOrElse {
        convertError(it)
    }

    private fun convertError(t: Throwable): Nothing {
        val errCode = when (t) {
            is CancellationException -> throw t
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

        val err = edgeApi.objectMapper.treeToValue<ApiErrorEnvelope>(
            edgeApi.objectMapper.readTree(resp)
        ).error

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
        configTypes = listOf(InterceptV1Cfg, ClientV1Cfg)
    }
}