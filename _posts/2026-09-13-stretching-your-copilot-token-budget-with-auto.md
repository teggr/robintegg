---
layout: post
title: "Stretching Your Copilot Token Budget With Auto"
date: "2026-09-13"
description: "Copilot Auto can help stretch a personal AI budget, but only if you watch how often it reaches for premium models and tune the model options available in VS Code."
image: /images/ai-retrospective-jun-2026.jpg
tags:
  - ai
  - github copilot
  - vscode
  - workflow
---

Copilot Auto looks like exactly the right feature for anyone trying to make a personal AI budget go further.

The pitch makes sense. Let Copilot decide which model is appropriate for the task instead of manually picking the most expensive option every time. According to the GitHub and VS Code documentation, Auto uses task complexity and model availability to route each request to an appropriate model, and there is even a 10% discount on model costs when you use it on a paid plan.

That should be a good deal for people like me. I do not want to manually micro-manage every prompt. I want the cheap tasks to stay cheap and the harder tasks to get the stronger models when they actually need them.

## Where Auto starts to feel expensive

The problem is not the idea. The problem is the practical result.

In my sessions I keep seeing Auto land on premium models more often than I would like. Opus. Sonnet. Sol class models. They are good models, and that is part of the problem. Once the stronger model is always available, it becomes very easy for the system to drift from "good enough for this task" toward "best available right now."

That might still be the right decision in pure quality terms. It is not always the right decision if the actual goal is to make a fixed monthly subscription last.

## Premium models are not wrong, just expensive

I do not think the answer is "never use premium models." Some tasks clearly justify them. Hard debugging, architectural changes, unfamiliar codebases, or multi-step agentic work can all be worth paying for if they save me enough time or failed attempts.

The issue is using that level of reasoning for work that probably does not need it. Small refactors, basic documentation, boilerplate, simple tests, and lightweight explanation work are often good enough on a cheaper model. If Auto regularly burns premium usage on those kinds of tasks, then it stops behaving like budget optimisation.

## What I am changing in VS Code

The first practical step is simply to pay attention to what Auto is doing. VS Code lets you hover over a response to see which model handled it. That sounds minor, but it gives you a way to stop treating Auto as a black box and start noticing patterns in your own usage.

The second step is to be more deliberate with the model picker. If I know I am doing simple editing or lightweight drafting, I can choose a cheaper model manually instead of leaving Auto to decide. That does not mean giving up Auto completely. It just means not pretending every task is equally hard.

The third step is probably the most useful one: open the model picker, choose **Manage Language Models**, and hide the models I do not want to reach for casually. VS Code's Language Models editor lets you control which models appear in the picker at all. That means I can keep the premium models available in principle, but remove some of the temptation and reduce the chance of using them by reflex.

Pinning a small set of preferred models also helps. If the top of the picker only shows the two or three models I actually want in regular rotation, the workflow becomes simpler and cheaper.

## What I am watching in Copilot settings

There is also a GitHub Copilot side to this. GitHub's model docs make it clear that plan type, model availability, and policy settings all affect what Auto can choose from. Individual users can disable evaluation models, and organization administrators can control model access more broadly.

For my own usage, the practical takeaway is that the available model list matters almost as much as Auto itself. If the model pool is full of expensive options, Auto has more chances to make an expensive choice. If I keep the available options tighter, I am shaping the economics before the routing decision even happens.

One other detail is worth remembering: changing the chat model does not change the model used for inline suggestions. That matters because "Copilot cost" is not one single knob. Different parts of the tool behave differently.

## Thinking effort matters too

Another quiet budget leak is reasoning effort. VS Code lets you raise or lower thinking effort on supported reasoning models directly from the model picker. More thinking can be useful, but it also means more thinking tokens. I do not need high effort for every chat about a config file or a minor content edit.

That is another reason I am leaning toward a smaller, more intentional model setup. A narrower toolset is often easier to use well than a bigger one with more ways to overspend.

## The goal is not to win the picker

I have paid for access to good models, but that does not mean I should optimise for using the best model as often as possible. The goal is to get useful work done without turning a monthly subscription into a constant negotiation with myself.

So my current experiment is simple. Keep Auto available. Watch which models it chooses. Hide the premium options I do not want to use casually. Manually choose a cheaper model for simpler work. Lower thinking effort unless the task genuinely needs it.

That feels like a more realistic way to stretch token usage than copying a workflow from somebody whose budget constraints are completely different.

## References

- [About Copilot auto model selection](https://docs.github.com/en/copilot/concepts/models/auto-model-selection)
- [Models in GitHub Copilot](https://docs.github.com/en/copilot/concepts/models/overview)
- [Changing the AI model for GitHub Copilot Chat](https://docs.github.com/en/copilot/how-tos/use-ai-models/change-the-chat-model)
- [AI language models in VS Code](https://code.visualstudio.com/docs/agent-customization/language-models)
- [The AI Agent Reality Check: I Can't Afford to Code Like a YouTuber](/2026/09/12/ai-agent-reality-check-cant-afford-to-code-like-a-youtuber)
