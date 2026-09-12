---
layout: post
title: "I'm Enjoying AI Coding. I Just Can't Afford to Use It Like an Influencer"
date: "2026-09-12"
description: "AI coding demos keep getting bigger. My question is simpler: how much agentic coding can I justify when the tokens come out of my own subscription?"
image: /images/my-cloud-agent.jpg
tags:
  - ai
  - github copilot
  - agents
  - workflow
---

I like coding with agents. That's what makes this awkward.

The recent wave of Codex and project demos has been genuinely interesting to watch. Better task management, more background work, more parallelism, more ambitious workflows. A lot of it looks useful, and some of it points to where day-to-day development is clearly heading.

Then I hear someone casually say they are managing 100 agents and my first reaction is not admiration. It is a cost question. How much is that going to cost me?

That question keeps coming back because there is now a visible gap between what these tools can do, what influencers demonstrate them doing, and what an ordinary developer can sustainably afford every month.

## The AI influencer problem

YouTube, X, and product launch videos are full of developers demonstrating increasingly elaborate AI workflows. Multiple coding agents. Agents running in parallel. Premium models. Long context windows. Autonomous loops that inspect, edit, test, rethink, and try again.

Most of these demos are probably real. That is not the issue. The issue is that the economics often stay off screen.

The person demonstrating the workflow may have company-funded API access, sponsorship credits, several subscriptions, a much larger token allowance, or simply a higher tolerance for spending money on experimentation than I do. That matters because it quietly sets a baseline for what "normal" AI coding is supposed to look like.

I end up watching what is technically possible and, for a moment, judging my own workflow against it as if it were also economically sensible. Those are different questions.

## The £40 question

For me, the number in the background is about £40 a month for Copilot Pro. That is already a meaningful subscription for a home developer. It is not hard to justify if I am using it well, but it is also not a throwaway amount that disappears into the noise.

That changes the optimisation problem.

I am not asking "can I make this agent do 20 things simultaneously?" I am asking whether I want to spend more than £40 a month to make it do those 20 things. That is the real constraint. Once you start thinking about agents as token consumers rather than slightly magical autonomous developers, the monthly budget becomes much easier to see.

The AI workflow for a home developer is not "unlimited tokens, let's see what happens." It is more like "I've got a subscription, how much of it do I want to burn this week?"

## Agents are very good at spending tokens

This is where the productivity story gets more complicated.

Agents are useful precisely because they can keep going. They inspect the repository, read multiple files, reason about the problem, make changes, run tests, inspect failures, revise the approach, and try again. That loop is often where the value comes from. It is also where the cost comes from.

One agent doing that carefully can still be good value. Five agents doing it in parallel might also be good value if the task is hard enough and the outcome matters enough. But 100 agents is not just "100 helpers." It is potentially 100 concurrent consumers of a finite monthly allocation.

That is a very different proposition when you are paying the bill yourself.

## My attempt to make the economics work

One thing I have been leaning on is Copilot's Auto model selection. In theory it is a sensible answer to this problem. Let the tool pick the appropriate model for the task instead of manually reaching for the expensive option every time.

That should be the ideal setup for someone on a budget. Use the premium reasoning only when it is actually needed. Save the stronger models for the problems where they earn their keep.

What I have started noticing, though, is that Auto increasingly seems to favour premium models. In my sessions I keep seeing higher-end choices turning up, particularly Opus and Sonnet or Sol class models. They are good models. That is exactly the problem. The optimisation starts drifting away from "good enough for this task" and towards "best available right now."

When that happens, Auto stops feeling like budget protection and starts feeling like a faster route through the monthly allocation.

## The premium model trap

The best model is not automatically the best choice for the job.

There are plenty of tasks where a cheaper model is probably enough: small refactors, boilerplate, straightforward tests, explaining an existing method, or tightening documentation. I do not need premium reasoning for every edit in a side project. I need something reliable enough to keep the flow moving.

Then there are tasks where paying for a better model makes obvious sense. Difficult debugging, architectural changes, unfamiliar codebases, multi-step agentic work, or anything where several failed attempts would cost me more in time than the model costs in tokens. That is where the expensive model can be a rational trade.

The trap is when the expensive model becomes the default for the cheap-model jobs as well. That is how you end up paying for intelligence you did not actually need.

## Why I may turn the good models off

I am increasingly considering something that sounds faintly ridiculous: removing some premium models from my available choices so I stop burning through my allowance so quickly.

I have paid for access to good models, and I may need to restrict access to those good models to get better value from the subscription. That sounds backwards until you define the actual goal clearly.

My goal is not to touch the most advanced model as often as possible. My goal is to get the most useful development output from a fixed monthly spend.

From that angle, constraining the model pool is perfectly rational. I am not saying the premium models are bad. I am saying they may be too expensive to use casually if I want the subscription to last the month.

## The missing piece is return on investment

This is the part I keep coming back to. I am enjoying coding with agents. They make software development more interesting. They reduce some of the friction around exploration, scaffolding, and repetitive work. They also make certain kinds of experimentation easier to justify because I can get to a first pass more quickly.

But enjoyment is not return on investment.

The question I need to answer is what I am actually getting back for the extra token spend. If an agent saves me an hour, helps me understand an unfamiliar codebase, or gets a tedious piece of work done cleanly, that is easy to defend. If I am running more agents mainly because the demos look impressive, while chewing through substantially more of my subscription, the value is a lot less obvious.

I think this is one of the main adoption questions for agentic coding. The limit is not purely technical. The limit is economic.

## The influencer workflow is not my workflow

I still want to watch the frontier. I want to see what Codex, Copilot, Claude Code, OpenCode, and the rest of this tooling ecosystem are making possible. Those demos are useful because they show the outer edge.

What I should not do is confuse somebody else's AI budget with my own.

If somebody can run 100 agents against a serious codebase with a generous allowance or a company card, great. I will probably learn something from watching it. But the more relevant question for me is smaller and more practical: what is the most useful agent workflow I can run within my budget?

That is a better metric anyway. I am less interested in maximising agents, tokens, and model quality in isolation. I am more interested in useful development output per pound.

## What I am going to try next

I do not want this to end as a complaint about AI coding, because that is not where I have landed. I want to keep using agents. I just want to use them more deliberately.

So the next few experiments are pretty simple. I want to restrict premium models and see how far the subscription goes. I want to use agents more selectively instead of reaching for them by default. I want to run fewer agents in parallel on small personal projects. And I want to pay closer attention to whether a session saved time, improved the result, or enabled something I would probably not have done otherwise.

That should get me closer to a personal sweet spot instead of an aspirational workflow copied from people with different economics.

The open question for me now is where that sweet spot actually sits. How much agentic coding is enough to be genuinely useful, without turning a good development tool into a monthly bill I have to keep explaining to myself?

## References

- [OpenAI Codex](https://openai.com/index/introducing-codex/)
- [GitHub Copilot plans](https://github.com/features/copilot/plans)
- [Setting up my AI Cloud Agent](/2026/08/24/setting-up-my-ai-cloud-agent)
- [June 2026 AI Retrospective](/2026/07/12/june-2026-ai-retrospective)
- [The Orchestration Tax](https://x.com/addyosmani/status/2059844244907696186?s=46)
