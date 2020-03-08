---
layout: post
title: Using cloud sleuth for testing
date: "2019-12-07"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - java
  - serenity
  - acceptance-test
  - spring-boot-cloud
  - sleuth
---
# Acceptance testing

Here we are going to use [Serenity](http://serenity-bdd.info/#/) for writing some acceptance tests for an API.

When we scale up to 10s or 100s of tests understanding what went wrong where can be a pain. This is a good indicator alone that debugging in live is probably going to be an issue too.

* Help with resolving issues with your tests (motes, not notes)
* Help with resolving issues on the server side (field too long for database)

So we are going to add a some functionality into our project to satisfy both debugging tests and live issues.

# The project

A simple Spring boot application for managing a list of favorite podcasts on [Github:teggr/xx-project](http://teggr).

We have endpoints to query / get / create that we have defined using gherkin format and wish to execute via Serenity against the deployed service.

```
Feature: Creating podcasts

Narrative:

As a user
I want to store my favorite podcast information
So that I can listen to the episodes

Scenario: Create new


```

We will not go into any further details around the deployment process in this article, suffice to say that once the app is built it is deployed into a remote environment before running the acceptance tests against it.



# Spring Cloud Sleuth

