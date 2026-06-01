---
layout: post
title: "Why does Spring Initializr set the parent pom relativePath to empty?"
date: "2019-01-20"
description: "Explaining why Spring Initializr sets the Maven parent POM relativePath to empty and how this optimization affects dependency resolution."
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - java
  - tools
  - spring boot
  - maven
---
If, like me, you use the [https://start.spring.io/](https://start.spring.io/) web service to create your new Spring Boot projects, then you may have noticed that the pom file defined an empty relativePath element with accompanying comment.

```
<parent
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.0.5.RELEASE</version>
  <relativePath/> <!-- lookup parent from repository -->
</parent>
```

Now I've not really had much concern to question this setting. It's certainly never given me any issues on my local development environment or or any CI server, but I recently received criticism on its usage when submitting a technical code exercise for a new job. This prompted me to dig deeper as I couldn't explain the reason why or why not to include it.

> tl;dr - it's an optimisation. in fact it says so in the comment :)

As you can see, the pom file already has a comment, presumably because this is perhaps a slightly irregular usage.

Diving into the maven documentation, the relativePath field is documented:

[http://maven.apache.org/ref/3.0/maven-model/maven.html#class\_parent](http://maven.apache.org/ref/3.0/maven-model/maven.html#class_parent)

> The relative path of the parent pom.xml file within the check out. If not specified, it defaults to ../pom.xml. Maven looks for the parent POM first in this location on the filesystem, then the local repository, and lastly in the remote repo. relativePath allows you to select a different location, for example when your structure is flat, or deeper without an intermediate parent POM. However, the group ID, artifact ID and version are still required, and must match the file in the location given or it will revert to the repository for the POM. This feature is only for enhancing the development in a local checkout of that project. Set the value to an empty string in case you want to disable the feature and always resolve the parent POM from the repositories.

Great, so what does that mean? It means that if you leave it out, which may be the more common approach, similar to the [Spring Boot guides](https://spring.io/guides), then maven will firstly look at parent directory, fail to find the pom and then fallback to looking up in your local repo and then remote repository. Spring Initializr, by using the empty relativePath, forces maven to move straight to your local/remote repositories, so missing a step that would obviously fail.

So, if nothing else, I've learnt a little bit about maven here and perhaps helped somebody else explain why things are what they are :)
