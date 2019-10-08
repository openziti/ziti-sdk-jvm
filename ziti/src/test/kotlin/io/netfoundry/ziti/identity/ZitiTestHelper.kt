/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.identity

import io.netfoundry.ziti.api.Controller
import io.netfoundry.ziti.api.Identity
import io.netfoundry.ziti.api.Login
import kotlinx.coroutines.runBlocking
import java.net.URI
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

internal object ZitiTestHelper {
    val controllerSSL = SSLContext.getInstance("TLS").apply {
        init(null, arrayOf(TrustAll), SecureRandom())
    }

    val objects = mutableListOf<String>()
    var session: String = ""
    lateinit var ctrlURI: URI
    lateinit var controller: Controller

    fun init(url: String, user: String, pass: String) = runBlocking {
        ctrlURI = URI.create(url)
        controller = Controller(ctrlURI.toURL(), controllerSSL, TrustAll)
        val s = controller.login(Login(user, pass))
        session = s.token
    }

    fun createDevice(name: String, enrollment: String = "ott"): Pair<String, Identity> {
        val id = controller.createIdentity(name = name, enrollment = enrollment)
        val device = controller.getIdentity(id.id)
        objects.add("/identities/${id.id}")
        return "/identities/${id.id}" to device!!
    }

    fun getDevice(id: String) = controller.getIdentity(id)

    fun delete(path: String) {
        controller.api.delete(session, path).execute()
    }

    fun cleanup() {
        objects.forEach {
            delete(it)
        }
        delete("/current-session")
    }

    private object TrustAll : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
}
