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

import org.junit.Test
import java.net.Socket
import java.security.cert.X509Certificate
import java.util.logging.Level
import java.util.logging.Logger


class ZitiSSLSocketTest {

    @Test
    fun handshakeTest() {
        val root = Logger.getLogger("")
        root.setLevel(Level.ALL)
        for (h in Logger.getLogger("").getHandlers()) {
            h.setLevel(Level.ALL)
        }
        val s = Socket("google.com", 443)
        val ssl = ZitiSSLSocket(s, "go9gle.com", 443)
        ssl.startHandshake()
        val session = ssl.session
        (session.peerCertificates[0] as X509Certificate).subjectAlternativeNames.forEach {
            println("${it[0]} => ${it[1]}")
        }
    }
}