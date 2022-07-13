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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RestTemplateCaller {

  private final RestTemplate restTemplate;

  @Autowired
  public RestTemplateCaller(@Qualifier("zitiRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Scheduled(initialDelayString = "PT5S", fixedDelayString = "PT15S")
  public void call() {
    final String url = "http://whatismyip.ziti/";
//    final String url = "https://whatismyip.ziti/";
//    final String url = "https://eth0.me";
//    final String url = "https://local.ziti";
    log.info("Calling: {}", url);
    final ResponseEntity<String> msg = restTemplate.getForEntity(url, String.class);
    log.info("Got message: {}", msg);
  }

}
