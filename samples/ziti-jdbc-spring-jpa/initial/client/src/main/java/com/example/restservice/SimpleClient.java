package com.example.restservice;

import okhttp3.*;
import org.openziti.Ziti;
import org.openziti.ZitiContext;
import org.openziti.api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class SimpleClient {
    private static final Logger log = LoggerFactory.getLogger( SimpleClient.class );

    private static void usageAndExit() {
        System.out.println("Usage: SimpleClient <-i identityFile> <-s serviceName> <-g greetingData> <-l>");
        System.out.println("\t-i identityFile\tYour Ziti network identity file");
        System.out.println("\t-s serviceName \tThe name of the greeting service to hit");
        System.out.println("\t-g greetingData\tGet a greeting from the server. Initial Test/prior to JPA update");
        System.out.println("\t-p greetingData\tPost a greeting to the server. After JPA update");
        System.out.println("\t-l             \tList greetings posted so far. After JPA update");

        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        String identityFile = "../../network/client.json";
        String serviceName = "demo-service";
        String url = "http://example.web:8080/greeting";
        String getGreetingData = null;
        String postGreetingData = null;
        boolean listGreetings = false;

        for (int i = 0; i < args.length; i++) {
            if ("-i".equals(args[i])) {
                if (i < args.length-1) {
                    identityFile = args[++i];
                } else {
                    usageAndExit();
                }
            }

            if ("-s".equals(args[i])) {
                if (i < args.length-1) {
                    serviceName = args[++i];
                } else {
                    usageAndExit();
                }
            }

            if ("-g".equals(args[i])) {
                if (i < args.length-1) {
                    getGreetingData = args[++i];
                } else {
                    usageAndExit();
                }
            }

            if ("-p".equals(args[i])) {
                if (i < args.length-1) {
                    postGreetingData = args[++i];
                } else {
                    usageAndExit();
                }
            }

            if("-l".equals(args[i])) {
                listGreetings = true;
            }
        }

        if (null != getGreetingData && (listGreetings || null != postGreetingData)) {
            System.err.println("Initial property (-g) and post-jpa properties (-l, -p) cannot be used together");
            usageAndExit();
        }

        ZitiContext zitiContext = null;
        try {
            zitiContext = Ziti.newContext(identityFile, "".toCharArray());

            Service svc = zitiContext.getService(serviceName,10000);
            if (svc == null) {
                throw new IllegalArgumentException(String.format("Service %s is not available on the OpenZiti network",serviceName));
            }

            OkHttpClient clt = newHttpClient();

            if(null != getGreetingData) {
                getGreeting(clt, url, getGreetingData);
            }

            if(null != postGreetingData) {
                postGreeting(clt, url, postGreetingData);
            }

            if(listGreetings) {
                listGreetings(clt,url);
            }

        } finally {
            Thread.sleep(1000);
            if( null != zitiContext ) zitiContext.destroy();
        }

    }

    private static OkHttpClient newHttpClient() throws Exception {
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
        return clt;
    }

    private static void listGreetings(OkHttpClient clt, String url) throws Exception {
        throw new RuntimeException("Not Yet Implemented");
    }

    private static void postGreeting(OkHttpClient clt, String url, String greetingData) {
        throw new RuntimeException("Not Yet Implemented");
    }

    private static void getGreeting(OkHttpClient clt, String url, String name) throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("name",name);

        Request req = new Request.Builder()
                .get()
                .addHeader("Accept", "application/json")
                .url(urlBuilder.build().toString())
                .build();
        log.info("Dialing service");
        Response resp = clt.newCall(req).execute();
        log.info("Response Body: " + StandardCharsets.UTF_8.decode(ByteBuffer.wrap(resp.body().bytes())));
    }
 }
