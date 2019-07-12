---
layout: post
title: "Testing Spring Boot applications with TestContainers and Selenium WebDriver - Part Three"
date: "2019-02-24"
---

- [Part One - Data access layer integration tests](http://robintegg.com/2019/02/09/testing-spring-boot-applications-with-testcontainers/)
- [Part Two - Application integration tests](https://www.robintegg.com/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/)
- Part Three - UI tests

This is the third of a short series of posts showing how the [TestContainers](https://www.testcontainers.org/) project can be leveraged to help test a [Spring Boot](https://spring.io/projects/spring-boot) application in a variety of ways.

In the [first post](http://robintegg.com/2019/02/09/testing-spring-boot-applications-with-testcontainers/), we concentrated on using the [TestContainers database support](https://www.testcontainers.org/modules/databases/) and the [second post](https://www.robintegg.com/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/)  
used [TestContainers](https://www.testcontainers.org/) to run a Spring Boot test that ran all our dependencies using Docker containers.

This third post looks at the final layer - UI. We will cover using the [WebDriver Container](https://www.testcontainers.org/modules/webdriver_containers/) support to spin up the UI, run our UI test and capture the whole session in a video.

## testcontainers-demo

We will continue to use the [testcontainers-demo](https://github.com/teggr/testcontainers-demo) application as the System under test (SUT). The application routes notification messages from a JMS Queue to a RabbitMQ exchange, storing each notification in a Postgres database. This application also provides a web interface to see a list of all the messages that are routed by the application.

![](/images/testcontainers-demo.png)

## UI tests

The [TestContainers](https://www.testcontainers.org/) projects contains support for [WebDriver Containers](https://www.testcontainers.org/modules/webdriver_containers/) that are pre-packaged Docker images based on the selenium docker images. A JUnit test case can spin up one of these containers, grab the RemoteWebDriver and start testing.

For our test example we are going to spin up a Chrome browser, navigate to the homepage and assert that the correct page is being shown.

In order for a WebDriver Container to connect to our UI, we need to ensure that the code is exposed via a local port. Spring Boot testing supports us here with the @SpringBootTest annotation webEnvironment value. We are going run this test on a random port.

```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UITest {
```

We continue by adding a JUnit rule to load a WebDriver Container.

```
@Rule
public BrowserWebDriverContainer chrome = 
                              new BrowserWebDriverContainer()
	    .withRecordingMode(VncRecordingMode.RECORD_FAILING, new File("./target/"))
            .withCapabilities(new ChromeOptions());
```

This container is configured with a default set of Chrome options and sets the container to save the recordings of any failed tests to our "target" directory.

Each test can now grab a RemoteWebDriver instance to drive the Chrome browser.

```
@Test
	public void shouldSuccessfullyPassThisTestUsingTheRemoteDriver() throws InterruptedException {

		RemoteWebDriver driver = chrome.getWebDriver();

		System.out.println("Selenium remote URL is: " + chrome.getSeleniumAddress());
		System.out.println("VNC URL is: " + chrome.getVncAddress());

String url = "http://host.docker.internal:" + port + "/";
		System.out.println("Spring Boot URL is: " + url);
		driver.get(url);
```

If this test was to fail, then the target directory would have a _\*.flv_ file created for playback. This is a great feature which provides a valuable feedback mechanism for debugging broken tests. I've downloaded VLC to playback the captured format.

![](/images/failed-capture-video-1024x638.png)

Because the @SpringBootTest annotation requires that the entire application is loaded, this means that our JMS and RabbitMQ auto configuration is also enabled, so for this test we also need to ensure that our other containers are running.

The full JUnit test is below:

```
package com.robintegg.testcontainersdemo;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { UITest.Initializer.class }, classes = RabbitMqTestConfiguration.class)
public class UITest {

	@LocalServerPort
	private int port;

	// @formatter:off
	@Rule
	public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
			.withRecordingMode(VncRecordingMode.RECORD_FAILING, new File("./target/"))
			.withCapabilities(new ChromeOptions());
	// @formatter:on

	@ClassRule
	public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

	@ClassRule
	public static GenericContainer<?> activeMQContainer = new GenericContainer<>("rmohr/activemq:latest")
			.withExposedPorts(61616);

	@ClassRule
	public static GenericContainer<?> rabbitMQContainer = new GenericContainer<>("rabbitmq:management")
			.withExposedPorts(5672);

	@Test
	public void shouldSuccessfullyPassThisTestUsingTheRemoteDriver() throws InterruptedException {

		RemoteWebDriver driver = chrome.getWebDriver();

		System.out.println("Selenium remote URL is: " + chrome.getSeleniumAddress());
		System.out.println("VNC URL is: " + chrome.getVncAddress());

		String url = "http://host.docker.internal:" + port + "/";
		System.out.println("Spring Boot URL is: " + url);
		driver.get(url);

		List<WebElement> results = new WebDriverWait(driver, 15)
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("h1")));

		assertThat(results.size(), is(1));
		assertThat(results.get(0).getText(), containsString("Notifications"));

	}

	@Test
	public void shouldFailThisTestUsingTheRemoteDriverAndGenerateAVideoRecording() throws InterruptedException {

		RemoteWebDriver driver = chrome.getWebDriver();

		System.out.println("Selenium remote URL is: " + chrome.getSeleniumAddress());
		System.out.println("VNC URL is: " + chrome.getVncAddress());

		String url = "http://host.docker.internal:" + port + "/";
		System.out.println("Spring Boot URL is: " + url);
		driver.get(url);

		// added for effect when viewing the video
		Thread.currentThread().sleep(1000);

		List<WebElement> results = new WebDriverWait(driver, 15)
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("h1")));

		assertThat(results.size(), is(2));

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

## Why wait? Try TestContainers today :)

So we've seen in this mini-series a set of configurations and uses for the TestContainers project.

There's plenty of other uses for the TestContainers project which we will have a look at in the future including getting TestContainers up and running with one of my favorite tools - [Serenity BDD](https://serenity-bdd.github.io/theserenitybook/latest/getting-started.html).
