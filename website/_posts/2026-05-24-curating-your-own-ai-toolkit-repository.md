---
layout: post
title: "Curating Your Own AI Toolkit Repository"
date: "2026-05-24"
description: "Why I created ai-toolkit to manage prompts, agents, skills, and instructions, and how a published catalog plus JBang installer helps me start faster."
image: /images/ai-toolkit-screenshot.gif
tags:
  - ai
  - github copilot
  - agents
  - skills
  - jbang
  - workflow
---

I wanted one place to manage the Copilot resources I keep reusing across projects: prompts, agents, skills, and custom instructions. Local folders were starting to drift, and setup for a new repository became too manual.

So I put everything into a dedicated repository: [ai-toolkit](https://github.com/teggr/ai-toolkit), and published it at [teggr.github.io/ai-toolkit](https://teggr.github.io/ai-toolkit/).

![ai-toolkit published site screenshot]({{site.baseurl}}/images/ai-toolkit-screenshot.gif)

## What the repository gives me

The repository is organised as installable bundles, so I can keep resources grouped by purpose and type, then pull only what I need into a project.

For example:

- `discovery/agents/discovery.md`
- `discovery/prompts/`
- `discovery/skills/`
- `discovery/instructions/`

That structure matters when you are iterating on agent workflows. I can version the full set, review changes in Git, and avoid copy-pasting snippets between random local directories.

## Why I added a JBang installer

I wanted startup friction close to zero, especially when testing new ideas in fresh repositories. The `AiToolkit.java` script gives me a simple install flow:

```bash
jbang https://raw.githubusercontent.com/teggr/ai-toolkit/main/AiToolkit.java list
jbang https://raw.githubusercontent.com/teggr/ai-toolkit/main/AiToolkit.java install discovery
```

That lets me front load a project with useful agent resources in minutes instead of rebuilding the same setup each time.

## Why publishing the catalog matters

A private local collection is useful, but publishing the catalog gives me two practical advantages.

First, I get a single place to browse and share what I have built. The published site makes discovery easier than digging through local paths.

Second, it keeps me honest about cloud constraints. Globally installed local skills are not available to cloud agents, so depending on local-only setup can produce inconsistent results between local and cloud runs. Keeping resources curated in a repository makes that gap visible and easier to manage.

## Where this helps most

This setup is most useful when I am creating new repositories quickly, experimenting with workflow patterns, or sharing a reusable baseline with other developers. I still use community resources like the Copilot marketplace and public skills repositories, but I also want a curated set that reflects my own working style and constraints.

## References

- [ai-toolkit repository](https://github.com/teggr/ai-toolkit)
- [ai-toolkit published catalog](https://teggr.github.io/ai-toolkit/)
- [GitHub Copilot docs](https://docs.github.com/en/copilot)
- [Awesome Copilot skills](https://awesome-copilot.github.com/skills/)
