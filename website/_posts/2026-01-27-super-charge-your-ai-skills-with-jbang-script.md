---
layout: post
title: "Supercharge your AI skills with JBang script"
date: "2026-01-27"
image: /images/jbang-ai-skills.png
tags:
  - java
  - jbang
  - ai
  - tools
  - mcp
  - skills
---

AI agents are becoming powerful collaborators in our development workflows, but their effectiveness depends on the skills they can access. While many AI platforms offer pre-built integrations, developers often face limitations—vendor APIs that don't support certain features, authentication complexities, or simply missing integrations for niche tools. This post explores how JBang scripts empower developers to create lightweight, shareable AI skills that work around these limitations and enrich the broader developer community's ability to build AI-powered workflows.

## What are AI Skills?

AI skills are reusable, self-contained capabilities that AI agents can discover and invoke to accomplish tasks. Think of them as specialized tools that extend what an AI agent can do—fetching data from APIs, processing files, interacting with services, or executing custom logic.

While skills often leverage existing APIs and tools, developers frequently encounter real-world challenges:

- **Vendor limitations**: The API you need doesn't support the exact operation you want
- **Authentication complexity**: OAuth flows, token management, or custom auth schemes that aren't supported by pre-built integrations
- **Missing integrations**: Your internal tools or niche services don't have official AI platform plugins
- **Customization needs**: Pre-built integrations force you into rigid workflows that don't match your team's processes

**This is where custom CLI tools as skills become powerful.** Instead of being locked into what a vendor provides, you can create your own lightweight executable that does exactly what you need. The Model Context Protocol (MCP) community has popularized the SKILL.md format—a simple markdown specification that makes these custom tools discoverable and usable by AI agents.

A skill typically includes:
- **Name and description**: What the skill does
- **Input parameters**: What data it needs (if any)
- **Execution details**: How to run it (shell command, script path, etc.)
- **Output format**: What it returns

Here's a minimal example using a weather API:

```markdown
# Weather Forecast Skill

Get weather forecast for a location.

## Parameters
- latitude: decimal number
- longitude: decimal number

## Execution
```bash
curl "https://api.weather.gov/points/${latitude},${longitude}"
```

## Output
JSON weather forecast data
```

AI agents can read this format and automatically integrate your skill into their workflow. No custom plugins, no platform-specific code—just a standard markdown file and an executable.

## Why Custom CLI Skills Matter

When working with AI agents, you'll often find yourself needing capabilities that don't exist in pre-built integrations. Skills that wrap custom CLI tools offer several advantages:

- **Bypass vendor limitations**: Don't wait for vendors to add features—implement exactly what you need
- **Handle complex authentication**: Manage credentials, tokens, and auth flows your way
- **Bridge internal tools**: Make proprietary or internal systems accessible to AI agents
- **Full control over behavior**: Customize queries, transform data, add filtering—whatever your workflow requires
- **Portable and shareable**: Distribute to your team or the broader developer community
- **Language-agnostic**: The skill wraps any executable—Python, Java, Go, shell scripts, compiled binaries

The key insight: **skills democratize AI capability**. Any developer can create a custom tool, wrap it with a SKILL.md, and instantly make it available to AI agents. This enriches the entire ecosystem, allowing developers to share specialized integrations that would never exist as vendor-supported features.

## JBang Makes Custom Skills Lightweight

Creating custom CLI tools traditionally meant setting up projects, managing build files, and dealing with dependencies. JBang changes this by letting you write executable Java scripts with minimal ceremony—no build files, no complex setup.

Version 0.123.0 (December 2024) introduced the `gpt` template, making JBang particularly attractive for creating AI-integrated skills. This template scaffolds scripts with built-in support for OpenAI's API, but the real power is JBang's ability to create **any kind of CLI tool quickly**.

Why JBang for custom skills?

- **Single-file executables**: Your entire tool lives in one Java file
- **Inline dependency declaration**: Add libraries with simple `//DEPS` comments
- **Instant execution**: Run Java code like a shell script—no compilation step
- **Easy distribution**: Share a single `.java` file; JBang handles the rest

This lightweight approach is perfect for creating custom skills because you can go from idea to working integration in minutes, not hours. The barrier to creating and sharing skills becomes minimal, **enriching the ecosystem with specialized tools** that developers actually need.

### The GPT template

For skills that need direct AI integration, JBang's `gpt` template provides a quick start:

```bash
jbang init --template=gpt my-ai-tool.java
```

This generates a script ready to interact with OpenAI's GPT models:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.openai:openai-java:0.2.0

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.*;

public class my_ai_tool {
    public static void main(String... args) {
        var client = OpenAIOkHttpClient.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .build();
            
        // Your GPT integration code here
    }
}
```

For more details:
- [JBang 0.123.0 release notes](https://github.com/jbangdev/jbang/releases/tag/v0.123.0)

## Building a Weather Forecast Skill

Let's build a practical example: a skill that fetches weather forecasts for any location. This demonstrates how you can wrap a third-party API into a reusable AI tool that works around the limitations of pre-built integrations.

Many AI platforms offer weather integrations, but they often:
- Require platform-specific setup
- Don't support all weather services
- Have authentication limitations
- Can't be customized for specific data formats

With a custom JBang skill, you control everything—from which API you use to what data you return.

### The JBang script

Here's a complete JBang script that fetches weather data from the National Weather Service API:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.fasterxml.jackson.core:jackson-databind:2.16.1
//DEPS org.apache.httpcomponents.client5:httpclient5:5.3

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class weather_forecast {
    
    public static void main(String... args) {
        if (args.length != 2) {
            System.err.println("Usage: weather_forecast.java <latitude> <longitude>");
            System.err.println("Example: weather_forecast.java 39.7456 -97.0892");
            System.exit(1);
        }
        
        try {
            double latitude = Double.parseDouble(args[0]);
            double longitude = Double.parseDouble(args[1]);
            fetchWeatherForecast(latitude, longitude);
        } catch (NumberFormatException e) {
            System.err.println("Error: Latitude and longitude must be valid numbers");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error fetching weather: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void fetchWeatherForecast(double latitude, double longitude) throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Step 1: Get the forecast URL for this location
            String pointsUrl = String.format("https://api.weather.gov/points/%.4f,%.4f", 
                latitude, longitude);
            
            HttpGet pointsRequest = new HttpGet(pointsUrl);
            pointsRequest.addHeader("User-Agent", "WeatherSkill/1.0 (JBang)");
            pointsRequest.addHeader("Accept", "application/json");
            
            var pointsData = httpClient.execute(pointsRequest, response -> {
                String json = EntityUtils.toString(response.getEntity());
                return new ObjectMapper().readTree(json);
            });
            
            String forecastUrl = pointsData.get("properties").get("forecast").asText();
            
            // Step 2: Fetch the actual forecast
            HttpGet forecastRequest = new HttpGet(forecastUrl);
            forecastRequest.addHeader("User-Agent", "WeatherSkill/1.0 (JBang)");
            forecastRequest.addHeader("Accept", "application/json");
            
            var forecastData = httpClient.execute(forecastRequest, response -> {
                String json = EntityUtils.toString(response.getEntity());
                return new ObjectMapper().readTree(json);
            });
            
            // Step 3: Parse and display the forecast
            JsonNode periods = forecastData.get("properties").get("periods");
            
            System.out.println("=== Weather Forecast ===\n");
            System.out.println("Location: " + latitude + ", " + longitude);
            System.out.println();
            
            for (int i = 0; i < Math.min(5, periods.size()); i++) {
                JsonNode period = periods.get(i);
                String name = period.get("name").asText();
                int temperature = period.get("temperature").asInt();
                String temperatureUnit = period.get("temperatureUnit").asText();
                String windSpeed = period.get("windSpeed").asText();
                String windDirection = period.get("windDirection").asText();
                String shortForecast = period.get("shortForecast").asText();
                String detailedForecast = period.get("detailedForecast").asText();
                
                System.out.println(name + ":");
                System.out.println("  Temperature: " + temperature + "°" + temperatureUnit);
                System.out.println("  Wind: " + windSpeed + " " + windDirection);
                System.out.println("  Conditions: " + shortForecast);
                System.out.println("  Details: " + detailedForecast);
                System.out.println();
            }
        }
    }
}
```

### Running the script

This weather API doesn't require authentication, making it a perfect example. Execute it with latitude and longitude:

```bash
# Get weather for San Francisco
jbang weather_forecast.java 37.7749 -122.4194
```

Output:

```
=== Weather Forecast ===

Location: 37.7749, -122.4194

This Afternoon:
  Temperature: 58°F
  Wind: 10 to 15 mph W
  Conditions: Partly Sunny
  Details: Partly sunny, with a high near 58. West wind 10 to 15 mph.

Tonight:
  Temperature: 48°F
  Wind: 5 to 10 mph W
  Conditions: Mostly Clear
  Details: Mostly clear, with a low around 48. West wind 5 to 10 mph.

Wednesday:
  Temperature: 61°F
  Wind: 5 to 10 mph W
  Conditions: Sunny
  Details: Sunny, with a high near 61. West wind 5 to 10 mph.

Wednesday Night:
  Temperature: 49°F
  Wind: 5 mph W
  Conditions: Mostly Clear
  Details: Mostly clear, with a low around 49.

Thursday:
  Temperature: 63°F
  Wind: 5 mph W
  Conditions: Sunny
  Details: Sunny, with a high near 63.
```

## Wrapping it as a SKILL

Now create a `SKILL.md` file that makes this accessible to AI agents:

```markdown
# Weather Forecast Skill

Get detailed weather forecast for any location using latitude and longitude coordinates.

## Description

This skill fetches weather forecasts from the National Weather Service API for US locations. 
It returns temperature, wind conditions, and detailed forecasts for the next several periods.

Useful for:
- Planning outdoor activities based on weather
- Getting current conditions for any US location
- Understanding weather patterns over the next few days
- Answering "what's the weather like in..." questions

## Prerequisites

- JBang installed
- Internet connection
- Location coordinates (latitude/longitude) within the United States

Note: This uses the US National Weather Service API, which provides free, 
no-authentication weather data specifically for US locations.

## Usage

```bash
jbang weather_forecast.java <latitude> <longitude>
```

## Parameters

- **latitude**: Decimal latitude (e.g., 37.7749 for San Francisco)
- **longitude**: Decimal longitude (e.g., -122.4194 for San Francisco)

## Output Format

Plain text output with forecast periods including:
- Period name (Today, Tonight, Wednesday, etc.)
- Temperature with unit
- Wind speed and direction
- Short condition summary
- Detailed forecast description

## Example AI Prompts

"What's the weather like in New York?" (AI converts location to coordinates)
"Should I bring an umbrella tomorrow in Seattle?"
"Give me the 5-day forecast for Denver"
```

### Using the skill with AI agents

AI agents that support the Model Context Protocol can discover and use this skill. For example, you could ask:

> "What's the weather forecast for Austin, Texas?"

The AI agent:
1. Reads the SKILL.md file to understand capabilities
2. Converts "Austin, Texas" to coordinates (30.2672, -97.7431)
3. Executes `jbang weather_forecast.java 30.2672 -97.7431`
4. Parses the output
5. Presents a natural language summary

This is where custom skills shine: **you control exactly what data is fetched and how it's formatted**. If you needed precipitation probability, UV index, or wanted to call a different weather API entirely, you'd just modify your script. No waiting for vendor support.

## Enriching Developer Capability

This example illustrates how JBang-based skills enrich the developer ecosystem:

**Speed to capability**:
- Script development: ~20 minutes
- SKILL.md documentation: ~10 minutes  
- Total: half an hour from idea to working AI skill

**What you gain**:
- **Control**: Customize queries, output format, error handling, data transformation
- **Portability**: Works with any AI agent supporting MCP
- **Shareability**: Distribute a single `.java` file and `SKILL.md` to your team
- **Independence**: Not locked into vendor API limitations or authentication schemes
- **Community contribution**: Share skills that solve real problems others face

**The broader impact**:
When developers can create and share skills this easily, the entire ecosystem benefits. A skill you build for weather data might inspire someone else to create one for traffic data, stock prices, or internal company APIs. Each shared skill enriches the collective capability of AI agents.

This is the power of lightweight, custom CLI tools wrapped as skills: **they democratize AI integration**, letting any developer extend what AI agents can do without waiting for vendor support or dealing with platform-specific implementations.

## Next steps

**Create your own skills** to enrich your AI workflows:
- Wrap your company's internal APIs that don't have public integrations
- Build custom reporting tools that format data exactly how your team needs it
- Create integration bridges between systems that don't talk to each other
- Automate specialized data fetching that vendor tools don't support

**Share skills with the community**:
- Publish your SKILL.md files and scripts on GitHub
- Contribute to the growing ecosystem of MCP-compatible tools
- Help other developers bypass vendor limitations
- Build on skills others have shared

The pattern is consistent:
1. Write a JBang script that solves a real problem
2. Document it with SKILL.md  
3. Share it with your team or the broader community

JBang removes the friction from creating Java-based CLI tools, and skills provide a simple standard for AI integration. Together, they **empower developers to rapidly build and share custom capabilities** that enrich the entire AI ecosystem. You're not just using AI—you're actively expanding what it can do.

## Resources

- [JBang documentation](https://www.jbang.dev/)
- [JBang templates](https://www.jbang.dev/documentation/guide/latest/templates.html)
- [JBang 0.123.0 release with gpt template](https://github.com/jbangdev/jbang/releases/tag/v0.123.0)
- [Model Context Protocol (MCP)](https://modelcontextprotocol.io/)
- [National Weather Service API](https://www.weather.gov/documentation/services-web-api)
- [Vercel: Agent Skills Explained](https://vercel.com/blog/agent-skills-explained-an-faq)
