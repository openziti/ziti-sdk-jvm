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

package org.openziti.springboot.client.web.config;

import java.io.IOException;
import java.net.Socket;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.http.ssl.TlsCiphers;
import org.apache.hc.core5.io.Closer;
import org.apache.hc.core5.util.Timeout;
import org.openziti.Ziti;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZitiTlsSocketStrategy extends DefaultClientTlsStrategy {
  private final SSLSocketFactory zitiSslSocketFactory;
  private final HostnameVerificationPolicy hostnameVerificationPolicy;

  public ZitiTlsSocketStrategy(final SSLContext sslContext,
      final HostnameVerificationPolicy hostnameVerificationPolicy,
      final HostnameVerifier hostnameVerifier) {
    super(sslContext, hostnameVerificationPolicy, hostnameVerifier);
    this.zitiSslSocketFactory = Ziti.getSSLSocketFactory(sslContext);
    this.hostnameVerificationPolicy = hostnameVerificationPolicy;
  }

  public ZitiTlsSocketStrategy(final SSLContext sslContext) {
    super(sslContext);
    this.zitiSslSocketFactory = Ziti.getSSLSocketFactory(sslContext);
    this.hostnameVerificationPolicy = HostnameVerificationPolicy.BOTH;
  }

  @Override
  public SSLSocket upgrade(Socket socket, String target, int port, Object attachment, HttpContext context) throws IOException {
    final SSLSocket upgradedSocket = (SSLSocket) zitiSslSocketFactory.createSocket(socket, target, port, true);
    try {
      executeHandshake(upgradedSocket, target, attachment);
      return upgradedSocket;
    } catch (IOException | RuntimeException ex) {
      Closer.closeQuietly(upgradedSocket);
      throw ex;
    }
  }

  // This is based on the private executeHandshake method in AbstractClientTlsStrategy
  private void executeHandshake(
      final SSLSocket upgradedSocket,
      final String target,
      final Object attachment) throws IOException {
    final TlsConfig tlsConfig = attachment instanceof TlsConfig tlsAttachment ? tlsAttachment : TlsConfig.DEFAULT;

    final SSLParameters sslParameters = upgradedSocket.getSSLParameters();
    sslParameters.setProtocols((TLS.excludeWeak(upgradedSocket.getEnabledProtocols())));
    sslParameters.setCipherSuites(TlsCiphers.excludeWeak(upgradedSocket.getEnabledCipherSuites()));
    if (hostnameVerificationPolicy == HostnameVerificationPolicy.BUILTIN || hostnameVerificationPolicy == HostnameVerificationPolicy.BOTH) {
      sslParameters.setEndpointIdentificationAlgorithm(URIScheme.HTTPS.id);
    }
    upgradedSocket.setSSLParameters(sslParameters);

    final Timeout handshakeTimeout = tlsConfig.getHandshakeTimeout();
    if (handshakeTimeout != null) {
      upgradedSocket.setSoTimeout(handshakeTimeout.toMillisecondsIntBound());
    }

    initializeSocket(upgradedSocket);

    if (log.isDebugEnabled()) {
      log.debug("Enabled protocols: {}", (Object) upgradedSocket.getEnabledProtocols());
      log.debug("Enabled cipher suites: {}", (Object) upgradedSocket.getEnabledCipherSuites());
      log.debug("Starting handshake ({})", handshakeTimeout);
    }
    upgradedSocket.startHandshake();
    verifySession(target, upgradedSocket.getSession());
  }

}
