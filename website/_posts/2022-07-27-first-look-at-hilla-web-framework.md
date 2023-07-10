---
layout: post
title: First look at Hilla Web Framework from Vaadin
date: "2022-07-26"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - hilla
  - vaadin
  - productivity
---

As part of a series of looking at [less code options](https://robintegg.com/2022/06/15/less-code-over-low-code.html) I started to take a look at [Hilla](https://hilla.dev/) from the Vaadin developers. It's a relatively recent entry into the Java Web Framework space and tightly integrates a Spring Boot Java back end with a reactive TypeScript front end.

There are plenty of resources in their documentation to help get you started such as the [Quickstart tutorial](https://hilla.dev/docs/tutorials/quickstart) and a fuller [In depth course](https://hilla.dev/docs/tutorials/in-depth-course).

What I liked about this framework is that the developers have put in a number of useful features for your development process, such as 

- auto creation of the Typescript bindings so you don't need to define the dtos twice
- cli creation of project with boiler plate security
- existing component library
- expressing Java validation annotations on the front end Typescript forms 

The framework also nicely segrates the front end and backend development without too much faff. Running the code like a normal spring boot app is sufficient in dev mode to also run the front end dev server so that all the code can be run at once.

If you are going to be using the framework, then the combination of builtin vaadin components should enable you to build a working webapp in pretty short time. However, it feels that to reach that higher level of productivity you will need to understand some of the more advanced features of the framework such as the design system. If you are not going to adopt the design system, then you'll likely have to build all the components again which you'd maybe have to do anyway if you used a different approach.

In summary, I started the small [hilla-dev-test-project](https://github.com/teggr/hilla-dev-test-project) on Github to explore the framework, but I think that for small projects there's too much of a learning curve for me to build apps as I would want. However, in a work environment where the teams are commited to using it, I believe it could be a very productive and collaborative approach to building webapps.