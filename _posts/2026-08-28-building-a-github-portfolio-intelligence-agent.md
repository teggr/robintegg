---
layout: post
title: "Building a GitHub Portfolio Intelligence Agent"
date: "2026-08-28"
description: "How I built devagent: a read-only OpenCode workspace that discovers, analyses, and plans across my entire GitHub portfolio."
image: /images/my-cloud-agent.jpg
tags:
  - ai
  - agents
  - opencode
  - github
  - workflow
---

I already had a remote OpenCode environment running on an Ubuntu VPS (covered in my [previous post](/2026/08/24/setting-up-my-ai-cloud-agent)). It could open repositories, write code, run tests, and use GitHub. What I noticed was that the questions I actually wanted to ask didn't belong to any single repository.

Questions like: what Spring Boot versions am I running? Which projects are still on Java 17? If I wanted to standardise Maven wrappers, where would I start?

These are portfolio questions. So instead of making OpenCode better at modifying individual repositories, I gave it a persistent workspace containing my GitHub portfolio and taught it how to discover, analyse, and plan across that portfolio. The result is `devagent`.

## The idea

The concept is simple. Give an OpenCode session its own workspace at `~/devagent/`, populate it with fresh checkouts of my GitHub repositories under `~/devagent/projects/`, and add a small number of skills that explain how to reason about those repositories.

The important distinction is that the projects aren't there for development. They're an analysis corpus. The agent's job is to understand them, not modify them.

## Three capabilities, three responsibilities

`devagent` is a three-stage pipeline: discovery, analysis, and planning.

**Discovery** answers: what do I have? It establishes which repositories exist, which are active or archived, which are public or private, what build systems they use, and what languages they contain. This isn't a GitHub repository list. The agent can inspect the actual contents, reading `pom.xml`, `package.json`, `.github/` layouts, and project structure.

**Analysis** answers: what does it look like? Once discovery has established what exists, analysis compares repositories. I asked it to review my Spring Boot portfolio, and rather than opening ten `pom.xml` files myself it produced a consolidated view: which projects are on which Spring Boot versions, correlated with Java versions, with no manual work on my side.

**Planning** answers: what could or should I do about it? When I asked what it would take to introduce Spotless formatting across my Java projects, it didn't just say "add the same Maven plugin everywhere." It classified the portfolio into Maven multi-module projects, single-module Maven projects, Java projects without Maven, and non-Java projects. It identified the decisions that needed to be made first, things like which formatter to standardise on and whether to reformat existing code immediately. That's a more useful engineering output than blindly editing files.

## Why a separate workspace?

I already had repositories checked out on the VPS for normal development. I chose not to point `devagent` at those. The portfolio agent has its own `~/devagent/projects/` directory, separate from `~/projects/`.

This matters because the two workspaces have different lifecycles. The analysis workspace can be refreshed or rebuilt without touching development work. The agent can inspect the full portfolio without interfering with whatever I happen to be working on.

## OpenCode 2 is the host

`devagent` is just an OpenCode 2 workspace. The intelligence comes from the combination of OpenCode's context, the repository corpus, GitHub access, and a small set of specialised skills.

The skills live in `.opencode/skills/`:

```text
.opencode/
└── skills/
    ├── repository-discovery/
    │   └── SKILL.md
    ├── portfolio-analysis/
    │   └── SKILL.md
    └── task-planning/
        └── SKILL.md
```

I separated the responsibilities rather than putting everything into `AGENTS.md`. Each skill has a clear purpose and the system has somewhere natural to grow. If I decide I want security analysis across my repositories, that becomes another skill rather than an ever-growing main prompt.

## The boundary that matters

The most deliberate decision was not to make this a coding agent.

`devagent` should read, analyse, and plan. It should not modify repositories, create branches, push changes, create pull requests, or create GitHub issues. If I ask how something should be implemented, it answers with a plan. If I decide to go ahead, that implementation happens through a separate development workflow.

This is a useful trust boundary. Analysis and planning can happen without any risk of unintended changes to the portfolio.

## A different way to look at a GitHub account

The conceptual shift is treating a GitHub account as a software portfolio rather than a collection of repositories. That creates a different class of questions.

The useful questions become things like: what does my dependency landscape actually look like? Where are the inconsistencies across projects? What would it take to bring everything up to a common standard?

The agent can determine scope itself. I don't have to tell it which repositories to inspect.

## Where this goes next

The current system is small, and that's part of what I like about it. Three skills, a projects directory, and an `AGENTS.md`. No custom database, no vector search, no autonomous coding loop. The repositories are the source material and the skills provide the methodology for reasoning about them.

The next capabilities I'd add are dependency analysis, CI analysis, and build configuration comparison. But there's no reason to build those now. The more useful next step is to use what exists, notice which questions I ask repeatedly, and add skills where they're genuinely needed.

The goal isn't to automate the development. The goal is an AI that can sit alongside the full portfolio and answer: given everything I've built, what should I know, and what should I do next?
