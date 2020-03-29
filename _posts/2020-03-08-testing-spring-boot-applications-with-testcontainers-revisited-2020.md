---
layout: post
title: "Testing Spring Boot applications with TestContainers revisted 2020"
date: "2020-03-08"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - testcontainers
  - tools
  - testing
  - docker
---
It's been a year since I wrote a quick series of posts about testing Spring Boot applications with TestContainers. 

- [Part One - Data access layer integration tests](/2019/02/09/testing-spring-boot-applications-with-testcontainers/)
- [Part Two - Application integration tests](/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/)
- [Part Three - UI tests](https://robintegg.com/2019/02/24/testing-spring-boot-applications-with-testcontainers-and-selenium-webdriver-part-three.html)

Since then, there have a been a few changes in the tools, frameworks and documentation that have outdated the information in those posts.

* [Spring Boot announced default support for Junit 5](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.2-Release-Notes#junit-5)
* [Spring Boot documentation for ConfigFileApplicationContextInitializer](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-configfileapplicationcontextinitializer-test-utility)
* [TestContainers support for JUnit 5](https://www.testcontainers.org/test_framework_integration/junit_5/)
* [@DynamicPropertySource in Spring Boot 2.2.6](https://spring.io/blog/2020/03/27/dynamicpropertysource-in-spring-framework-5-2-5-and-spring-boot-2-2-6)

So this post, will cover the changes required to upgrade.

## testcontainers-demo

We will continue to use the [testcontainers-demo](https://github.com/teggr/testcontainers-demo) application as the System under test (SUT). The application routes notification messages from a JMS Queue to a RabbitMQ exchange, storing each notification in a Postgres database. This application also provides a web interface to see a list of all the messages that are routed by the application. The source code from the previous posts will now be availble on a [spring_boot_2_1_x_junit_4 branch](https://github.com/teggr/testcontainers-demo/tree/spring_boot_2_1_x_junit_4).

![]({{site.baseurl}}/assets/images/testcontainers-demo.png)

## Upgrade Spring Boot

Spring Boot is now on the [2.2.x branch](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-documentation) so first change is to upgrade the version in the maven pom.xml. This unlocks some of the new features that support TestContainers usage.

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.6.RELEASE</version>
    <relativePath />
</parent>
```

## Upgrade TestContainers versions

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.13.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.13.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>selenium</artifactId>
    <version>1.13.0</version>
    <scope>test</scope>
</dependency>
```

## Upgrade tests from JUnit 4 to JUnit 5

TestContainers requires new JUnit 5 jars.

```xml
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.13.0</version>
    <scope>test</scope>
</dependency>
```

Typically the changes to be made are to replace the old `org.junit.*` packages for `org.junit.jupiter.api.*`. We no longer have a requirement to expose public methods for JUnit 5. JUnit 5 also drops the `org.junit.Assert.assertThat` type which we can replace with Hamcrest type `org.hamcrest.MatcherAssert.assertThat`.

```java
package com.robintegg.testcontainersdemo.routing;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class NotificationRepositoryTest {
    @Test
    void shouldStoreEachNotification() {
        ...
        // then
        assertThat(count, is(2L));
    }
}
```

## Replace ApplicationContextInitializer configuration with DynamicPropertySource

Since the inclusion of Spring Framework 5.2.5, the configuration of the Spring Boot environment from TestContainers is now simpler.

Replace:

```java
@ContextConfiguration(initializers = {RoutingTest.Initializer.class}, classes = RabbitMqTestConfiguration.class)
@Testcontainers
class RoutingTest {
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
     
         @Override
         public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
 
             DemoApplicationTestPropertyValues.using(postgreSQLContainer, activeMQContainer, rabbitMQContainer)
                     .applyTo(configurableApplicationContext.getEnvironment());
 
         }
 
     }
}
```

With:

```java
class RoutingTest {
    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        DemoApplicationTestPropertyValues.populateRegistryFromPContainers(registry, postgreSQLContainer, activeMQContainer, rabbitMQContainer);
    }
}

public class DemoApplicationTestPropertyValues {
    public static void populateRegistryFromContainers(DynamicPropertyRegistry registry, PostgreSQLContainer<?> postgreSQLContainer ... ) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
            ...
        }
}
```

## Configure logback

As per the [recommended logback configuration](https://www.testcontainers.org/supported_docker_environment/logging_config/)

```xml
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT"/>
    </root>

    <logger name="org.testcontainers" level="INFO"/>
    <logger name="com.github.dockerjava" level="WARN"/>
</configuration>
```

