---
layout: post
title: "May 2026 AI Retrospective"
date: "2026-06-01"
description: "Discovery-first workflows, toolkit curation and Copilot still rules."
image: /images/ai-retrospective-may-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
---

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet), or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

**Tools I'm currently using**: GitHub Copilot, Claude, Gemini, vscode, Java Copilot SDK, [Granola](https://www.granola.ai/), Github Copilot App (Preview)

## Discovery before planning is now default

The main shift in my day-to-day work is that discovery now has its own phase instead of being mixed into planning. I used to jump from ticket to plan too quickly, then pay for it halfway through implementation when missing context showed up or the planning stage did not fully understand the requirements.

My baseline sequence now is simple and repeatable: load ticket context, run discovery, write plan, implement. Loading the context up front pays off cumulatively. The first plan is usually stronger because it starts from verified context instead of optimistic assumptions. That change reduces wrong turns, mid-task resets, and prompt churn during implementation and reviews.

One bit I will be keeping an eye on is determining whether I need to use the discovery stage for every ticket. GitHub has recently changed its pricing model so I may start to get a little more aware of token costs which I've felt less aware of with the previous request model.

## Curating my own toolkit removed setup drag

The second theme for May was standardising reusable resources. I am moving my prompts, agents, skills, and instructions into a dedicated toolkit repository and making the setup installable instead of copy-paste driven.

## Tool evaluations for May

GitHub Copilot in VSCode remains the core tool in my daily workflow because the Copilot support continues to be first class and each release brings more features and better usability. Some recent additions such as the Agents Window and Integrated browser are great.

The Agents Window is a neat way to manage all your agent sessions at a higher level, though I probably still prefer the main coding IDE sidebar so that I can keep the code in plain sight.

The integrated browser is already useful to view web development, but I think the real productivity gains are going to be with the agent integration for both the feedback loop and being able to share screenshots and DOM elements with your chat window and also the ability to potentially drive the browser window in an automated fashion.

## What I wrote in May

Two posts from May capture most of the practical changes:

[A Discovery Agent for Your VSCode Workflow](/2026/05/13/a-discovery-agent-for-your-vscode-workflow), where I documented a dedicated discovery phase before planning and implementation.

[Curating Your Own AI Toolkit Repository](/2026/05/24/curating-your-own-ai-toolkit-repository), where I described the repo + installer approach for reusable Copilot resources.

## Links worth your time

- [Using Claude Code: The Unreasonable Effectiveness of HTML](https://x.com/trq212/status/2052809885763747935?s=46): Thariq makes a practical case for asking agents to generate HTML instead of Markdown for long specs, reviews, and explainers. The main benefit is readability and interaction, not token efficiency.

- [Ask for HTML output](https://x.com/karpathy/status/2053872850101285137?s=46): Karpathy extends the same idea and frames HTML as a useful step toward richer, more visual AI outputs. It is a short but helpful mental model for where agent interfaces might go next.

- [Agent Harness Engineering](https://x.com/addyosmani/status/2053231239721885918?s=46): Addy breaks down why agent quality is mostly a harness problem, not just a model problem. The post is useful if you are tuning prompts, tools, hooks, and feedback loops based on real failures.

- [The Orchestration Tax](https://x.com/addyosmani/status/2059844244907696186?s=46): This one captures the practical cost of running many agents in parallel when human review is still the bottleneck. The advice to size parallelism to review capacity is one I am trying to internalize.

- [Advanced Slop Prevention Techniques](https://www.lorenstew.art/blog/slop-prevention/): Loren outlines a layered review pipeline that gets progressively harder to fool, from cross-model review through deterministic checks. The one-PR-at-a-time discipline is a useful counterweight to agent sprawl.

- [Build agents, not pipelines](https://www.seangoedecke.com/build-agents-not-pipelines/): Sean compares pipelines and agents with concrete trade-offs around predictability, context, and cost. I found the framing useful: prefer pipelines for strict bounds, prefer agents for hard tasks with unknown context needs.

## What I want to answer in June

June's open question is thinking more about longer running workflow agents. How can I let my agents loose in an autopilot fashion whilst keeping the guardrails there.
