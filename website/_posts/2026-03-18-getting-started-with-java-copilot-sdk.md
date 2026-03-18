---
layout: post
title: "Getting Started with the Java Copilot SDK"
date: "2026-03-18"
image: /images/java-copilot-sdk.png
tags:
  - java
  - github copilot
  - ai
  - jbang
  - llm
---

I use GitHub Copilot every single day. My company pays for a license, and I also pay for a personal one — it's my go-to platform for AI-assisted coding, and I want to make the most of both. That means going beyond autocomplete and building my own tools, automations, and agentic workflows on top of it.

In a [previous post](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills) I wrote about a mental model for AI orchestration using three Copilot primitives: Agents, Prompts, and Skills. The Java Copilot SDK is what makes that model programmable. Instead of writing workflow instructions in Markdown and hoping the AI interprets them correctly, the SDK lets you write proper Java code that calls the LLM, structures the interaction, and wires everything together. You can automate your agents and prompts, not just describe them.

GitHub released a Copilot SDK, and although it initially launched without a Java version, a team of developers from the Java community stepped up and created a downstream port of the .NET version. That port has now become the official Java SDK. A big well done to everyone involved.

Here's where you can find out more:

- [copilot-sdk-java on GitHub](https://github.com/github/copilot-sdk-java) — SDK source code
- [Bruno Borges on Medium](https://medium.com/@brunoborges) — follow Bruno for updates and deep dives on the SDK
- [Official Java SDK Documentation](https://github.github.com/copilot-sdk-java/) — documentation and getting started guides

## Getting started with a JBang file summarizer

One of the most practical things you can do with the Copilot SDK is build command-line tools that use the LLM to reason over local content. A file summarizer is a perfect first project: read a file, send it to the model with a prompt, and get a structured summary back.

JBang makes this frictionless. No Maven project, no IDE, no build file — just a single Java file you can run directly from the terminal.

Here's a complete file summarizer script using the Java Copilot SDK:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21+
//DEPS com.github.github:copilot-sdk-java:0.1.0

import com.github.copilot.sdk.CopilotClient;
import com.github.copilot.sdk.chat.ChatRequest;
import com.github.copilot.sdk.chat.Message;
import com.github.copilot.sdk.chat.Role;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class summarize {

    public static void main(String[] args) throws Exception {
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

        CopilotClient client = CopilotClient.create();

        ChatRequest request = ChatRequest.builder()
            .model("gpt-4o")
            .messages(List.of(
                Message.of(Role.SYSTEM, """
                    You are a technical writing assistant. Summarise the provided content 
                    concisely, covering: the main topic, key points, and any action items 
                    or conclusions. Keep the summary to 3-5 sentences.
                    """),
                Message.of(Role.USER, "Please summarise the following content:\n\n" + content)
            ))
            .build();

        String summary = client.chat().complete(request)
            .choices().getFirst()
            .message().content();

        System.out.println("Summary of: " + filePath.getFileName());
        System.out.println("-".repeat(50));
        System.out.println(summary);
    }
}
```

Save this as `summarize.java` and run it with:

```bash
jbang summarize.java path/to/your/file.md
```

The `//DEPS` directive pulls the Copilot SDK from Maven Central automatically — no setup required. JBang handles the classpath for you. The SDK picks up your Copilot credentials from the environment, the same credentials your IDE uses, so there's no additional authentication to configure.

## What the SDK handles for you

The script above is intentionally minimal, but the Copilot SDK does a lot of work behind the scenes. It manages authentication against your Copilot license, handles token refresh, and routes your requests to the appropriate model endpoint. You get access to the same models your IDE uses, without having to manage API keys or set up separate accounts.

The `CopilotClient` is the entry point for everything. From there you can access the chat API (as shown above), but also tools and skills for building more structured agentic workflows — which I'll cover in a follow-up post.

## Next steps

Once you've got a summarizer working, the natural next step is to wire it into a GitHub Copilot skill so the AI can call it on your behalf. The JBang script becomes a skill script, and Copilot orchestrates when and how to invoke it based on your conversation.

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
