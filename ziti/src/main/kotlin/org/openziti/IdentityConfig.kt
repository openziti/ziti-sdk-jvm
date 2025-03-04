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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
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
@Serializable data class IdentityConfig (
    /**
     * Ziti controller address.
     */
    @SerialName("ztAPI") val controller: String,

    /**
     * List of ziti controller addresses.
     */
    @SerialName("ztAPIs") val controllers: Collection<String> = listOf(controller),

    /**
     * Identity credentials.
     */
    val id: Id): Identity {

    @Serializable data class Id(
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
        output.write(json.encodeToString(this).encodeToByteArray())
    }

    /**
     * @inheritDoc
     */
    override fun controllers(): Collection<String> = controllers

    /**
     * @inheritDoc
     */
    override fun sslContext(): SSLContext = makeSSLContext(key, cert, caCerts)

    internal val key: PrivateKey? by lazy {
        id.key?.let {
            readKey(it)
        }
    }

    internal val cert: List<X509Certificate> by lazy {
        id.cert?.let {
            readCerts(it)
        } ?: emptyList()
    }

    internal val caCerts by lazy {
        readCerts(id.ca)
    }


    companion object {

        /**
         * Load identity configuration from the input stream.
         */
        @OptIn(ExperimentalSerializationApi::class)
        @JvmStatic
        fun load(input: InputStream): IdentityConfig = Json.decodeFromStream(serializer(), input)

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

        private val json = Json {
            prettyPrint = true
        }
    }
}