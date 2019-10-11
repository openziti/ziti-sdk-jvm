/*
 * Copyright (c) 2019 NetFoundry, Inc.
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

package io.netfoundry.ziti.sample;

import io.netfoundry.ziti.Ziti;
import io.netfoundry.ziti.ZitiConnection;
import io.netfoundry.ziti.ZitiContext;

import java.io.ByteArrayOutputStream;

public class Sample {

    public static void main(String[] args) {
        ZitiContext ziti = Ziti.newContext("/home/eugene/work/java.p12", "".toCharArray());

        ZitiConnection conn = ziti.dial("demo-weather");

        String req = "GET / HTTP/1.1\n" +
                "Accept: */*\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Connection: close\n" +
                "Host: wttr.in\n" +
                "User-Agent: HTTPie/1.0.2\n" +
                "\n";
        conn.write(req.getBytes());

        byte[] resp = new byte[1024];

        int rc = 0;
        ByteArrayOutputStream r = new ByteArrayOutputStream();
        do {
            rc = conn.read(resp, 0, resp.length);
            if (rc > 0)
                r.write(resp, 0, rc);
        } while (rc > 0);

        try {
            System.out.println(new String(r.toByteArray()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("\n=================== Done ==================\n");

        ziti.stop();

        System.exit(0);
    }
}
