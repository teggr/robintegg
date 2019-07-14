---
layout: post
title: "Testing Spring Boot applications with TestContainers - Part Two"
date: "2019-02-12"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
---
- [Part One - Data access layer integration tests](/2019/02/09/testing-spring-boot-applications-with-testcontainers/)
- Part Two - Application integration tests
- [Part Three - UI tests](/2019/02/24/testing-spring-boot-applications-with-testcontainers-and-selenium-webdriver-part-three/)

This is the second of a short series of posts showing how the [TestContainers](https://www.testcontainers.org/) project can be leveraged to help test a [Spring Boot](https://spring.io/projects/spring-boot) application in a variety of ways.

In the [first post](/2019/02/09/testing-spring-boot-applications-with-testcontainers/), we concentrated on using the [TestContainers database support](https://www.testcontainers.org/modules/databases/) to ensure that our [Flyway](https://flywaydb.org/) scripts and [Spring Data JPA](https://spring.io/projects/spring-data-jpa) configuration were integrated correctly.

For this second part, we will move up a gear and look to use [TestContainers](https://www.testcontainers.org/) to run a Spring Boot test that will run all our dependencies using Docker containers.

## testcontainers-demo

We will continue to use the [testcontainers-demo](https://github.com/teggr/testcontainers-demo) application as the System under test (SUT). The application routes notification messages from a JMS Queue to a RabbitMQ exchange, storing each notification in a Postgres database. This application also provides a web interface to see a list of all the messages that are routed by the application.

![]({{site.baseurl}}/assets/images/testcontainers-demo.png)

## Application integration tests

In order to run a test that reads from the JMS Queue and publishes to the RabbitMQ Exchange we will need JMS and RabbitMQ brokers. Using [TestContainers](https://www.testcontainers.org/) we can spin up these brokers using docker and configure our tests to use the transient brokers.

[TestContainers](https://www.testcontainers.org/) does not have any advanced support for these services so we will need to use the [GenericContainer](https://www.testcontainers.org/features/creating_container/) support. To do this we specify an image we want to run.

```
@ClassRule
public static GenericContainer<?> activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
			.withExposedPorts(61616);

@ClassRule
public static GenericContainer<?> rabbitMQContainer = new GenericContainer<>("rabbitmq:management")
			.withExposedPorts(5672);
```

In the above code, using the available fluent methods, we also declare what ports we want TestContainers to make available to our unit test.

In the first post we configured the JUnit test to override our application's Spring Boot properties with the TestContainer configuration values. Now we have three containers, we can look to move those items into a single static method for convenience. Also note that for the ActiveMQ and RabbitMQ port properties we have to specify what port mapping we want to retrieve.

```
public class DemoApplicationTestPropertyValues {

	public static TestPropertyValues using(PostgreSQLContainer<?> postgreSQLContainer,
			GenericContainer<?> activeMQContainer, GenericContainer<?> rabbitMQContainer) {
		List<String> pairs = new ArrayList<>();

		// postgres
		pairs.add("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl());
		pairs.add("spring.datasource.username=" + postgreSQLContainer.getUsername());
		pairs.add("spring.datasource.password=" + postgreSQLContainer.getPassword());
		// activemq
		pairs.add("spring.activemq.broker-url=tcp://localhost:" + activeMQContainer.getMappedPort(61616));
		// rabbitmq
		pairs.add("spring.rabbitmq.port=" + rabbitMQContainer.getMappedPort(5672));

		return TestPropertyValues.of(pairs);
	}
}
```

This code will now be called from the ApplicationContextInitializer in our test

```
static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

			DemoApplicationTestPropertyValues.using(postgreSQLContainer, activeMQContainer, rabbitMQContainer)
					.applyTo(configurableApplicationContext.getEnvironment());

		}

	}
```

Now that we have all three containers configured we are ready to write a test that can invoke the application through actual services rather than in memory versions.

The full JUnit test is below:

```
package com.robintegg.testcontainersdemo.routing;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robintegg.testcontainersdemo.inbound.JMSNotification;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { RoutingTest.Initializer.class }, classes = RabbitMqTestConfiguration.class)
public class RoutingTest {

	@ClassRule
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

	@ClassRule
	public static GenericContainer<?> activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
			.withExposedPorts(61616);

	@ClassRule
	public static GenericContainer<?> rabbitMQContainer = new GenericContainer<>("rabbitmq:management")
			.withExposedPorts(5672);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	public void shouldStoreANotifcationFromTheJmsQueueAndForwardToTheRabbitMQExchange() throws Exception {

		// given
		String message = "TestContainers are great";
		JMSNotification jmsNotification = new JMSNotification(message);

		// when
		sendNotificationToJmsQueue(jmsNotification);

		// then
		assertThatNotificationIsForwardedToRabbitMq(message);
		assertThatNotificationIsStoredInTheDatabase(message);

	}

	private void assertThatNotificationIsStoredInTheDatabase(String message) {
		Notification notification = notificationRepository.findAll().get(0);
		assertThat(notification.getMessage(), is(message));
		assertThat(notification.getSource(), is("JMS"));
		assertThat(notification.getId(), notNullValue());
	}

	private void assertThatNotificationIsForwardedToRabbitMq(String message) {
		Notification notification = readNotificationFromRabbitMqQueue();
		assertThat(notification.getMessage(), is(message));
		assertThat(notification.getSource(), is("JMS"));
		assertThat(notification.getId(), notNullValue());
	}

	private void sendNotificationToJmsQueue(JMSNotification jmsNotification) throws Exception {
		jmsTemplate.convertAndSend("jms.events", objectMapper.writeValueAsString(jmsNotification));
	}

	private Notification readNotificationFromRabbitMqQueue() {
		ParameterizedTypeReference<Notification> notificationTypeRef = new ParameterizedTypeReference<Notification>() {
		};

		Notification notification = rabbitTemplate.receiveAndConvert("testcontainers.test.queue", 1000,
				notificationTypeRef);
		return notification;
	}

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

			DemoApplicationTestPropertyValues.using(postgreSQLContainer, activeMQContainer, rabbitMQContainer)
					.applyTo(configurableApplicationContext.getEnvironment());

		}

	}

}

```

Now we've got a template [TestContainers](https://www.testcontainers.org/) JUnit test you can start to explore further scenarios that might be more relevant to your own projects.

One extension might be to use databases loaded with production levels of data to test performance of your application. This can be managed by attaching [volumes](https://www.testcontainers.org/features/files/) to your database containers.

Furthermore, externally managed HTTP services could be replaced with [WireMock](http://wiremock.org/) stubs running in containers.

[TestContainers](https://www.testcontainers.org/) also contains some pretty useful support for [containers running WebDriver](https://www.testcontainers.org/modules/webdriver_containers/). This gives you excellent support for further UI test automation and regression testing. This is something we will elaborate on in the next post.
