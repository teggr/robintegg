---
layout: post
title: "Exploring MCP with Spring AI"
date: "2026-04-26"
description: "MCP gives your LLM access to external systems via your agent. Spring AI makes it straightforward to build MCP servers in Java using annotations."
image: /images/spring-shell-ai.png
tags:
  - java
  - spring
  - spring ai
  - mcp
  - ai
---

MCP (Model Context Protocol) is becoming an important primitive in the AI tooling landscape. It gives an LLM structured access to external systems through an agent. If you want to support this protocol from a Java application and make it available to AI assistants like GitHub Copilot or Claude Desktop, MCP is the protocol to know.

Spring AI has had MCP support since the 1.0.0-M4 milestone release and wraps the [MCP Java SDK](https://modelcontextprotocol.io/sdk/java/mcp-overview) with Boot starters and annotation-based configuration that feel natural to Spring developers.

## What MCP gives you

MCP defines four primitives a server can expose to clients:

**Tools** are callable functions. The LLM can invoke them with structured input and receive a result. A search operation, a write action, or a calculation are all good candidates.

**Resources** are addressable content, accessed via URI templates. Notes, configuration files, or any structured content the client can read falls in here.

**Prompts** are named prompt templates. The server can provide reusable prompt fragments that clients can invoke by name, optionally with arguments.

**Apps** (or "embedded UI") is a newer addition to the protocol that lets a server expose HTML-based views or mini-applications rendered inside the MCP client.

These four primitives cover most of what you need to give an AI assistant structured access to your application.

## Transport options

MCP servers can communicate with clients over several transports:

**STDIO** is the simplest. The client starts the server as a child process and talks to it over standard input and output. This is common for local tools and CLI integrations.

**SSE (Server-Sent Events)** runs the server as an HTTP endpoint. The client connects and receives events over a persistent HTTP connection. Good for services already running as web applications.

**Streamable HTTP** is a newer transport that supports both request-response and streaming over HTTP. This is the recommended transport for new HTTP-based MCP servers.

For production Spring Boot services you will typically use the WebMVC or WebFlux HTTP-based transports. For local tooling or CLI wrappers, STDIO is the quickest path.

## Spring AI MCP support

Spring AI provides Boot starters for both MCP clients and servers. For building a server that exposes your Spring application to MCP clients, add the WebMVC starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

Configure the server type in `application.properties`:

```properties
spring.ai.mcp.server.name=my-mcp-server
spring.ai.mcp.server.version=1.0.0
spring.ai.mcp.server.protocol=STREAMABLE
```

The full set of server starters and transport options is documented at [docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html).

## Annotations

The annotation-based approach in Spring AI lets you declare MCP capabilities directly on Spring components without additional wiring. The four main annotations map directly to the MCP primitives.

### @McpTool

Mark a method as an MCP tool and Spring AI generates the JSON schema from the method signature:

```java
@Component
public class NotebookMcpTools {

    private final NoteService noteService;

    public NotebookMcpTools(NoteService noteService) {
        this.noteService = noteService;
    }

    @McpTool(
        name = "searchNotes",
        description = "search through the list of user notes",
        generateOutputSchema = true,
        annotations = @McpTool.McpAnnotations(
            title = "Notes search",
            readOnlyHint = true
        )
    )
    public NoteSearchResult searchNotes(
            @McpToolParam(description = "query", required = true) String query,
            @McpToolParam(description = "limit", required = false) Integer limit
    ) {
        int safeLimit = limit == null ? 10 : Math.max(1, Math.min(limit, 10));
        List<NoteSearchItem> items = noteService.searchNotes(query, safeLimit);
        return new NoteSearchResult(items, items.size());
    }
}
```

The `readOnlyHint` annotation tells MCP clients this tool does not modify state, which allows clients to invoke it more freely. The `generateOutputSchema` flag produces a schema for the return type so clients understand the structure of the response.

### @McpResource

Expose content addressable by URI. The path variables in the URI template are mapped to method parameters:

```java
@McpResource(
    uri = "note://notes/{id}",
    name = "Note",
    title = "Note",
    description = "Content of a note"
)
public String noteResource(String id) {
    return noteService.getNote(id).get().getContent();
}
```

You can also return a `ReadResourceResult` directly when you need more control over the content type or structure.

For dynamically enumerated resources where the full list of URIs is only known at runtime, Spring AI provides a lower-level `McpServerFeatures.SyncResourceSpecification` API. The notebook example uses this to register each note as its own individually addressable resource:

```java
@Bean
public List<McpServerFeatures.SyncResourceSpecification> notes() {

    List<SyncResourceSpecification> resourceSpecifications = new ArrayList<>();

    for (NoteListItem item : noteService.listNotes()) {

        McpSchema.Resource noteResource = new McpSchema.Resource(
                "note://notes/" + item.getId(),
                item.getTitle(),
                item.getTitle(),
                "Note content",
                null, null, null, null);

        McpServerFeatures.SyncResourceSpecification resourceSpecification =
                new McpServerFeatures.SyncResourceSpecification(
                        noteResource, (exchange, request) -> {
                            return new McpSchema.ReadResourceResult(
                                    List.of(new McpSchema.TextResourceContents(
                                            "note://notes/" + item.getId(),
                                            "text/markdown",
                                            noteService.getNote(item.getId()).get().getContent())));
                        });

        resourceSpecifications.add(resourceSpecification);
    }

    return resourceSpecifications;
}
```

Spring AI picks up any `List<McpServerFeatures.SyncResourceSpecification>` bean and registers the resources automatically.

### @McpPrompt

Define named prompt templates the client can retrieve by name. Arguments let the template be parameterised at invocation time:

```java
@Component
public class NotebookMcpPrompts {

    @McpPrompt(
        name = "Search notes",
        description = "Search for notes using the Notebook MCP tool"
    )
    public GetPromptResult searchPrompt() {
        String message = "Search my notes using the query ";
        return new GetPromptResult(
            "Search",
            List.of(new PromptMessage(Role.ASSISTANT, new TextContent(message)))
        );
    }
}
```

More complex prompts can use `@McpArg` to accept named arguments and build dynamic messages based on context.

The full annotation reference is at [docs.spring.io/spring-ai/reference/api/mcp/mcp-annotations-server.html](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-annotations-server.html).

## Working example: notebook-api

The [notebook-api](https://github.com/teggr/notebook-api) project is a small Spring Boot application that manages markdown notes and exposes them via MCP. It uses both the annotation style shown above and the lower-level `SyncResourceSpecification` API for dynamically registering each note as an individual resource.

The [notebook-react-app](https://github.com/teggr/notebook-react-app) is the React frontend for the same notes data and runs separately:

```bash
# Start the Spring Boot API (MCP endpoint at http://localhost:8080/mcp)
./mvnw spring-boot:run

# Start the React frontend (http://localhost:5173)
npm run dev
```

With both services running you have a working application to layer MCP capabilities onto.

## Testing with the MCP Inspector

The [MCP Inspector](https://modelcontextprotocol.io/docs/tools/inspector) is a browser-based tool for exploring and testing MCP servers. Point it at your running server, and you can browse available tools, resources, and prompts, invoke them manually, and inspect the responses without needing a full AI client set up.

To connect the Inspector to a local Streamable HTTP server, use the server URL:

```text
http://localhost:8080/mcp
```

This lets you verify your MCP surface before connecting it to a real client. It is faster than round-tripping through an LLM for debugging purposes.

## Using from VS Code

Once your MCP server is running, you can connect VS Code's Copilot agent to it by adding an entry to your `.vscode/mcp.json`:

```json
{
  "servers": {
    "notebook-api": {
      "type": "http",
      "url": "http://localhost:8080/mcp"
    }
  }
}
```

After reloading, GitHub Copilot in agent mode can discover and invoke the tools you have exposed. You can then ask Copilot to search your notes, retrieve specific ones by ID, or use any prompt templates you have defined.

## References

- [Spring AI MCP Overview](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-overview.html)
- [Spring AI MCP Annotations](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-annotations-server.html)
- [Spring AI Reference Documentation](https://docs.spring.io/spring-ai/reference/index.html)
- [MCP Java SDK](https://modelcontextprotocol.io/sdk/java/mcp-overview)
- [MCP Inspector](https://modelcontextprotocol.io/docs/tools/inspector)
- [notebook-api example](https://github.com/teggr/notebook-api)
- [notebook-react-app example](https://github.com/teggr/notebook-react-app)
