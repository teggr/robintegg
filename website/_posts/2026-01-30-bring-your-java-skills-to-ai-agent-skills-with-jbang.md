---
layout: post
title: "Bring your Java skills to AI Agent Skills with JBang"
date: "2026-01-30"
image: /images/jbang-ai-skills.jpg
image_width: "1200"
image_height: "630"
image_alt: "JBang and AI Skills integration diagram"
tags:
  - java
  - jbang
  - ai
  - github copilot
  - scripting
---

If you're a Java developer working with GitHub Copilot or other AI coding agents, you might think you need to learn Python, JavaScript or Shell scripts to power your custom skills. I'm here to show you another way. With [JBang](https://www.jbang.dev/), you can leverage your Java knowledge and the entire Java ecosystem to build AI agent skills that integrate seamlessly with GitHub Copilot.

This post walks through creating a custom GitHub Copilot skill powered by JBang. The skill retrieves location and country information based on IP addresses—demonstrating how Java can be your scripting language for AI agent interactions.

## Why JBang for AI skills?

JBang lets you write your scripts in Java that run directly without the ceremony of Maven projects, build files, or IDE setup. For AI agent skills, this means:

- Use Java as a scripting language with single-file programs
- Use any Java library available through Maven or Jetpack
- Leverage the JDK's built-in HTTP client and JSON handling
- Stay in the Java ecosystem you already know
- Let AI agents call your Java code as easily as they'd call Python scripts

## What we're building

We'll create a location-info skill that:

1. Takes an IP address (or discovers the local machine's public IP)
2. Fetches location data from ipapi.co
3. Retrieves detailed country information based on the country code
4. Presents a summary to the AI agent

The skill demonstrates GitHub Copilot calling JBang scripts to gather information the AI can then reason about.

## Prerequisites

Before starting, make sure you have:

- [JBang](https://www.jbang.dev/download/) installed on your system
- Github Copilot CLI installed

## Creating the skill structure

GitHub Copilot looks for skills in the `.github/skills/` directory. Create the structure for our location-info skill:

```bash
mkdir -p .github/skills/location-info/scripts
```

## Defining the skill

The `SKILL.md` file tells GitHub Copilot what your skill does and how to use it. This is where you describe the workflow and when the AI should invoke your Java code.

Create `.github/skills/location-info/SKILL.md`:

```markdown
---
name: location-info
description: Call this skill when you need to get country information from the current ip address
---

# location info

To find out more location information about a computer, we need to lookup the country information based on the ip address.

## Validate IP Address

Firstly, if no ip address is provided, then get the local machine's public ip address.

## Get ip address location information

Once we have an ip address, use the script `jbang ./scripts/ipinfo.java` and pass the ip address as the argument.

## Get country information by country code

Using the address location info returned, find the country code and fetch the country information.

Use the script `jbang ./scripts/countryinfo.java` and pass the country code as the argument.

## Summarize

Present a summary of the address location and country information.
```

This skill definition provides clear steps that GitHub Copilot can follow, with explicit instructions about when to call each JBang script.

## Building the IP info script

JBang includes an `init` command that generates a JBang Java script using AI. Let's use it to create our IP address lookup script:

```bash
cd .github/skills/location-info/scripts
jbang init ipinfo.java "Fetch address info based on an ip. The ip should be passed in as an argument. The cli should use the jdk http client to curl https://ipapi.co/{IP_ADDRESS}/json/ and print the result to the system out"
```

This generates `.github/skills/location-info/scripts/ipinfo.java`. Here's what the complete script looks like:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ipinfo {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: jbang ipinfo.java <IP_ADDRESS>");
            System.exit(1);
        }

        String ipAddress = args[0];
        String apiUrl = "https://ipapi.co/" + ipAddress + "/json/";

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.err.println("Error: Received status code " + response.statusCode());
                System.exit(1);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching IP info: " + e.getMessage());
            System.exit(1);
        }
    }
}
```

The script uses the JDK's built-in HTTP client—no external dependencies required. The `///usr/bin/env jbang` shebang line makes it executable, and the `//JAVA 25+` directive specifies the Java version.

## Building the country info script

Following the same pattern, create the country information script:

```bash
jbang init countryinfo.java "Fetch country info based on a country code. The country code should be passed in as an argument. The cli should use the jdk http client to curl https://restcountries.com/v3.1/alpha/{COUNTRY_CODE} and print the result to the system out"
```

This generates `.github/skills/location-info/scripts/countryinfo.java`:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class countryinfo {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: jbang countryinfo.java <COUNTRY_CODE>");
            System.exit(1);
        }

        String countryCode = args[0];
        String apiUrl = "https://restcountries.com/v3.1/alpha/" + countryCode;

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.err.println("Error: Received status code " + response.statusCode());
                System.exit(1);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching country info: " + e.getMessage());
            System.exit(1);
        }
    }
}
```

Both scripts follow the same pattern: validate arguments, build an HTTP request using the JDK's HTTP client, and print the JSON response to stdout. GitHub Copilot can then parse this output and reason about the results.

## Testing your skill

Once you've created the scripts, test them from the command line:

```bash
# Test IP info lookup
jbang .github/skills/location-info/scripts/ipinfo.java 8.8.8.8

# Test country info lookup (using a country code like 'US')
jbang .github/skills/location-info/scripts/countryinfo.java US
```

You should see JSON responses printed to the console. If the scripts work correctly, GitHub Copilot will be able to call them and process the results.

## How GitHub Copilot uses your skill

When you ask GitHub Copilot a question like "What country is IP address 1.2.3.4 from?", it will:

1. Recognize this matches your location-info skill description
2. Execute `jbang ./scripts/ipinfo.java 1.2.3.4`
3. Parse the JSON output to extract the country code
4. Execute `jbang ./scripts/countryinfo.java <country-code>`
5. Parse the country information
6. Present a natural language summary combining both results

The skill workflow you defined in `SKILL.md` guides Copilot through each step, and your JBang scripts provide the actual data.

We can see the captured output below:

```shell
❯ /location-info

● I'll invoke the location-info skill for you.

● skill(location-info)

● Get public IP address
  $ Invoke-RestMethod -Uri "https://api.ipify.org?format=text"
  └ 2 lines...

● Get IP location information
  $ cd C:\Users\robin\IdeaProjects\skills-example\.github\skills\location-info && jbang ./scripts/ipinfo.java 81.179.66.100
  └ 30 lines...

● Get country information for GB
  $ cd C:\Users\robin\IdeaProjects\skills-example\.github\skills\location-info && jbang ./scripts/countryinfo.java GB
  └ 38 lines...

● Location Information Summary

  IP Address: 81.179.66.100

  Location Details:

    - City: Preston, England
    - Country: United Kingdom (GB)
    - Coordinates: 53.77°N, 2.72°W
    - Timezone: Europe/London (UTC+0000)
    - ISP: TalkTalk

  Country Information:

    - Official Name: United Kingdom of Great Britain and Northern Ireland
    - Capital: London
    - Population: ~69.3 million
    - Area: 244,376 km²
    - Currency: British pound (£)
    - Language: English
    - Region: Northern Europe
    - Drives on: Left side
```

## Next steps

We've built a complete AI agent skill using JBang and Java using some new custom scripts, but wait there's more. Now that you have the basics down, consider using any library from the Java ecosystem to enhance your skills.

Why not build a skill to deploy your application using [deploy4j](https://deploy4j.dev/installation/)?

```markdown
---
name: deploy-application
description: Call this skill to deploy an Java application to a server using deploy4j
---

# deploy application

Run this script `jbang dev.deploy4j:deploy4j-cli:0.0.6 --version 0.0.1-SNAPSHOT

```

With JBang and GitHub Copilot skills, Java developers can build powerful AI agent capabilities while staying in their comfort zone. The combination of Java's robustness and JBang's scripting convenience creates a compelling alternative to traditional scripting languages for AI agent skills.

## References

* [Vercel AI Skills FAQ](https://vercel.com/blog/agent-skills-explained-an-faq)
- [JBang documentation](https://www.jbang.dev/documentation/jbang/latest/templates.html#ai-powered-code-generation)
- [GitHub Copilot Agent Skills](https://docs.github.com/en/copilot/concepts/agents/about-agent-skills)
- [ipapi.co API documentation](https://ipapi.co/)
- [REST Countries API](https://restcountries.com/#endpoints-code)
