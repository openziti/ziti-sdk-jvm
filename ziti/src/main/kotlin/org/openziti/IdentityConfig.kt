/*
 * Copyright (c) 2018-2025 NetFoundry Inc.
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.openziti.identity.makeSSLContext
import org.openziti.util.readCerts
import org.openziti.util.readKey
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext

/**
 * Identity loaded from identity configuration JSON.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class IdentityConfig (
    /**
     * Ziti controller address.
     */
    @JsonProperty("ztAPI")
    val controller: String,

    /**
     * List of ziti controller addresses.
     */
    @JsonProperty("ztAPIs")
    val apiEndpoints: Collection<String>? = listOf(controller),

    /**
     * Identity credentials.
     */
    val id: Id): Identity {

    data class Id(
        /** Identity private key in PEM format */
        val key: String? = null,
        /** Identity X.509 certificate in PEM format */
        val cert: String? = null,
        /** OpenZiti network CA bundle in PEM format */
        val ca: String
    )

    /**
     * Store identity configuration to the output stream.
     */
    fun store(output: OutputStream) {
        jsonMapper.writeValue(output, this)
    }

    /**
     * @inheritDoc
     */
    override fun controllers(): Collection<String> = apiEndpoints ?: listOf(controller)

    /**
     * @inheritDoc
     */
    override fun sslContext(): SSLContext = makeSSLContext(key(), cert(), caCerts())

    internal fun key(): PrivateKey? = id.key?.let { readKey(it) }

    internal fun cert(): List<X509Certificate> = id.cert?.let {
        readCerts(it)
    } ?: emptyList()

    internal fun caCerts() = readCerts(id.ca)

    companion object {
        val jsonMapper: ObjectMapper =
            ObjectMapper().registerModule(kotlinModule())
        /**
         * Load identity configuration from the input stream.
         */
        @JvmStatic
        fun load(input: InputStream): IdentityConfig =
            jsonMapper.readValue(input, IdentityConfig::class.java)

        /**
         * Load identity configuration from the byte array.
         */
        @JvmStatic
        fun load(cfg: ByteArray): IdentityConfig = load(cfg.inputStream())

        /**
         * Load identity configuration from the file.
         */
        @JvmStatic
        fun load(f: File): IdentityConfig = load(f.inputStream())

        /**
         * Load identity configuration from the file path or JSON string.
         */
        @JvmStatic
        fun load(cfg: String): IdentityConfig {
            val file = File(cfg)
            if (file.exists()) {
                return load(file)
            }

            return load(cfg.toByteArray())
        }
    }
}