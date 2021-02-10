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

package org.openziti.identity

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.openziti.api.Controller
import org.openziti.api.Identity
import org.openziti.api.Login
import java.io.File
import java.net.URI
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import kotlin.test.fail

internal object ZitiTestHelper {

    val controllerSSL = SSLContext.getInstance("TLSv1.2").apply {
        init(null, arrayOf(TrustAll), SecureRandom())
    }

    private val objects = mutableListOf<String>()
    var session: String = ""
    lateinit var ctrlURI: URI
    var controller: Controller? = null

    fun init() = runBlocking(Dispatchers.IO) {

        val props = File(System.getProperty("ziti-test.properties")).let {
            if (it.exists())
                Properties().apply {
                    load(it.inputStream())
                }
            else
                fail("test properties not found")
        }

        val username = props.getProperty("username") ?: fail("need admin username")
        val password = props.getProperty("password") ?: fail("need admin password")
        val url = props.getProperty("controller") ?: fail("need controller")

        ctrlURI = URI.create(url)
        controller = Controller(ctrlURI.toURL(), controllerSSL, TrustAll)

        val s = controller!!.login(Login(username, password))
        session = s.token
    }

    fun createDevice(name: String, enrollment: String = "ott"): Pair<String, Identity> {
        val id = controller!!.createIdentity(name = name, enrollment = enrollment)
        val device = controller!!.getIdentity(id.id)
        objects.add("/identities/${id.id}")
        return "/identities/${id.id}" to device!!
    }

    fun getDevice(id: String) = controller!!.getIdentity(id)

    fun delete(path: String) {
        controller?.api?.delete(session, path)?.execute()
    }

    fun cleanup() {
        objects.forEach {
            delete(it)
        }
    }

    fun cleanupSession() {
        delete("/current-session")
    }

    internal object TrustAll : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    internal object TrustNoOne : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) { throw CertificateException("no ziti for you") }
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
}
