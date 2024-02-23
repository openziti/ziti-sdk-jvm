# Microsoft SQL Server Example
This project contains a simple java application that uses the [OpenZiti](https://openziti.github.io/ziti/overview.html) Java SDK to connect to a Microsoft SQL Server (MSSQL) database and read the contents of a table.

# What you will need
* (Maven) [https://maven.apache.org/] to build the sample
* A Java JDK to build and run the sample. If you don't have one you can get one from the [Oracle Open JDK download site](https://jdk.java.net/)

# Building the sample
Compilation is done using the Maven build tooling.

1. Change into the `ziti-sdk-jvm/samples/ziti-jdbc-mssql` sample directory
1. execute `../../gradlew -PbuildForAndroid=false build`

# Running the example
Running the example is done using the gradle run facility. 
It's assumed you already have the OpenZiti network running and the ziti services configured.

Your services must have as intercept configuration the host `mssql_host` with the port `1433`; if different you may change the connection string in the `MSSQLExample.java` file.

It's also assumed you have a Microsoft SQL Server running and a database named `DBNAME` with a table called `TABLENAME` with two columns `column_1` and `column_2`.


You can run the example from the sample directory using:

```
../../gradlew -PbuildForAndroid=false run
```

## Example Output
```
> Task :ziti-jdbc-mssql:runWithJavaExec
[main] INFO org.openziti.impl.ZitiImpl - ZitiSDK version 0.25.1 @344b49b()
Feb 22, 2024 5:32:02 PM org.openziti.jdbc.ZitiDriver setupZiti
INFO: Ziti JDBC wrapper is configuring Ziti identities. Production applications should manage Ziti identities directly
[DefaultDispatcher-worker-2] INFO org.openziti.api.Controller - controller[https://ziti-edge-controller/] version(v0.28.1/f9a62c0baf1c)
[DefaultDispatcher-worker-1] INFO org.openziti.api.Controller - controller[https://ziti-edge-controller/] version(v0.28.1/f9a62c0baf1c)
Column_1: AAA0000 - Column_2: 10.10.10.0
Column_1: AAA0001 - Column_2: 10.10.10.1
Column_1: AAA0002 - Column_2: 10.10.10.2
Column_1: AAA0003 - Column_2: 10.10.10.3
Column_1: AAA0004 - Column_2: 10.10.10.4
Column_1: AAA0005 - Column_2: 10.10.10.5
Column_1: AAA0006 - Column_2: 10.10.10.6
Column_1: AAA0007 - Column_2: 10.10.10.7
Column_1: AAA0008 - Column_2: 10.10.10.8
Column_1: AAA0009 - Column_2: 10.10.10.9
```
