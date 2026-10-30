---
layout: post
title: "Taking TeaVM to the Desktop with JCEF and jr"
date: "2026-10-29"
description: "Taking a Java game beyond the browser: TeaVM compiles it to JavaScript, JCEF hosts it on the desktop, and jr packages the launchers."
image: /images/teavm-desktop.png
tags:
  - java
  - teavm
  - javascript
  - desktop
---

Some of my recent articles covered [getting Java into the browser](/2026/10/17/teavm-getting-java-into-the-browser.html), a [currency explorer with a Spring Boot API](/2026/10/18/from-java-to-web-app-currency-explorer-with-teavm.html), and a [drawing app using HTML Canvas](/2026/10/19/building-drawing-app-java-teavm-html-canvas.html) where I've been writing Java applications that run in the browser with [TeaVM](https://teavm.org/).

Alongside those introductory examples, there's Skybound, a fun little canvas platform game where you jump across blocks and collect stars. Now having successfully produced the demos for the web platform, I started wondering where else I could take the same code - why stop at web apps?.

This time, I wanted to take that game and distribute it as a desktop application. The result is in [teggr/java-game](https://github.com/teggr/java-game), and here's the Windows version:

![Skybound running in a Windows desktop window, with all 5 stars collected](/images/teavm-skybound-windows.png)

The game logic is still the same Java compiled to JavaScript. What's changed is how it reaches the player.

I also maintain [Awesome Java UI](https://awesome-java-ui.com/), so I've been collecting projects that approach Java interfaces from different directions. Two looked like useful pieces for this experiment: JCEF for embedding a browser, and jr for giving a Java application a platform-specific launcher.

![Awesome Java UI website](/images/awesome-java-ui-refresh.png)

The chain looks a little roundabout when written out:

```text
Java game -> TeaVM -> JavaScript -> embedded Chromium
          -> Java desktop host -> Windows EXE or macOS app launcher
```

Each piece has a fairly small job. TeaVM compiles the game, Chromium renders the web interface, the Java host owns the desktop window, and jr handles launching it with a suitable Java runtime.

## Keep the game, change the host

The demo has 2 Maven modules: `game` and `desktop`. The game module reuses the Java game classes and browser resources from the showcase unchanged. Movement, jumping, star collection and drawing still follow the same path they did in the browser.

In the [game module's POM](https://github.com/teggr/java-game/blob/main/game/pom.xml), TeaVM runs during `process-classes`. This is the plugin configuration:

```xml
<plugin>
    <groupId>org.teavm</groupId>
    <artifactId>teavm-maven-plugin</artifactId>
    <version>${teavm.version}</version>
    <executions>
        <execution>
            <phase>process-classes</phase>
            <goals><goal>compile</goal></goals>
            <configuration>
                <mainClass>io.github.teggr.teavmshowcase.platformgame.PlatformGameApp</mainClass>
                <targetType>JAVASCRIPT</targetType>
                <targetDirectory>${project.build.outputDirectory}/web</targetDirectory>
                <targetFileName>classes.js</targetFileName>
                <minifying>true</minifying>
            </configuration>
        </execution>
    </executions>
</plugin>
```

The parent POM sets `teavm.version` to `0.16.0`. Maven compiles the Java first, then TeaVM compiles that bytecode into `classes.js`. The generated JavaScript sits beside the HTML, CSS and bootstrap script under `web` in the game JAR.

That placement matters. The desktop application carries the web app with it rather than opening the hosted showcase. The desktop module uses Maven Shade to package the game and its own dependencies into `java-game.jar`.

## JCEF brings Chromium into Java

[JCEF](https://awesome-java-ui.com/jcef.html) wraps the Chromium Embedded Framework for Java. It gives us a Chromium browser component that we can put inside a Swing window.

For this project, I use [jcefmaven](https://github.com/jcefmaven/jcefmaven) to set up JCEF and obtain its platform-specific native files. The host starts a small HTTP server bound to `127.0.0.1` on an available port, serving the bundled game resources.

The browser creation in [`DesktopApp`](https://github.com/teggr/java-game/blob/main/desktop/src/main/java/io/github/teggr/javagame/DesktopApp.java) is the point where those pieces meet:

```java
CefBrowser browser = client.createBrowser(server.uri().toString(), false, false);
```

The same method adds that browser component to the Swing window:

```java
window.add(browser.getUIComponent(), BorderLayout.CENTER);
window.setSize(1100, 850);
```

These are extracts from the window setup, rather than a complete JCEF application. The full method also installs navigation handlers, handles window closure and gives the browser focus. Chromium initialization happens before the window is created on Swing's event dispatch thread.

Once loaded, the page behaves much as it does in a standalone browser. Chromium supplies the DOM, Canvas and input events; the TeaVM-compiled Java code runs the game.

That's the part I like about this combination. We keep the web platform's rendering capabilities and can build a Java desktop host around them. This demo only needs a window and a resource server, but the boundary leaves room for desktop-side functionality later.

There are some details worth keeping contained. [`GameServer`](https://github.com/teggr/java-game/blob/main/desktop/src/main/java/io/github/teggr/javagame/GameServer.java) serves an explicit list of bundled resources, not arbitrary files from the machine. The host blocks navigation outside the local game origin and blocks popups. Closing the window disposes JCEF and stops the server.

## jr handles the launcher

An executable JAR still leaves the person running it with questions about Java versions and startup commands.

[jr, or JarRunner](https://github.com/jarrunner/jr), provides a native launcher for a Java application. It looks for a suitable Java installation and, if one isn't available, asks before downloading a runtime.

The [desktop POM](https://github.com/teggr/java-game/blob/main/desktop/pom.xml) configures `jr-maven-plugin` version `1.3.0`. Its shared configuration includes:

```xml
<configuration>
    <name>java-game</name>
    <javaVersion>25</javaVersion>
    <jvmMode>exe</jvmMode>
    <aot>false</aot>
    <productName>Skybound</productName>
    <fileDescription>Skybound Java Game</fileDescription>
</configuration>
```

The Windows and macOS profiles activate the plugin and supply the platform-specific settings. Windows produces `java-game.exe`; macOS produces a universal `Skybound.app` launcher for Intel and Apple Silicon.

For this demo, `jvmMode=exe` starts a child JVM for desktop compatibility, and AOT training is disabled. The platform profiles also pass the native-access option JCEF needs, with extra Java desktop module opens on macOS.

The final distribution ZIP contains the launcher, the application JAR and notices. You need to extract the whole ZIP and keep the JAR beside the launcher or app.

## Build it and download it

To build the demo locally, use JDK 25 and Maven 3.9 or newer. From the repository root:

```bash
mvn clean verify
```

On Windows, you can then run the desktop JAR directly:

```powershell
java --enable-native-access=ALL-UNNAMED -jar .\desktop\target\java-game.jar
```

For the Windows distribution, build on Windows with:

```bash
mvn -Pwindows clean verify
```

The macOS distribution uses its own profile:

```bash
mvn -Pmacos clean verify
```

Build one platform profile at a time. The [GitHub Actions workflow](https://github.com/teggr/java-game/blob/main/.github/workflows/build.yml) runs those profiles on Windows and macOS runners and uploads the distribution ZIPs as `java-game-windows` and `java-game-macos`.

You can download them from a successful run on the repository's [Actions page](https://github.com/teggr/java-game/actions). They're workflow artifacts rather than published GitHub releases. The screenshot above shows the Windows application running the same canvas game.

## What comes with this approach?

The browser UI stays a browser UI, with HTML and CSS around the canvas. We gain desktop packaging and a Java host, while taking on Chromium's native dependencies and runtime footprint.

There are also 2 separate first-run downloads to understand. jr finds or offers to install Java 25. jcefmaven downloads and extracts the matching JCEF native bundle. Neither the JVM nor the Chromium natives are bundled in the distribution ZIP, so that setup needs an internet connection. Later launches reuse the local caches; the game assets themselves are already in the JAR.

For a small experiment, this is a useful way to connect projects I've been exploring separately. A Java game becomes a web app through TeaVM, then a desktop application through JCEF and jr, without rewriting the game for each host.

For now, [the demo repository](https://github.com/teggr/java-game) gives us the complete path from Java source to a downloadable application.

## A showcase, then another platform

I put together a [TeaVM showcase](https://teggr.github.io/teavm-showcase/) to bring the web app examples together. You can play the platform game, draw on a canvas, try the currency explorer, or start with the number-guessing game.

![TeaVM showcase homepage introducing the platform game, drawing app, currency converter and number-guessing examples](/images/teavm-showcase-home.png)

After, the webaapp case, where next? Can i put the game on mobile?
