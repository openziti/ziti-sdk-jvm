Ziti SDK for JVM
=================
The **Ziti SDK for JVM** enables Java and other developers to easily and securely connect their applications
to backend services over NetFoundry Ziti networks.

.. contents::


Release Notes
-------------


Obtaining SDK
------------
The recommended way to use the Ziti SDK for Java in your project is to consume it from Maven. Register our repository and
add the SDK Maven dependency to your project.

Maven
_____
.. code-block:: xml

  <project>
     ....
     <repositories>
        ....
        <repository>
          <id>ziti-repo</id>
          <name>NetFoundry Ziti Repository</name>
          <url>https://netfoundry.jfrog.io/netfoundry/ziti-maven</url>
        </repository>
     </repositories>
     ....
     <dependencies>
        ...
        <dependency>
           <groupId>io.netfoundry.ziti</groupId>
           <artifactId>ziti</artifactId>
           <version>0.4.4-7</version>
        </dependency>
     </dependencies>
     ....
  </project>

Gradle
______
.. code-block:: gradle

   repositories {
       ...
       maven { url = "https://netfoundry.jfrog.io/netfoundry/ziti-maven" }
   }
   ...
   dependencies {
      ...
      implementation 'io.netfoundry.ziti:ziti:0.4.4-7'
   }

Building from Source
--------------------

Features
--------


Android Support
---------------



Getting Help
------------
Please use these community resources for getting help. We use GitHub issues_ for tracking bugs and feature requests and have limited bandwidth
to address them.

- Read the docs_
- Participate in discussion on Discourse_



.. _docs: https://netfoundry.github.io/ziti-doc/ziti/overview.html
.. _Discourse: https://netfoundry.discourse.group/
.. _issues: https://github.com/NetFoundry/ziti-sdk-jvm/issues
