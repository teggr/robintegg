---
layout: post
title: j2html Extensions 0.0.3 Released
date: "2025-06-23"
image: /images/release.png
tags:
  - java
  - j2html
  - rebebcraft
---

## Release Notes for 0.0.3

### Maven BOM available to manage j2html-extensions artefacts

The bom currently manages the following artefacts:

* j2html-extensions-core
* bootstrap-j2html-extension
* htmx-j2html-extension
* j2html-extensions-spring-boot-starter

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.rebelcraft</groupId>
            <artifactId>j2html-extensions-bom</artifactId>
            <version>0.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### j2html Spring Boot Starter

To help get started, the `j2html-extensions-spring-boot-starter` defines a `BeanViewNameResolver` and defines a new `J2HtmlView` which only requires you to return some `DomContent`.

```xml
<dependency>
    <groupId>dev.rebelcraft</groupId>
    <artifactId>j2html-extensions-spring-boot-starter</artifactId>
</dependency>
```

Using the new `J2HtmlView` view will reduce the amount of boilerplate required to define a new view.

```java
import dev.rebelcraft.j2html.spring.webmvc.J2HtmlView;
import j2html.tags.DomContent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Map;

import static j2html.TagCreator.div;
import static j2html.TagCreator.p;

@Component
public class HomeView extends J2HtmlView {

    @Override
    protected DomContent renderMergedOutputModelDomContent(
            Map<String, Object> model,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return div(
                p("Hello World")
        );

    }

}
```