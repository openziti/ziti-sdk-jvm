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
import io.netfoundry.ziti.util.Version
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import java.net.URI
import java.security.KeyStore
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 */
class EnrollTest {

    @Rule
    @JvmField
    val tn = TestName()

    companion object {
        val controllerURL = "https://demo.ziti.netfoundry.io:1080/"
        val username = "admin"
        val password = "admin"
    }


    internal lateinit var device: Identity
    var deviceURI: String? = null
    var enrollmentJWT: String? = null

    @Before
    fun controllerInit() {
        ZitiTestHelper.init(controllerURL, username, password)
    }

    @After
    fun controllerCleanup() {
        ZitiTestHelper.cleanup()
    }

    @Before
    fun init() {
        val device = ZitiTestHelper.createDevice(tn.methodName)
        deviceURI = device.first
        this.device = device.second
        enrollmentJWT = device.second.enrollment?.ott?.jwt
    }

    var keyStore: KeyStore? = null

    @After
    fun cleanUp() {
        keyStore?.let {
            for (a in it.aliases()) {
                it.deleteEntry(a)
            }
        }
    }

    @Test
    fun enrollWithJWT() = runBlocking {
        val enroller = Enroller.fromJWT(enrollmentJWT!!)

        val defaultType = "PKCS12"
        val ks = KeyStore.getInstance(defaultType).apply {
            load(null)
        }

        val alias = "test-identity"
        enroller.enroll(null, ks, alias)

        val deviceCheck = ZitiTestHelper.getDevice(device.id)

        assertNotNull(deviceCheck)
        assertNull("JWT should be removed", deviceCheck?.enrollment?.ott)
        assertTrue("keystore entry is incorrect", ks.isKeyEntry(alias))

        val e = ks.getEntry(alias, KeyStore.PasswordProtection(charArrayOf()))

        val ksFile = createTempFile(tn.methodName, ".p12")
        val p12pwd = generateSequence {
            Random.nextInt().absoluteValue
        }.take(14).map { it.toString(16) }.joinToString("")

        ks.store(ksFile.outputStream(), p12pwd.toCharArray())

        val ks1 = KeyStore.getInstance(defaultType).apply {
            load(ksFile.inputStream(), p12pwd.toCharArray())
        }

        val identiity = KeyStoreIdentity(ks1, alias)
        //verify login with cert

        assertEquals("ctrlURI URL", controllerURL, identiity.controller())
        assertNotNull("Name", identiity.name())

        val controller =
            Controller(URI.create(identiity.controller()).toURL(), identiity.sslContext(), identiity.trustManager())
        val loginResp = controller.login()
        assertNotNull("login with device cert", loginResp.token)
        controller.logout()
    }

    @Test
    fun testVersionInfo() {
        val info = Version.VersionInfo

        println(info)
    }
}
