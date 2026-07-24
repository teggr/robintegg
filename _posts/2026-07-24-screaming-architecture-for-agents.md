---
layout: post
title: "Screaming Architecture for Agents"
date: "2026-07-24"
description: "AI agents default to three-layer codebases because that's what most public projects use. Here's why that matters and how a reusable skill fixes it."
image: /images/screaming-architecture.jpg
tags:
  - ai
  - github copilot
  - agents
  - architecture
  - skills
---

My default preference for organising a codebase has been "Screaming Architecture" for years. The term comes from [Uncle Bob's 2011 post](https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html): your directory structure should scream what the application does, not how it's wired together. If you open the top-level package and see `com.example.customer`, `com.example.order`, `com.example.payment`, you know you're in an e-commerce system. If you see `com.example.controller`, `com.example.service`, `com.example.repository`, you've learned nothing about the business.

I've found this style the most productive approach in practice. New team members orient faster. Features are easier to scope. Deletions are clean because all the related code is grouped. Oliver Drotbohm's [Whoops! Where Did My Architecture Go?](https://odrotbohm.de/2013/01/whoops-where-did-my-architecture-go/) captures the argument well: technical layering at the top level doesn't prevent the slow collapse into a big ball of mud, it just delays it. Package-by-feature exerts architectural pressure on every change. The talk [Package by Feature](https://www.youtube.com/watch?v=v1XIcgFUIEw) is worth watching as a companion piece.

## The problem agents bring

When I started using AI agents for code generation, I noticed a pattern quickly. Left to their own defaults, agents would produce three-layer structures: controllers in one package, services in another, repositories in a third. The generated code worked. The architecture was exactly what I didn't want.

This isn't the agent's fault. The models are trained on a large volume of public projects, and three-layer structure is dominant there. It's the path of least resistance when you don't say otherwise. The models have seen it countless times and reproduce it confidently.

The [Whoops! Where Did My Architecture Go?](https://odrotbohm.de/2013/01/whoops-where-did-my-architecture-go/) reasoning applies here directly. Oliver argues that a controller in a controller package has no business neighbour to create gravity around. Entropy wins because the structure offers no resistance. A three-layer layout is effectively neutral on every feature decision, which means over time it accumulates accidental coupling and implicit dependencies that nobody consciously put there.

A package-by-feature layout gives the agent a different gravity. If the agent is generating code into `com.example.order` it's forced to think about what belongs there and what crosses a boundary. That context shapes better decisions, even when the agent is working autonomously.

## Why prompting isn't enough on its own

You can get agents to produce better structure by including architecture guidance in individual prompts. That works, but it doesn't scale. Every new session, every new codebase, every new agent task needs the same reminder. Review overhead stays high because you're correcting the same drift in code review rather than preventing it up front.

What I wanted was a way to install the expectation once per project and have it apply persistently across all agent sessions, both local and cloud runs. That's what a skill does well.

## The screaming-architecture skill

I've built a skill for this and added it to my [ai-toolkit](https://teggr.github.io/ai-toolkit/). The skill is invocable, so agents can call it explicitly during design, code generation, or review tasks. It guides agents to organise packages and modules by business capability first, with technical details nested beneath feature slices rather than promoted to the top level.

The toolkit also includes a companion custom instruction file that references the skill and enforces feature-first architecture outputs during PR review. Drop the instruction into `.github/copilot-instructions.md` and it applies to every agent task in that repository without any per-session prompting.

Install it with:

```bash
jbang https://raw.githubusercontent.com/teggr/ai-toolkit/main/AiToolkit.java install screaming-architecture
```

That puts the skill and instruction files into `.github/` in your project. From that point the agent has access to the context on every run.

## What changes in practice

With the skill in place, agents default to business-named packages and push technical concerns down inside the feature slice. Code review overhead drops because the structural expectation is established before generation, not corrected after it. The agent doesn't need reminding in every session because the skill is part of the project context.

It also helps when you're onboarding an existing codebase. Running the skill against existing code surfaces where the three-layer structure has leaked in, which gives a clearer picture of where refactoring effort is most useful.

The underlying argument from Uncle Bob and Oliver Drotbohm still holds. Screaming architecture isn't a style preference, it's a structural forcing function. The skill just extends that forcing function to agents.

## References

- [Screaming Architecture - Uncle Bob](https://blog.cleancoder.com/uncle-bob/2011/09/30/Screaming-Architecture.html)
- [Whoops! Where Did My Architecture Go? - Oliver Drotbohm](https://odrotbohm.de/2013/01/whoops-where-did-my-architecture-go/)
- [Package by Feature - YouTube](https://www.youtube.com/watch?v=v1XIcgFUIEw)
- [ai-toolkit](https://teggr.github.io/ai-toolkit/)
- [screaming-architecture skill on GitHub](https://github.com/teggr/ai-toolkit/tree/main/screaming-architecture)
