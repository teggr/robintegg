---
layout: post
title: "Testing Spring Boot applications with TestContainers"
date: "2019-02-09"
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - testcontainers
  - tools
  - testing
  - docker
---
Note: Some time has passed since writing this post, once read it maybe worth visiting the [Testing Spring Boot applications with TestContainers revisted 2020](/2020/03/08/testing-spring-boot-applications-with-testcontainers-revisited-2020) post that details some revisions for later versions of Spring Boot and JUnit 5. The source for this post is now on the [spring_boot_2_1_x_junit_4 branch](https://github.com/teggr/testcontainers-demo/tree/spring_boot_2_1_x_junit_4).
***
This is the first of a short series of posts showing how the [TestContainers](https://www.testcontainers.org/) project can be leveraged to help test a [Spring Boot](https://spring.io/projects/spring-boot) application in a variety of ways.

- Part One - Data access layer integration tests
- [Part Two - Application integration tests](/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/)
- [Part Three - UI Tests](/2019/02/24/testing-spring-boot-applications-with-testcontainers-and-selenium-webdriver-part-three/)

In this first part, we are going to concentrate on using the [TestContainers database support](https://www.testcontainers.org/modules/databases/) to ensure that our [Flyway](https://flywaydb.org/) scripts and [Spring Data JPA](https://spring.io/projects/spring-data-jpa) configuration are integrated correctly.

## testcontainers-demo

We will be using the [testcontainers-demo](https://github.com/teggr/testcontainers-demo) application as the System under test (SUT). The application routes notification messages from a JMS Queue to a RabbitMQ exchange, storing each notification in a Postgres database. This application also provides a web interface to see a list of all the messages that are routed by the application.

![]({{site.baseurl}}/images/testcontainers-demo.png)

## What is TestContainers?

The TestContainers site perfectly describes it's goals:

> Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

And the areas of testing that it can help with:

> Testcontainers make the following kinds of tests easier
> 
> **Data access layer integration tests**: use a containerized instance of a MySQL, PostgreSQL or Oracle database to test your data access layer code for complete compatibility, but without requiring complex setup on developers' machines and safe in the knowledge that your tests will always start with a known DB state. Any other database type that can be containerized can also be used.  
> **Application integration tests**: for running your application in a short-lived test mode with dependencies, such as databases, message queues or web servers.  
> **UI/Acceptance tests**: use containerized web browsers, compatible with Selenium, for conducting automated UI tests. Each test can get a fresh instance of the browser, with no browser state, plugin variations or automated browser upgrades to worry about. And you get a video recording of each test session, or just each session where tests failed.

## Data access layer integration tests

As we are only looking at the data layer in this post, we can make use of the [Spring Boot Auto-configured tests](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-tests) feature. Our application uses the Spring Data JPA framework to store and retrieve Notifications to a Postgres database. Testing this approach is supported by the [Auto-configured Data JPA tests](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test). By default the support will wire up a inmemory database and use the JPA "create-drop" functionality to apply a schema to the db for testing purposes.

Our application uses Flyway to manage it's database schema. This is normally applied when the application starts up as part of the [Spring Boot support for Flyway](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html) scripts. By using only an inmemory database we are not validating the Flyway script or that the Flyway script and JPA annotated entities are being kept in sync.

The Flyway script for our project is shown below:

```
CREATE TABLE notification (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    message varchar(255) not null,
    source varchar(255) not null
);

create sequence notification_id_sequence start with 1 increment by 1;
```

We have a corresponding Repository interface and Entity in the code as shown below:

```
public interface NotificationRepository extends JpaRepository<Notification,Long>  {}
```

```
/**
 * Notification representing an event in the ecosystem
 */
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @SequenceGenerator(name = "notification_id_generator", sequenceName = "notification_id_sequence", allocationSize = 1)
    @GeneratedValue(generator = "notification_id_generator")
    private Long id;
    private String message;
    private String source;
```

We want to use TestContainers to start up a Postgres database, allow Spring Boot to apply the Flyway script, then test our NotificationRepository is configured correctly and can talk to a running instance of Postgres using JUnit tests.

We start with a plain [Auto-configured Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-testing-spring-boot-applications-testing-autoconfigured-jpa-test) unit test.

```
@RunWith(SpringRunner.class)
@DataJpaTest
public class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository repository;
```

At this point the test will fail because an embedded database cannot be found on the classpath. The next step is to use the @AutoConfigureTestDatabase annotation to configure the JUnit test to not replace the application database configuration.

```
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository repository;
```

The tests will now be picking up your application database configuration which will likely be pointing to your local development environment. So next steps are to introduce the PostgresContainer from the TestContainers project.

```
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class NotificationRepositoryTest {
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    @Autowired
    private NotificationRepository repository;
```

This configuration now loads up a Postgres container in Docker at the start of the test. This can be configured per test if required.

However, Spring Boot has not been configured to point to this running database yet. This requires a little more configuration of the JUnit test.

Adding a [ConfigFileApplicationContextInitializer](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-configfileapplicationcontextinitializer-test-utility) to the test will allow us to inject some new environment variables into the test context that will point the application code at the running docker environment.

```
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { NotificationRepositoryTest.Initializer.class })
public class NotificationRepositoryTest {
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    @Autowired
    private NotificationRepository repository;
...
    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
            "spring.datasource.password=" + postgreSQLContainer.getPassword())
                .applyTo(configurableApplicationContext.getEnvironment());
            }
}
```

This code allows us to get a handle on the container configuration and override the spring boot properties, in doing so that test will now apply the flyway script to our containerised database and the jpa code is connected.

The console will show all the logs from the TestContainers code starting up the database container before executing the tests.

```
...
2019-02-09 16:35:08.796  INFO 8016 --- [           main] o.f.c.internal.database.DatabaseFactory  : Database: jdbc:postgresql://localhost:32815/test (PostgreSQL 11.1)
...
2019-02-09 16:35:09.263  INFO 8016 --- [           main] o.f.core.internal.command.DbMigrate      : Successfully applied 1 migration to schema "public" (execution time 00:00.303s)
...
2019-02-09 16:35:13.806  INFO 8016 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2019-02-09 16:35:15.394  INFO 8016 --- [           main] c.r.t.r.NotificationRepositoryTest       : Started NotificationRepositoryTest in 11.925 seconds (JVM running for 27.482)
2
```

The full JUnit test is below:

```
package com.robintegg.testcontainersdemo.routing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { NotificationRepositoryTest.Initializer.class })
public class NotificationRepositoryTest {

	@ClassRule
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

	@Autowired
	private NotificationRepository repository;

	@Test
	public void shouldStoreEachNotification() {

		// given
		repository.save(new Notification("message1", "test"));
		repository.save(new Notification("message2", "test"));

		// when
		long count = repository.count();

		// then
		assertThat(count, is(2L));

	}

	@Test
	public void shouldStoreEachNotificationWithAUniqueIdentifier() {

		// given
		Notification n1 = repository.save(new Notification("message3", "test"));
		Notification n2 = repository.save(new Notification("message4", "test"));

		// when
		Notification persistedNotification1 = repository.getOne(n1.getId());
		Notification persistedNotification2 = repository.getOne(n2.getId());

		// then
		assertThat(persistedNotification1, equalTo(n1));
		assertThat(persistedNotification2, equalTo(n2));

	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

			TestPropertyValues
					.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSQLContainer.getUsername(),
							"spring.datasource.password=" + postgreSQLContainer.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());

		}

	}

}

```

So at the end of this post we have successfully created a JUnit test that executes the data access layer of the application against a Postgres database running in a Docker container

In the [second part](/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/), we will move up a gear and look to use [TestContainers](https://www.testcontainers.org/) to run a Spring Boot test that will run all our dependencies using Docker containers.
