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
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PreDestroy;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HeaderElements;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.message.MessageSupport;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.openziti.Ziti;
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

  // The default timeout when requesting a connection from the connection manager.
  private static final long DEFAULT_CONNECTION_REQUEST_TIMEOUT = 30000;

  // The default time to wait for data, the maximum time between two consecutive packets of data.
  private static final int DEFAULT_RESPONSE_TIMEOUT = 60000;

  // The default time to keep a connection alive.
  private static final long DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;

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
        .register(URIScheme.HTTPS.getId(), zitiSSLConnectionSocketFactory)
        .register(URIScheme.HTTP.getId(), zitiConnectionSocketFactory)
        .build();

    // Fake dns resolution by always returning localhost
    final DnsResolver dnsResolver = getDnsResolver();

    final PoolingHttpClientConnectionManager poolingConnectionManager =
        new PoolingHttpClientConnectionManager(socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO,
            TimeValue.NEG_ONE_MILLISECOND, null, dnsResolver, null);

    Optional.ofNullable(maxTotal).ifPresent(poolingConnectionManager::setMaxTotal);
    Optional.ofNullable(maxPerRoute).ifPresent(poolingConnectionManager::setDefaultMaxPerRoute);
    return poolingConnectionManager;
  }

  @Bean("zitiConnectionKeepAliveStrategy")
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(
      @Value("${spring.ziti.httpclient.keep-alive-time:}") Long keepAliveTime) {
    return (response, context) -> {
      final Iterator<HeaderElement> it = MessageSupport.iterate(response, HeaderElements.KEEP_ALIVE);
      while (it.hasNext()) {
        HeaderElement he = it.next();
        String param = he.getName();
        String value = he.getValue();

        if (value != null && param.equalsIgnoreCase("timeout")) {
          return TimeValue.of(Long.parseLong(value), TimeUnit.SECONDS);
        }
      }
      return TimeValue.of(Optional.ofNullable(keepAliveTime).orElse(DEFAULT_KEEP_ALIVE_TIME_MILLIS), TimeUnit.MILLISECONDS);
    };
  }

  @Bean("zitiHttpClient")
  public CloseableHttpClient httpClient(
      @Qualifier("zitiPoolingConnectionManager") PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
      @Qualifier("zitiConnectionKeepAliveStrategy") ConnectionKeepAliveStrategy connectionKeepAliveStrategy,
      @Value("${spring.ziti.httpclient.connection-request-timeout:}") Long connectionRequestTimeout,
      @Value("${spring.ziti.httpclient.response-timeout:}") Integer responseTimeout) {

    return HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectionRequestTimeout(Optional.ofNullable(connectionRequestTimeout).orElse(DEFAULT_CONNECTION_REQUEST_TIMEOUT), TimeUnit.MILLISECONDS)
            .setResponseTimeout(Optional.ofNullable(responseTimeout).orElse(DEFAULT_RESPONSE_TIMEOUT), TimeUnit.MILLISECONDS)
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

  private static DnsResolver getDnsResolver() {
    return new DnsResolver() {
      @Override
      public InetAddress[] resolve(String s) throws UnknownHostException {
        return new InetAddress[]{InetAddress.getLocalHost()};
      }

      @Override
      public String resolveCanonicalHostname(String s) throws UnknownHostException {
        return InetAddress.getLocalHost().getCanonicalHostName();
      }
    };
  }

}
