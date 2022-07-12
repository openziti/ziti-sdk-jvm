# Getting Started

To run the sample application you need a service with an intercept. Under Additional Links there is a link to ZEDS or you can run the ziti-spring-boot sample.
In the RestTemplateCaller you can update the URL it's calling.
Once a service is configured you need an **enrolled** identity that has access to that service. Copy the identity to the src/resources folder and update the src/resources/application.yaml
```
spring:
  ziti:
    httpclient:
      identity:
        file: classpath:<<your token file name>>
```
then execute
```
gradle bootRun
```

### Reference Documentation

For further reference, please consider the following sections:
* [Using RestTemplate in Spring](https://springframework.guru/using-resttemplate-in-spring/)

### Additional Links

These additional references should also help you:
* [Ziti Edge Developer Sandbox](https://zeds.openziti.org/)
* Ziti Spring Server [sample](../ziti-spring-boot)

