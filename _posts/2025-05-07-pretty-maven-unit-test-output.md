---
layout: post
title: Pretty Maven unit test output
date: "2025-05-07"
description: "Improve Maven Surefire test output with the JUnit 5 Tree Reporter plugin for hierarchical test visualization."
image: /images/surefire-tests.png
tags:
  - java
  - maven
  - junit
---

Hey there, Java developers! Ever found yourself scrolling through pages of Surefire output, trying to piece together which JUnit 5 tests passed or failed, and in what order they ran? It can feel like searching for a needle in a haystack, right?

Well, the [Maven Surefire JUnit 5 Tree Reporter](https://github.com/fabriciorby/maven-surefire-junit5-tree-reporter?tab=readme-ov-file) project aims to make that experience significantly better. This nifty little reporter plugin steps in to provide a more structured and readable output of your JUnit 5 test executions directly in your Maven build logs.

[https://github.com/fabriciorby/maven-surefire-junit5-tree-reporter?tab=readme-ov-file](https://github.com/fabriciorby/maven-surefire-junit5-tree-reporter?tab=readme-ov-file)

Instead of a flat list of test results, this reporter presents your tests in a hierarchical tree view, mirroring your test class structure. This makes it instantly easier to:

* Visualize your test organization: See at a glance how your tests are grouped within classes and nested classes.
* Pinpoint failures quickly: Failed tests are clearly highlighted within their context, making it much faster to identify the source of the problem.
* Understand test execution flow: The tree structure can give you a better sense of the order in which your tests and their lifecycle methods are being executed.

Let's take a whirlwird look at how we can improve the out-of-the-box unit test reporting in a couple of easy steps.

Here's a simple junit 5 test with Nested and Parameterized tests.

```java
class TreeReporterTest {

	@Test
	void simpleTest() {}

	@Nested
	class NestedTests {

		@Test
		void simpleNestedTest() {}

		@ParameterizedTest
		@ValueSource(strings = {"a", "b"})
		void simpleNestedParameterisedTest(String str) {}

	}

}
```

What does this output look like? 

```bash
> mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running cf.dev.presentation.TreeReporterTest
[INFO] Running cf.dev.presentation.TreeReporterTest$NestedTests
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.024 s -- in cf.dev.presentation.TreeReporterTest$NestedTests
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.043 s -- in cf.dev.presentation.TreeReporterTest
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
```

This can be described as functional output at best. So how do we get that fancy tree-like structure to our output. Read on.

Make the following changes to your maven POM file to update or declare your `maven-surefire-plugin` configuration.

```xml
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${maven-surefire-plugin.version}</version>
    <dependencies>
        <dependency>
            <groupId>me.fabriciorby</groupId>
            <artifactId>maven-surefire-junit5-tree-reporter</artifactId>
            <version>1.4.0</version>
        </dependency>
    </dependencies>
    <configuration>
        <reportFormat>plain</reportFormat>
        <consoleOutputReporter>
            <disable>true</disable>
        </consoleOutputReporter>
        <statelessTestsetInfoReporter
                implementation="org.apache.maven.plugin.surefire.extensions.junit5.JUnit5StatelessTestsetInfoTreeReporter">
                <theme>UNICODE</theme>
        </statelessTestsetInfoReporter>
    </configuration>
</plugin>
```

Eh voila.

```bash
> mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] ├─ cf.dev.presentation.TreeReporterTest - 0.046 s
[INFO] │  └─ ✔ simpleTest - 0.010 s
[INFO] ├─ ── cf.dev.presentation.TreeReporterTest$NestedTests - 0.023 s
[INFO] │     ├─ ✔ simpleNestedParameterisedTest(String)[1] str=a - 0.004 s
[INFO] │     ├─ ✔ simpleNestedParameterisedTest(String)[2] str=b - 0 s
[INFO] │     └─ ✔ simpleNestedTest - 0.001 s
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
```

Real nice ! 

* Tree structure of tests
* Nested tests are children
* Parameterized tests are shown as separate tests

But we can go one more if you have time? Lets start using the `@DisplayName` annotation to make those tree entries nicely formatted and using plain wording.

```java
@DisplayName( "Surefire Tree Reporting Plugin Examples" )
class TreeReporterTest {

	@DisplayName( "A simple test at the top level" )
	@Test
	void simpleTest() {

	}

	@DisplayName( "A set of @Nested tests" )
	@Nested
	class NestedTests {

		@DisplayName( "A nested test" )
		@Test
		void simpleNestedTest() {

		}

		@DisplayName( "A nested and parameterised test " )
		@ParameterizedTest(name = "using the string value: {0}")
		@ValueSource(strings = {"a", "b"})
		void simpleNestedParameterisedTest(String str) {}

	}

}
```

Now we get some delightful test output.

```bash
> mvn test
...
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] ├─ Surefire Tree Reporting Plugin Examples - 0.048 s
[INFO] │  └─ ✔ A simple test at the top level - 0.008 s
[INFO] ├─ ── Surefire Tree Reporting Plugin Examples A set of @Nested tests - 0.022 s
[INFO] │     ├─ ✔ A nested and parameterised test using the string value: a - 0.005 s
[INFO] │     ├─ ✔ A nested and parameterised test using the string value: b - 0.001 s
[INFO] │     └─ ✔ A nested test - 0.001 s
[INFO] 
[INFO] Results:
[INFO] 
[WARNING] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
```