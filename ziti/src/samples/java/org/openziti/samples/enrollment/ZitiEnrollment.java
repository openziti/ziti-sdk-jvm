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

package org.openziti.samples.enrollment;

import org.openziti.IdentityConfig;
import org.openziti.Ziti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * demonstrates enrolling Ziti identity.
 */
public class ZitiEnrollment {
    private static final Logger logger = LoggerFactory.getLogger(ZitiEnrollment.class);

    static void usage() {
        System.err.println("Usage: ZitiEnrollment <jwt-file> [key-file] [cert-file]");
        System.exit(1);
    }

    public static void main(String[] args) {

        if (args.length != 1 && args.length != 3) {
            usage();
        }

        var enrollment = Ziti.createEnrollment(args[0]);

        IdentityConfig cfg;

        logger.info("method = {}", enrollment.getMethod());
        if (enrollment.requiresClientCert()) {
            if (args.length != 3) {
                logger.error("Client key and certificate are required for method[{}]",
                        enrollment.getMethod());
                usage();
            }

            cfg = enrollment.enroll(args[1], args[2]);
        } else {
            cfg = enrollment.enroll();
        }

        var out = new ByteArrayOutputStream();
        cfg.store(out);

        logger.info("you should save this = {}", out.toString(StandardCharsets.UTF_8));
    }
}
