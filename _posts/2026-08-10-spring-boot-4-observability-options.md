---
layout: post
title: "Spring Boot 4 Observability Options: Boot UI, Spring Boot Admin, and Ostara"
date: "2026-08-10"
description: "A practical comparison of three observability tools for Spring Boot 4 applications"
image: /images/observing-spring-boot-applications.jpg
tags:
  - spring boot
  - observability
  - java
  - deploy4j
  - actuator
  - tools
---

AI is getting most of the airtime right now, but Java development and operations hasn't gone anywhere. Spring Boot 4 applications still need to be monitored, managed, and debugged in production. The Spring ecosystem has a few good options in the observability space, and this post walks through 3 of them: [Boot UI](https://www.julien-dubois.com/boot-ui/), [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin), and [Ostara](https://ostara.dev/). 

Each one slightly different than the other in terms of their purpose, hosting and architecture. Boot UI is most at home embedded in a developer workflow, Spring Boot Admin can run as either an embedded or standalone server, and Ostara is a desktop application that gives you a richer local experience without needing to run a server. Maybe one of them will hit a sweet spot for your own use.

I'll use my [articulate](https://github.com/teggr/articulate) project as the sample application. It runs on Spring Boot 4, and each section walks through getting tool up and running locally and what the experience looks like.

---

## The Sample App: articulate

[Articulate](https://github.com/teggr/articulate) is a Spring Boot 4 application for converting YouTube videos to blog articles. It's a real app with a database, external API calls, and some background processing, which makes it a reasonable stand-in for most production Java services. Currently we don't have any actuator or observability tooling baked in.

Given we don't have any prod observability, let's kick off with Boot UI. Primarily focussed on developer/local workflows it is also pretty quick to get installed.

## Boot UI

[Boot UI](https://www.julien-dubois.com/boot-ui/) is a local-only developer console for Spring Boot 4. 

To get this working, I only needed to add the `bootui-spring-boot-starter` as I already had the `spring-boot-devtools` dependency, which enables the boot-ui project by default in development mode.

```xml
<dependency>
  <groupId>com.julien-dubois.bootui</groupId>
  <artifactId>bootui-spring-boot-starter</artifactId>
  <version>1.13.1</version>
</dependency>
```        

Once the dependency was added, I spun up my service and navigated to `http://localhost:8080/bootui`. To be frank, I was blown away with the depth and breath of options and features available - live metrics, code scanners, runtime status, configuration panels, diagnostics and tools. It exposes absolutely everything I could possibly want to know about my application and it's runtime. Seriously impressive and practically zero configuration.

![boot ui interface](/images/observe-boot-ui.png)

The interesting feature I spent most time with were the advisors - architecture, security, pen testing. Run a scan and the tooling gives you a nice report with some take away actions.

![boot ui advisor](/images/observe-boot-ui-advisor.png)

---

## Spring Boot Admin

[Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) by Codecentric is a proper server that your Spring Boot applications register with. It polls their actuator endpoints and shows the results in a dashboard. 

Spring Boot Admin has two parts: the server and the client. The server runs as its own Spring Boot application. The client is a small dependency in your application that handles registration. You typically run the admin server separately from your application. For this demo, we will embed the server in our own service.

Add the dependencies for client and server, enable the server in your Spring Boot application and config the client loopback.

```xml
<!-- server -->
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-server</artifactId>
  <version>4.0.4</version>
</dependency>
<!-- client -->
<dependency>
  <groupId>de.codecentric</groupId>
  <artifactId>spring-boot-admin-starter-client</artifactId>
  <version>4.0.4</version>
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

```yaml
spring:
  boot:
    admin:
      # server
      context-path: /admin
      # client
      client:
        url: http://localhost:8080
```

Again, I only needed to spin up the service and because we've embedded the server in our app and self-registered, we could see our service straight away on `http://localhost:8080/admin/applications`. From there we could navigate down to the instance and see the dashboard for our service.

![spring boot admin interface](/images/observe-sba.png)

As the interface is based off the Spring Boot actuator endpoints, it only supports a subset of the Boot UI project but that still leaves a lot of support! The UI gives you all the insights / logging and configuration data that you would typically need for observing and managing a Spring Boot application.

Where the server/client architecture shines is that you are able to view a fleet of services and multiple instances of services, which means it's suitable for more deployment environments. There's also a number of different supported ways to register and keep the config secure in the documentation.

![spring boot admin dashboard](/images/observe-sba-dashboard.png)

---

## Ostara

[Ostara](https://ostara.dev/) is a desktop application that connects directly to your Spring Boot actuator endpoints. This means that you can start using it out of the box with no appication changes (apart from actuator support).

![ostara interface](/images/observe-ostara.png)

The interface is grounded in the available actuator data, much like Spring Boot admin, a gives you a great in sight into the runtime performance and configuration of your Spring Boot application.

The application provides some nice features around defining and grouping your connection configurations so that you can monitor multiple Spring Boot applications and environments.

![ostara dashboard](/images/observe-ostara-dashboard.png)

I liked the ability to spin up the application locally and see my Spring Boot estate at a glance, without the overhead of maintaining code or infrastructure.

---

## Comparison

<table>
  <thead>
    <tr>
      <th></th>
      <th>Boot UI</th>
      <th>Spring Boot Admin</th>
      <th>Ostara</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Deployment</td>
      <td>Embedded dependency</td>
      <td>Separate server</td>
      <td>Desktop app</td>
    </tr>
    <tr>
      <td>Server component</td>
      <td>No</td>
      <td>Yes</td>
      <td>No</td>
    </tr>
    <tr>
      <td>Multi-app aggregation</td>
      <td>No</td>
      <td>Yes</td>
      <td>Yes (connections)</td>
    </tr>
    <tr>
      <td>Best for</td>
      <td>Local inspection and development</td>
      <td>Team dashboard</td>
      <td>Single-user desktop inspection</td>
    </tr>
  </tbody>
</table>

Boot UI makes sense when you want to boost your local development. Spring Boot Admin makes sense when you're running several services and want one place to look. Ostara is a good middle ground for solo developers or small teams who want richer inspection without the server/registration overhead.

For articulate specifically, I can see a use case for each. I can certainly see the boot-ui project being added as a default dependency in my Spring Boot projects going forward. If I needed to have a hosted service with a protected infrastructure then Spring Boot admin certainly fits the bill. To reduce the hosting overhead, I'll certainly be looking at using Ostara to talk to my production apps. 

---

## References

- [Boot UI](https://www.julien-dubois.com/boot-ui/) by Julien Dubois
- [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) by Codecentric
- [Ostara](https://ostara.dev/)
- [Spring Boot Actuator documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [articulate on GitHub](https://github.com/teggr/articulate)
