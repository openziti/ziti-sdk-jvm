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

package io.netfoundry.ziti.net.nio;

import javax.net.ssl.SSLSocket;

abstract public class SSLSocketShim extends SSLSocket {
    /**
     * Returns the most recent application protocol value negotiated for this
     * connection.
     * <p>
     * If supported by the underlying SSL/TLS/DTLS implementation,
     * application name negotiation mechanisms such as <a
     * href="http://www.ietf.org/rfc/rfc7301.txt"> RFC 7301 </a>, the
     * Application-Layer Protocol Negotiation (ALPN), can negotiate
     * application-level values between peers.
     *
     * @return null if it has not yet been determined if application
     * protocols might be used for this connection, an empty
     * {@code String} if application protocols values will not
     * be used, or a non-empty application protocol {@code String}
     * if a value was successfully negotiated.
     * @throws UnsupportedOperationException if the underlying provider
     *                                       does not implement the operation.
     * @implSpec The implementation in this class throws
     * {@code UnsupportedOperationException} and performs no other action.
     * @since 8
     */
    public String getApplicationProtocol() {
        return null;
    }
}
