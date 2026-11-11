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
import kotlinx.coroutines.flow.*
import org.openziti.ZitiException
import org.openziti.ZitiException.Errors
import org.openziti.api.http.ControllerHttp
import org.openziti.api.http.EdgeClient
import org.openziti.api.http.ZitiHttpException
import org.openziti.api.http.edgeMapper
import org.openziti.api.http.queryOf
import org.openziti.edge.model.*
import org.openziti.impl.ZitiImpl
import org.openziti.util.*
import java.io.IOException
import javax.net.ssl.SSLContext

internal class Controller internal constructor(
    ep: String,
    sslContext: SSLContext,
    val capabilities: Set<Capabilities> = emptySet()) :
    Logged by ZitiLog() {

    private val pageSize = 100

    private val edge = EdgeClient(ControllerHttp.client(sslContext), ep).apply {
        setBasePath("")
    }

    internal val endpoint: String
        get() = edge.baseUri

    internal suspend fun listControllers(): List<ControllerDetail> {
        return runCatching {
            edge.get<ListControllersEnvelope>("/controllers", queryOf("limit" to 100, "offset" to 0)).data
        }.getOrElse {
            convertError(it)
        }
    }

    suspend fun version(): org.openziti.edge.model.Version {
        val version = edge.get<ListVersionEnvelope>("/version").data
        i { "controller[${edge.baseUri}] version(${version.version}/${version.revision})"}
        val prefix = version.apiVersions?.run { get("edge-client") ?: get("edge") }?.get("v1")?.path

        prefix?.let {
            edge.setBasePath(it)
        } ?: w {"did not receive expected apiVersions mapping"}
        return version
    }

    internal suspend fun currentIdentity(): IdentityDetail = runCatching {
        edge.get<CurrentIdentityDetailEnvelope>("/current-identity").data
    }.getOrElse {
        convertError(it)
    }

    internal suspend fun currentApiSession(): ApiSession = runCatching {
        edge.get<CurrentApiSessionDetailEnvelope>("/current-api-session").data
    }.getOrElse {
        convertError(it)
    }

    internal suspend fun setAccessToken(token: ZitiAuthenticator.ZitiAccessToken): ApiSession {
        edge.token = token
        return currentApiSession()
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
            edge.post<CurrentApiSessionDetailEnvelope>("/authenticate", auth, queryOf("method" to method)).data
        }.getOrElse {
            convertError(it)
        }

        return apiSession
    }

    suspend fun logout() {
        runCatching {
            edge.delete("/current-api-session")
        }
    }

    fun shutdown() = Unit

    internal suspend fun getServiceUpdates() = runCatching {
        edge.get<ListCurrentApiSessionServiceUpdatesEnvelope>("/current-api-session/service-updates").data
    }.getOrElse {
        convertError(it)
    }

    internal fun getServices(): Flow<Service> = paged { limit, offset ->
        val page = edge.get<ListServicesEnvelope>(
            "/services",
            queryOf("limit" to limit, "offset" to offset, "configTypes" to ALL_CONFIGS)
        )
        page.data to page.meta
    }

    internal suspend fun createNetSession(s: Service, t: SessionType): Session  = runCatching {
        val req = SessionCreate().type(t).serviceId(s.id)
        edge.post<SessionCreateEnvelope>("/sessions", req).data!!
    }.getOrElse { convertError(it) }


    internal fun getServiceTerminators(s: Service): Flow<TerminatorClientDetail> = paged { limit, offset ->
        val page = edge.get<ListClientTerminatorsEnvelope>(
            "/services/${s.id}/terminators",
            queryOf("limit" to limit, "offset" to offset)
        )
        page.data to page.meta
    }

    // routers permitted to host the specified service (the response is not paged)
    internal suspend fun getServiceRouters(s: Service): List<CommonEdgeRouterProperties> = runCatching {
        edge.get<ListServiceEdgeRoutersEnvelope>("/services/${s.id}/edge-routers").data?.edgeRouters
            ?: emptyList()
    }.getOrElse { convertError(it) }

    internal suspend fun postMFA() =
        edge.post<CreateEnvelope>("/current-identity/mfa").data

    internal suspend fun getMFAEnrollment(): DetailMfa? =
        runCatching {
            edge.get<DetailMfaEnvelope>("/current-identity/mfa").data
        }.getOrElse {
            if (it is ZitiHttpException && it.code == 404) {
                null
            } else {
                convertError(it)
            }
        }

    internal suspend fun verifyMFA(code: String) {
        runCatching {
            edge.send("POST", "/current-identity/mfa/verify", body = MfaCode().code(code))
        }.getOrElse { convertError(it) }
    }

    internal suspend fun authMFA(code: String) {
        runCatching {
            edge.send("POST", "/authenticate/mfa", body = MfaCode().code(code))
        }.getOrElse { convertError(it) }
    }

    internal suspend fun removeMFA(code: String) {
        runCatching {
            edge.delete("/current-identity/mfa", mapOf("mfa-validation-code" to code))
        }.getOrElse { convertError(it) }
    }

    internal suspend fun getMFARecoveryCodes(code: String, newCodes: Boolean): Array<String> {
        if (newCodes) {
            val c = MfaCode().code(code)
            runCatching {
                edge.send("POST", "/current-identity/mfa/recovery-codes", body = c)
            }.getOrElse {
                convertError(it)
            }
        }

        return edge.get<DetailMfaEnvelope>("/current-identity/mfa").data.recoveryCodes?.toTypedArray() ?: emptyArray()
    }

    // Pages are fetched one after the other and emitted in order. The server reports the page size it used,
    // so the next offset is taken from that, as before.
    private fun <T> paged(fetch: suspend (limit: Int, offset: Int) -> Pair<List<T>, Meta?>): Flow<T> = flow {
        var offset = 0
        while (true) {
            val (items, meta) = fetch(pageSize, offset)
            items.forEach { emit(it) }

            val pagination = meta?.pagination ?: break
            val limit = pagination.limit.toInt()
            if (limit <= 0) break
            offset += limit
            if (offset >= pagination.totalCount.toInt()) break
        }
    }

    internal suspend fun sendPostureResp(responses: List<PostureResponseCreate>) {
        runCatching {
            edge.send("POST", "/posture-response-bulk", body = responses)
        }.getOrNull()
    }

    internal suspend fun getEdgeRouters() = runCatching {
        edge.get<ListCurrentIdentityEdgeRoutersEnvelope>("/current-identity/edge-routers").data
    }.getOrElse {
        convertError(it)
    }

    private fun convertError(t: Throwable): Nothing {
        val errCode = when (t) {
            is CancellationException -> throw t
            // an answer that is not an error envelope (a proxy's error page, an empty body) says nothing
            // about the request, so it is treated like an unreachable controller: retried
            is ZitiHttpException -> getError(t.responseBody)?.let { ZitiException.getZitiError(it) }
                ?: Errors.ControllerUnavailable
            is IOException -> Errors.ControllerUnavailable
            else -> Errors.WTF(t.toString())
        }

        throw ZitiException(errCode,  t)
    }

    private fun getError(resp: String?): String? = runCatching {
        val err = edgeMapper.readValue(resp, ApiErrorEnvelope::class.java).error
        err.code ?: err.message
    }.getOrNull()

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

    class NotAvailableException(msg: String = "Controller is not available") : Exception(msg)

    companion object: Logged by ZitiLog(Controller.TAG) {
        private const val TAG = "Controller"
        val ALL_CONFIGS = listOf("all")

        suspend fun getActiveController(endpoints: Collection<String>, sslContext: SSLContext): Controller {

            val http = ControllerHttp.client(sslContext)

            val eps = endpoints.shuffled()
            for (ep in eps) {
                d { "attempting to connect to controller $ep" }

                runCatching {
                    // the controller's version document is at the root, whatever path the address has
                    val probe = EdgeClient(http, ep).apply { setBasePath("") }
                    val version = probe.get<ListVersionEnvelope>("/version").data
                    val capabilities = version.capabilities?.map { Capabilities.valueOf(it) } ?: emptyList()

                    val url = version.apiVersions!!["edge"]!!["v1"]!!.apiBaseUrls!![0]
                    Controller(url, sslContext, capabilities.toSet())
                }.onFailure {
                    w { "failed to connect to controller $ep: ${it.message}" }
                }.onSuccess {
                    return it
                }
            }

            throw NotAvailableException()
        }
    }
}
