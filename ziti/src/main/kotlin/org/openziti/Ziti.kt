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

package org.openziti

import org.openziti.impl.ZitiImpl
import org.openziti.net.ZitiSSLSocketFactory
import org.openziti.net.ZitiSocketFactory
import org.openziti.net.dns.DNSResolver
import org.openziti.net.dns.ZitiDNSManager
import java.io.File
import java.security.KeyStore
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory

object Ziti {
    @JvmStatic
    fun newContext(idFile: File, pwd: CharArray): ZitiContext = ZitiImpl.loadContext(idFile, pwd, null)

    @JvmStatic
    fun newContext(fname: String, pwd: CharArray): ZitiContext = newContext(File(fname), pwd)

    @JvmStatic
    fun init(fname: String, pwd: CharArray, seamless: Boolean) = ZitiImpl.init(File(fname), pwd, seamless)

    @JvmStatic
    fun init(ks: KeyStore, seamless: Boolean) =  ZitiImpl.init(ks, seamless)

    @JvmStatic
    fun enroll(ks: KeyStore, jwt: ByteArray, name: String) = ZitiImpl.enroll(ks, jwt, name)

    @JvmStatic
    fun getSocketFactory(): SocketFactory = ZitiSocketFactory()

    @JvmStatic
    fun getSSLSocketFactory(): SSLSocketFactory = ZitiSSLSocketFactory()

    @JvmStatic
    fun getDNSResolver(): DNSResolver = ZitiDNSManager
}