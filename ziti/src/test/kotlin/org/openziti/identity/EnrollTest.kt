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

import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.rules.TestName
import org.openziti.api.Controller
import org.openziti.api.Identity
import java.net.URI
import java.net.URL
import java.security.KeyStore
import kotlin.io.path.createTempFile
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 */
class EnrollTest {

    @Rule
    @JvmField
    val tn = TestName()

    lateinit var controllerURL: URL

    internal lateinit var device: Identity
    var deviceURI: String? = null
    var enrollmentJWT: String? = null


    @After
    fun controllerCleanup() {
        ZitiTestHelper.cleanup()
    }

    companion object {

        var initSuccess: Boolean = false
        @BeforeClass
        @JvmStatic
        fun initTest() {
            kotlin.runCatching {
                ZitiTestHelper.init()
            }.onSuccess { initSuccess = true }
        }

        @AfterClass
        @JvmStatic
        fun cleanupTest() {
            ZitiTestHelper.cleanupSession()
        }
    }

    @Before
    fun init() {
        Assume.assumeTrue(initSuccess)

        controllerURL = ZitiTestHelper.ctrlURI.toURL()

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

        val name = "test-identity"
        enroller.enroll(null, ks, name)

        val alias = "ziti://${controllerURL.host}:${controllerURL.port}/$name"

        val deviceCheck = ZitiTestHelper.getDevice(device.id)

        assertNotNull(deviceCheck)
        assertNull("JWT should be removed", deviceCheck?.enrollment?.ott)
        assertTrue("keystore entry is incorrect", ks.isKeyEntry(alias))

        val e = ks.getEntry(alias, KeyStore.PasswordProtection(charArrayOf()))

        val ksFile = createTempFile(tn.methodName, ".p12").toFile()
        val p12pwd = generateSequence {
            Random.nextInt().absoluteValue
        }.take(14).map { it.toString(16) }.joinToString("")

        ks.store(ksFile.outputStream(), p12pwd.toCharArray())

        val ks1 = KeyStore.getInstance(defaultType).apply {
            load(ksFile.inputStream(), p12pwd.toCharArray())
        }

        val identity = KeyStoreIdentity(ks1, alias)
        //verify login with cert

        assertEquals("ctrlURI URL", controllerURL.toString(), identity.controller())
        assertNotNull("Name", identity.name())

        val controller =
            Controller(URI.create(identity.controller()).toURL(), identity.sslContext(), identity.trustManager())
        val loginResp = controller.login()
        assertNotNull("login with device cert", loginResp.token)
        controller.logout()
    }
}
