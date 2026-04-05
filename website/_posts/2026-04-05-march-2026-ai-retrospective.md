---
layout: post
title: "March 2026 AI Retrospective"
date: "2026-04-05"
description: "Harnesses, orchestrators, and the growing ecosystem of AI workflow tooling — a personal retrospective on March 2026."
image: /images/ai-retrospective-mar-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
---

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet) — or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

**Tools I'm currently using**: GitHub Copilot, Claude, Java Copilot SDK

## Harnesses and Orchestration

March has been dominated by a single theme for me: harnesses. Specifically, building workflows for the development process in my own domain. I am not trying to build a full lifecycle platform — I don't have the expertise or the capacity for that — but I do want to understand the tooling I'm using from the inside out. The best way I know to do that is to build something myself.

Most of my time has been spent building my own harness on the Copilot SDK platform. The choice to build rather than adopt a ready-made tool is deliberate. When you build your own harness — even a modest one — you develop a real feel for the features and trade-offs that the larger tools make on your behalf. That understanding becomes invaluable when you're evaluating those tools later. You know what they're choosing to hide from you and why.

### The Wider Ecosystem

There is a whole theme emerging online around this exact space. Harnesses, orchestrators, and task trackers are multiplying fast. Then there are the agentic workflow tools, the online coding agents that bring in remote and non-interactive execution, and tooling aimed at the entire lifecycle — products like Spec Kit that take you from specification all the way through to code. So there's a tool for every kind of process, but each one is general purpose, which means for any sufficiently specific workflow, you'll probably want to write your own. That's both the frustration and the opportunity.

At the framework level, there are already plenty of Java SDKs operating at the agent harness layer with common primitives: LLM access, skills, agents, tools, and prompts. These are the building blocks. But I'm also watching an emergent category that operates at a higher level of abstraction — frameworks like [Embabel](https://github.com/embabel/embabel-agent) and now Kook, which are positioned around the concept of a **goal** rather than a workflow step. The framing shifts from "what does the agent do next" to "what is the agent trying to achieve." That's a meaningful change in perspective.

### What I've Written About

I wrote three posts in March that connect closely to this theme:

[Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills) lays out the mental model I've been using — Agents, Prompts, and Skills as a three-layer architecture that mirrors clean code principles. Agents own the why, Prompts own the what, Skills own the how. Getting these roles clear has made my workflows considerably easier to reason about and extend.

[AI Feedback: Close the Loop](/2026/03/14/ai-feedback-close-the-loop) tackles something most workflows quietly skip: the step at the end of a session where you encode what you just learned so the AI doesn't make the same mistake next time. Copilot instructions and ArchUnit tests are the two places you can do this in a way the AI will actually respect. A reusable prompt to drive this process makes it something that actually happens rather than something you intend to do.

[Getting Started with the Java Copilot SDK](/2026/03/26/getting-started-with-java-copilot-sdk) is where the harness building becomes concrete. The SDK makes the Agents, Prompts, and Skills model programmable in Java rather than just described in Markdown. Combined with JBang for zero-setup experimentation, it's the fastest path I've found to building real tooling on top of your Copilot subscription.

## A Better Approach to Research

The most impactful process change I made in March has nothing to do with tooling — it's about how I approach research. The shift is simple but easy to overlook: ask all your questions independently of the desired outcome. Before you have a plan, before you've decided on an approach, run the research without framing it around a conclusion you've already reached. You get less biased data back, which then informs a much better plan.

The practical version of this looks like running a set of research prompts against the codebase and the ticket before any implementation planning begins. The questions are factual — what does this module do, where does this pattern appear, what does the existing test coverage look like — not leading questions designed to confirm a solution you've already settled on. Once the research is done, then you bring in the codebase context and plan.

This sounds obvious stated plainly, but it's easy to shortcut when you're under pressure to just get on with it. The research-first discipline has been consistently worth the extra time.

## Building a Knowledge Base

Following from the research approach comes a related idea that I've been thinking about more seriously: a persistent knowledge base grounded in the codebase itself. The concept is simple. Feed all your session learnings, meeting notes, and research outputs into a folder inside the repository. Then use your Copilot instructions to make the AI aware of that folder when it starts a new session.

The effect is that you stop losing the work you've already done. A session ends, the context window closes, but the learning survives because it's committed as a file. The next session can be grounded in the previous one. Over time this compounds — you're not repeatedly rediscovering the same things.

For me this folder is becoming a kind of institutional memory for my personal projects, the kind that normally only exists in someone's head or scattered across chat histories. It's early days, but the direction feels right.

## Some links worth reading

[Giving LLMs a Personality](https://www.seangoedecke.com/giving-llms-a-personality/) — Sean Goedecke's explanation of why AI models behave like people. The short answer is that it's not a design gimmick — personality is what makes a base model useful. Without post-training to carve out a reliable, predictable region of behaviour, a model is essentially unpredictable. The personality is the consistency. I have my own preferences about which model I prefer talking to, and this article helped me understand why those preferences are legitimate rather than superficial.

[How Squad runs coordinated AI agents inside your repository](https://github.blog/ai-and-ml/github-copilot/how-squad-runs-coordinated-ai-agents-inside-your-repository/) — Squad is an open source project built on GitHub Copilot that initialises a preconfigured AI team — lead, frontend developer, backend developer, and tester — directly inside your repository. Two commands and you have a multi-agent setup without heavy orchestration infrastructure. The interesting design choice is keeping everything repository-native and inspectable. Worth a look as a reference implementation for coordinated agents.

[Dex Worthy — Orchestrating AI workflows](https://www.youtube.com/watch?v=c630qv03i8g) — A video that felt like validation of the workflow orchestration approach I've been developing. If you've been thinking along similar lines, it's good to see the same ideas landing independently from a different angle.

[LangChain Open SWE](https://x.com/langchain/status/2033959303766512006?s=46) — LangChain's take on internal coding agents. The "Open SWE" framing is interesting — software engineering as a capability you can give to an agent rather than something a human exclusively owns.

[Harness Engineering 101](https://x.com/vtrivedy10/status/2031408954517971368?s=46) — A useful framing of what harness engineering actually is and why it matters. Validates the idea that building your own harness is worthwhile even when ready-made options exist.

[Google Cloud Skills](https://x.com/googlecloudtech/status/2033953579824758855?s=46) — Google Cloud's angle on skills as a primitive in AI workflows.

[Beyond the IDE](https://www.linkedin.com/posts/lucaronin_what-comes-after-the-ide-the-ide-has-been-ugcPost-7440685122653143040-uOys) — Luca Ronin's post on what the development environment looks like once the IDE is no longer the centre of gravity. The IDE has been the primary interface for developers for decades. What replaces it when AI agents are doing most of the implementation?

[Nagmi and Wsgmi](https://x.com/rahulgs/status/2036857870042411438?s=46) — A useful frame for deciding what to focus on while the models themselves continue to improve rapidly. Not everything you build today will be worth keeping, but some of it will compound.

[Automating Maven Dependency Upgrades Using AI](https://dzone.com/articles/automating-maven-dependency-upgrades-using-ai) — Another article in a long line of AI-generated Java content on DZone that doesn't bring much that's new. The real skill here isn't prompting an AI to generate this — it's being able to build on code that already exists and does the job. That would be worth reading.

[Copilot CLI: Comparing CLI Features](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/comparing-cli-features) — A much cleaner version of the Copilot CLI feature documentation than what existed before. It clearly breaks down the distinction between custom instructions, skills, tools, MCP servers, hooks, subagents, and custom agents. If you've been confused about where one ends and the other begins, this is the article to read.

## What I'm thinking about for April

The harness work continues. April will probably be less about building new things and more about stabilising what I have — making the workflow reliable enough that I'm not constantly debugging the orchestration rather than the actual problem I'm trying to solve. There's also the question of when to stop building the harness and start trusting the tools that already exist. I'm getting closer to having a good enough answer to that question.
