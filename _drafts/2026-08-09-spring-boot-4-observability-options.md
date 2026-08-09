---
layout: post
title: "Spring Boot 4 Observability Options: Boot UI, Spring Boot Admin, and Ostara"
date: "2026-08-09"
description: "A practical comparison of three observability tools for Spring Boot 4 applications: Boot UI, Spring Boot Admin, and Ostara, with deploy4j configuration included."
image: /images/spring-boot-application.png
tags:
  - spring boot
  - observability
  - java
  - deploy4j
  - actuator
  - tools
---

AI is getting most of the airtime right now, but Java development and operations hasn't gone anywhere. Spring Boot 4 applications still need to be monitored, managed, and debugged in production. The Spring ecosystem has a few good options in the observability space, and this post walks through 3 of them: [Boot UI](https://www.julien-dubois.com/boot-ui/), [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin), and [Ostara](https://ostara.dev/).

I'll use [articulate](https://github.com/teggr/articulate) as the sample application. It runs on Spring Boot 4, and I deploy it with [deploy4j](https://deploy4j.dev), which is my port of Kamal to the Java ecosystem. Each section covers how to get the tool connected, what deploy4j config is needed to expose the right endpoints, and what the experience looks like once everything is wired up.

The goal is to give you enough to pick one and try it yourself.

---

<!-- AUTHOR INSTRUCTION: Before publishing, fill in the screenshot placeholders marked [SCREENSHOT] below. Run through each integration locally or against a test deployment and capture the relevant UI views. Replace placeholder lines with: ![description](/images/your-screenshot.png) -->

## The Sample App: articulate

[Articulate](https://github.com/teggr/articulate) is a Spring Boot 4 application for converting YouTube videos to blog articles. It's a real app with a database, external API calls, and some background processing, which makes it a reasonable stand-in for most production Java services.

The Spring Boot Actuator endpoints are the foundation for all three tools. Actuator ships with Spring Boot and exposes health, metrics, environment info, and more over HTTP. None of the tools in this post work without it.

<!-- AUTHOR INSTRUCTION: Check the current Spring Boot version in articulate's pom.xml and confirm it's Spring Boot 4.x before publishing. Update the version reference below accordingly. -->

The dependency:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

By default, only the `health` endpoint is exposed over HTTP. For most observability tools you'll want more:

```properties
management.endpoints.web.exposure.include=health,info,metrics,env,loggers,threaddump,heapdump,conditions,beans,mappings
management.endpoint.health.show-details=always
```

Be careful about what you expose in production. Endpoints like `heapdump`, `env`, and `beans` contain sensitive application details. The sections below call out which endpoints each tool actually needs.

## deploy4j and Actuator Exposure

<!-- AUTHOR INSTRUCTION: Verify the deploy4j configuration below against the current deploy4j docs at https://deploy4j.dev before publishing. Version numbers and config key names may have changed. -->

Deploy4j handles zero-downtime deployments using Docker on your own servers. It uses the actuator health endpoint for its health checks during deployments. If the health check fails, the deploy rolls back automatically.

The default deploy4j configuration already expects `/actuator/health` to be reachable from the host. Here's the relevant section in your `deploy4j.yml`:

```yaml
service:
  name: articulate
  image: ghcr.io/teggr/articulate

healthcheck:
  path: /actuator/health
  port: 8080
  max_attempts: 10
  interval: 2s
```

The health endpoint needs no authentication to work for deploy4j's health check. If you're running behind a reverse proxy (Traefik, Nginx, Caddy), you'll typically want to:

1. Block the `/actuator` path externally so it's not reachable from the public internet.
2. Allow it on the internal Docker network so deploy4j can reach it during deploys.

For tools that connect to actuator remotely (like Spring Boot Admin or Ostara), you'll need to make a decision about access control. Options include:

- **Spring Security**: Protect actuator endpoints with HTTP Basic or ****** auth. Spring Boot Admin and Ostara both support credentials.
- **Network-level restriction**: Expose actuator only on an internal interface or a separate management port.
- **VPN/tunnel**: Route admin tool traffic through a VPN so the endpoints never need to be public.

The simplest approach for a small deployment is a separate management port that's firewalled from the public:

```properties
management.server.port=9090
management.server.address=127.0.0.1
```

Then in deploy4j, you'd reference port 9090 for health checks only. If your admin tool runs on the same server, it can reach this port directly.

---

## Boot UI

[Boot UI](https://www.julien-dubois.com/boot-ui/) is a lightweight browser extension for inspecting Spring Boot Actuator endpoints. There's no server component to run. You install the extension in Chrome or Firefox, point it at your app's actuator base URL, and it renders the data in a clean interface.

<!-- AUTHOR INSTRUCTION: Install the Boot UI extension and run it against a local articulate instance. Capture screenshots of: the main dashboard view, the metrics tab, and the environment/config tab. -->

### Setup

1. Install the Boot UI browser extension from the Chrome Web Store or Firefox Add-ons. <!-- AUTHOR INSTRUCTION: Add the direct install link once confirmed -->
2. Start your Spring Boot 4 application locally or make sure the actuator base URL is reachable.
3. Open the extension popup, enter your actuator base URL (for example `http://localhost:8080/actuator`), and connect.

No server-side changes needed beyond the actuator config above.

### deploy4j considerations

Boot UI is browser-based, so it connects directly from your browser to the actuator endpoints. For a local setup, no extra deploy4j config is needed. For production, the actuator endpoints need to be reachable from your browser, which means exposing them publicly or connecting through a VPN or SSH tunnel.

An SSH tunnel keeps things simple:

```bash
ssh -L 9090:localhost:8080 your-server
```

Then connect Boot UI to `http://localhost:9090/actuator` from your browser.

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Boot UI dashboard connected to articulate -->

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Boot UI metrics view -->

### What you get

Boot UI gives you a quick read on health, environment, metrics, and configuration without setting up any infrastructure. It's good for local development inspection or ad-hoc checks on a deployed app. The downside is it's stateless: no history, no alerting, no aggregation across multiple instances.

---

## Spring Boot Admin

[Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) by Codecentric is a proper server that your Spring Boot applications register with. It polls their actuator endpoints and shows the results in a dashboard. You run the admin server separately from your application.

<!-- AUTHOR INSTRUCTION: Run a local Spring Boot Admin server and register articulate against it. Capture screenshots of: the application list, the instance detail view, the log viewer, and the metrics charts. -->

### Setup

Spring Boot Admin has two parts: the server and the client. The server runs as its own Spring Boot application. The client is a small dependency in your application that handles registration.

**Admin server** (in a new Spring Boot app):

```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-server</artifactId>
  <version>3.4.0</version>  <!-- AUTHOR INSTRUCTION: confirm latest version compatible with Spring Boot 4 -->
</dependency>
```

```java
@SpringBootApplication
@EnableAdminServer
public class AdminServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(AdminServerApplication.class, args);
  }
}
```

**In articulate** (or any app you want to monitor):

```xml
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-client</artifactId>
  <version>3.4.0</version>  <!-- AUTHOR INSTRUCTION: confirm latest version compatible with Spring Boot 4 -->
</dependency>
```

```properties
spring.boot.admin.client.url=http://your-admin-server:8080
spring.boot.admin.client.instance.service-url=http://your-app:8080
management.endpoints.web.exposure.include=*
```

The `service-url` is what the admin server uses to poll your actuator. It needs to be reachable from the admin server's network, not from your browser.

### deploy4j considerations

If you're deploying the admin server and articulate on the same server with deploy4j, they'll share a Docker network and can reach each other by service name:

```yaml
# articulate deploy4j.yml
env:
  SPRING_BOOT_ADMIN_CLIENT_URL: "http://admin:8080"
  SPRING_BOOT_ADMIN_CLIENT_INSTANCE_SERVICE_URL: "http://articulate:8080"
```

<!-- AUTHOR INSTRUCTION: Verify that deploy4j allows setting environment variables this way in the current version of the config spec. Check https://deploy4j.dev/configuration -->

For the admin server to be reachable from your browser, you'll need to expose it via your reverse proxy. The actuator endpoints on your app don't need to be public since the admin server polls them from inside the Docker network.

If you add Spring Security to the admin server (recommended for production), configure HTTP Basic credentials:

```properties
# admin server
spring.security.user.name=admin
spring.security.user.******

# articulate client
spring.boot.admin.client.username=admin
spring.boot.admin.client.******
```

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Spring Boot Admin application list showing articulate registered -->

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Spring Boot Admin instance detail with health, info, and metrics -->

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Spring Boot Admin log viewer tab -->

### What you get

Spring Boot Admin gives you a real dashboard with health history, log level management, thread dumps, heap dumps on demand, and metric charts. For teams running a small number of Java services, it covers most of what you need without pulling in something like Grafana. The log level management is particularly useful in production: you can enable DEBUG logging on a running instance without restarting.

The admin server is another service to run and secure. For a single app, that overhead probably isn't worth it. For 3 or more services, the aggregation pays for itself.

---

## Ostara

[Ostara](https://ostara.dev/) is a desktop application (built with Electron) that connects directly to your Spring Boot actuator endpoints. It sits between Boot UI and Spring Boot Admin: more capable than a browser extension, no server to run.

<!-- AUTHOR INSTRUCTION: Install Ostara desktop app, connect it to a running articulate instance (local or tunnelled), and capture screenshots of: the application connection screen, the main dashboard, the metrics view, and the cache/thread view if available. -->

### Setup

1. Download Ostara from [ostara.dev](https://ostara.dev/) for your OS.
2. Start Ostara and add a new application by entering the actuator base URL.
3. Ostara connects and starts displaying your app's data.

No code changes or client dependencies needed. It talks directly to the actuator endpoints you've already exposed.

### deploy4j considerations

Ostara connects from your desktop to the actuator endpoints, the same model as Boot UI. For production use, you'll need the actuator reachable from your machine. The SSH tunnel approach works here too:

```bash
ssh -L 9090:localhost:8080 your-server
# Then add http://localhost:9090/actuator in Ostara
```

If you've put actuator on a separate management port in deploy4j:

```properties
management.server.port=9090
```

Tunnel that port specifically:

```bash
ssh -L 9090:localhost:9090 your-server
```

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Ostara adding a new application connection -->

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Ostara dashboard view for articulate -->

<!-- AUTHOR INSTRUCTION: [SCREENSHOT] Ostara metrics or cache tab -->

### What you get

Ostara has a well-designed UI and covers caches, scheduled tasks, thread dumps, and HTTP request logs alongside the standard health and metrics views. It stores connection history locally, which makes it easy to pick up where you left off. No server, no auth to configure between components, but it's bound to your desktop. No alerting, no history beyond a single session, no multi-user access.

---

## Comparison

| | Boot UI | Spring Boot Admin | Ostara |
|---|---|---|---|
| Deployment | Browser extension | Separate server | Desktop app |
| Server component | No | Yes | No |
| Multi-app aggregation | No | Yes | Yes (connections) |
| Log level management | No | Yes | No |
| History and alerting | No | Partial | No |
| Auth between components | N/A | Yes | N/A |
| deploy4j overhead | Minimal | Moderate | Minimal |

Boot UI makes sense when you want a zero-setup check on a single local app. Spring Boot Admin makes sense when you're running several services and want one place to look. Ostara is a good middle ground for solo developers or small teams who want richer inspection without the server overhead.

For articulate specifically, I'd lean toward Spring Boot Admin if I were adding a second service, or Ostara for solo use where I want more than what the raw actuator JSON gives me.

---

## References

- [Boot UI](https://www.julien-dubois.com/boot-ui/) by Julien Dubois
- [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) by Codecentric
- [Ostara](https://ostara.dev/)
- [Spring Boot Actuator documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [articulate on GitHub](https://github.com/teggr/articulate)
- [deploy4j](https://deploy4j.dev)
- [deploy4j on GitHub](https://github.com/teggr/deploy4j)
