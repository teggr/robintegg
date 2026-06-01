---
layout: post
title: "April 2026 AI Retrospective"
date: "2026-05-06"
description: "Consolidation, knowledge bases, and trying out the new wave of harness tools, a personal retrospective on April 2026."
image: /images/ai-retrospective-apr-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
---

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet), or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

**Tools I'm currently using**: GitHub Copilot, Claude, Gemini, vscode, Java Copilot SDK, [Granola](https://www.granola.ai/)

## The Industry Tempo

April has felt different from the months before it. The constant wave of new frameworks, new agent platforms, and new models hasn't stopped, but there's a change in how people are responding to it. Rather than immediately jumping to the next thing, more people seem to be pausing to figure out how to make proper use of what they already have. The conversation has shifted from "what's new" towards "what actually works in practice and how do I get more out of it."

That matches where I am. It's less satisfying to describe, but the return on investment is real. You can only learn so much from building the seventh slightly different version of the same thing.

## Persistent Knowledge Bases

The most interesting thread running through April for me has been around persistent knowledge. The idea is not new, but it's crystallising fast in the community.

Andrej Karpathy published a gist describing the concept clearly: instead of using RAG, which retrieves from raw sources at query time and rediscovers knowledge from scratch on every question, you build and maintain a wiki. The LLM reads new material, integrates it into the existing wiki, flags contradictions, updates summaries, and keeps cross-references current. The wiki is a compounding artifact rather than an index. You never write the wiki yourself; the LLM does all the bookkeeping. Karpathy's own setup has the LLM agent open on one side and Obsidian open on the other, with the LLM making edits in real time while he browses the result. The gist is written as a prompt template you can hand to your own agent to get started.

[gist.github.com/karpathy/442a6bf555914893e9891c11519de94f](https://gist.github.com/karpathy/442a6bf555914893e9891c11519de94f)

Dan Vega has already built a working implementation in Java using Spring AI: [karpathy-wiki](https://github.com/danvega/karpathy-wiki). It is a Spring Shell CLI application with three agents: a `WikiCompilerAgent` that turns raw source drops into interlinked wiki pages, a `ResearchAgent` that answers questions with citations, and a `WikiLinterAgent` that runs structural checks and surfaces contradictions. The wiki schema and agent behaviour are defined in a single `SCHEMA.md` file that all agents read at startup. This is a practical demonstration that the architecture translates cleanly into Java and Spring, and it is worth looking at closely if you are considering something similar.

Both of these connect to something I mentioned in the March retro: the knowledge base folder I've been building inside my own projects. The Karpathy framing gives that a cleaner foundation to build on. Sessions end, context windows close, but the wiki persists and compounds. The next session starts from everything the previous one learned rather than from scratch.

## Getting On with Granola

I mentioned [Granola](https://www.granola.ai/) in the March tools list without much detail. April has given me enough hands-on time to say something more useful about it. Granola sits alongside your meetings and transcribes your computer audio directly without a bot joining the call. It works across Zoom, Google Meet, Teams, Webex, and Slack. When the meeting ends, it enhances the rough notes you typed during the call into something structured and readable.

What makes it worth using over a raw transcript is the combination: you still take partial notes yourself, and Granola fills in the structure around them. The result is a clean record that captures both what you wrote and what you missed. Over a month of regular use the value compounds in the same way the wiki idea does. You stop losing the substance of meetings.

## Trying Emdash and Supacode

April was a good month to properly evaluate the new wave of agentic development environments, specifically [emdash.sh](https://www.emdash.sh/) and [supacode.sh](https://supacode.sh/). Both take a similar approach: you run multiple coding agents in parallel, each isolated in its own Git worktree, with a dashboard view across all of them.

The worktree isolation is well thought through. Emdash supports 20+ coding agents, pull from Linear, Jira, GitHub, or GitLab, has a Kanban view for tracking running agents, and a built-in diff view for reviewing and committing changes. As a tool for orchestrating parallel work it is genuinely good.

The Jira integration is thinner than I was hoping. I expected it to set up a session with the ticket information loaded into context, but it does not. It uses the ticket to name the branch and adds a link to the ticket in the UI, which is useful but not transformative. You still need to load the ticket content into context yourself.

The bigger constraint for my situation is that both tools are built around a single project model. A task is specific to one repository. At work our tickets routinely touch multiple repositories, and neither tool handles that well. A task scoped across three repos would need to be managed at the ticket level or broken into subtasks, which is not how I want to think about it.

I will come back to both as they mature. The direction is right, the workflow assumptions just do not quite match mine yet.

## What I've Written About

Two posts in April that connect to this month's themes:

[Why a Consistent Voice Matters (and How a Writing Skill Helps)](/2026/04/23/consistent-voice-with-a-writing-skill) covers the problem of AI-assisted content drifting in tone and style over time. The solution is a writing skill that encodes voice as explicit rules rather than vague intention. The [inside-lago-voice-skill template](https://github.com/getlago/inside-lago-voice-skill) is the practical starting point, and the article explains how to split a monolithic instructions file into separate voice and structure concerns.

[Exploring MCP with Spring AI](/2026/04/26/exploring-mcp-with-spring-ai) is a technical walk through the annotation-based MCP server support in Spring AI 2.0.0-M4. If you want to give an LLM structured access to a Java application, this is the current cleanest path. The post covers all four MCP primitives (tools, resources, prompts, and apps), both STDIO and HTTP transports, and includes a working example with the [notebook-api](https://github.com/teggr/notebook-api) project.

## Some links worth reading

[LLM knowledge base in a gist (Andrej Karpathy)](https://gist.github.com/karpathy/442a6bf555914893e9891c11519de94f): The clearest articulation I have seen of the wiki-over-RAG idea. Read this before building any knowledge management tooling around AI.

[Context as Code: The Missing Layer (Ben Detron)](https://bendetron.substack.com/p/context-as-code-the-missing-layer): A complementary framing. If the Karpathy gist is about what a persistent wiki should be, this article approaches context as a first-class engineering artifact, something you version, structure, and maintain with the same discipline as code.

[karpathy-wiki (Dan Vega)](https://github.com/danvega/karpathy-wiki): The Spring AI implementation. Practical proof that this architecture works in Java without much friction.

[Convergence towards goal-based harnesses](https://x.com/nichochar/status/2039739581772554549?s=46): A long-form piece on where harness architectures are converging. The core argument is that goal plus model plus tools plus feedback is becoming the standard shape. The feedback loop is the piece most current implementations still underweight.

[Obsidian, wikis, and Claude](https://x.com/defileo/status/2042241063612502162?s=46): Another angle on the persistent knowledge base theme, this time with Claude and Obsidian specifically.

[inside-lago-voice-skill](https://github.com/getlago/inside-lago-voice-skill): The writing skill template I used to build the voice skill for this site. If you produce content with AI assistance and care about consistency, this template is the right place to start.

[Fat skills / thin harness / fat code (Garry Tan)](https://x.com/garrytan/status/2043566215927328955?s=46): A short framing from Garry Tan. The idea is that investment belongs in the skills and in the code, not in the harness layer itself. Thin harnesses with rich skills and strong codebases is where stability lives. Matches my own experience from building the Copilot SDK harness.

## What I'm thinking about for May

The multi-repo problem with tools like Emdash also still needs a solution. I want to be able to run agents across a bounded set of related repositories as a single unit of work. No obvious tool does this well yet.
