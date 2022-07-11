# Getting Started

To run the sample application first you need an enrolled identity. Copy the identity to the src/resources folder and update the src/resoruces/application.yaml
```
spring:
  ziti:
    httpclient:
      identity:
        file: classpath:<<your token file name>>.zid
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

