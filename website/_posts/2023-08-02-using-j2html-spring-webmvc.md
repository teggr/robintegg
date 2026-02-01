---
layout: post
title: Using j2html for rendering web pages with Spring Web MVC
date: "2023-08-02"
description: "Integrating j2html with Spring Web MVC using BeanViewResolver to render HTML pages programmatically in Java."
image: /images/j2html.png
tags:
  - web
  - spring
  - j2html
---

![j2html.png]({{site.baseurl}}/images/j2html.png)

# Using j2html for rendering web pages with Spring Web MVC

Working through the [Hypermedia Systems](https://www.amazon.co.uk/Hypermedia-Systems-Carson-Gross/dp/B0C9S88QV6) book I've been writing a pure Java implementation of the [Contacts app](https://github.com/teggr/htmx-j2html-demo) with [j2html](https://j2html.com) and Spring Boot.

I've recently implemented a couple of static sites using [j2html](https://j2html.com) and have been pretty happy with the outcome. Writing the html document using Java is pretty natural and the unit testing and refactoring tools makes it pretty productive too,

To support writing a Spring Boot app where I can also use j2html for the rendering I need to be able to hook into the view resolving mechanism of the Spring WebMVC framework as j2html is not a supported technology out of the box.

Luckily there is already a `BeanViewResolver` implementation that I can leverage in order to integrate j2html rendering quickly. The `BeanViewResolver` will map the WebMVC view names to `View` beans available in the application context. So all I need to do is register a `BeanViewResolver` and a `View` and we are good to go!

In this example, declaring `IndexPage` as a `@Component` will register a bean `indexPage`. This bean name can then be referenced by `IndexController` for the `/` path when returning the view name of `indexPage`.

Full code available on the github project [https://github.com/teggr/htmx-j2html-demo](https://github.com/teggr/htmx-j2html-demo).

```java
@Configuration
public class ApplicationConfiguration {
    @Bean
    public ViewResolver j2htmlViewResolver() {
        return new BeanNameViewResolver();
    }
}

@Component
public class IndexPage implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        html(
            body(
                h1("HelloWorld")
            )
        ).render(
                IndentedHtml.into(response.getWriter())
        );

    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

}

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "indexPage";
    }
}

@SpringBootApplication
public class HtmxJ2htmlDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HtmxJ2htmlDemoApplication.class, args);
    }
}

```