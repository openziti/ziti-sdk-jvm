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
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.openziti.Ziti;
import org.openziti.ZitiContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractZitiConnectionSocketFactory implements ConnectionSocketFactory {

  private static final long DEFAULT_WAIT_TIME = 3;

  @Getter
  private ZitiContext ctx;

  public AbstractZitiConnectionSocketFactory(InputStream inputStream) {
    this(inputStream, DEFAULT_WAIT_TIME);
  }
  public AbstractZitiConnectionSocketFactory(InputStream inputStream, long waitTime) {
    this(inputStream, new char[0], waitTime);
  }

  public AbstractZitiConnectionSocketFactory(InputStream inputStream, char[] pwd) {
    this(inputStream, pwd, DEFAULT_WAIT_TIME);
  }
  public AbstractZitiConnectionSocketFactory(InputStream inputStream, char[] pwd, long waitTime) {
    try {
      ctx = Ziti.newContext(inputStream, pwd);

      // sleep to let ZitiContext initialize
      new CountDownLatch(1).await(waitTime, TimeUnit.SECONDS);

    } catch (Exception e) {
      log.error("Failed to create Ziti context", e);
    }
  }

  public AbstractZitiConnectionSocketFactory(File file) {
    this(file, DEFAULT_WAIT_TIME);
  }

  public AbstractZitiConnectionSocketFactory(File file, long waitTime) {
    this(file, new char[0], waitTime);
  }

  public AbstractZitiConnectionSocketFactory(File file, char[] pwd) {
    this(file, pwd, DEFAULT_WAIT_TIME);
  }
  public AbstractZitiConnectionSocketFactory(File file, char[] pwd, long waitTime) {
    try {
      ctx = Ziti.newContext(file, pwd);

      // sleep to let ZitiContext initialize
      new CountDownLatch(1).await(waitTime, TimeUnit.SECONDS);

    } catch (Exception e) {
      log.error("Failed to create Ziti context", e);
    }
  }

  public AbstractZitiConnectionSocketFactory(String fileName) {
    this(fileName, DEFAULT_WAIT_TIME);
  }

  public AbstractZitiConnectionSocketFactory(String fileName, long waitTime) {
    this(fileName, new char[0], waitTime);
  }

  public AbstractZitiConnectionSocketFactory(String fileName, char[] pwd) {
    this(fileName, pwd, DEFAULT_WAIT_TIME);
  }

  public AbstractZitiConnectionSocketFactory(String fileName, char[] pwd, long waitTime) {
    try {
      ctx = Ziti.newContext(fileName, pwd);

      // sleep to let ZitiContext initialize
      new CountDownLatch(1).await(waitTime, TimeUnit.SECONDS);

    } catch (Exception e) {
      log.error("Failed to create Ziti context", e);
    }
  }

  public void shutdown() {
    Optional.ofNullable(getCtx()).ifPresent(ZitiContext::destroy);
  }

}
