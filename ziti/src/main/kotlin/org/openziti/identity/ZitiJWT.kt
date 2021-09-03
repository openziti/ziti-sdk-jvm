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

package org.openziti.identity

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SigningKeyResolver
import java.net.URI
import java.net.URL
import java.security.Key
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class ZitiJWT(cl: Claims, val serverKey: Key) {
    private val claims = cl

    val controller: URI by lazy {
        URI.create(claims.get("iss", String::class.java))
    }

    val token: String by lazy {
        claims.get("jti", String::class.java)
    }
    val method: String by lazy {
        claims.get("em", String::class.java)
    }

    val name: String
        get() = claims.get("sub", String::class.java)

    val enrollmentURL: URL
        get() = controller.resolve("/enroll?method=${method}&token=${token}").toURL()

    companion object {
        fun fromJWT(jwt: String): ZitiJWT {
            val tm = JwtTrustManager()

            val jwtParser = Jwts.parserBuilder()
                .setSigningKeyResolver(KeyResolver(tm)).build()

            val claims = jwtParser.parse(jwt).body as Claims
            return ZitiJWT(claims, tm.serverKey)
        }
    }

    class JwtTrustManager : X509TrustManager {
        lateinit var serverKey: Key
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) = TODO("not needed")

        override fun checkServerTrusted(certs: Array<out X509Certificate>?, authType: String) {
            serverKey = checkNotNull(certs?.first()?.publicKey)
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    class KeyResolver(val tm: JwtTrustManager) : SigningKeyResolver {

        override fun resolveSigningKey(header: JwsHeader<*>, claims: Claims): Key? {
            val ssl = SSLContext.getInstance("TLSv1.2").apply {
                init(null, arrayOf(tm), SecureRandom())
            }

            val url = URI.create(claims.get("iss").toString()).toURL()
            val conn = url.openConnection() as HttpsURLConnection
            conn.sslSocketFactory = ssl.socketFactory

            try {
                conn.connect()
                return tm.serverKey
            } finally {
                conn.disconnect()
            }
        }

        // this is not used
        override fun resolveSigningKey(header: JwsHeader<*>, plaintext: String) = TODO()
    }
}