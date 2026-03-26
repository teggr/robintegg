---
layout: post
title: "Getting Started with the Java Copilot SDK"
date: "2026-03-26"
image: /images/java-copilot-sdk.png
tags:
  - java
  - github copilot
  - ai
  - jbang
  - llm
---

I use GitHub Copilot every single day. My company pays for a license, and I also pay for a personal one — it's my go-to platform for AI-assisted coding, and I want to make the most of both. That means going beyond autocomplete and building my own tools, automations, and agentic workflows on top of it.

In a [previous post](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills) I wrote about a mental model for AI orchestration using three Copilot primitives: Agents, Prompts, and Skills.

The Java Copilot SDK is what makes that model programmable. Instead of writing workflow instructions in Markdown and hoping the AI interprets them correctly, the SDK lets you write proper Java code that calls the LLM, structures the interaction, and wires everything together. You can automate your agents and prompts, not just describe them.

GitHub released a Copilot SDK, and although it initially launched without a Java version, a team of developers from the Java community stepped up and created a downstream port of the .NET version. That port has now become the official Java SDK. A big well done to everyone involved.

Here's where you can find out more:

- [copilot-sdk-java on GitHub](https://github.com/github/copilot-sdk-java) — SDK source code
- [Bruno Borges on Medium](https://medium.com/@brunoborges) — follow Bruno for updates and deep dives on the SDK
- [Official Java SDK Documentation](https://github.github.com/copilot-sdk-java/) — documentation and getting started guides

## Getting started with a JBang file summarizer

One of the most practical things you can do with the Copilot SDK is build command-line tools that use the LLM to reason over local content. A file summarizer is a perfect first project: read a file, send it to the model with a prompt, and get a structured summary back.

JBang makes this frictionless. No Maven project, no IDE, no build file — just a single Java file you can run directly from the terminal.

```shell
# create the jbang script
jbang init summarize.java

# edit the script
jbang edit . summarize.java
```

Here's a complete file summarizer script using the Java Copilot SDK:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS com.github:copilot-sdk-java:0.2.1-java.0

import com.github.copilot.sdk.CopilotClient;
import com.github.copilot.sdk.json.MessageOptions;
import com.github.copilot.sdk.json.PermissionHandler;
import com.github.copilot.sdk.json.SessionConfig;
import com.github.copilot.sdk.json.SystemMessageConfig;

void main(String... args) throws Exception {

    if (args.length != 1) {
        System.err.println("Usage: jbang summarize.java <file-path>");
        System.exit(1);
    }

    Path filePath = Path.of(args[0]);
    if (!Files.exists(filePath)) {
        System.err.println("File not found: " + filePath);
        System.exit(1);
    }

    String content = Files.readString(filePath);

    // Create and start client
    try (var client = new CopilotClient()) {

        client.start().get();

        // Create a session
        var session = client.createSession(
                new SessionConfig()
                        .setSystemMessage(new SystemMessageConfig()
                                .setContent(
                                        """
                                                You are a technical writing assistant.
                                                Summarise the provided content concisely, covering: the main topic, key points, and any action or conclusions.
                                                Keep the summary to 3-5 sentences.
                                                """))
                        .setOnPermissionRequest(PermissionHandler.APPROVE_ALL))
                .get();

        // Send a message
        var response = session.sendAndWait(new MessageOptions()
                .setPrompt(String.format("Please summarise the content of %s", content))).get();

        System.out.println(response.getData().content());

        client.stop().get();

    }

}

```

Save this as `summarize.java` and run it with:

```bash
# run command
jbang summarize.java path/to/your/file.md

# output
jbang summarize.java README.md
[jbang] Building jar for summarize.java...
Mar 26, 2026 12:00:55 PM com.github.copilot.sdk.CopilotClient lambda$startCore$1
INFO: Copilot client connected
The **GitHub Copilot SDK for Java** is a pre-GA Java library (Java 17+, available via Maven/Gradle) that enables programmatic control of GitHub Copilot CLI to build AI-powered applications and agentic workflows. It provides a `CopilotClient` API for creating sessions, sending prompts, and handling streaming events like assistant messages and token usage. The SDK tracks the official .NET/Node.js Copilot SDK reference implementations, with weekly automated upstream syncs via GitHub Actions and AI-assisted porting to Java. It is MIT-licensed, supports JBang for zero-setup experimentation, and includes full documentation, Javadoc, MCP server integration guides, and a cookbook of common recipes.
```

The `//DEPS` directive pulls the Copilot SDK from Maven Central automatically — no setup required. JBang handles the classpath for you. The SDK picks up your Copilot credentials from the environment, the same credentials your IDE uses, so there's no additional authentication to configure.

## What the SDK handles for you

The script above is intentionally minimal, but the Copilot SDK does a lot of work behind the scenes. It manages authentication against your Copilot license, handles token refresh, and routes your requests to the appropriate model endpoint. You get access to the same models your IDE uses, without having to manage API keys or set up separate accounts.

The `CopilotClient` is the entry point for everything. From there you can access the chat API (as shown above), but also tools and skills for building more structured agentic workflows — which I'll cover in a follow-up post.

## Next steps

Once you've got a summarizer working, the natural next step is to wire the client into your Java application or even add it into a GitHub Copilot skill so the AI can call it on your behalf. The JBang script becomes a skill script, and Copilot orchestrates when and how to invoke it based on your conversation.

I covered how skills work in more depth in [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang), which pairs well with this post if you want to see how the pieces connect.

## A note on Spring AI

If you're already using Spring AI, you might wonder where the Copilot SDK sits in relation to it. It feels like a higher-level abstraction — the SDK has built-in concepts like skills and tools out of the box, whereas Spring AI operates at a lower level and gives you more control. The other significant difference is that, as far as I'm aware, you can't use your GitHub Copilot license with Spring AI — it targets models through its own provider integrations. If you're investing in a Copilot license and want to use that in your own tooling, the Copilot SDK is the natural fit. Whether the two ecosystems converge in the future remains to be seen.

## References

- [copilot-sdk-java on GitHub](https://github.com/github/copilot-sdk-java)
- [Bruno Borges on Medium](https://medium.com/@brunoborges)
- [Official Java SDK Documentation](https://github.github.com/copilot-sdk-java/)
- [JBang documentation](https://www.jbang.dev/documentation/)
- [Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills)
- [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang)
