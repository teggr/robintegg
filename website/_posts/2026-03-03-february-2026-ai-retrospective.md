---
layout: post
title: "February 2026 AI Retrospective"
date: "2026-03-03"
description: "A personal journey through AI tools, skills, and agent workflows in February 2026."
image: /images/ai-retrospective-feb-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - vscode
  - productivity
---

February 2026 AI Retrospective

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet) — or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

Tools I'm currently using: GitHub Copilot, Gemini, vscode

# Success story for AI authoring

Surprisingly this has taken up a lot of my free time this month!

Read a pretty crap story on Java Danone which used to be the space to go, but now seems to be on the down with ai generated slop or certainly poorly edified content being relaresed. I’ve certainly know unsubscribed. I was so incensed that I used my own knowledge and used AI to write an article about Java Jo on my blog. First time getting three agents working. Discussion and article authorship in Claude, then both ChatGPT and Copilot with Gemini to fact check and help with verification
Glad that the AO was able to help me generate the content I want to share

https://robintegg.com/2026/02/08/java-ui-in-2026-the-complete-guide.html

This got a great feedback from the Reddit community. I even used the AI to help refine my post to set the right tone for the communicate. The response was overwhelmingly positive and we went top post of the month. I was pretty chuffed at that so I then decided to push it even further to create https://awesome-java-ui.com/.

Here I was able to use ai to collate all the feedback and comments from the Reddit thread to compile a full list, then got it to work on creating the website and then crawling the sites for screenshots, code snippets and other content. It worked well on this using a combination of GitHub issues with skills to ensure a consistent approach.

https://github.com/teggr/awesome-java-ui

# Tools

I've seen a couple of tools out in the wild now that look pretty good:

- https://www.granola.ai/ - Capture notes from calls and dictation. What's then pretty awesome is that the ai enhancements that come next once you've got loads of notes - summarization / queries etc.
- https://wisprflow.ai/ - 1 button dictation that both looks accurate and also corrects and edits your sentences on the fly. Looks to work anywhere you can write text + loads of verbal shortcuts.

# Some links worth reading 

I'm trying to avoid too many doom posts :) Here are some positive articles to help with your AI journey.

[My AI Adoption Journey](https://mitchellh.com/writing/my-ai-adoption-journey) - Mitchell talks about his journey with AI. A key takeaway for me was "Step 3: End-of-day Agents" - kicking off agents at the end of the day to do some work overnight. I use the copilot cloud agent for this for implementing a feature that I've been able to flesh out a little. I maybe use the plan agent first then get the cloud agent to implement. If accessing the Internet, don't forget to configure this otherwise you'll only find out about it in the morning!

[The Agent Will Eat Your System of Record](https://x.com/zain_hoda/status/2019049069134417975?s=46) - If your data in system X or Y can be downloaded and reasoned about by ai, why keep the products around?

[Leading an Agentic Development Team)[https://bryanfinster.substack.com/p/leading-an-agentic-development-team?utm_campaign=post&triedRedirect=true] - Some thoughts on scaling up your agent usage

[The AI Vampire](https://steve-yegge.medium.com/the-ai-vampire-eda6e4f07163) - Are you spending too much time with your Agents? I'm certainly starting to feel guilt of this - watch out for burn out.

[Context Engineering for Coding Agents](https://martinfowler.com/articles/exploring-gen-ai/context-engineering-coding-agents.html) - Technical article breaking down how context is used by coding agents.

# Goodbye old friend :(

I've been using IntelliJ now for 10 years after being reluctantly forced to use it over my old trusty - Eclipse. It took me a little while to get used to the new way of doing things, the new shortcuts and double shift to search but over time we gelled and it's been a powerhouse for me since...

Until recently. There's been so much change since I last renewed my personal licence and now that I'm leaning on my coding agents more and more, I need an ecosystem at the cutting edge. Unfortunately I've found that the copilot support within IntelliJ is just far enough behind the other platforms that I'm having to be ruthless and change in order to keep up and leverage the latest features. I've therefore ripped off the bandaid and jumped ship to vscode. I've had a previous attempt at this but it didn't stick but this time may be different. The copilot support is first class and integrated everywhere I need it. The terminal support is first class and is incredibly helpful alongside the deeper integration with the copilot services such as the cloud agents. 

So far so good for now.

I'm using  VSCode with pretty minimal plugins for Java/Spring/Copilot with a nice high contrast Github theme and thanks to AI I've already built a vscode extension for previewing j2Html snippets - https://github.com/teggr/j2html-preview .

# What do I intend to be looking into in March?

- I've got some short posts lined up around multi-repository workflows, copilot clones, agent feedback and a evolving approach to my jira workflow.
- Continue to evolve the jira task workflow, potentially looking to wrap the skills with custom agents. This will give further context to each of the skills and also in vscode, custom agents can hand off to other agents, which provides a nice workflow out of the box.
- I'd like to be looking at reducing some token usage by creating some tooling for generating cli/mcp/skill packs from specs or docs to get to a working integration perhaps quicker than ai can get there