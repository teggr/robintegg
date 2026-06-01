---
layout: post
title: "May 2026 AI Retrospective"
date: "2026-06-01"
description: "Discovery-first workflows, toolkit curation, and where orchestration still breaks for me in May 2026."
image: /images/ai-retrospective-may-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
---

April was good, and May turned out to be where a few ideas actually stuck. I spent less time trying every new tool and more time tightening the parts of my workflow that kept paying back.

The month mostly came down to 3 things: discovery before planning, curating reusable Copilot resources, and pressure-testing orchestration tools against real multi-repo work.

## Discovery before planning is now default

The biggest change in my day-to-day work is that I now treat discovery as a dedicated phase. I used to jump from ticket to plan too quickly, then discover missing context halfway through implementation.

This month I moved to a cleaner sequence: load ticket context, run discovery, write plan, implement. The point is simple: gather facts before choosing a direction.

What worked was the quality of the first plan after discovery. Fewer wrong assumptions, fewer mid-task resets, and less prompt churn.

What still needs work is speed calibration. A deep discovery pass on every task is overkill. I still need a better rule for when to run a quick pass versus a full sweep.

## Curating my own toolkit removed setup drag

The second theme was standardising my reusable resources. I pulled prompts, agents, skills, and instructions into a dedicated toolkit repository and made it installable.

That solved a practical problem I kept hitting: starting a fresh project with the same baseline without manually copying files around. The JBang installer approach is simple and reliable enough to keep using.

The more interesting bit is portability. Local global skills are convenient, but cloud agent runs do not share that setup. Keeping resources in a repo and installing per project closes that gap and gives more consistent behaviour across environments.

The trade-off is maintenance overhead. Once you publish and version a toolkit, you need to curate it like product code. I think the payoff is worth it, but only if I keep the catalog focused.

## Orchestration tools are improving, but repo boundaries still hurt

I spent more time with harness and orchestration tooling in May, mostly with a practical question: can this handle how I actually work, not how a demo workflow looks.

The current generation is genuinely strong at running parallel agent work in one repository, with clean worktree isolation and decent visibility into active tasks.

The boundary is still multi-repo work. A lot of my real tickets cross services, shared libraries, and integration repos. The tooling usually wants one task, one repo. That mismatch creates manual coordination overhead right where these tools are supposed to help.

So my position has not changed much from April: good direction, useful today for bounded tasks, not yet a complete fit for cross-repo workflows.

## Tool evaluations for May

GitHub Copilot remains the core tool in my daily workflow because it is embedded where the work already happens. The custom agent model is useful once scope and intent are clear.

Claude is still my best option for longer-form synthesis and sense checking when I need a second pass on trade-offs.

Gemini is useful in rotation, mostly as a comparative perspective when I want to test whether a conclusion is model-specific.

Granola keeps proving its value in meetings. The transcript-plus-notes model gives me enough structure to reuse decisions later, especially when I feed outcomes back into project context.

## What I wrote in May

Two posts from May capture most of the practical changes:

[A Discovery Agent for Your VSCode Workflow](/2026/05/13/a-discovery-agent-for-your-vscode-workflow), where I documented a dedicated discovery phase before planning and implementation.

[Curating Your Own AI Toolkit Repository](/2026/05/24/curating-your-own-ai-toolkit-repository), where I described the repo + installer approach for reusable Copilot resources.

## Links worth your time

[The Illustrated Deep Research Prompting Guide by Maxime Goubet](https://www.moderndistributed.com/p/the-illustrated-deep-research-prompting): A practical guide on structuring deep research prompts, useful if you are trying to make discovery outputs more dependable.

[How to Build an Agent by Harrison Chase](https://blog.langchain.com/how-to-build-an-agent/): A grounded breakdown of agent architecture choices and where complexity starts to matter.

[Andrej Karpathy's LLM knowledge base gist](https://gist.github.com/karpathy/442a6bf555914893e9891c11519de94f): Still one of the clearest references for persistent knowledge workflows over session-by-session rediscovery.

[Copilot CLI feature comparison](https://docs.github.com/en/copilot/concepts/agents/copilot-cli/comparing-cli-features): Useful for keeping terminology straight across instructions, skills, tools, and agent behaviors.

## What I want to answer in June

I want a practical multi-repo orchestration pattern that does not turn into project management overhead. If I can define one workflow that treats related repositories as one bounded unit of work, that will be the next meaningful step.
