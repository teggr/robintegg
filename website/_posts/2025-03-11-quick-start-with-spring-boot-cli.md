---
layout: post
title: Quick Start with Spring Boot CLI
date: "2025-03-11"
description: "Using the Spring Boot CLI to scaffold new Spring Boot projects from the command line with the init command."
image: /images/spring-boot-cli-suick-start-space.webp
tags:
  - java
  - cli
  - tools
  - spring
---
# 🚀 Quick Start with Spring Boot CLI

Want to supercharge your Spring Boot development? The Spring Boot CLI is your command-line companion for scaffolding projects in seconds!

## 📦 Installation

First things first - grab the latest CLI using the instructions from the Spring Boot website - [https://docs.spring.io/spring-boot/installing.html#getting-started.installing.cli](https://docs.spring.io/spring-boot/installing.html#getting-started.installing.cli)

## 🎯 What Can It Do?

The CLI’s super power is generating new Spring Boot projects. Let's focus on project generation - it's a game-changer for quick starts!

## 🛠️ Command Reference

Get familiar with the basic commands. The real magic happens with the `init` command, which lets you customize everything about your new project.

```shell
> spring help init

spring init - Initialize a new project using Spring Initializr (start.spring.io)

usage: spring init [options] [location]

Option                       Description
------                       -----------
-a, --artifact-id <String>   Project coordinates; infer archive name (for
                               example 'test')
-b, --boot-version <String>  Spring Boot version (for example '1.2.0.RELEASE')
--build <String>             Build system to use (for example 'maven' or
                               'gradle') (default: gradle)
-d, --dependencies <String>  Comma-separated list of dependency identifiers to
                               include in the generated project
--description <String>       Project description
-f, --force                  Force overwrite of existing files
--format <String>            Format of the generated content (for example
                               'build' for a build file, 'project' for a
                               project archive) (default: project)
-g, --group-id <String>      Project coordinates (for example 'org.test')
-j, --java-version <String>  Language level (for example '1.8')
-l, --language <String>      Programming language  (for example 'java')
--list                       List the capabilities of the service. Use it to
                               discover the dependencies and the types that are
                               available
-n, --name <String>          Project name; infer application name
-p, --packaging <String>     Project packaging (for example 'jar')
--package-name <String>      Package name
-t, --type <String>          Project type. Not normally needed if you use --
                               build and/or --format. Check the capabilities of
                               the service (--list) for more details
--target <String>            URL of the service to use (default: https://start.
                               spring.io)
-v, --version <String>       Project version (for example '0.0.1-SNAPSHOT')
-x, --extract                Extract the project archive. Inferred if a
                               location is specified without an extension

examples:

    To list all the capabilities of the service:
        $ spring init --list

    To creates a default project:
        $ spring init

    To create a web my-app.zip:
        $ spring init -d=web my-app.zip

    To create a web/data-jpa gradle project unpacked:
        $ spring init -d=web,jpa --build=gradle my-dir

```

## 🧩 Dependencies Made Easy

Need to know what dependencies are available? Just use the `--list` argument to see all your options.

```shell
> spring init --list

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
:: Service capabilities ::  https://start.spring.io

Supported dependencies
+--------------------------------------+--------------------------------------------------------------+-----------------------+
| Id                                   | Description                                                  | Required version      |
+--------------------------------------+--------------------------------------------------------------+-----------------------+
| activemq                             | Spring JMS support with Apache ActiveMQ 'Classic'.           |                       |
|                                      |                                                              |                       |
| actuator                             | Supports built in (or custom) endpoints that let you monitor |                       |
|                                      | and manage your application - such as application health,    |                       |
|                                      | metrics, sessions, etc.                                      |                       |
```

## 🔥 Project Generation

Here's where the magic happens - let's bring all these commands together to create a complete web project in seconds:

```shell
> spring init \
    --type=maven-project \
    --java=21 \
    --group-id="com.robintegg" \
    --artifact-id="example-webapp" \
    --description="Example webapp created from the command line" \
    --name="ExampleWebapp" \
    --package-name="com.robintegg.webapp" \
    --dependencies=web,jdbc,h2,lombok \
    --extract
		
Using service at https://start.spring.io
Project extracted to '/Users/robintegg/spring-boot-cli'
```

## ⚡ Power User Tips

Save time by creating aliases for your most-used project templates. Once set up, you can spin up new projects with a single command and jump straight into coding!

```shell
# spring cli commands
alias webapp-here="spring init --type=maven-project --java=21 --group-id='com.robintegg' --artifact-id='example-webapp' --description='Example webapp created from the command line' --name='ExampleWebapp' --package-name='com.robintegg.webapp' --dependencies=web,jdbc,h2,lombok --extract"
```

You can now make an alias for this command. For example with `zsh` , open your `.zshrc` and add the following.

Then you can use the alias and open in your IDE of choice.

```shell
> webapp-here
Using service at https://start.spring.io
Project extracted to '/Users/robintegg/spring-boot-cli'

> ls -al
total 72
drwxr-xr-x  ...   320 11 Mar 09:30 .
drwxr-x---+ ...   1760 11 Mar 09:28 ..
-rw-r--r--  ...     38 11 Mar 09:30 .gitattributes
-rw-r--r--  ...    395 11 Mar 09:30 .gitignore
drwxr-xr-x  ...     96 11 Mar 09:30 .mvn
-rw-r--r--  ...   1506 11 Mar 09:30 HELP.md
-rwxr-xr-x  ...  10665 11 Mar 09:30 mvnw
-rw-r--r--  ...   6912 11 Mar 09:30 mvnw.cmd
-rw-r--r--  ...   2348 11 Mar 09:30 pom.xml
drwxr-xr-x  ...    128 11 Mar 09:30 src

> idea .
```

**Now, go build some great new web applications!**