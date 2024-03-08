/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
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

package org.openziti.springboot.client.web.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.openziti.Ziti;
import org.openziti.ZitiContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZitiSSLConnectionSocketFactory extends AbstractZitiConnectionSocketFactory {

  private SSLSocketFactory sslSocketFactory;

  public ZitiSSLConnectionSocketFactory(InputStream inputStream) {
    super(inputStream);
  }
  public ZitiSSLConnectionSocketFactory(InputStream inputStream, long waitTime) {
    super(inputStream, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(InputStream inputStream, char[] pwd) {
    super(inputStream, pwd);
  }
  public ZitiSSLConnectionSocketFactory(InputStream inputStream, char[] pwd, long waitTime) {
    super(inputStream, pwd, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(File file) {
    super(file);
  }
  public ZitiSSLConnectionSocketFactory(File file, long waitTime) {
    super(file, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(File file, char[] pwd) {
    super(file, pwd);
  }
  public ZitiSSLConnectionSocketFactory(File file, char[] pwd, long waitTime) {
    super(file, pwd, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(String fileName) {
    super(fileName);
  }
  public ZitiSSLConnectionSocketFactory(String fileName, long waitTime) {
    super(fileName, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(String fileName, char[] pwd) {
    super(fileName, pwd);
  }
  public ZitiSSLConnectionSocketFactory(String fileName, char[] pwd, long waitTime) {
    super(fileName, pwd, waitTime);
  }

  public ZitiSSLConnectionSocketFactory(ZitiContext ctx) {
    super(ctx);
  }

  @Override
  public Socket createSocket(HttpContext context) {
    return new Socket();
  }


  @Override
  public Socket connectSocket(TimeValue timeValue, Socket socket, HttpHost host, InetSocketAddress inetSocketAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {

    if (this.sslSocketFactory == null) {
      try {
        final SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build();
        sslSocketFactory = Ziti.getSSLSocketFactory(sslContext);
      } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
        throw new RuntimeException(e);
      }
    }

    final InetSocketAddress address = new InetSocketAddress(host.getHostName(), host.getPort());
    final Socket sock = sslSocketFactory.createSocket(address.getAddress(), address.getPort());
    if (localAddress != null) {
      sock.bind(localAddress);
    }
    return sock;
  }

}
