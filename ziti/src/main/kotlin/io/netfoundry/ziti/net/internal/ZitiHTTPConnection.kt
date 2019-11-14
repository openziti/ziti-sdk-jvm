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

package io.netfoundry.ziti.net.internal

import io.netfoundry.ziti.net.ZitiSocketImpl
import io.netfoundry.ziti.net.dns.ZitiDNSManager
import io.netfoundry.ziti.util.JULogged
import io.netfoundry.ziti.util.Logged
import okhttp3.*
import okhttp3.internal.http.HttpMethod
import okio.BufferedSink
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.Socket
import java.net.URL
import java.security.KeyStore
import java.security.cert.Certificate
import javax.net.SocketFactory
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class ZitiHTTPConnection(url: URL) :
    HttpURLConnection(url),
    Logged by JULogged("ziti-http")
{
    override fun usingProxy(): Boolean = false

    val body = ByteArrayOutputStream(1024)
    val req = Request.Builder().apply {
        url(this@ZitiHTTPConnection.url)
    }

    lateinit var call: Call

    override fun setRequestMethod(method: String) {
        super.setRequestMethod(method)
        if (HttpMethod.permitsRequestBody(method)) {
            req.method(method, object : RequestBody(){
                override fun contentType(): MediaType? {
                    return req.build().header("Content-Type")?.let { MediaType.parse(it) }
                }

                override fun contentLength(): Long {
                    return body.size().toLong()
                }

                override fun writeTo(sink: BufferedSink) {
                    sink.write(body.toByteArray())
                }
            })
        } else {
            req.method(method, null)
        }
    }

    override fun setRequestProperty(key: String?, value: String?) {
        super.setRequestProperty(key, value)
        req.header(key, value)
    }

    override fun addRequestProperty(key: String?, value: String?) {
        super.addRequestProperty(key, value)
        req.addHeader(key, value)
    }

    override fun connect() {
        execute()
    }

    override fun disconnect() {
        d("disconnect()")
    }

    override fun getResponseCode(): Int {
        return execute().code()
    }

    override fun getResponseMessage(): String {
        return execute().message()
    }

    override fun getHeaderField(name: String?): String? {
        return execute().header(name)
    }

    override fun getContentLengthLong(): Long {
        return execute().body()?.contentLength() ?: 0
    }

    override fun getHeaderFields(): MutableMap<String, MutableList<String>> {
        return execute().headers().toMultimap()
    }

    override fun getContentLength(): Int {
        return execute().body()?.contentLength()?.toInt() ?: 0
    }

    override fun getOutputStream(): OutputStream {
        return body
    }

    override fun getInputStream(): InputStream {
        execute()
        return response?.body()?.byteStream()!!
    }

    inner class Output : OutputStream() {
        override fun write(b: Int) = error("not implemented")
    }

    fun execute(): Response {
        if (response == null) {
            val request = req.build()
            d("executing $request")
            val newCall = clt.newCall(request)
            response = newCall.execute()
            d("finished executing ${response}")
        }

        return response!!
    }

    var response: Response? = null

    companion object {

        val factory = object : SocketFactory(){
            override fun createSocket(): Socket = ZitiSocket(ZitiSocketImpl())

            override fun createSocket(p0: String?, p1: Int): Socket {
                error("not implemented")
            }

            override fun createSocket(p0: String?, p1: Int, p2: InetAddress?, p3: Int): Socket {
                error("not implemented")
            }

            override fun createSocket(p0: InetAddress?, p1: Int): Socket {
                error("not implemented")
            }

            override fun createSocket(p0: InetAddress?, p1: Int, p2: InetAddress?, p3: Int): Socket {
                error("not implemented")
            }
        }

        val clt = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .socketFactory(factory)
            .dns{ hostname ->
                mutableListOf(ZitiDNSManager.resolve(hostname))
            }
            .build()

    }
}