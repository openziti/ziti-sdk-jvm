/*
 * Copyright (c) 2018-2020 NetFoundry, Inc.
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

package org.openziti.sample.http;

import org.openziti.Ziti;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Sample program to demonstrate `seamless` SDK usage.
 */
public class HttpSample {

    public static void main(String[] args) {
        try {
            Ziti.init(args[0], "".toCharArray(), true);

            URL url = new URL(args[1]);

            runHttp(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void runHttp(URL url) {

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "text/plain");
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("User-Agent", "curl");
            conn.setDoInput(true);
            int rc = conn.getResponseCode();
            byte[] buf = new byte[1024];
            ByteArrayOutputStream resp = new ByteArrayOutputStream();

            if (rc > 399) {
                int len = conn.getErrorStream().read(buf);
                System.err.println(String.format("%d %s\n%s", rc, conn.getResponseMessage(), new String(buf, 0, len)));
            } else {
                do {
                    int len = conn.getInputStream().read(buf);
                    if (len < 0) break;
                    resp.write(buf, 0, len);
                } while (true);

                System.out.println(new String(resp.toByteArray()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("\n=============== Done! ==================\n");
    }
}
