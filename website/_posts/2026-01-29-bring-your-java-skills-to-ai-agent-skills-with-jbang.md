---
layout: post
title: "Bring your Java skills to AI Agent Skills with JBang"
date: "2026-01-29"
image: /images/jbang-ai-skills.png
tags:
  - java
  - jbang
  - ai
  - github copilot
  - scripting
---

If you're a Java developer working with GitHub Copilot or other AI coding agents, you might think you need to learn Python or JavaScript to write custom skills. Not anymore. With [JBang](https://www.jbang.dev/), you can leverage your Java knowledge and the entire Java ecosystem to build AI agent skills that integrate seamlessly with GitHub Copilot.

This post walks through creating a complete custom GitHub Copilot skill using JBang. The skill retrieves location and country information based on IP addresses—demonstrating how Java can be your scripting language for AI agent interactions.

## Why JBang for AI skills?

JBang lets you write Java scripts that run directly without the ceremony of Maven projects, build files, or IDE setup. For AI agent skills, this means:

- Use Java as a scripting language with single-file programs
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
- GitHub Copilot enabled in VS Code
- Access to the internet for API calls

## Creating the skill structure

GitHub Copilot looks for skills in the `.github/skills/` directory. Create the structure for our location-info skill:

```bash
mkdir -p .github/skills/location-info/scripts
```

## Defining the skill

The `SKILL.md` file tells GitHub Copilot what your skill does and how to use it. This is where you describe the workflow and when the AI should invoke your Java scripts.

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

JBang includes an `init` command that generates Java scripts with boilerplate. Let's use it to create our IP address lookup script:

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

## Why this matters

This approach demonstrates several important points:

**Java as a scripting language** — JBang removes the friction of Java development for simple, single-purpose tools. No pom.xml, no project structure, just a Java file that runs.

**Leverage existing knowledge** — Java developers don't need to learn Python or JavaScript to build AI agent skills. Use the language and ecosystem you already know.

**Access to the Java ecosystem** — While these examples use only the JDK, you can add any Maven dependency with JBang's `//DEPS` directive, giving you access to the entire Java ecosystem.

**Type safety and tooling** — You get Java's type safety, IDE support, and compile-time checking even in "script" mode.

## Next steps

Try extending this skill:

- Add error handling for invalid IP addresses
- Cache API responses to reduce external calls
- Add more data sources (weather, timezone, etc.)
- Use JBang's `//DEPS` to add JSON parsing libraries like Jackson or Gson for more sophisticated data handling

You can also explore JBang's other features:

- `//DEPS` directive to add Maven dependencies
- `//JAVA_OPTIONS` to configure the JVM
- `//SOURCES` to split code across multiple files
- Aliases to install scripts globally with `jbang alias add`

## References

- [JBang official website](https://www.jbang.dev/)
- [JBang documentation](https://www.jbang.dev/documentation/)
- [GitHub Copilot workspace skills documentation](https://docs.github.com/en/copilot/using-github-copilot/using-github-copilot-workspace-skills)
- [ipapi.co API documentation](https://ipapi.co/api/)
- [REST Countries API](https://restcountries.com/)
- [Java HTTP Client documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html)

With JBang and GitHub Copilot skills, Java developers can build powerful AI agent capabilities while staying in their comfort zone. The combination of Java's robustness and JBang's scripting convenience creates a compelling alternative to traditional scripting languages for AI agent skills.
