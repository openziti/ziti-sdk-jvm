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
package org.openziti.sample.springboot.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestSampleCaller {

  private final RestTemplate restTemplate;
  private final RestClient restClient;

  @Autowired
  public RestSampleCaller(@Qualifier("zitiRestTemplate") RestTemplate restTemplate, @Qualifier("zitiRestClient") RestClient restClient) {
    this.restTemplate = restTemplate;
    this.restClient = restClient;

  }

  @Scheduled(initialDelayString = "PT5S", fixedDelayString = "PT15S")
  public void call() {
    final String url = "http://whatismyip.ziti/";
//    final String url = "https://whatismyip.ziti/";
//    final String url = "https://eth0.me";
//    final String url = "https://local.ziti";
    log.info("Calling w RestClient: {}", url);
    final String msg1 =restClient.get()
        .uri(url)
        .retrieve()
        .body(String.class);
    log.info("Got message: {}", msg1);

    log.info("Calling w RestTemplate: {}", url);
    final ResponseEntity<String> msg2 = restTemplate.getForEntity(url, String.class);
    log.info("Got message: {}", msg2);
  }

}
