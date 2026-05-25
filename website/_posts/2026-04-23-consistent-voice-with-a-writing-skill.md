---
layout: post
title: "Why a Consistent Voice Matters (and How a Writing Skill Helps)"
date: "2026-04-23"
description: "A short note on why voice consistency matters as content output grows, and how a writing skill plus an agent-driven quiz loop keeps posts aligned."
tags:
  - ai
  - writing
  - github copilot
  - skills
  - workflow
image: /images/voice-skill.jpg
---

As a Developer spending so much time in code, it's quite a context shift when going to long form writing. One of the top reasons for me to start writing on my website was to keep practicing. One area in which AI has been really helpful for me is in the authoring and editing of content.

However, as I have started producing more articles, I noticed how quickly posts can start to feel inconsistent and ai-generated. emdashes, long lists, emojis - i'm sure you've noticed these all before. As I'm keen to ensure that the articles reflect my ideas and my voice, this has led to longer editing and rewriting sessions, in turn reducing the overall benefit of using AI in the first place. This article describes one approach that I'm taking to address the issue and maximise the value I'm getting from those precious tokens.

If you are in the same boat, then try out this [inside-lago-voice-skill writing template](https://github.com/getlago/inside-lago-voice-skill). It's a template that you can use to generate a custom skill that understands your voice and is able to apply it to any content that you write. I'm using to write posts but you could make it a global skill and apply it to multiple formats.

The writing skill helps because it turns "style" from a vague intention into explicit rules. Instead of hoping I remember all the voice choices each time, I have a reusable guide that the agent can apply every time I draft or edit a post. The key part is that it enforces practical patterns: lead with substance, remove filler, keep claims grounded, and explain limits.

It also gave me a nice composable benefit. I split my previous instruction setup into two parts: one file focused on post structure and publishing conventions, and one skill focused on voice. That modularity makes both easier to maintain, and easier to evolve independently.

The implementation was straightforward. I used the agent to inspect existing articles and identify recurring patterns in tone, structure, and phrasing. I then added a quiz-style pass where the agent asks me to confirm or reject specific voice rules. That "quiz me" step was useful because it forced me to make choices that were previously implicit.

The result is not perfect uniformity, and I do not want that anyway. The point is consistency of intent. Posts can still vary in topic and depth, but the reader experience stays coherent.

If you are publishing more with AI support, do this early. Defining voice after dozens of posts is possible, but harder. Defining it now makes each new draft easier to shape.

## References

- [inside-lago-voice-skill writing template](https://github.com/getlago/inside-lago-voice-skill)
- [GitHub Copilot skills](https://docs.github.com/en/copilot/concepts/agents/about-agent-skills)
- [Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills)
