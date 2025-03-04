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

package org.openziti.samples;

import org.openziti.IdentityConfig;
import org.openziti.Ziti;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * demonstrates subscribing to Ziti context and service events.
 */
public class ZitiContextEvents {
    private static final Logger logger = LoggerFactory.getLogger(ZitiContextEvents.class);

    static void usage() {
        System.err.println("Usage: ListServices <identity-file>");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        }

        try {
            var cfg = IdentityConfig.load(args[0]);
            var ziti = Ziti.newContext(cfg);

            ziti.onStatus(status -> {
                System.out.printf(">>> status = %s%n", status);
            });

            ziti.onServiceEvent(service -> {
                System.out.printf(">>> %s = %s%n", service.getService().getName(), service.getType());
            });

            Thread.sleep(5000);
            ziti.destroy();

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
