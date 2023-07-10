---
layout: post
title: An approach for a Spring Boot Service Template 
date: "2022-06-20"
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - service template
  - productivity
---
# An approach for a Spring Boot Service Template

## The problem

In my current organisation we have created and continue to create a number of Java projects using Maven and Spring Boot which we need to manage. 

We are continuing to evolve our approach to managing these codebases. We intially started from a single codebase using Spring Boot. This codebase was then copied and pasted a few times as we built new services. Beyond a few services it was clear that we had some common patterns and needed to apply some automation to ensure that there was some degree of consistency between projects.

This first phase consisted of creating a [Service Template](https://microservices.io/patterns/service-template.html) using a maven archetype to ensure that the projects are created with fixed set of dependencies and structure.  More on Service Templates in the article [Service Templates and Service Chassis
managing the cost of microservice plumbing](https://blog.thepete.net/blog/2020/09/25/service-templates-service-chassis/). This has ensured that each project continues to start off on the right tracks.

To further complement the template approach, we bundled a number of shared dependencies and patterns into their own [spring-boot-starter](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration) feature libraries. This means that any updates to common code can be shared simply between projects, typically requiring only a version update to bring in the changes.

So we are now running into an issue when a code change requires more that just a version update, such as a new configuration class or application property change. This class of change set includes minor and major upgrades to the Spring Boot version used, infrastructure, CI-CD pipeline changes and soon Java version upgrades.

These types of change require some manual interaction with the codebase, typically following a loose script of changes that need to be made. Potentially this could be scripted in some circumstances if you were confident that the script could be applied to all codebases or at least the majority of cases (TODO: maybe we should investigate further the approach of applying scripts to multiple codebases).

## Using git

One approach that we might be able to use is forking a git repository and pulling those changes from a master branch. On Github this is called [forking](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/working-with-forks/about-forks), though you are not able to fork your own repositories.

The steps to do this are:

### Creating a fork from the seed project

1. Create your service template repository - [spring-boot-seed](https://github.com/teggr/spring-boot-seed)
2. Create a new repository for your new service - `<forked-repo>`
3. Clone your fork
   `git clone https://github.com/<username>/<forked-repo>.git`
4. Add your original repository as an Upstream Remote
   ```
   cd <forked-repo>
   git remote add seed https://github.com/teggr/spring-boot-seed.git
   ```
5. Update your fork
   `git pull seed main`
6. Push
   `git push origin master`

### Updating a fork from the seed project

1. Update your fork
   `git pull seed main`
2. Resolve any merge conflicts
   * Common merge conflicts to resolve - Readme.md, pom.xml, application.properties

### Impact

So does this reduce the time to implement the changes? Well, it depends on the result of your merge. If you don't make many changes to the files that originated from the seed project, then you may be able to simply automerge the changes and run a pipeline, otherwise conflicts will need to be resolved.

Conflict resolution does at least ensure that the correct changes are being applied but the human operator still needs to decide on those conflicts so it's not guaranteed to be completely error free.

## Some other thoughts

I'm not aware of any feature in the maven archetype approach that helps in this regard. [Can you update an existing project from a maven archetype?](https://stackoverflow.com/questions/6547965/maven-archetype-to-modify-an-existing-project#:~:text=You%20simply%20run%20the%20archetype,version%20of%20the%20existing%20project)).

So it might be possible according to the article [How do I create an archetype that can run on an existing project?](http://www.avajava.com/tutorials/lessons/how-do-i-create-an-archetype-that-can-run-on-an-existing-project.html?page=2) but there look to be some issues if there are conflicting files so this approach may require some further investigation.

Using maven archetypes does seem to be an under utlized approach, perhaps due to a lack of comprehensive documentation, so maybe the simple seed project / git approach may be a lower barrier of entry for developers looking to create and manage a service template.
