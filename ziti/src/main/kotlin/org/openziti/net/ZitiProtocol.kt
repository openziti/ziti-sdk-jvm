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

import org.openziti.edge.proto.EdgeClient.Flag
import org.openziti.edge.proto.EdgeClient.HeaderId
import org.openziti.edge.proto.EdgeClient.ContentType as CT

/**
 *
 */
object ZitiProtocol {
    val VERSION = byteArrayOf(0x3, 0x6, 0x9, 0xc)

    const val HEADER_LENGTH = 20

    enum class ContentType(val id: Int) {

        HelloType(CT.Hello),
        PingType(CT.Ping),
        ResultType(CT.Result),
        LatencyType(CT.Latency),

        //  EDGE
        Connect(CT.ConnectType),
        StateConnected(CT.StateConnectedType),
        StateClosed(CT.StateClosedType),
        Data(CT.DataType),
        Dial(CT.DialType),
        DialSuccess(CT.DialSuccessType),
        DialFailed(CT.DialFailedType),
        Bind(CT.BindType),
        Unbind(CT.UnbindType),
        StateSessionEnded(CT.StateSessionEndedType),
        Probe(CT.ProbeType),
        UpdateBind(CT.UnbindType),
        HealthEvent(CT.HealthEventType),
        TraceRoute(CT.TraceRouteType),
        TraceRouteResponse(CT.TraceRouteResponseType),
        ConnInspectRequest(CT.ConnInspectRequest),
        ConnInspectResponse(CT.ConnInspectResponse),
        TokenUpdate(CT.UpdateTokenType),
        TokenUpdateSuccess(CT.UpdateTokenSuccessType),
        TokenUpdateFailure(CT.UpdateTokenFailureType);
        
        constructor(ct: CT): this(ct.number)
    }

    enum class Header(val id: Int) {
        ReplyFor(1),
        ResultSuccess(2),
        HelloListener(3),

        ConnId(HeaderId.ConnId),
        SeqHeader(HeaderId.Seq),
        SessionToken(HeaderId.SessionToken),
        PublicKeyHeader(HeaderId.PublicKey),
        CostHeader(HeaderId.Cost),
        PrecedenceHeader(HeaderId.Precedence),
        TerminatorIdentityHeader(HeaderId.TerminatorIdentity),
        TerminatorIdentitySecretHeader(HeaderId.TerminatorIdentitySecret),
        CallerIdHeader(HeaderId.CallerId),
        CryptoMethodHeader(HeaderId.CryptoMethod),
        FlagsHeader(HeaderId.Flags),
        AppDataHeader(HeaderId.AppData),
        RouterProvidedConnId(HeaderId.RouterProvidedConnId),
        HealthStatusHeader(HeaderId.HealthStatus),
        ErrorCodeHeader(HeaderId.ErrorCode),
        TimestampHeader(HeaderId.Timestamp),
        TraceHopCountHeader(HeaderId.TraceHopCount),
        TraceHopTypeHeader(HeaderId.TraceHopType),
        TraceHopIdHeader(HeaderId.TraceHopId),
        TraceSourceRequestIdHeader(HeaderId.TraceSourceRequestId),
        TraceError(HeaderId.TraceError),
        ListenerId(HeaderId.ListenerId),
        ConnTypeHeader(HeaderId.ConnType),
        SupportsInspectHeader(HeaderId.SupportsInspect),
        SupportsBindSuccessHeader(HeaderId.SupportsBindSuccess),
        ConnectionMarkerHeader(HeaderId.ConnectionMarker);

        constructor(hdr: HeaderId): this(hdr.number)
    }

    object CryptoMethod {
        const val Libsodium = 0
        const val AES256_GCM = 1
    }

    object EdgeFlags {
        const val FIN = Flag.FIN_VALUE
        const val TRACE_UUID = Flag.TRACE_UUID_VALUE
        const val MULTIPART = Flag.MULTIPART_VALUE
        const val STREAM = Flag.STREAM_VALUE
        const val MULTIPART_MSG = Flag.MULTIPART_MSG_VALUE
    }

    class UnknownContent(val id: Int): Exception("unknown content type: $id")
    fun contentType(i: Int): Result<ContentType> {
        val v = ContentType.entries.find { it.id == i }
            return v?.let { Result.success(it) } ?: Result.failure(UnknownContent(i))
    }
}