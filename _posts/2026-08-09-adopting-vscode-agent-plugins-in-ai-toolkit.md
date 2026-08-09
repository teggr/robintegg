---
layout: post
title: "Adopting Agent plugin standards in ai-toolkit"
date: "2026-08-09"
description: "I updated ai-toolkit to follow the new Agent plugin standard, rewired the installer, and added a Javadoc Central plugin."
image: /images/ai-toolkit-plugins.jpg
tags:
  - ai
  - github copilot
  - agents
  - vscode
  - plugins
  - jbang
  - workflow
---

A recent announcement around agent plugins lined up closely with the way I had already been thinking about ai-toolkit, so I moved the project over to that model. The project already had the idea of bundles, but plugins fit the mental model better, especially now that VS Code has an explicit plugin story.

Here's an example plugin layout:

```txt
my-testing-plugin/
  plugin.json              # Plugin metadata and configuration
  skills/
    test-runner/
      SKILL.md             # Testing skill instructions
      run-tests.sh         # Supporting script
  agents/
    test-reviewer.agent.md # Code review agent
  hooks/
    hooks.json             # Hook configuration
  scripts/
    validate-tests.sh      # Hook script
  .mcp.json                # MCP server definitions
```

I use VS Code as my daily driver, so I followed the guidance from [agent plugins](https://agent-plugins.org/) and the [VS Code reference](https://code.visualstudio.com/docs/agent-customization/agent-plugins) while reshaping the repo. The result is that the bundles I had been curating in ai-toolkit are now plugins, and the published site at [teggr.github.io/ai-toolkit](https://teggr.github.io/ai-toolkit/) is now powered by that plugin structure rather than by a more ad hoc folder layout.

## The installer changed too

The installer had to change as part of the same shift. I updated it to support placing resources into a common AI folder (`.ai/`), which matches the pattern described in [this article](https://www.keboca.com/articles/cursorrules-ai-how-i-unified-my-cursor-and-claude-config-one-place). That pattern has also been useful at work, where we want to be able to support multiple agent harnesses with one set of ai artefacts.

The workflow looks like this for installing now for a bundle of skills/agents/mcp/hooks/instructions:

```bash
jbang https://github.com/teggr/ai-toolkit/blob/main/AiToolkit.java list
jbang https://github.com/teggr/ai-toolkit/blob/main/AiToolkit.java install discovery
```

## A new Javadoc Central plugin

Whilst updating the repo, I was able to push a new [Javadoc Central plugin](https://github.com/teggr/ai-toolkit/tree/main/javadoc-central). It wraps the [javadocs.dev](https://www.javadocs.dev/) service in a skill that can be used with either HTTP or MCP access. That gives an agent a way to look up current Javadoc for the libraries I am using without downloading sources or decompiling anything.

That feels like the right kind of plugin. It is small, focused, and directly useful when I am working with Java libraries that change quickly or when I want to avoid guessing about a method signature.

## References

- [ai-toolkit repository](https://github.com/teggr/ai-toolkit)
- [agent plugins site](https://agent-plugins.org/)
- [VS Code agent plugins docs](https://code.visualstudio.com/docs/agent-customization/agent-plugins)
- [javadocs.dev](https://www.javadocs.dev/)
