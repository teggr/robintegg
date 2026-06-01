---
layout: post
title: "j2html Static Site Generator"
date: "2023-06-19"
description: "Building type-safe HTML documents in Java using j2html's fluent API for static site generation."
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - java
  - j2html
  - web
---
[j2html](https://j2html.com/) is a *fast and fluent* Java Html5 builder from the authors of the [Javalin](https://javalin.io/) web framework. It allows the developer to code HTML documents using Java code with type safety and in  a preferred language and development environment.

To see a simple example, add the j2html dependency.

```xml
<dependency>
    <groupId>com.j2html</groupId>
    <artifactId>j2html</artifactId>
    <version>1.6.0</version>
</dependency>
```

Use the TagCreator to get started. j2html's syntax is fluent and closely matched to HTML:

```java
import static j2html.TagCreator.*;

public class Main {
    public static void main(String[] args) {
        body(
            h1("Hello, World!"),
            img().withSrc("/img/hello.png")
        ).render();
    }
}
```

The Java code above becomes the HTML below:

```html
<body>
    <h1>Hello, World!</h1>
    <img src="/img/hello.png">
</body>
```

As a Java developer I like the idea of staying within a single language for both generation and rendering of web content.

I'm going to explore how far I can push this concept, starting with the blog and re-developing it using the j2html library instead of jekyll.

It will be interesting to see if it feels more or less productive to code with that the typical code in a html template or javascript framework. My recent experience with the [hilla framework](https://robintegg.com/2022/07/26/first-look-at-hilla-web-framework.html) left me feeling that the learning curve was too steep and required too broad a knowledge for me to start using effectively. 