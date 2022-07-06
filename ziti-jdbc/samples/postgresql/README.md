# Postgresql Example
This project contains a simple java application that uses the [OpenZiti](https://openziti.github.io/ziti/overview.html) Java SDK to connect to a [Postgresql](https://www.postgresql.org/) database and read the contents of a table.

# What you will need
* (Maven) [https://maven.apache.org/] to build the sample
* A Java JDK to build and run the sample. If you don't have one you can get one from the [Oracle Open JDK download site](https://jdk.java.net/)
* [Docker Compose](https://docs.docker.com/compose/)

# Building the sample
Compilation is done using the Maven build tooling.

1. Change into the postgresql sample directory
1. execute `mvn clean compile`

# Setting up the example Ziti network
The network directory in the source repository contains a script to start and configure an OpenZiti network for this sample.  You can read the [network README.md](network/README.md) for instructions, or just change into the `network` subdirectory and run `setup.sh` if you're feeling lucky.

# Running the example
Running the example is done using the Maven exec plugin. If you ran the network setup script, then you can run the example from the sample directory using:

```
mvn exec:java
```

## Example Output
```
Jun 27, 2022 2:56:05 PM org.openziti.jdbc.ZitiDriver setupZiti
INFO: Ziti JDBC wrapper is configuring Ziti identities. Production applications should manage Ziti identities directly
[DefaultDispatcher-worker-2] INFO Controller - controller[https://ziti-edge-controller:1280/] version(v0.25.1/8e9be956d49f)
[DefaultDispatcher-worker-2] INFO Controller - controller[https://ziti-edge-controller:1280/] version(v0.25.1/8e9be956d49f)
[DefaultDispatcher-worker-5] INFO ZitiContextImpl - current edge routers = [EdgeRouter(name=ziti-edge-router, hostname=, supportedProtocols={tls=tls://ziti-edge-router:3022}, urls=null)]
[DefaultDispatcher-worker-3] INFO ZitiContextImpl - current edge routers = [EdgeRouter(name=ziti-edge-router, hostname=, supportedProtocols={tls=tls://ziti-edge-router:3022}, urls=null)]
Result from database is: a:1
Result from database is: b:2
Result from database is: c:3
Result from database is: d:4
Result from database is: e:5
Result from database is: f:6
Result from database is: g:7
Result from database is: h:8
Result from database is: i:9
Result from database is: j:0
```
