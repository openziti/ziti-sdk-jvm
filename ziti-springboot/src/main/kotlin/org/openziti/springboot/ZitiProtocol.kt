/*
 * Copyright (c) 2018-2021 NetFoundry Inc.
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

package org.openziti.springboot

import org.apache.coyote.http11.AbstractHttp11JsseProtocol
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.apache.tomcat.util.net.Nio2Channel
import org.openziti.ZitiAddress
import org.openziti.ZitiContext

class ZitiProtocol: AbstractHttp11JsseProtocol<Nio2Channel>(ZitiEndpoint()) {
    private val log = LogFactory.getLog(ZitiProtocol::class.java)

    private val SERVICE_TIMEOUT = 10_000L

    lateinit var ztx: ZitiContext
    lateinit var service: String

    override fun getLog(): Log = log

    override fun getNamePrefix(): String {
        return if (isSSLEnabled()) {
            "https-" + getSslImplementationShortName()+ "-ziti"
        } else {
            "http-ziti"
        }
    }

    override fun start() {
        (endpoint as ZitiEndpoint).also {
            val svc = ztx.getService(service, SERVICE_TIMEOUT)
            it.server = ztx.openServer()
            it.zitiAddress = ZitiAddress.Bind(svc.name)
        }
        super.start()
    }

    override fun getId(): String = service
    override fun getPortOffset(): Int {
        return 0
    }
}