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

/**
 *
 */
object ZitiProtocol {
    val VERSION = byteArrayOf(0x3, 0x6, 0x9, 0xc)

    const val HEADER_LENGTH = 20

    enum class ContentType(val id: Int) {
        HelloType(0),
        PingType(1),
        ResultType(2),
        LatencyType(3),

        //  EDGE
        Connect(60783),
        StateConnected(60784),
        StateClosed(60785),
        Data(60786),
        Dial(60787),
        DialSuccess(60788),
        DialFailed(60789),
        Bind(60790),
        Unbind(60791),
        StateSessionEnded(60792),
        Probe(60793),
        UpdateBind(60794);

        companion object {
            fun fromInt(i: Int): ContentType = values().find { it.id == i } ?: error("unknow content type[${i}]")
        }
    }

    object Header {
        const val ConnectionId: Int = 0
        const val ReplyFor: Int = 1
        const val ResultSuccess: Int = 2
        const val HelloListener: Int = 3

        // Headers in the range 128-255 inclusive will be reflected when creating replies
        val ReflectedHeaderBitMask = (1 shl 7)
        val MaxReflectedHeader = (1 shl 8) - 1

        const val LatencyProbeTime = 128

        const val ConnId = 1000
        const val SeqHeader = 1001
        const val SessionToken = 1002
        const val PublicKeyHeader = 1003
        const val CostHeader = 1004
        const val PrecedenceHeader = 1005
        const val TerminatorIdentityHeader = 1006
        const val TerminatorIdentitySecretHeader = 1007
        const val CallerIdHeader = 1008
        const val CryptoMethodHeader = 1009
        const val FlagsHeader = 1010
        const val AppDataHeader = 1011
    }

    object CryptoMethod {
        const val Libsodium = 0
        const val AES256_GCM = 1
    }

    object EdgeFlags {
        const val FIN = 0x1
    }

}