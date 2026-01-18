---
layout: post
title: "Understanding Context Windows in GitHub Copilot"
date: "2026-01-18"
tags:
  - ai
  - github copilot
  - developer tools
  - context windows
---

If you've been working with GitHub Copilot, you've probably wondered how all the different features—prompts, custom agents, subagents, skills, tools, MCP servers, and Copilot Spaces—actually fit together. The answer lies in understanding that they all share the same fundamental resource: the model's context window.

The context window is the LLM's working memory for a single inference. Think of it as a whiteboard where everything competes for space—your prompt, agent instructions, tool outputs, code snippets, and conversation history all need to fit within the same token budget. Understanding how these features interact with the context window helps you use them more effectively.

## The Mental Model

Here's the key insight: the context window is **mutable**, not append-only. Between LLM calls, the client (GitHub Copilot) can summarize, remove, or replace instructions and data. This is different from chat applications where everything just appends to history until you run out of tokens.

A typical context layout for a single LLM call looks like this:

```
[System Role Instructions]  
[Main Agent (.md)]  
[Optional Subagent (.md, injected client-side)]  
[Copilot Spaces (summaries or selected files)]  
[Skills / reusable patterns]  
[Tools / MCP outputs]  
[User prompt]  
[Conversation or editor state]
```

The client orchestrates which pieces are active for any given inference, managing the token budget by adding or removing components as needed.

## Prompts: The Starting Point

Prompts are the most straightforward way to interact with Copilot—they're one-off, local instructions you enter via editor comments or Copilot Chat:

```java
// refactor this method to use streams
public List<User> filterActiveUsers(List<User> users) {
    List<User> active = new ArrayList<>();
    for (User user : users) {
        if (user.isActive()) {
            active.add(user);
        }
    }
    return active;
}
```

Or in chat: `/* explain how this authentication flow works */`

Prompts are linear and ephemeral—cheap in token usage because they don't persist across interactions. Use them for quick tasks, small edits, or immediate clarifications where you need explicit control without long-term persistence.

## Custom Agents: Persistent Instructions

Custom agents are persistent instruction blocks defined in `.md` files that the client injects into the context window. Unlike prompts, they stick around to ensure consistent behavior across multiple interactions.

A custom agent might contain project-wide coding conventions:

```markdown
# Java Project Agent

## Code Style
- Use Optional<T> instead of null returns
- Prefer constructor injection over field injection
- Always use @Slf4j for logging

## Architecture Rules
- Controllers should be thin, delegate to services
- Services must be transactional
- Repository methods should use Spring Data JPA conventions
```

Custom agents have high token cost if verbose, because they occupy space while active. Use them for architectural standards, security policies, or multi-step reasoning that should apply consistently across your codebase.

## Subagents: Modular Specialization

Subagents are a client-side concept—they're not separate LLM processes, but rather modular instruction blocks that get injected into the same context window when needed.

This is important: **subagents don't have their own context window**. They're just a way to organize and inject specialized instructions temporarily. After the subagent completes its task, the client can discard or summarize its instructions to free up tokens.

Think of a main agent that coordinates development tasks, with subagents for specialized roles:

```markdown
# Main Development Agent
Use these subagents for specialized tasks:
- @security-reviewer for security analysis
- @test-generator for writing unit tests
- @formatter for code style cleanup
```

When you need security review, the client injects the security-reviewer subagent instructions, uses them for that task, then removes them to make room for other work. This provides on-demand modularity without permanently bloating the context window.

## Skills: Reusable Workflows

Skills are predefined prompt and tool patterns—essentially reusable mini-workflows. They're compact and low-cost because they're ephemeral unless combined with tools or agents.

A skill might be "generate unit test for current method" or "add error handling to this endpoint." The skill defines both the instruction pattern and which tools to use.

Use skills for repeated tasks, predictable transformations, or when you want to standardize workflows across your team. They optimize for consistency and reduce the need to repeat detailed instructions in the context window.

## Tools and MCP: External Information

Tools and Model Context Protocol (MCP) servers are structured interfaces for fetching data or performing computations—APIs, filesystem access, databases, or external services.

The schema definitions are small, but outputs can be token-heavy. Here's a simple example of a tool call:

```json
{
  "tool": "read_file",
  "params": {
    "path": "src/main/java/com/example/UserService.java"
  }
}
```

The tool schema is compact, but if that file is 500 lines of code, the output consumes significant tokens when injected back into context.

This is where the value becomes clear: tools move information **out of the LLM prompt**, allowing controlled reinjection into context only when needed. Use tools for external data, heavy computation, or fresh information that shouldn't live permanently in the context window.

The MCP integration in tools like Spring Shell (as I covered in my [previous post on exposing Spring Shell to MCP clients](/2025/08/25/exposing-spring-shell-to-mcp-clients)) makes this pattern even more powerful by standardizing how tools are exposed and invoked.

## Copilot Spaces: Project-Level Context

Copilot Spaces are project-level or multi-file context stores—repo summaries, metadata, multiple files. Critically, Spaces exist **outside the LLM**; only relevant portions are injected to avoid token overload.

Instead of pasting your entire project into context repeatedly, Spaces maintain an external index. When you ask "refactor the authentication flow," the client injects only the relevant authentication-related files or summaries into the context window.

Context impact is low-to-moderate if summaries or selected files are injected, but can be high if entire large files are included. Use Spaces for multi-file reasoning, repo-wide refactoring, or architectural analysis where you need to understand relationships across your codebase.

## How It All Flows Together

Here's how the client orchestrates these features through the context window:

1. **Client prepares context**: Injects system instructions, main agent, optional subagent, Copilot Spaces content, tools, skills, and user prompt
2. **LLM call**: Processes the combined context to generate output
3. **Client post-processing**: Summarizes or discards high-cost instructions (main agent, subagent, Spaces, tool outputs) to manage tokens
4. **Next step**: Client sends updated context to the LLM for continued reasoning or further agent/tool calls

The key idea: **only one LLM context window exists**. All features share it. Subagents, tools, skills, prompts, and Spaces are just different ways of injecting and managing tokens.

## Managing Your Token Budget

Here's a practical view of relative costs:

| Component      | Relative Cost  | Notes                                            |
|----------------|----------------|--------------------------------------------------|
| Main Agent     | High           | Persistent, verbose instructions take tokens     |
| Subagent       | Moderate       | Temporary, modular; injected only when needed    |
| Copilot Spaces | Low → Moderate | Summaries are cheap; full files can be expensive |
| Skills         | Low            | Compact, reusable instructions                   |
| Tools / MCP    | Variable       | Output size determines cost                      |
| User Prompt    | Low            | Ephemeral, linear addition to context            |

The principle: use **modular injections, summarization, and selective retrieval** to maximize usable context while staying under token limits.

## Practical Implications

Understanding context window management changes how you use these features:

**Start with prompts** for quick one-off tasks. They're cheap and don't persist.

**Add custom agents** when you need consistent behavior across sessions. Keep them focused—a 200-line agent document might be too verbose.

**Use subagents** to modularize complex workflows without permanently consuming tokens. Let the client inject and remove them as needed.

**Leverage skills** for repeated patterns. Instead of explaining "write a test with given-when-then structure" every time, define it once as a skill.

**Design tool outputs carefully**. If your tool returns megabytes of JSON, you're wasting tokens. Return focused, relevant data and let the LLM request more detail if needed.

**Let Spaces manage project context**. Don't manually paste file after file into chat. Let Spaces index your project and inject only what's relevant.

## Key Takeaways

The context window is a shared resource, and every feature competes for space within it. Understanding this helps you:

- Choose the right feature for each task based on token cost and persistence needs
- Recognize that subagents aren't separate processes—they're modular instructions in the same window
- Use Copilot Spaces to reduce manual context management
- Design tools and MCP servers that return focused, relevant data
- Keep custom agents concise to minimize their permanent token footprint

Design your workflows around **on-demand injection, modularity, and context efficiency**. The features are powerful, but they're most effective when you understand how they interact with the fundamental constraint: the model's context window.

## References

- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [Model Context Protocol](https://modelcontextprotocol.io/)
- [Exposing Spring Shell to MCP Clients](/2025/08/25/exposing-spring-shell-to-mcp-clients)
