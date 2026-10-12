---
layout: post
title: "The AI Agent Reality Check: I Can't Afford to Code Like a YouTuber"
date: "2026-09-12"
description: "AI coding demos keep getting more ambitious. My question is simpler: what can I sustainably justify when the tokens come out of my own budget?"
image: /images/my-cloud-agent.jpg
tags:
  - ai
  - github copilot
  - agents
  - workflow
---

The recent wave of Codex and project demos has been fun to watch, right up to the moment somebody casually mentions they are managing 100 agents.

That is the point where my brain stops thinking about capability and starts thinking about cost. Not because the feature looks bad. It usually looks great. The question is whether the economics of that workflow have anything to do with my own day-to-day reality.

I think there is a widening gap between what AI coding tools can do, what influencers demonstrate them doing, and what an ordinary developer can sustainably afford each month.

## When the demo budget stays off screen

YouTube, X, and launch videos are full of elaborate AI workflows now. Multiple coding agents running in parallel. Premium models. Large context windows. Long autonomous loops that inspect a repo, make changes, run tests, rethink the result, and keep going.

I do not think most of these demos are fake. The problem is simpler than that. The economics are often invisible.

The person doing the demo may have company-funded API usage, sponsorship credits, several subscriptions, a higher allowance than I do, or simply a much higher appetite for spending money on experimentation. That is all fine, but it quietly shifts the baseline. It makes technically possible look economically normal.

Those are not the same thing.

## The home developer budget is different

For me, the number sitting in the background is roughly £40 a month for Copilot Pro. That is already a meaningful subscription. It is not outrageous if I am getting value from it, but it is also not something I want to casually double because a workflow demo looked impressive.

That changes the actual question I am trying to answer.

I am not asking whether I can make an agent do 20 things in parallel. I am asking whether I want to pay for that level of parallelism on a personal subscription. The AI workflow for a home developer is not "let's see how far this goes." It is more like "how much of this month's budget do I want to burn this week?"

## Agents are excellent at spending tokens

This is the part that makes the trade-off real.

Agents create value by looping. They inspect the codebase, read files, reason about the problem, make changes, run tests, inspect failures, revise the plan, and try again. That is exactly why they are useful. It is also exactly why they can chew through an allocation much faster than a simple chat prompt.

One agent doing that for a hard task can be easy to justify. A couple of agents in parallel might also be worth it if the task is large enough. But when a demo reaches 100 agents, I do not see 100 helpful assistants. I see 100 concurrent token consumers attached to a budget I do not have.

That is a very different proposition when I am paying the bill myself.

## The frontier is useful, but it is still the frontier

I still want to watch these demos. They are useful because they show the outer edge of what the tooling can do. They show where workflows are heading, what kinds of orchestration are becoming possible, and which ideas are worth stealing for smaller scale use.

What I should not do is mistake somebody else's AI budget for my own.

If somebody can run a swarm of agents against a serious codebase with a generous allowance or a company card, good. I will probably learn something from watching it. But my optimisation problem is different. I am trying to maximise useful development output per pound, not just agents multiplied by tokens multiplied by model quality.

## Return on investment matters more than spectacle

I enjoy coding with agents. That is why this question matters to me at all. They make certain kinds of work faster, they reduce some of the friction around exploration and repetitive tasks, and they make software development more fun than it was before.

But enjoyment is not return on investment.

The question I need to keep asking is what I am actually getting back for the extra spend. If an agent saves me an hour, helps me understand an unfamiliar codebase, or gets a boring piece of work done cleanly, that is easy to justify. If I am running more agents mainly because the demo looked good on YouTube, the value gets much harder to defend.

That is why I think agentic coding has an adoption constraint that is not purely technical. It is economic.

## What I am taking from it

I am not backing away from agentic coding. I am enjoying it too much for that. I am backing away from the assumption that more agents, bigger models, and more tokens automatically mean more productivity.

If you are paying for this yourself, the monthly bill sits alongside context size, model quality, and orchestration features as a real design constraint. I think that makes the more interesting question much smaller and more personal: what is the most useful agent workflow I can run within my own budget?

I have written separately about the other side of this problem, which is how I am trying to stretch my Copilot usage with Auto while watching out for premium model overuse and tuning the available model options in VS Code and GitHub Copilot.

## References

- [OpenAI Codex](https://openai.com/index/introducing-codex/)
- [GitHub Copilot plans](https://github.com/features/copilot/plans)
- [Stretching Your Copilot Token Budget With Auto](/2026/09/13/stretching-your-copilot-token-budget-with-auto)
- [Setting up my AI Cloud Agent](/2026/08/24/setting-up-my-ai-cloud-agent)
- [The Orchestration Tax](https://x.com/addyosmani/status/2059844244907696186?s=46)
