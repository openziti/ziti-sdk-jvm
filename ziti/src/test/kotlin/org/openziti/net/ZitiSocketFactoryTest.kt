/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
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
                listOf(getDNSResolver().resolve(hostname) ?: InetAddress.getByName(hostname)).filterNotNull()
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
            .url("http://httpbin.ziti/anything").build()
        val call = clt.newCall(req)
        val resp = call.execute()
        println(resp)
        println(resp.headers())
        val respBody = Gson().fromJson(resp.body()?.byteStream()?.reader(), JsonObject::class.java)
        println(respBody)
    }

    @Test
    fun testConnect() {
        val req = "GET /json HTTP/1.1\r\n"+
                "Accept: */*\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: close\r\n" +
                "Host: httpbin.org\r\n" +
                "User-Agent: HTTPie/1.0.2\r\n" +
                "\r\n"

        val zock = ctx.connect("httpbin.ziti", 80)
        zock.getOutputStream().writer().apply {
            write(req)
            flush()
        }

        val resp = zock.getInputStream().reader().readLines()
        assert(resp[0].startsWith("HTTP/1.1 200"))
        println(resp)
    }
}