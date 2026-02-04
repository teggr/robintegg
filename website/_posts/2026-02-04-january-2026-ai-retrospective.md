---
layout: post
title: "January 2026 AI Retrospective"
date: "2026-02-04"
description: "A personal journey through AI tools, skills, and agent workflows in January 2026."
image: /images/ai-retrospective-jan-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - claude
  - productivity
---

2026 is a big year for all of us in the tech sector with AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once—or perhaps a marker for at least one person on this journey to follow.

**Tools I've been using:** GitHub Copilot, Gemini, and Claude

## Professionally: Beyond Just Coding

My initial focus for January was trying to build on my usage of prompting with plan and agent mode. I wanted to push beyond just the coding tasks and make the tools work in more areas than just writing code.

### The JIRA ticket workflow experiment

My thoughts were to look at a toy-ideal flow of refining JIRA tickets so that they could be picked up and implemented in some kind of Ralph-type loop. At the moment there's very much a mental translation going on between the text in a ticket and me either writing code or prompting.

The plan mode in Copilot can help in this regard, however the current experience is pretty janky when using a chat window. Way too much text—I'll later appreciate that aspect of Claude later in the month. I feel like the UX can be better and I may explore this more. Will lay some foundations for this in February.

### Diving into GitHub Copilot skills

Started looking at skills. This led me down a rabbit hole of trying to understand the implementation. There's a lot of regurgitating marketing spiel online, so it's a good challenge to seek out more technical details.

I've been experimenting with using the `jira-cli` to download tasks. My trials with just the Atlassian MCP have not been great—too many requests, not the right information, no access to some custom fields. In all honesty this was before skills, so perhaps this could be revisited and used in the skill much like other integrations.

The CLI downloads the raw info into a ticket folder, then I engage a new CLI with an agent to specifically assess the ticket for implementation readiness. Could a junior dev work through the instructions? Usually this is no, so we move to plan agent to build the instructions. These audits and instructions are also saved to the bucket. Once we've passed audit, then we can implement. This was mainly implemented with Copilot CLI.

What I'm left struggling with is that I just don't like CLI interfaces. I want a richer interactive space that expresses what I want to see rather than the lowest common denominator of text and thinking.

## On My Personal Projects

### Website metadata audit

I used Claude to read my website and RSS feed metadata and audit the metadata for LinkedIn, Twitter, Bluesky, and Mastodon. It gave me ratings and a list of items to improve. I turned this into a prompt, asked Copilot to convert it into issues, then assigned them to Copilot to get started on.

The combination worked well—Claude for analysis and recommendations, Copilot for implementation.

### Writing about architecture and tooling

I wrote about [self-contained systems](/2026/01/07/self-contained-systems-for-ai-coding-agents) and also [building custom tooling](/2026/01/21/leveraging-ai-to-build-custom-tooling), though I am wondering if I'm focused too much on building better hammers, when perhaps I should be looking at building X? But as I say, start small—start today.

### Authoring the JBang skills post

I [authored a JBang skills post](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang). Started with writing a JBang skill experiment and then asked Copilot to firstly create the details of what I had done, then in a separate context, turn that into a post.

### Other experiments

Started listening to [How AI](https://www.howaipod.com/). Have started reading [Vibe Coding](https://itrevolution.com/product/vibe-coding/) by Gene Kim and Steve Yegge.

Created a GitHub clone using Spring AI to help explore and share understanding with my work colleagues. It's been a useful exercise in understanding the capabilities and limitations of different AI models when building production-like systems.

## Wrap-Up Thoughts

Enjoying using Claude (free) with offline changes to Copilot (paid). Perhaps trial a Claude subscription?

Still not on a path to multiple local agents. Prefer the revised agent tab on the GitHub repo. Getting more flow at scoping changes, but again the GitHub experience is poor in the PR review section. Too much clicking and can't see the context enough. Eclipse used to have a special view that only showed what you'd actually worked on. Perhaps that might work in IntelliJ?

OpenClaw is getting a lot of attention. Feels like I could write something similar using Spring AI. Not sure what I'd use it for right now, but it's an interesting architecture to study.

## Looking Ahead to February

The foundations are laid for some interesting experiments. I want to:

- Explore better UX patterns for AI agent interactions beyond CLI and chat windows
- Revisit the Atlassian MCP integration with skills
- Continue the JIRA ticket workflow refinement
- Balance building better tools with actually using AI to build products

The journey continues. One step at a time, one experiment at a time.

## References

- [How AI Podcast](https://www.howaipod.com/)
- [Vibe Coding by Gene Kim and Steve Yegge](https://itrevolution.com/product/vibe-coding/)
- [Self-Contained Systems for AI Coding Agents](/2026/01/07/self-contained-systems-for-ai-coding-agents)
- [Leveraging AI to Build Your Own Custom Tooling](/2026/01/21/leveraging-ai-to-build-custom-tooling)
- [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang)
