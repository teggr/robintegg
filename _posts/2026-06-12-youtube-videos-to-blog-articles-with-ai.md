---
layout: post
title: "From YouTube Videos to 5-Minute Blog Articles with AI"
date: "2026-06-12"
image: /images/custom-tooling.jpg
tags:
  - ai
  - workflow
  - youtube
  - github copilot
  - productivity
---

I wanted a practical way to consume long YouTube content faster. I like watching conference talks and long AI chats, but keeping up with both is not realistic when each video is 60 minutes or more. So I built a workflow that turns a YouTube URL into a readable article I can scan in about 5 minutes.

## AI Interaction 1: Starting with Google AI Chat

My first move was trying the new Google AI chat feature to see if it could summarize videos directly. It was a useful start for exploring options, but I needed something repeatable and scriptable, not a one-off chat flow.

That exploration led me to [VideoToBlog](https://videotoblog.com), which was very close to what I wanted.

## AI Interaction 2: Validating the Existing Service

VideoToBlog gave me a strong baseline for output quality. The generated articles were solid, and that helped me define the bar for my own tool.

The issue was usage limits for my own volume. I also looked at alternatives, but the pricing felt high for how I wanted to use it.

## AI Interaction 3: Scaffolding the App with Copilot

At that point I decided to build my own version: [articulate](https://github.com/teggr/articulate).

I used Copilot to scaffold the initial app and then iterated on the core flow. The early implementation was quick, but transcript retrieval from YouTube became the main blocker because of IP-based access issues.

## AI Interaction 4: Handling Transcript Retrieval Constraints

Copilot could not bypass the blocking issue directly, but it helped map realistic options. We reviewed third-party transcript APIs and compared costs based on expected usage.

That process surfaced Supadata as a good fit for this project.

## AI Interaction 5: Discovery-Style Prompting for Integration

For the Supadata integration, I used a clear prompt structure with my discovery workflow: here is the documentation, here is the interface I need, now plan it, implement it, and finish it.

That made the handoff to AI much cleaner. I got faster iterations because the interface contract and source documentation were explicit from the start.

## AI Interaction 6: Article Generation with Gemini Flash

Once transcript ingestion was stable, I used AI again for article generation itself. I run this through my Google Cloud account, using Gemini Flash because it is low cost for this kind of transformation work.

The output was decent early on, then improved as I tightened prompts around structure, tone, and constraints.

## AI Interaction 7: Prompt Refinement with Side-by-Side Evaluation

I compared my generated output against VideoToBlog and iterated with Copilot in short rounds. That gave me a practical feedback loop: evaluate, adjust prompt, regenerate, compare again.

After a few rounds, the quality was where I wanted it for daily use.

## What Changed in Practice

Now I can grab a YouTube URL from a script or an Apple Shortcut and generate an article quickly. The result is not a replacement for video when delivery style matters, but it is a better default for information-heavy content.

For me, this turns a 60-minute watch into a 5-minute read and makes back catalogs much more manageable.

## References

- [articulate](https://github.com/teggr/articulate)
- [VideoToBlog](https://videotoblog.com)
- [Supadata](https://supadata.ai)
