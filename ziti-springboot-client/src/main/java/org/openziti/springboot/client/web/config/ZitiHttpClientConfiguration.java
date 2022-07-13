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

package org.openziti.springboot.client.web.config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;
import javax.annotation.PreDestroy;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.openziti.springboot.client.web.httpclient.ZitiConnectionSocketFactory;
import org.openziti.springboot.client.web.httpclient.ZitiSSLConnectionSocketFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ZitiHttpClientConfiguration {

  // The default timeout in milliseconds until a connection is established.
  private static final int DEFAULT_CONNECT_TIMEOUT = 30000;

  // The default timeout when requesting a connection from the connection manager.
  private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 30000;

  // The default time to wait for data, the maximum time between two consecutive packets of data.
  private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

  // The default time to keep a connection alive.
  private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;

  private ZitiConnectionSocketFactory zitiConnectionSocketFactory;
  private ZitiSSLConnectionSocketFactory zitiSSLConnectionSocketFactory;

  @ConditionalOnMissingBean(name = "zitiRestTemplate")
  @Bean("zitiRestTemplate")
  public RestTemplate restTemplate(@Qualifier("zitiRestTemplateBuilder") RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @ConditionalOnMissingBean(name = "zitiRestTemplateBuilder")
  @Bean("zitiRestTemplateBuilder")
  public RestTemplateBuilder restTemplateBuilder(@Qualifier("zitiHttpClient") HttpClient httpClient) {
    return new RestTemplateBuilder()
        .requestFactory(() -> clientHttpRequestFactory(httpClient));
  }

  @ConditionalOnMissingBean(name = "zitiClientHttpRequestFactory")
  @Bean("zitiClientHttpRequestFactory")
  public ClientHttpRequestFactory clientHttpRequestFactory(@Qualifier("zitiHttpClient") HttpClient httpClient) {
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setHttpClient(httpClient);
    return clientHttpRequestFactory;
  }

  @ConditionalOnMissingBean(ZitiConnectionSocketFactory.class)
  @Bean
  public ZitiConnectionSocketFactory zitiConnectionSocketFactory(
      @Value("${spring.ziti.httpclient.identity.file:}") Resource identityFile,
      @Value("${spring.ziti.httpclient.identity.password:}") String password) throws IOException {

    if (zitiConnectionSocketFactory == null) {

      if (identityFile == null) {
        throw new BeanCreationException("No ZitiConnectionSocketFactory defined. Define one with @Bean or add a property named "
            + "`spring.ziti.httpclient.identity.file` pointing to a ziti identity file.");
      }

      zitiConnectionSocketFactory = new ZitiConnectionSocketFactory(identityFile.getInputStream(),
          Optional.ofNullable(password).map(String::toCharArray).orElse(new char[0]));
    }
    return zitiConnectionSocketFactory;
  }

  @ConditionalOnMissingBean(ZitiSSLConnectionSocketFactory.class)
  @Bean
  public ZitiSSLConnectionSocketFactory zitiSSLConnectionSocketFactory(
      @Value("${spring.ziti.httpclient.identity.file:}") Resource identityFile,
      @Value("${spring.ziti.httpclient.identity.password:}") String password) throws IOException {

    if (zitiSSLConnectionSocketFactory == null) {
      if (identityFile == null) {
        throw new BeanCreationException("No ZitiConnectionSocketFactory defined. Define one with @Bean or add a property named "
            + "`spring.ziti.httpclient.identity.file` pointing to a ziti identity file.");
      }

      zitiSSLConnectionSocketFactory = new ZitiSSLConnectionSocketFactory(identityFile.getInputStream(),
          Optional.ofNullable(password).map(String::toCharArray).orElse(new char[0]));
    }
    return zitiSSLConnectionSocketFactory;
  }

  @Bean("zitiPoolingConnectionManager")
  public PoolingHttpClientConnectionManager poolingConnectionManager(
      ZitiConnectionSocketFactory zitiConnectionSocketFactory,
      ZitiSSLConnectionSocketFactory zitiSSLConnectionSocketFactory,
      @Value("${spring.ziti.httpclient.max-total:}") Integer maxTotal,
      @Value("${spring.ziti.httpclient.max-per-route:}") Integer maxPerRoute) {

    Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("https", zitiSSLConnectionSocketFactory)
        .register("http", zitiConnectionSocketFactory)
        .build();

    // Fake dns resolution by always returning localhost
    final DnsResolver dnsResolver = host -> new InetAddress[]{InetAddress.getLocalHost()};

    PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, dnsResolver);
    Optional.ofNullable(maxTotal).ifPresent(poolingConnectionManager::setMaxTotal);
    Optional.ofNullable(maxPerRoute).ifPresent(poolingConnectionManager::setDefaultMaxPerRoute);
    return poolingConnectionManager;
  }

  @Bean("zitiConnectionKeepAliveStrategy")
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(
      @Value("${spring.ziti.httpclient.keep-alive-time:}") Integer keepAliveTime) {
    return (response, context) -> {
      HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
      while (it.hasNext()) {
        HeaderElement he = it.nextElement();
        String param = he.getName();
        String value = he.getValue();

        if (value != null && param.equalsIgnoreCase("timeout")) {
          return Long.parseLong(value) * 1000;
        }
      }
      return Optional.ofNullable(keepAliveTime).orElse(DEFAULT_KEEP_ALIVE_TIME_MILLIS);
    };
  }

  @Bean("zitiHttpClient")
  public CloseableHttpClient httpClient(
      @Qualifier("zitiPoolingConnectionManager") PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
      @Qualifier("zitiConnectionKeepAliveStrategy") ConnectionKeepAliveStrategy connectionKeepAliveStrategy,
      @Value("${spring.ziti.httpclient.connection-request-timeout:}") Integer connectionRequestTimeout,
      @Value("${spring.ziti.httpclient.connect-timeout:}") Integer connectTimeout,
      @Value("${spring.ziti.httpclient.socket-timeout:}") Integer socketTimeout) {

    return HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectionRequestTimeout(Optional.ofNullable(connectionRequestTimeout).orElse(DEFAULT_CONNECTION_REQUEST_TIMEOUT))
            .setConnectTimeout(Optional.ofNullable(connectTimeout).orElse(DEFAULT_CONNECT_TIMEOUT))
            .setSocketTimeout(Optional.ofNullable(socketTimeout).orElse(DEFAULT_SOCKET_TIMEOUT))
            .build())
        .setConnectionManager(poolingHttpClientConnectionManager)
        .setKeepAliveStrategy(connectionKeepAliveStrategy)
        .build();
  }

  @PreDestroy
  public void destroy() {
    Optional.ofNullable(zitiConnectionSocketFactory).ifPresent(ZitiConnectionSocketFactory::shutdown);
    Optional.ofNullable(zitiSSLConnectionSocketFactory).ifPresent(ZitiSSLConnectionSocketFactory::shutdown);
  }

}
