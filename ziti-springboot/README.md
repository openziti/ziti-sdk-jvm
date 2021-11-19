This component allows to Zitify a spring boot application.

Project goals:
* make spring boot service available directly on a Ziti network
* simplify zitification work -- one-line-zitify


## Configuration
In order to bind service to a Ziti network the following configuration is needed
```properties
ziti.id = <path to ziti identity file>
ziti.serviceName = <ziti service to bind to>
```

## Application code change
The only thing needed is to specify Ziti customizer class that bootstraps Tomcat configuration
```kotlin
@SpringBootApplication(
    scanBasePackageClasses = [
        ZitiTomcatCustomizer::class, // without this line tomcat runs on default(localhost:8080)
        HelloController::class
    ]
)
class ZitiSpringBootApplication


@RestController
class HelloController {
    @GetMapping("/")
    fun index(): String {
        return "Greetings from ZprIng booTI"
    }
}
```

Try the complete sample in [../samples/ziti-spring-boot]
