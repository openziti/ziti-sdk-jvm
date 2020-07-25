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

package org.openziti.net

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.junit.Assume
import org.junit.BeforeClass
import org.junit.Test
import org.openziti.Ziti
import org.openziti.Ziti.getDNSResolver
import org.openziti.ZitiContext
import java.net.InetAddress
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


class ZitiSocketFactoryTest {

    companion object {
        lateinit var ctx: ZitiContext
        lateinit var tm: X509TrustManager
        lateinit var dns: Dns
        @BeforeClass
        @JvmStatic
        fun setup() {
            val path = System.getProperty("test.identity")
            Assume.assumeNotNull(path)

            ctx = Ziti.newContext(path, charArrayOf())
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                val ks = KeyStore.getInstance(KeyStore.getDefaultType())
                init(ks)
            }
            tm = tmf.trustManagers[0] as X509TrustManager

            dns = Dns { hostname ->
                var address = getDNSResolver().resolve(hostname)
                if (address == null) {
                    address = InetAddress.getByName(hostname)
                }
                if (address != null) listOf(address) else emptyList()
            }
            runBlocking {
                delay(4000)
            }
        }
    }
    @Test
    fun testHttpClient() {

        val clt = OkHttpClient.Builder()
            .socketFactory(Ziti.getSocketFactory())
            .sslSocketFactory(Ziti.getSSLSocketFactory(), tm)
            .dns(dns)
            .callTimeout(5, TimeUnit.MINUTES)
            .build()

        val req = Request.Builder()
            .post(RequestBody.create(MediaType.get("text/plain"),"this is request body"))
            //.get()
            .url("https://httpbin.org/anything").build()
        val call = clt.newCall(req)
        val resp = call.execute()
        println(resp)
        println(resp.headers())
        val respBody = Gson().fromJson(resp.body()?.byteStream()?.reader(), JsonObject::class.java)
        println(respBody)
    }
}