---
layout: post
title: "July 2026 AI Retrospective"
date: "2026-08-08"
description: "Skills, MCPs, and feedback loops took the centre of my July AI work."
image: /images/ai-retrospective-jul-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
  - skills
---

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once yet, or perhaps for those just behind, there is a trail of breadcrumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there is still not a lot of practical content about how people actually use AI in their day-to-day work. I have spent most of July going deeper on the infrastructure around that work, especially the reusable assets that make agents more dependable in my own repositories.

**Tools I'm using**: [GitHub Copilot](https://github.com/features/copilot), [Gemini](https://gemini.google.com/app?hl=en-GB), [vscode](https://code.visualstudio.com/), [Granola](https://www.granola.ai/)

**Tools I'm evaluating**: [Github Copilot App (Preview)](https://github.com/features/ai/github-app)

**Frameworks I'm developing with**: [Java Copilot SDK](https://github.github.com/copilot-sdk-java/), [Spring AI](https://spring.io/projects/spring-ai)

**Services I'm using**: [Github Copilot Pro+](https://github.com/features/copilot/plans), [ChatGPT](https://chatgpt.com/pricing/), [OpenRouter](https://openrouter.ai/models?order=pricing-low-to-high)

**Services I'm evaluating**: [Ollama Cloud](https://ollama.com/pricing)

## Copilot and VS Code are aligning more with other harnesses

The main theme in July was that Copilot and VS Code felt more aligned with the broader ecosystem of agent harnesses, such as Claude and Codex. One shift was the support for migrating prompts into skills. Strategically, that feels like another sign that the industry is moving away from prompts as the main unit of workflow and toward skills, installable capabilities, and plugin-style extensions that are easier to reuse across repos and teams.

A second shift for me was trying to land on a potential alternative to the Copilot AI service. I spent more time evaluating other options, including OpenRouter, Ollama Cloud, and local inference through Ollama. The broader point is that VS Code and Copilot have expanded support for BYOK, so it is easier to integrate with other providers while staying inside the same ecosystem. I am still sticking with the Copilot Pro+ plan for now, but I do think there are better pricing options out there to be found.

## MCPs are interesting when they stay close to the repo

My second theme was MCPs. I have actually been using more MCP tool references in my skills, especially for Buildkite, GitHub, and Jira. That has made the value of MCPs feel much more concrete to me because the integration is not just about exposing a generic tool surface. It is about bringing together workflows that already exist in my day-to-day process and making them easier for an agent to use inside a skill.

The most useful examples have been the ones that connect an agent to a repo or workflow in a very specific way. A skill can pull in the right context, call the right MCP-backed capability, and then keep the rest of the task moving without the agent needing to improvise around every edge case. That feels much more grounded than the earlier "MCP as a novelty" version of the idea.

## Feedback loops matter more than raw capability

The other big thread this month was feedback loops. I kept returning to the idea that agent quality is often a harness problem more than a model problem. The small things matter: how a tool reports failure, how a task gets re-asked, how a review step is surfaced back into the next attempt, and how a repo can nudge an agent toward better behaviour without hand-holding it at every step.

That is why I have been paying more attention to the workflow around skills, MCPs, and review loops rather than chasing the next shiny capability. A better feedback loop usually produces more reliable progress than a more powerful model running in an unfocused context.

## What I wrote in July

One post from July connects closely to the themes above:

[Screaming Architecture for Agents](/2026/07/24/screaming-architecture-for-agents), where I explored why agents tend to default to three-layer codebases and how reusable skills can help push structure back toward the business domain.

## Links worth your time

- [Migrate prompt files to skills (experimental)](https://code.visualstudio.com/updates/v1_129#_migrate-prompt-files-to-skills-experimental): A useful reference if you are thinking about moving from prompt-based workflows to more durable skill assets.
- [Building an AI-Native Content Machine for Scaled Employee Advocacy](https://www.youtube.com/watch?v=1_jlukb7gm4): Good practical thinking on how to design workflows for quality output rather than just raw throughput.
- [Airails](https://github.com/AdamBien/airails/blob/main/README.md): A strong example repository for thinking about how skills can be organised and reused in a real project.
- [Inside Anthropic](https://newsletter.pragmaticengineer.com/p/inside-anthropic): A useful read if you want a grounded view of how teams at Anthropic approach agent systems and operating constraints.
- [5 Lessons Running AI Agents in Production](https://developers.podcast.go-aws.com/web/episodes/215/index.html): A helpful episode for thinking about how agents behave when they stop being experiments and start being part of real delivery flows.
- [A scale of home AI setups](https://x.com/retrochainer/status/2078701730439504302?s=61): The thread is a bit scattered, but it is still a good reminder that local inference is a trade-off between cost, control, and hardware limits.

## What I want to answer in August

In August, I am doubling down on refining skills within repos so they help the agent with specific context rather than overly generic additions. I think that is an important constraint when adding skills and AI artefacts because the frontier models are so good now that you need fewer instructions for general-purpose changes. What the models do not have is the context of your repo, your principles, and your focus.

I am also going to spend more time investigating local model inference. I have not had much success there yet, but I want to be able to use more tokens without leaking more service fees. The trade-off is latency and context window size, and I want to understand where that is acceptable and where it is not.
