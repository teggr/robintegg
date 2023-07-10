---
layout: post
title: Less-code over low-code 
date: "2022-06-15"
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - outsystems
  - productivity
---
# I'd prefer less-code over low-code

In my current workplace I've had some experience working with a low-code platform called [OutSystems](https://www.outsystems.com/) which provides a drag'n'drop like experience to building apps. This low-code approach can be quite refreshing and also quite productive. The Low-code approach to development is definitely gaining traction and I can see the benefits for both experienced and casual developer use cases. It's certainly had me questioning the productivity of my own toolset. The visual aspect of developing UI and API components is particularly appealing to me personally as I can find the amount of HTML/Java code sometimes too much when scaled to the larger projects.

My standard toolset for building webapps is the Spring Boot platform. Spring Boot is great for getting a project up and running with much of the framework infrastructure ready to go pending your configuration. The integration of the [Spring Boot Initializr](https://start.spring.io/) into the [IDE](https://code.visualstudio.com/docs/java/java-spring-boot)s is really useful, especially when trying any new Spring features or integrations.

However, I feel like once the project has been loaded with my dependencies like the Web and Thymeleaf starters, there's still quite a lot of boilerplate code to get a decent web application out of the door. Mainly I'm talking about plumbing together html, dtos, API and form controllers, mapping to domain objects and persistence.

So I'm commiting to write a short series of articles where I evaluate some new features and combinations of libraries and tooling to see if I should be updating my toolset to enable some greater productivity or at least make the dev process a little more enjoyable.
