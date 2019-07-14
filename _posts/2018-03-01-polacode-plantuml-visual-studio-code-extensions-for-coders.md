---
layout: post
title: "Polacode + PlantUML - Visual Studio Code extensions for coders"
date: "2018-03-01"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
---
## **Polacode**

The [Polacode](https://marketplace.visualstudio.com/items?itemName=pnp.polacode) extension can generate screenshots of your code in Visual Studio Code.

It's a great extension to get example code into a well formatted and nicely displayed image. Good for code snippets where you don't see a need for any copy or pasting. Also useful for putting screenshots into chat windows like skype or slack.

![]({{site.baseurl}}/assets/images/code.png)

## **PlantUML**

I'm a big fan of using PlantUML to generate technical diagrams on the fly using a text based dsl. I've previously written about deploying a [PlantUML editor in a docker container](/2018/01/14/deploying-a-plantuml-spring-boot-application-in-a-docker-container-to-sloppy-io/) and having a solution in Visual Studio Code gives me a neat offline ability to edit files too.

[PlantUML ](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml#overview) is one of a number of PlantUML extensions wrappers. This one by the author [jebbs](https://marketplace.visualstudio.com/search?term=publisher%3A%22jebbs%22&target=VSCode&category=All%20categories&sortBy=Relevance) seemed to be the most downloaded which can be an indicator of a good extension. It certainly has a number of supported features including image generation and document formatting.

PlantUML documents can be recognised and edited if they have a supporting file extension such as \*.puml

![]({{site.baseurl}}/assets/images/plantuml-code-1024x387.png)

Those documents can then be exported as diagrams of various formats.

![]({{site.baseurl}}/assets/images/example.png)

## **Installation notes**

- Install GraphViz by going to https://graphviz.gitlab.io/, download the \*.msi and install
- To install PlantUML go to http://plantuml.com/, download the JAR file and put in an easily accessible folder, such as C:\\tools\\plantuml\\plantuml.1.2018.1.jar
- Install the PlantUML extension through the Extension tab in Visual Studio Code and update your user settings to point at your latest PlantUML jar

![]({{site.baseurl}}/assets/images/plantuml-settings-1024x267.png)
