/*
 * Copyright (c) 2018-2021 NetFoundry, Inc.
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

package org.openziti.sample;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.openziti.Ziti;
import org.openziti.ZitiContext;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SampleOkHttp {

    public static void main(String[] args) {
        String path = args[0];
        String url = args[1];

        ZitiContext ctx = null;
        try {
            ctx = Ziti.newContext(path, new char[0]);

            // sleep to let ZitiContext initialize
            Thread.sleep(3000);

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            tmf.init(ks);

            X509TrustManager tm = (X509TrustManager)tmf.getTrustManagers()[0];

            OkHttpClient clt = new OkHttpClient.Builder()
                .socketFactory(Ziti.getSocketFactory())
                .sslSocketFactory(Ziti.getSSLSocketFactory(), tm)
                .dns(hostname -> {
                    InetAddress address = Ziti.getDNSResolver().resolve(hostname);
                    if (address == null) {
                        address = InetAddress.getByName(hostname);
                    }
                    return (address != null) ? Collections.singletonList(address) : Collections.emptyList();
                })
                .callTimeout(5, TimeUnit.MINUTES)
                .build();

            Request req = new Request.Builder()
                    .get()
                    .url(url).build();

            Response resp = clt.newCall(req).execute();
            System.out.println(resp);
            System.out.println(resp.headers());
            System.out.println(StandardCharsets.UTF_8.decode(ByteBuffer.wrap(resp.body().bytes())));
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            ctx.destroy();
        }
    }
}
