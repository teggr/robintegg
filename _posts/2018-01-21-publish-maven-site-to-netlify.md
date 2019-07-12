---
layout: post
title: "Publish Maven Site to Netlify"
date: "2018-01-21"
---
Having written a couple of posts with the [Hexo](https://hexo.io/) static site generator I wanted to start building out my site with extras such as [Pocket](https://getpocket.com/) links and details about my side Projects. Trying to extend the site has proved harder that I thought it might, so I'm going to switch over to another platform for publishing. This is likely going to be based on Spring Boot which I had previously considered writing after trying [JBake](http://jbake.org/).

What I liked about Hexo originally was that I could easily publish to [Netlify](https://www.netlify.com/) out of the box. It looked like I would only be able to publish the static output as part of a build step outside of Netlify. So I started to look around for an alternative and ended up on a site called [StaticGen](https://www.staticgen.com/) which I think is produced by Netlify and lists out all their known Static Site Generators. Turns out there were plenty of Java based generators. One of them, called [Orchid](https://javaeden.github.io/Orchid/latest/OrchidCore/), had a **Deploy to Netlify** badge indicating that a Java project might be supported by Netlify, if not offically documented or easily found.

The starter project that you can deploy to Netlify is available through the github repo [JavaEden/OrchidStarter](https://github.com/JavaEden/OrchidStarter). The process of deploying to Netlify was simple and the deployment uses the gradle wrapper to download and execute the build step for Orchid.

My preference is to use Maven over Gradle so I investigated the possibility of using maven to generate the site and have that deployed on Netlify.

Starting with a standard maven project

```
mvn -B archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DgroupId=com.robintegg.blog \
  -DartifactId=netlify-upload
```

Influenced by the Gradle Wrapper, there is a \[Maven Wrapper\](mvn -N io.takari:maven:wrapper) available. The wrapper can be added using the maven task

```
mvn -N io.takari:maven:wrapper
```

**Windows Users **Be aware that the \`./mvnw\` command needs to retain it's executable state when being deployed to GitHub so that it can be executed by Netlify. The [Lennart Schedin blog](http://blog.lesc.se/2011/11/how-to-change-file-premissions-in-git.html) provides details and explanations on how to do this. The commands for changing the permissions are summarized below:

```
>git ls-tree HEAD100644 blob 55c0287d4ef21f15b97eb1f107451b88b479bffe mvnw
>git update-index --chmod=+x .mvnw
>git status>git commit -m "Changing file permissions"
>git ls-tree HEAD100755 blob 55c0287d4ef21f15b97eb1f107451b88b479bffe   mvnw
```

For supporting the Netlify deployment, you can add the optional **netlify.toml** which will override any deploy settings you might have set in the UI.

```
[build]  base    = ""  publish = "target/site"  command = "./mvnw clean site"
```

Now, you can publish to Github and deploy the project through Netlify.

My finished Github repo [teggr/netlify-upload](https://github.com/teggr/netlify-upload) and is was automatically deployed to [https://compassionate-easley-929b6f.netlify.com](https://compassionate-easley-929b6f.netlify.com).
