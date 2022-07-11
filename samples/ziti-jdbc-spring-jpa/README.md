# Connecting to a dark database using ZDBC and Spring JPA
In this example, we will take the dark web application you built in [Hosting an OpenZiti service using Spring boot](https://blogs.oracle.com/javamagazine/post/java-zero-trust-openziti) and extend it to store greetings in a dark postgres database.  

You didn't do that example?   That's OK, this sample works as a stand-alone exercise too.

## What you will need to get started
* About 30 minutes
* A favorite text editor or IDE
* JDK 11 or later
* Docker compose to run the OpenZiti network
* Access to a Linux environment with Bash 
  * A VM or WSL2 works for Windows users 

## Get the code
The code can be downloaded from [here](https://github.com/openziti-test-kitchen/zdbc) or clone it using Git:
```shell
 git clone https://github.com/openziti-test-kitchen/zdbc.git
```

Like most Spring guides, you can start from scratch and complete each step, or you can bypass basic 
setup steps that are already familiar to you. Either way, you end up with working code.

# Create the test OpenZiti network
This example will use a very simple OpenZiti network.

<p align="center">
<img id="exampleNetworkImage" src="images/DemoNetwork.png" alt="Example OpenZiti Network" width="300"/>
</p>

It isn't important right now to understand all components of the OpenZiti network. The important things you need to know are:
1. The controller manages the network. It is responsible for configuration, authentication, and authorization of components that connect to the OpenZiti network.
2. The router delivers traffic from the client to the server and back again.
3. The tunneler provides access to the Postgres database running in docker

Want to know more about OpenZiti? Head over to https://openziti.github.io/ziti/overview.html#overview-of-a-ziti-network.

Let's get into it and create the test network!

## Get the OpenZiti quickstart simplified docker compose file
OpenZiti provides a minimal docker compose file that runs the controller and one edge router.

Open a terminal and change into the `<repository>/samples/spring-jpa/network` directory.

Leave this terminal window open when you're done. You'll be using it to start and stop your ziti network. 

```shell
# Pull the docker compose file
curl -o simplified-docker-compose.yml https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/simplified-docker-compose.yml

#Pull the docker environment file
curl -o .env https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/.env
```

## Start the OpenZiti network
Starting and configuring the OpenZiti network is done with a script.  From the `<repository>/samples/spring-jpa/network`
directory:

```shell
./express-network-config.sh
```

The docker compose environment will write out three identity files (client.json, private-service.json, and database.json) that you will need for the Java code.

This repository includes a file called [NETWORK-README.md](network/NETWORK-README.md) if you want to learn more about what the script is doing and why.

# Resetting the OpenZiti demo network
You may want to stop and clean up your OpenZiti demo network when you're done with this article or things have gone really off the rails.  
From the `<repository>/samples/spring-jpa/network` directory:

```shell
./teardown.sh
```

# Connect to a dark database
There are a handful changes that need to be made to connect to a database over your OpenZiti network:

1. Add the OpenZiti zdbc dependency
2. Add Spring JPA dependencies
3. Add a JPA repository to store Greeting content
4. Add application properties to configure the JPA data provider

The example code contains an initial/server project. Pull that up in your favorite editor and follow along.

## Add the OpenZiti zdbc dependency
ZDBC is a JDBC driver provided by the OpenZiti project. The driver wraps the JDBC driver for your favorite database, 
automatically configuring it to connect over your OpenZiti network. 
See the [ZDBC Readme](https://github.com/openziti-test-kitchen/zdbc) for more information and to see which JDBC drivers are supported and tested.

Add the following to build.gradle:
```kotlin
implementation 'org.openziti:zdbc:0.1.12'
```

If you prefer maven, add the following to pom.xml:
```xml
<dependency>
         <groupId>org.openziti</groupId>
         <artifactId>zdbc</artifactId>
         <version>0.1.12</version>
</dependency>
```


## Add a greeting JPA repository
We'll use [Spring JPA](https://spring.io/projects/spring-data-jpa) for persistence and [Spring Data Rest](https://spring.io/projects/spring-data-rest) to expose the repository as a REST API.  

First up we need to add some dependencies.

### JPA and Database Dependencies
Add the following dependencies to build.gradle:
```kotlin
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.springframework.boot:spring-boot-starter-data-rest'

implementation 'org.postgresql:postgresql'
```

If you prefer maven, add the following to pom.xml:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>

<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
```

Now that the dependencies are in place, the next step is to create the JPA repository

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

Next up is a JPA repository for the `Greeting` entity.  Create this file in `src/main/java/com/example/restservice`

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
recognized by the OpenZiti ZDBC driver. The ZDBC driver will remove the OpenZiti part of the URL before delegating.  So 
`jdbc:ziti:postgresql://host:port/db` will become `jdbc:postgresql://host:port/db` when it is forwarded to the Postgresql
JDBC driver

## Run the application
That’s all you need to do!  The OpenZiti Java SDK will connect to the test network, authenticate, 
and bind your service so that OpenZiti overlay network clients can connect to it. The OpenZiti ZDBC driver
will connect to the postgres database running in docker to store and retrieve greetings.

To run the application, enter the following in a terminal window (in your project directory)
```shell
./gradlew bootRun
```

If you use maven, run the following in a terminal window in your project directory:
```shell
./mvnw spring-boot:run
```

# Test your new Spring Boot service
The Spring Boot service you have just created is now totally dark. It has no listening ports at all. 
Go ahead - run `netstat` and find out for yourself!

```shell
netstat -anp | grep 8080
netstat -anp | grep 5432
```
You should find nothing `LISTENING`. Now, the only way to access it is via the OpenZiti network!
Let’s write a simple client to connect to it and check that everything is working correctly.

## Connect to OpenZiti
This example includes a client project that makes connections using the OpenZiti Java SDK. 
Open the skeleton project `spring-jpa/initial/client` to follow along.

If you want to skip building the client then you can skip ahead to [Run The Client](#run-the-client).

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

This client needs some configuration to know how to use OpenZiti:

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

### Save a greeting
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
To run the client, run the following in a terminal window (in the client project):
```shell
./gradlew build run
```

If you use maven, run the following in a terminal window (in the client project):
```shell
./mvnw package exec:java
```
# Dig Deeper
* **OpenZiti documentation:** https://openziti.github.io/ziti/overview.html
* **OpenZiti ZDBC Driver: ** https://github.com/openziti-test-kitchen/zdbc
* **OpenZiti Github project:** https://github.com/openziti
* **NetFoundry hosted OpenZiti NaaS offering:** https://netfoundry.io

**Spring and Spring Boot are trademarks of Pivotal Software, Inc. in the U.S. and other countries.*
