# Connecting to a dark database using ZDBC and Spring JPA
In this exercise, we will take the dark web application you built in [Hosting an OpenZiti service using Spring boot](https://blogs.oracle.com/javamagazine/post/java-zero-trust-openziti) and extend it to store greetings in a dark postgres database.  

You didn't do that exercise? That's OK, this sample works as a stand-alone exercise too.

## What you will need to get started
* About 30 minutes
* A favorite text editor or IDE
* JDK 11 or later
* Docker compose to run the OpenZiti network
* Access to a Linux environment with Bash 
  * A VM or WSL2 works for Windows users 

## What you will build
At the end of this exercise you will have a web application that:
* Listens only on the OpenZiti network overlay
* Uses JPA to store greeting content into a database that listens only on the OpenZiti network overlay

You will also have a simple Java application that acts as a client to send and retrieve greeting data.

## Get the code
The code is part of the OpenZiti Java SDK.  The SDK source can be downloaded from [here](https://github.com/openziti/ziti-sdk-jvm) or clone it using Git:
```shell
 git clone https://github.com/openziti/ziti-sdk-jvm.git
```

This README and the code for the exercise is in `<repository>/samples/ziti-jdbc-spring-jpa`.

Like most Spring guides, you can start from scratch and complete each step, or you can bypass basic 
setup steps that are already familiar to you. Either way, you end up with working code.

## Code Layout
The code is broken down into three top level directories:
* **network:** Contains files necessary to start an OpenZiti network for this exercise
* **initial:** Contains the skeleton of the exercise. This is a dark stateless server and a simple client to interact with it.
* **complete:** Contains a completed exercise. 

The `initial` and `complete`directories contain:
* **client:** Client code that can be used to interact with the dark service
* **server:** A spring boot project that runs the serve

# Create the test OpenZiti network
This exercise will use a very simple OpenZiti network.

<p align="center">
<img id="exerciseNetworkImage" src="images/DemoNetwork.png" alt="exercise OpenZiti Network" width="300"/>
</p>

It isn't important right now to understand all components of the OpenZiti network. The important things you need to know are:
1. The controller manages the network. It is responsible for configuration, authentication, and authorization of components that connect to the OpenZiti network.
2. The router delivers traffic from the client to the server and back again.
3. The tunneler provides access to the Postgres database running in docker

Want to know more about OpenZiti? Head over to https://openziti.github.io/ziti/overview.html#overview-of-a-ziti-network.

Let's get into it and create the test network!

## The OpenZiti quickstart simplified docker compose and environment files
OpenZiti provides a minimal docker compose environment that runs the controller and one edge router. 
The `startup.sh` script will automatically download these into the `network` folder for you if you are missing them.

## Start the OpenZiti network
Starting and configuring the OpenZiti network is done with a script.

1. Open a terminal and change into the `<repository>/samples/ziti-jdbc-spring-jpa/network` directory.
2. Leave this terminal window open when you're done. You'll be using it to start and stop your ziti network.
3. Run:
```shell
./startup.sh
```

The docker compose environment will create out two identity files (client.json and private-service.json) in the 
`network` directory. These are the credentials that the client and service will use to connect to the OpenZiti network.

This repository includes a file called [README.md](network/README.md) if you want to learn more about what the script is doing and why.

# Resetting the OpenZiti demo network
You may want to stop and clean up your OpenZiti demo network when you're done with this article or things have gone really off the rails.  
From the `<repository>/samples/ziti-jdbc-spring-jpa/network` directory:

```shell
./teardown.sh
```

# Test the network
This is an optional step to verify that the OpenZiti network is up and running correctly. This test will use the
`initial` client and server applications to exchange some data.

1. Start the server. Open a terminal and change directory into `<repository>/samples/ziti-jdbc-spring-jpa/initial/server`
```shell
../../../../gradlew -PbuildForAndroid=false bootRun
```
2. Start the client. Open a terminal and change directory into `<repository>/samples/ziti-jdbc-spring-jpa/initial/client`
```shell
../../../../gradlew -PbuildForAndroid=false clean build run
```

If all is working then you should see this in the client output:
```
[main] INFO com.example.restservice.SimpleClient - Response Body: {"id":5,"content":"Hello, InitialExample!"}
```

Now that the network is up and running it's time to start working on the project!

# Configure the project for Spring JPA
There are a handful changes that need to be made to connect to a database over your OpenZiti network:

1. Add the OpenZiti zdbc dependency
2. Add Spring JPA dependencies
3. Add a JPA repository to store Greeting content
4. Add application properties to configure the JPA data provider

The exercise code contains a `<repository>/samples/ziti-jdbc-spring-jpa/initial/server` skeleton project. 
Pull that up in your favorite editor and follow along.

## Add the OpenZiti ZDBC dependency
ZDBC is a JDBC driver provided by the OpenZiti project. The driver wraps the JDBC driver for your favorite database, 
automatically configuring it to connect over your OpenZiti network. 
See the [ZDBC Readme](https://github.com/openziti/ziti-sdk-jvm/blob/main/ziti-jdbc/README.md) for more information and 
to see which JDBC drivers are supported.

Add the following to build.gradle:
```kotlin
implementation 'org.openziti:ziti-jdbc:0.23.15'
```

## Add a greeting JPA repository
We'll use [Spring JPA](https://spring.io/projects/spring-data-jpa) for persistence and
[Spring Data Rest](https://spring.io/projects/spring-data-rest) to expose the repository as a REST API.  

First up we need to add these libraries as dependencies for our project.

### JPA and Database Dependencies
Add the following dependencies to build.gradle:
```kotlin
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-data-rest'

implementation 'org.postgresql:postgresql'
```

Now that the dependencies are in place, the next step is to create the JPA repository.

### Greeting JPA repository
The `Greeting` class needs to be converted to a JPA Entity. 
To do this we will add a couple of annotations and remove immutability from the class members.

```java
@Entity
public class Greeting {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String content;

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
```

Next up is a JPA repository for the `Greeting` entity.  Create this class next to `Greeting` in `src/main/java/com/exercise/restservice`

```java
@RepositoryRestResource(collectionResourceRel = "greetings", path = "greetings")
public interface GreetingRepository extends PagingAndSortingRepository<Greeting, Long> {
}
```

This Repository is very basic, and will give CRUD access to our `Greeting` entity. 
The `RepositoryRestResource` annotation will expose it via REST for us.

## Add application properties
Open the application properties file: `src/main/resources/application.properties`

JPA and ZDBC require a couple of properties to be added here

```properties
# Tell spring to use the OpenZiti ZDBC driver
spring.datasource.driverClassName=org.openziti.jdbc.ZitiDriver

# The ZDBC driver knows how to interpret a url that starts with jdbc:ziti
spring.datasource.url=jdbc:ziti:postgresql://private-postgres-server.demo/simpledb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

Most of these are standard JPA properties. Some interesting ones:
* **spring.datasource.driverClassName:** This property tells Spring to use the OpenZiti ZDBC driver for making database
connections
* **spring.datasource.url:** This is the JDBC url for your database.  URLs that start with `zdbc` or `jdbc:ziti` are 
recognized by the OpenZiti ZDBC driver. The ZDBC driver will remove the OpenZiti part of the URL before delegating to 
the Postgresql driver.  So `jdbc:ziti:postgresql://host:port/db` will become `jdbc:postgresql://host:port/db`.

## Run the application
That’s all you need to do! The OpenZiti Java SDK will connect to the test network, authenticate, 
and bind your service so that OpenZiti overlay network clients can connect to it. The OpenZiti ZDBC driver
will connect to the postgres database running in docker to store and retrieve greetings.

Open a terminal and change directory into `<repository>/samples/ziti-jdbc-spring-jpa/initial/server`
```shell
../../../../gradlew -PbuildForAndroid=false bootRun
```

If you skipped updating the skeleton, then use the project from `<repository>/samples/ziti-jdbc-spring-jpa/complete/server` instead.
The command to run is the same.

# Test your new Spring Boot service
The Spring Boot service you have just created is now totally dark. It has no listening ports at all. 
Go ahead - run `netstat` and find out for yourself!

```shell
# No listening port for the application
netstat -anp | grep 8080
# No listening port for the Postgresql database
netstat -anp | grep 5432
```
You should find nothing `LISTENING`. Now, the only way to access it is via the OpenZiti network!
Let’s update the initial client to connect to it and check that everything is working correctly.

# Update the client
This exercise includes a client project that makes connections using the OpenZiti Java SDK. 
Open the skeleton project `<repository>/samples/ziti-jdbc-spring-jpa/initial/client` to follow along.

If you want to skip building the client then you can skip ahead to [Run The Client](#run-the-client).

## OpenZiti SDK connection
The Java SDK needs to be initialized with an OpenZiti identity. It is polite to destroy the context 
once the code is done, so we’ll wrap it up in a `try/catch` with a `finally` block.

```java
ZitiContext zitiContext = null;
try {
  zitiContext = Ziti.newContext(identityFile, "".toCharArray());

  if (null == zitiContext.getService(serviceName,10000)) {
    throw new IllegalArgumentException(String.format("Service %s is not available on the OpenZiti network",serviceName));
  }

} catch (Throwable t) {
  log.error("OpenZiti network test failed", t);
}
finally {
  if( null != zitiContext ) zitiContext.destroy();
}
```
* **Ziti.newContext:** Loads the OpenZiti identity and starts the connection process.
* **zitiContext.getService** It can take a little while to establish the connection with the OpenZiti 
network fabric. For long-running applications this is typically not a problem, but for this little
client we need to give the network some time to get everything ready.
* **zitiContext.destroy():** Disposes of the context and cleans up resources locally and on the 
OpenZiti network.

## Configure the HTTP client
We'll use the [OkHttp](https://square.github.io/okhttp/) HTTP client to make requests to the service.

This client needs some configuration to know how to use OpenZiti.

```java
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
```

* **socketFactory:** The socketFactory teaches OkHttp how to create connections over the OpenZiti overlay network
* **sslSocketFactory:** The sslSocketFactory teaches OkHttp how to create https connections over the OpenZiti network
* **dns:** This setting teaches OkHttp how to resolve web requests using the OpenZiti DNS resolver

## Send a request to the service
The client has a connection to the test OpenZiti network and OkHttp knows how to connect using the OpenZiti overlay.
Sending requests works just like any website that does not use OpenZiti.

### Post a greeting
Saving a greeting means sending an HTTP POST request to our service.

```java
    Request req = new Request.Builder()
            .post(RequestBody.create(String.format("{\"content\":\"%s\"}", greetingData), MediaType.parse("application/json") ))
            .addHeader("Content-Type","application/json")
            .url(url)
            .build();

    Response resp = clt.newCall(req).execute();
    log.info("Response Headers: {}", resp.headers());
    log.info("Response Body: {}", StandardCharsets.UTF_8.decode(ByteBuffer.wrap(resp.body().bytes())));
```

* **post:** Here the user submitted data is wrapped in a simple JSON document and sent in.  
There isn't any validation or even escaping of content here, so this client is very fragile. 
Not for production use!
* **clt.newCall(req).execute:** This is the line that sends the request to the server and gets back a response 

### List saved greetings
Listing greetings means sending an HTTP GET request to our service.

```java
   Request req = new Request.Builder()
            .get()
            .url(url)
            .build();

    Response resp = clt.newCall(req).execute();
    log.info("Response Headers: {}", resp.headers());
    log.info("Response Body: {}", StandardCharsets.UTF_8.decode(ByteBuffer.wrap(resp.body().bytes())));
```

## Run The Client
Open a terminal and change directory into `<repository>/samples/ziti-jdbc-spring-jpa/initial/client`.

If you skipped updating the skeleton, then change into `<repository>/samples/ziti-jdbc-spring-jpa/complete/client` instead.

Run this command:
```shell
../../../../gradlew -PbuildForAndroid=false clean build run
```

This will post a greeting to the server, then list all greetings posted so far.

If all is working then you should see this in the client output:
```
[main] INFO com.example.restservice.SimpleClient - Posting greeting: hello from 07/19/2022 09:14:16
[main] INFO com.example.restservice.SimpleClient - Response Body: 
[main] INFO com.example.restservice.SimpleClient - Listing greetings
[main] INFO com.example.restservice.SimpleClient - Response Body: {
  "_embedded" : {
    "greetings" : [{
      "content" : "hello at 07/19/2022 09:14:16",
      "_links" : {
        "self" : {
          "href" : "http://example.web:8080/greetings/4"
        },
        "greeting" : {
          "href" : "http://example.web:8080/greetings/4"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://example.web:8080/greetings"
    },
    "profile" : {
      "href" : "http://example.web:8080/profile/greetings"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}

[main] INFO com.example.restservice.SimpleClient - Response Body: {"id":5,"content":"Hello, InitialExample!"}
```

# Dig Deeper
* **OpenZiti documentation:** https://openziti.github.io/ziti/overview.html
* **OpenZiti ZDBC Driver: ** https://github.com/openziti-test-kitchen/zdbc
* **OpenZiti Github project:** https://github.com/openziti
* **NetFoundry hosted OpenZiti NaaS offering:** https://netfoundry.io

**Spring and Spring Boot are trademarks of Pivotal Software, Inc. in the U.S. and other countries.*
