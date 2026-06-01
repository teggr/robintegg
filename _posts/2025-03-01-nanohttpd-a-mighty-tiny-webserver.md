---
layout: post
title: NanoHTTPD - A mighty tiny webserver
date: "2025-03-01"
description: "Build lightweight web servers in Java with NanoHTTPD and JBang for embedded HTTP serving and quick prototypes."
image: /images/aedrian-salazar-Tws17PwytpA-unsplash.jpg
tags:
  - java
  - web
  - tools
  - tips
# Photo by <a href="https://unsplash.com/@aedrian?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Aedrian Salazar</a> on <a href="https://unsplash.com/photos/grayscale-photo-of-man-riding-atv-Tws17PwytpA?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
---
# NanoHTTPD: Your Tiny, Mighty Web Server in Java (JBang Edition!)

Alright, let's talk [NanoHTTPD](https://nanohttpd.org/), the little engine that can do so much. Think of it as your trusty pocketknife – not for chopping down redwoods, but perfect for opening that pesky package or tightening a loose screw. In the coding world, that translates to quick, efficient, and wonderfully simple web serving. And today, we're making it even quicker with [JBang!](https://www.jbang.dev/)

## NanoHTTPD: Your No-Fuss, No-Frills Web Buddy

So, what's the deal with NanoHTTPD? It's a tiny, embeddable HTTP server, written in pure Java. No dependencies (well, almost, JBang handles it), no fuss, just plain, simple web serving. It's like that reliable friend who's always there when you need a quick hand, without any drama.

## Why NanoHTTPD? Because Sometimes, Less is More!

* **Light as a Feather:** Seriously, this thing is tiny. Perfect for when you're working with limited resources.
* **Embed It and Forget It:** Drop it into your Java app and boom, you've got a web server.
* **Simple as Pie:** You'll be up and running faster than you can say "Hello, World!"
* **Universal Java Goodness:** Runs anywhere Java does.

## Let's Get Real: Where Would You Use This?

* **Gadget Guru:** Building IoT devices? NanoHTTPD lets you whip up a simple web interface in no time.
* **Rapid Prototyping:** Need a quick mock server? NanoHTTPD's got your back.
* **Internal Tools:** Building a small admin panel? You don't need a battleship, just a dinghy.
* **Testing, Testing, 1, 2, 3:** Perfect for spinning up temporary servers for your integration tests.
* **Sharing Files with Friends (or Colleagues):** Local file sharing made easy.

## Getting Your Hands Dirty: A Quick File Serving Example (JBang Style!)

Alright, enough chit-chat. Let's get this party started with JBang!

### 1. Create a JBang File:

Initialise a JBang file `jbang init serve.java` (or whatever you like), open the file for editing `jbang edit --open=[editor] serve.java` and paste the following code:

```java
/// usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.nanohttpd:nanohttpd-webserver:2.3.1

import fi.iki.elonen.SimpleWebServer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class serve {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: jbang serve.java <directory>");
            System.exit(1);
        }

        File publicDir = new File(args[0]);
        if (!publicDir.exists() || !publicDir.isDirectory()) {
            System.out.println("Invalid directory: " + publicDir.getAbsolutePath());
            System.exit(1);
        }

        SimpleWebServer server = new SimpleWebServer("localhost", 8080, publicDir, false);
        server.start();
        System.out.println("Server started on port 8080, serving files from: " + publicDir.getAbsolutePath());
        System.out.println("Press Enter to stop...");

        new Scanner(System.in).nextLine(); // Wait for Enter key press
        server.stop();
        System.out.println("Server stopped.");

    }

}
```

### 2. Run the Server:

Open your terminal and run the following command, replacing `<directory>` with the path to the folder you want to serve:

```bash
jbang serve.java <directory>
```

For example:

```bash
jbang serve.java public
```

#### The Lowdown:

* The `//usr/bin/env jbang...` line tells JBang to execute the script.
* The `//DEPS...` line tells JBang to download and include the NanoHTTPD dependency.
* The code now takes a command-line argument for the directory.
* The server stays running until you press Enter.
* Error handling is included to validate the directory input.

### 3. Browse the files:

Open your browser on `localhost:8080`... voila!

## A Word of Caution (But Not Too Scary):

NanoHTTPD is a fantastic tool, but remember, it's not designed for high-traffic, mission-critical applications. For those, you'll want something more robust. But for quick, simple web serving, it's a winner. And with JBang, it's even quicker!

So, go forth and build something awesome with NanoHTTPD and JBang! You've got this.