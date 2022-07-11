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
import javax.net.SocketFactory;
import org.apache.http.HttpHost;
import org.apache.http.protocol.HttpContext;
import org.openziti.Ziti;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZitiConnectionSocketFactory extends AbstractZitiConnectionSocketFactory {

  private SocketFactory socketFactory;

  public ZitiConnectionSocketFactory(InputStream inputStream) {
    super(inputStream);
  }
  public ZitiConnectionSocketFactory(InputStream inputStream, long waitTime) {
    super(inputStream, waitTime);
  }

  public ZitiConnectionSocketFactory(InputStream inputStream, char[] pwd) {
    super(inputStream, pwd);
  }
  public ZitiConnectionSocketFactory(InputStream inputStream, char[] pwd, long waitTime) {
    super(inputStream, pwd, waitTime);
  }

  public ZitiConnectionSocketFactory(File file) {
    super(file);
  }
  public ZitiConnectionSocketFactory(File file, long waitTime) {
    super(file, waitTime);
  }

  public ZitiConnectionSocketFactory(File file, char[] pwd) {
    super(file, pwd);
  }
  public ZitiConnectionSocketFactory(File file, char[] pwd, long waitTime) {
    super(file, pwd, waitTime);
  }

  public ZitiConnectionSocketFactory(String fileName) {
    super(fileName);
  }
  public ZitiConnectionSocketFactory(String fileName, long waitTime) {
    super(fileName, waitTime);
  }

  public ZitiConnectionSocketFactory(String fileName, char[] pwd) {
    super(fileName, pwd);
  }
  public ZitiConnectionSocketFactory(String fileName, char[] pwd, long waitTime) {
    super(fileName, pwd, waitTime);
  }

  @Override
  public Socket createSocket(HttpContext context) throws IOException {
    if (socketFactory == null) {
      this.socketFactory = Ziti.getSocketFactory();
    }
    return socketFactory.createSocket();
  }

  @Override
  public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
      InetSocketAddress localAddress, HttpContext context) throws IOException {

    final Socket sock = socket != null ? socket : createSocket(context);
    sock.connect(new InetSocketAddress(host.getHostName(), host.getPort()), connectTimeout);
    if (localAddress != null) {
      sock.bind(localAddress);
    }
    return sock;
  }

}
