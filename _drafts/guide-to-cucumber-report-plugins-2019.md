---
layout: post
title: Guide to Cucumber report plugins 2019
date: "2019-07-14"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - java
  - bdd
  - cucumber
---
The [Cucumber framework](https://cucumber.io/docs) is a great framework for executing the Gherkin documented features elaborating during your BDD sessions.

Getting started with Cucumber is relatively easy if you start with the [Cucumber 10 minute tutorial](https://cucumber.io/docs/guides/10-minute-tutorial/).

Executing the Cucumber tests is only one piece of the puzzle. Debugging the tests can be a barrier  to realising the full value of using the Cucumber framework. Once debugged and executed having a informative report on the tests is vital to provide feedback on the success of the tests and other useful information to help fault resolution, performance metrics and documentation. 

Cucumber provides a Plugin framework that supports hooks into the execution phases of the tests. The debug and reporting features are based on the Plugin framework. 

We are going to look at both the provided plugins as well as some available third-party options. The review will then cover some apects such as:

* running locally
* running on the CI server such as Jenkins
* handling passing/failing/unspecified/pending scenario steps
* handling parameterised 
* support for screenshots
* support for embedded types

We are going to be developing our cucumber tests using our [toolkit for cucumber demo github project](https://github.com/teggr/toolkit-for-cucumber-demo). 

[Take me straight to the comparison table.](#comparison-table) 

### [Summary of Provided Plugins](#provided-plugins)

* [junit](#junit)
* [html](#html)
* [pretty](#pretty)
* [progress](#progress)
* [json](#json)
* [usage](#usage)
* [rerun](#rerun)
* [default_summary](#default_summary)
* [summary](#summary)
* [unused](#unused)
* [timeline](#timeline)

## [Summary of third-party plugins](#third-party-plugins)

* [Cluecumber](#cluecucumber)
* [Extent Reports](#extent-reports)
* [Cucumber Reports](#cucumber-reports)
* [Cucumber Sandwich](#cucumber-sandwich)
* [Prettier Reports](#prettier-reports)

## Provided Plugins

Out of the box, the Cucumber provides a number of [reporting plugins](https://cucumber.io/docs/cucumber/reporting/).

### Junit

Inteprets the results of the tests and writes a [JUnit]() compatible XML file.

```
@RunWith(Cucumber.class)
@CucumberOptions(plugin = "junit:target/junit/output.xml")
public class CucumberJUnit {}
```
XML file.

```
```

If supported by your IDE, you may also open the file in a JUnit view. Such as in Eclipse.

TODO: insert eclipse screenshot here

Use this when you want a JUnit XML format to integrate with other tools such as your CI server, such as Jenkins, which may support the format out of the box. 

```

```

TODO: insert jenkins screenshot here



### Progress

Console Output
```
UUUUUUUUU
```

### Pretty

```
Feature: Addition
  As a user
  In order to be better at arithmetic
  I want to add two numbers toether

  Scenario Outline: Two positive numbers # com/robintegg/cucumber/add.feature:7
    Given I have the numbers <number1> and <number2>
    When I use the calculator to add the two numbers together
    Then I should get the <result>

    Examples: 

  Scenario Outline: Two positive numbers                      # com/robintegg/cucumber/add.feature:14
    Given I have the numbers 0 and 1                          # null
    When I use the calculator to add the two numbers together # null
    Then I should get the 1                                   # null

  Scenario Outline: Two positive numbers                      # com/robintegg/cucumber/add.feature:15
    Given I have the numbers 1 and 0                          # null
    When I use the calculator to add the two numbers together # null
    Then I should get the 1                                   # null

  Scenario Outline: Two positive numbers                      # com/robintegg/cucumber/add.feature:16
    Given I have the numbers 1 and 1                          # null
    When I use the calculator to add the two numbers together # null
    Then I should get the 2                                   # null
```

### Html

TODO: add screenshot

### Json

```
[
  {
    "line": 1,
    "elements": [
      {
        "start_timestamp": "2019-07-14T17:39:39.049Z",
        "line": 14,
        "name": "Two positive numbers",
        "description": "",
        "id": "addition;two-positive-numbers;;2",
        "type": "scenario",
        "keyword": "Scenario Outline",
        "steps": [
          {
            "result": {
              "status": "undefined"
            },
            "line": 8,
            "name": "I have the numbers 0 and 1",
            "match": {},
            "keyword": "Given "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 9,
            "name": "I use the calculator to add the two numbers together",
            "match": {},
            "keyword": "When "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 10,
            "name": "I should get the 1",
            "match": {},
            "keyword": "Then "
          }
        ]
      },
      {
        "start_timestamp": "2019-07-14T17:39:39.078Z",
        "line": 15,
        "name": "Two positive numbers",
        "description": "",
        "id": "addition;two-positive-numbers;;3",
        "type": "scenario",
        "keyword": "Scenario Outline",
        "steps": [
          {
            "result": {
              "status": "undefined"
            },
            "line": 8,
            "name": "I have the numbers 1 and 0",
            "match": {},
            "keyword": "Given "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 9,
            "name": "I use the calculator to add the two numbers together",
            "match": {},
            "keyword": "When "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 10,
            "name": "I should get the 1",
            "match": {},
            "keyword": "Then "
          }
        ]
      },
      {
        "start_timestamp": "2019-07-14T17:39:39.082Z",
        "line": 16,
        "name": "Two positive numbers",
        "description": "",
        "id": "addition;two-positive-numbers;;4",
        "type": "scenario",
        "keyword": "Scenario Outline",
        "steps": [
          {
            "result": {
              "status": "undefined"
            },
            "line": 8,
            "name": "I have the numbers 1 and 1",
            "match": {},
            "keyword": "Given "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 9,
            "name": "I use the calculator to add the two numbers together",
            "match": {},
            "keyword": "When "
          },
          {
            "result": {
              "status": "undefined"
            },
            "line": 10,
            "name": "I should get the 2",
            "match": {},
            "keyword": "Then "
          }
        ]
      }
    ],
    "name": "Addition",
    "description": "  As a user\n  In order to be better at arithmetic\n  I want to add two numbers toether",
    "id": "addition",
    "keyword": "Feature",
    "uri": "classpath:com/robintegg/cucumber/add.feature",
    "tags": []
  }
]
```

### Rerun

```
???
```



### Usage

```

```

## Thirdparty plugins for Cucumber-Jvm

#### Cluecumber

https://github.com/trivago/cluecumber-report-plugin

#### Extent Reports

https://github.com/extent-framework/extentreports-cucumber4-adapter

http://grasshopper.tech/824/

#### Cucumber Reports

- Not supporting cucumber 4

#### Prettier Reports

- Standalone

- Jenkins

#### Cucumber Sandwich

## Summary

Out of the box the Cucumber framework does handle a number of use cases but does not provide a fully featured reporting solution that would satify an enterprise level team.

### Comparison Table

| Plugin | Provided |
|-
| [junit](#junit) |x|
| [html](#html) |x|
| [pretty](#pretty) |x|
| [progress](#progress) |x|
| [json](#json) |x|
| [usage](#usage) |x|
| [rerun](#rerun) |x|
| [default_summary](#default_summary) |x|
| [summary](#summary) |x|
| [unused](#unused) |x|
| [timeline](#timeline) |x|
| [Cluecumber](#cluecucumber) ||
| [Extent Reports](#extent-reports) ||
| [Cucumber Reports](#cucumber-reports) ||
| [Cucumber Sandwich](#cucumber-sandwich) ||
| [Prettier Reports](#prettier-reports) ||
|-