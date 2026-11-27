[![Java CI with Maven](https://github.com/hendisantika/spring-cloud-zuul/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-cloud-zuul/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.2.0-6DB33F?logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2026.0.0-6DB33F?logo=spring)

# spring-cloud-zuul-example

API gateway example with service discovery, originally based on the blog post
[Spring Cloud Zuul example](https://exampledriven.wordpress.com/2016/07/06/spring-cloud-zuul-example/).

> **Note:** Netflix Zuul 1 is in maintenance mode and does not run on Spring Boot 3/4 (it depends on `javax.servlet`).
> The `zuul-server` module now uses **Spring Cloud Gateway Server WebMVC**, the official successor, while keeping the
> same port and the same `/api/**` URLs as the original Zuul example.

## Tech Stack

| Component           | Version                                  |
|---------------------|------------------------------------------|
| Java                | 25 (Temurin)                             |
| Spring Boot         | 4.2.0                                    |
| Spring Cloud        | 2026.0.0                                 |
| API Gateway         | Spring Cloud Gateway Server WebMVC       |
| Service Discovery   | Netflix Eureka                           |
| Build               | Maven 3.9+                               |

## Modules

| Module             | Port | Description                                                       |
|--------------------|------|-------------------------------------------------------------------|
| `eureka-server`    | 8761 | Eureka service registry                                           |
| `customer-service` | 9098 | Sample REST service (`/customer/{id}`) registered in Eureka       |
| `zuul-server`      | 9090 | API gateway, all routes under `/api` (see `GatewayRoutesConfig`)  |

## Prerequisites

- JDK 25
- Maven 3.9+

## Build & Test

```shell
mvn clean verify
```

## Run

Start the services in this order (each in its own terminal):

```shell
mvn -f eureka-server/pom.xml spring-boot:run
mvn -f customer-service/pom.xml spring-boot:run
mvn -f zuul-server/pom.xml spring-boot:run
```

Eureka dashboard: http://localhost:8761

## Routes

| Feature                                   | Test URL                                                 |
|-------------------------------------------|----------------------------------------------------------|
| Service routed via Eureka (service id)    | http://localhost:9090/api/customer-service/customer/1    |
| Service registration by address           | http://localhost:9090/api/customer-by-address/customer/1 |
| Service registration by Eureka service id | http://localhost:9090/api/customer-by-service/customer/1 |
| Static response                           | http://localhost:9090/api/static                         |
| Static response (SVG)                     | http://localhost:9090/api/static/dsf.svg                 |
| Dynamic routing to external host          | http://localhost:9090/api/redirect                       |
| Display request mappings / routes         | http://localhost:9090/actuator/mappings                  |
| Health check                              | http://localhost:9090/actuator/health                    |

The target URLs of the address-based and external routes can be changed in `zuul-server/src/main/resources/application.yml`
(`gateway.routes.customer-by-address.url` and `gateway.routes.redirect.url`). Every request through the gateway is
logged by `AccessLogFilter`.

## CI/CD & Dependency Updates

- **GitHub Actions** (`.github/workflows/maven.yml`) builds and tests all modules with JDK 25 on every push and pull
  request to `master`, and submits the Maven dependency graph on push.
- **Dependabot** (`.github/dependabot.yml`) checks Maven dependencies daily and GitHub Actions weekly. Spring Boot and
  Spring Cloud updates are grouped across all modules so they arrive in a single pull request.

## Author

**Hendi Santika**

- Email: hendisantika@gmail.com
- Telegram: [@hendisantika34](https://t.me/hendisantika34)
- Link: [s.id/hendisantika](https://s.id/hendisantika)
