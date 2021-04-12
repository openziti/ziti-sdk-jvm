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

package org.openziti.crypto

import com.goterl.lazysodium.LazySodium
import com.goterl.lazysodium.utils.Key
import com.goterl.lazysodium.utils.KeyPair
import com.goterl.lazysodium.utils.SessionPair
import java.util.*

object Crypto {
    interface SecretStream {
        fun header(): ByteArray
        fun init(peerHeader: ByteArray)
        fun initialized(): Boolean
        fun encrypt(b: ByteArray): ByteArray
        fun decrypt(b: ByteArray): ByteArray
    }

    internal val sodium: LazySodium

    fun newKeyPair(): KeyPair = sodium.cryptoKxKeypair()

    fun kx(pair: KeyPair, peerPub: Key, server: Boolean): SessionPair =
        if(server)
            sodium.cryptoKxServerSessionKeys(pair.publicKey, pair.secretKey, peerPub)
        else
            sodium.cryptoKxClientSessionKeys(pair.publicKey, pair.secretKey, peerPub)

    fun newStream(pair: SessionPair): SecretStream = StreamImpl(pair)

    init {
        val sl = ServiceLoader.load(CryptoLoader::class.java)
        val ld = sl.firstOrNull() ?: JavaCryptoLoader()
        sodium = ld.load()
    }
}