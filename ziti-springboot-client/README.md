This component allows to Zitify a spring boot application.

Project goals:
* make spring boot rest template able to connect to a Ziti service


## Configuration
To enable auto configuration of the Ziti beans add `@EnableZitiHttpClient` to either a @Configuration class or your main application
```java
@SpringBootApplication
@EnableZitiHttpClient
public class SampleApplication {

  public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
	
}
```
All ziti related beans are created with a prefix of `ziti`. Overriding any of them is done by adding a @Configuration with a @Bean("ziti...") and that will be used instead of the auto configured one.

### Simple Ziti file based context
To connect using a ziti connection add a ziti section to your config:
```yaml
spring:
  ziti:
    httpclient:
      identity:
        file: classpath:client.zid
```
the file `client.zid` must be an enrolled token.
Then to use the ziti rest template you autowire it as usual but with a qualifier.
```java
@Configuration
public class RestTemplateCaller {

  @Autowired
  public RestTemplateCaller(@Qualifier("zitiRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

}
```
The ziti rest template is now ready to use just like any other rest template but it will resolve addresses and connect to services through your ziti controller.
Highly recommended not to use it for other public/external services even if it works to do so.
To read more about ziti services please see [ziti service](https://openziti.github.io/ziti/services/overview.html).

### Ziti identity from other sources
To override the socket factory in order to provide your own identity you can do the following:
```java
@Configuration
public class SampleConfiguration {

  private ZitiConnectionSocketFactory zitiConnectionSocketFactory;

  @Bean("zitiConnectionSocketFactory")
  public ZitiConnectionSocketFactory connectionSocketFactory() throws IOException {
    if (zitiConnectionSocketFactory == null) {
      try (InputStream is = /* insert input stream creation here */ ) {
        zitiConnectionSocketFactory = new ZitiConnectionSocketFactory(is);
      }
    }
    return zitiConnectionSocketFactory;
  }

  @PreDestroy
  public void destroy() {
    Optional.ofNullable(zitiConnectionSocketFactory).ifPresent(ZitiConnectionSocketFactory::shutdown);
  }

}
```
The placeholder above should be replaced with your own input stream for an identity.
One such example would be to read from a secrets manager or a vault where the identity is stored securely.

### Overriding RestTemplate or RestTemplateBuilder features
The configuration can also include a RestTemplate or RestTemplateBuilder bean in order to customize it.
```java
@Configuration
public class SampleConfiguration {

  @Bean("zitiRestTemplate")
  public RestTemplate restTemplate(@Qualifier("zitiRestTemplateBuilder") RestTemplateBuilder restTemplateBuilder) {
    // customizations
    return restTemplateBuilder.build();
  }

}
```
or
```java
@Configuration
public class SampleConfiguration {

  @Bean("zitiRestTemplateBuilder")
  public RestTemplateBuilder restTemplateBuilder(@Qualifier("zitiHttpClient") HttpClient httpClient) {
    return new RestTemplateBuilder()
        // customizations
        .requestFactory(() -> clientHttpRequestFactory(httpClient));
  }

}
```

## Application code change
Add `ziti-springboot-client` dependency
```groovy
implementation "org.openziti:ziti-springboot-client:+"
```
Autowire the 'zitiRestTemplate' into your application and use it to connect to ziti services.

Try the [complete sample](../samples/ziti-spring-boot-client)

### Reference Documentation
* [ziti service](https://openziti.github.io/ziti/services/overview.html)

For further reference, please consider the following sections:
* [Using RestTemplate in Spring](https://springframework.guru/using-resttemplate-in-spring/)

An example of running a service is in our samples
* Ziti Spring Server [sample](../samples/ziti-spring-boot)
