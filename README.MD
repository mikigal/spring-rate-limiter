# spring-rate-limiter
Simple rate limiter library for Spring

## Import
#### Gradle
```groovy
maven {
    url = 'https://repo.mikigal.pl/releases'
}

compile group: 'pl.mikigal', name: 'spring-rate-limiter', version: '1.0.0'
```

#### Maven
```xml
<repository>
    <id>mikigal-repo</id>
    <url>https://repo.mikigal.pl/releases</url>
</repository>

<dependency>
    <groupId>pl.mikigal</groupId>
    <artifactId>spring-rate-limiter</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

## Example usage

```java
// Enable request limiter for application
@EnableRateLimiter
@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
```

```java
@RestController
public class ExampleController {

    // Allow up to 100 requests per 5 minutes for this endpoint per IP address
    @RateLimiter(max = 100, time = 5, timeUnit = TimeUnit.MINUTES)
    @GetMapping("/example")
    public String root() {
        return "success";
    }
}
```
