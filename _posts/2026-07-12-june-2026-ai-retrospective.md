---
layout: post
title: "June 2026 AI Retrospective"
date: "2026-07-12"
description: "Claude certification, Copilot cost pressure, and why local-first AI experiments moved up my list in June."
image: /images/ai-retrospective-jun-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - agents
  - java
---

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet), or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

**Tools I'm currently using**: GitHub Copilot, Claude, Gemini, vscode, Java Copilot SDK, [Granola](https://www.granola.ai/), Spring AI

**Tools I'm currently evaluating**: Ollama, OpenRouter, Spring AI

## Certification was useful, but it hasn't changed my daily default

This retrospective is a bit late because I put a lot of focused time into completing my Claude architect foundations certification. I'm very much out of practice with exams but I'm glad I passed it and it's over ... for at least 12 months. 

The certifcation content was useful as it gave me clearer language for prompting patterns, safety boundaries, and evaluation loops. It also introduced some new use case scenarios. However, for my workflow, not much has changed yet. I still default to Copilot in VS Code because the the tooling just sits in a sweet spot for me in terms of the features such as prompts/instructions/skills and the actual results which I find to generally quite accurate and what I expect. This seems to definitely be making me more productive, though I don't have hard numbers to compare nor know what I'm missing by using a different approach. 

## Token costs are forcing me to think about my subscriptions

The pricing changes for Copilot subscriptions from requests to tokens has pushed cost into the foreground for me. Under the previous request model, I felt like I was getting good value out of my Copilot Pro+ subscription with enough tokens to last my personal usage for the whole month. With token-based pricing, that percentage used doesn't creep up, it leaps, even on copilot pro and personally I don't want to pay upwards of £100 on tokens each month, £40 was probably my personal limit.

In reaction to the pricing my first practical direction was to revisit Ollama and local models. I spent time reading setup and model guidance, then testing where local inference is good enough for my own flow (mostly drafting, summarization, and prompt iteration before I jump to higher-cost hosted runs).

```bash
ollama pull qwen3:8b
ollama run qwen3:8b "Summarise this spec into 6 implementation steps with risks"
ollama list
```

What I've found is that my personal laptop is vastly underpowered for running the kind of higher powered models that I've got used to with my Copilot subscription. Some people have invested in the newer macbook pros, which with a top spec and the latests chips look to run local models very well, though you are looking at £5k. That's a hefty upfront payment!

It feels that the middle ground might be a open model provider or a more dynamic provider. Ollama provides a good subscription for hosted open models and I also spent time looking at OpenRouter and self-hosted AI server setups as part of the same cost and control question. I do not have a final architecture yet, but June it clear that I want optionality in where inference runs to reduce costs.

## Building my own path from video to article

Another strong June thread was developing my own Youtube video-to-article workflow. [VideoToBlog](https://videotoblog.com/) remains a strong reference point and I still think it is a very practical product. The limit for me was usage volume.

That is why I continued investing in my own implementation at [From YouTube Videos to 5-Minute Blog Articles with AI](https://robintegg.com/2026/06/12/youtube-videos-to-blog-articles-with-ai). The setup combines Google Cloud Gemini Flash with Spring AI and a transcription service, then turns a long video into a readable article quickly. This is still evolving, but it is already useful enough to support daily reading workflows.

The bigger lesson from this thread is that when a product proves some value, building a constrained clone for your own workflow can be a rational next step if usage limits or pricing get in your way.

## What I wrote in June

Two June posts connect directly to this month's themes:

[Hypermedia APIs for Autonomous Agents](/2026/06/03/hypermedia-apis-for-autonomous-agents), where I explored whether state-aware affordances reduce agent transition errors in multi-step workflows.

[From YouTube Videos to 5-Minute Blog Articles with AI](/2026/06/12/youtube-videos-to-blog-articles-with-ai), where I documented the workflow and constraints behind building my own article generation pipeline.

## Links worth your time

- [Setting up and using Ollama on macOS](https://jonbrown.org/blog/setting-up-and-using-ollama-on-macos/): A practical setup reference for getting a local Ollama flow running quickly.
- [Best local LLMs for Apple Silicon](https://apxml.com/posts/best-local-llms-apple-silicon-mac): Useful when narrowing model choices for spec drafting and summarization workloads.
- [OpenRouter](https://openrouter.ai/): Worth looking at if you want routing flexibility and easier model cost comparison.
- [I built my own AI server](https://remus-software.org/articles/i-built-my-own-ai-server/): A grounded write-up on self-hosting and control trade-offs.
- [Loop engineering write-up](https://x.com/addyosmani/status/2064127981161959567?s=46): Helpful framing for tightening feedback loops in agent workflows.
- [Risk ladder for auto-approving PRs](https://developers.podcast.go-aws.com/web/episodes/209/index.html): Good practical discussion of staged trust when introducing automation into review pipelines.

## What I want to answer in July

The next focus writing up some of my thoughts and practical usaga of knowledge bases, how I'm using a focussed knwoledge base to drive a user agent capable of navigating an application for me a as a developer.

Auto approval of PRs is high on the agenda of things to tackle in my day to day workflows and after that, I intend to trial a PR babysitting agent, suggested to me that takes a PR and handles feedback as part of a process.