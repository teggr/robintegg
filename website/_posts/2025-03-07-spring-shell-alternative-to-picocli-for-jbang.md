---
layout: post
title: Spring Shell alternative to picocli for JBang
date: "2025-03-07"
image: /images/spring-shell-picocli-jbang.webp
tags:
  - java
  - cli
  - tools
  - spring
---
# Spring Shell alternative to picocli for JBang

<aside>

Libraries such as [picocli](https://picocli.info/) and [Spring Shell](https://docs.spring.io/spring-shell/reference/index.html) make handling command line arguments so much easier than parsing the `String[] args` parameter of your `main` function.

</aside>

If you’re a keen user of [JBang](https://www.jbang.dev/) you’ve probably taken a look at the [templates](https://www.jbang.dev/documentation/guide/latest/templates.html) feature for scaffolding a new JBang script.

You can see what built in templates are available using `jbang template list` .

As of March 2025 you’ll see something similar to:

```bash
agent
   Agent template
cli
   CLI template
gpt
   Template using ChatGPT (requires --preview and OPENAI_API_KEY)
gpt.groovy
   Template using ChatGPT for groovy (requires --preview and OPENAI_API_KEY)
gpt.kt
   Template using ChatGPT for kotlin (requires --preview and OPENAI_API_KEY)
hello
   Basic Hello World template
hello.groovy
   Basic groovy Hello World template
hello.kt
   Basic kotlin Hello World template
qcli
   Quarkus CLI template
qmetrics
   Quarkus Metrics template
qrest
   Quarkus REST template
readme.md
   Basic markdown readme template
```

The `cli` template provides a scaffolded JBang script that is configured to use the [picocli](https://picocli.info/) command line library that will provide a lot of rich command line features such as named options and parameters.

## picoli **- a mighty tiny command line interface**

Running the command `jbang init --template=cli helloworld.java` will generate the following code that is ready to go with adding your command and option definition and code. This is really helpful if you want to whip up a quick command line tool.

```bash
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.6.3

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "helloworld", mixinStandardHelpOptions = true, version = "helloworld 0.1",
        description = "helloworld made with jbang")
class helloworld implements Callable<Integer> {

    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private String greeting;

    public static void main(String... args) {
        int exitCode = new CommandLine(new helloworld()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.println("Hello " + greeting);
        return 0;
    }
}
```

[Picocli](https://picocli.info/) shines when you need a lean, efficient command-line parser. Its strength lies in its simplicity and speed, quickly handling argument parsing and offering powerful features like subcommands and autocompletion without the weight of a larger framework.

## > Spring Shell —try-next

[Spring Shell](https://docs.spring.io/spring-shell/reference/index.html) goes beyond basic command-line functionality, offering built-in support for interactive shells and data capture. This allows developers to create more dynamic scripts and fully interactive applications. Moreover, Spring Shell seamlessly integrates with the broader Spring ecosystem, leveraging existing features and project integrations to streamline development.

To start using Spring Shell within a JBang script, start with an empty script using the command `jbang init springshell.java` and edit the script in your preferred editor.

```bash
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.shell:spring-shell-dependencies:3.4.0@pom
//DEPS org.springframework.shell:spring-shell-starter
//DEPS org.springframework.boot:spring-boot-starter:3.4.3

package dev.springshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.command.annotation.Option;

@SpringBootApplication
@CommandScan
public class springshell {

    public static void main(String... args) {
        SpringApplication.run(springshell.class, args);
    }

    @Command(group = "Examples")
    static class ExampleCommands {

        @Command(command="helloworld", description = "helloworld made with jbang + spring shell")
        public void helloworld(
                @Option(longNames = "arg1", description = "The greeting to print", defaultValue = "World!") String greeting
        ) {
            System.out.println("Hello " + greeting);
        }
    }

}
```

- We use a package declaration `package dev.springshell;` so that Spring boot does not try to classpath scan all packages. JBang allows this even though there are no package folders.
- We define a single class for the command, which will be injected into the Spring context as a bean. This means you can autowire in whatever you need.

The JBang script can now be executed with the command name and a greeting argument.

```bash
JBangProjects % jbang springshell.java helloworld Robin

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.4.3)

...Starting springshell using Java 21.0.6 with PID 8508 ...
...No active profile set, falling back to 1 default profile: "default"
...Started springshell in 0.613 seconds (process running for 0.742)
Hello Robin
```

Voila, here we have a running cli tool made with Spring shell.

Though it would be nicer if we didn’t have the log messages and spring boot banner…

### Add spring boot properties

It is possible to include an [`application.properties`](http://application.properties) file that should be familiar to most Spring Boot developers.

Create the [`application.properties`](http://application.properties) file in the same directory as your JBang script.

```bash
spring.shell.interactive.enabled=false
spring.main.banner-mode=off
logging.level.root=off
```

and add a reference to your script.

```bash
//DEPS org.springframework.boot:spring-boot-starter:3.4.3
//FILES META-INF/resources/application.properties=application.properties

package dev.springshell;
```

This will now bundle the [application.properties](http://application.properties) file with the script. Because it’s a Spring Boot app, of course all the properties can be set in the normal way.

```bash
% jbang springshell.java helloworld Robin
[jbang] Building jar for springshell.java...
Hello Robin
```

## Next steps?

That’s it! You can start building great command line tools with the features such as [Flow Components](https://docs.spring.io/spring-shell/reference/components/ui/index.html) or turn on `spring.shell.interactive.enabled=`true  and build some amazing interactive tooling with the [Terminal UI](https://docs.spring.io/spring-shell/reference/tui/index.html).