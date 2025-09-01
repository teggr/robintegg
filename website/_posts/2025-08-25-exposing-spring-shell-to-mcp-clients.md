---
layout: post
title: Exposing Spring Shell to MCP Clients
date: "2025-08-25"
image: /images/spring-shell-ai.png
tags:
  - java
  - spring
  - spring shell
  - mcp
  - spring ai
---

This short demo shows how to expose existing Spring Shell commands as tools consumable by MCP (Model Context Protocol) clients such as GitHub Copilot, using Spring AI's MCP server integration. The example repository is available at https://github.com/teggr/mcp-cli-demo and demonstrates two simple weather-focused commands: a state-based alerts command and a latitude/longitude forecast command.

## Why this is useful

- Reuse existing Spring Shell utilities from language model-powered clients without reimplementing logic.
- Expose well-documented, typed commands to LLM-based assistants via the MCP server protocol.
- Keep the same artifact runnable as both a CLI and a background MCP server.

## Quick overview

- A Spring Shell command is annotated with @Command and can be annotated with @Tool (Spring AI) so it becomes available to MCP clients.
- The same Spring Boot jar can run in a CLI profile or an MCP server profile via Spring profiles and properties.

## Let's see it in action

The demo contains a simple service that exposes a forecast-by-location command and is annotated so it becomes an MCP tool:

```java
@Command
@Service
public class WeatherService {

    @Command(command = "weather-forecast-by-location", description = "Get weather forecast for a specific latitude/longitude")
    @Tool(description = "Get weather forecast for a specific latitude/longitude")
    public String getWeatherForecastByLocation(
            double latitude,
            double longitude
    ) {
        // implementation in the demo
    }
}
```

### Profiles and properties

The project uses profiles to switch between running as a normal CLI and running as an MCP server.

application.properties (high level):

```properties
spring.application.name=mcp-cli-demo
spring.main.banner-mode=off
logging.pattern.console=
spring.profiles.default=cli
```

When running as a pure CLI (application-cli.properties):

```properties
spring.ai.mcp.server.enabled=false
spring.shell.noninteractive.enabled=true
spring.shell.interactive.enabled=false
```

When running as an MCP server (application-mcp.properties):

```properties
spring.ai.mcp.server.enabled=true
spring.shell.noninteractive.enabled=false
spring.shell.interactive.enabled=false
```

## Run the CLI

Start the jar and invoke the command directly from the shell:

```shell
java -jar <jar-location>/mcp-cli-demo-0.0.1-SNAPSHOT.jar weather-forecast-by-location --latitude 37.7749 --longitude -122.4194
```

Local CLI/terminal output for the demo commands:

![Terminal output]({{site.baseurl}}/images/cli-output.png)

## Run as an MCP server

A simple mcp.json entry (used by an MCP-capable client) that starts the jar in the mcp profile looks like this:

```json
{
  "servers": {
    "weather-service": {
      "type": "stdio",
      "command": "java",
      "args": [
        "-jar",
        "-Dspring.profiles.active=mcp",
        "<jar-location>/mcp-cli-demo-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

Tool selection and MCP configuration: 

![Terminal output]({{site.baseurl}}/images/mcp-cli-demo.png)

Example Copilot/assistant response using the getAlerts tool:

![Terminal output]({{site.baseurl}}/images/mcp-weather-alerts.png)

## Notes and references

- The demo intentionally keeps the implementation small so you can adapt the pattern to your own Spring Shell commands.
- See the official docs for details on the Spring AI MCP server integration, Spring Shell single-command customization, and the MCP quickstart:
  - https://docs.spring.io/spring-ai/reference/getting-started.html
  - https://docs.spring.io/spring-shell/reference/customization/singlecommand.html
  - https://modelcontextprotocol.io/quickstart/server#java

## Next steps

- Try annotating other Spring Shell commands with @Tool and exposing them via the MCP server.
- Add input validation and richer parameter types so LLM clients can pass structured inputs reliably.
