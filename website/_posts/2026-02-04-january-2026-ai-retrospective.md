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

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet) — or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next. 

**Tools I'm currently using:** GitHub Copilot, Gemini, and Claude

## Beyond Just Coding

My initial focus for January was trying to branch out beyond the local plan / agent modes. I wanted to push beyond just the coding tasks and make the tools work in more areas than just writing code.

I'm pretty relaxed about the code being produced by the AI right now, given some good custom instructions and a decent code layout. Less surprises the better with the existing code does appear to help the quality of the code generated. Thank goodness for all those boring three layered and hexagonal architecture patterns.

## Managing tasks

One thought I had was to look into how I manage my day-to-day tasks. My own "context" I guess. Work out what ticket is next, read it, determine if I have enough to go on with, then start implementing.

Where this took me was to start organising my work around the idea of a **task**, originating from a JIRA ticket but gathering artefacts as it goes through the process - a bucket of information that can be referenced. This would be more than a chat context window and more persistent. So I started with a folder named after a JIRA ticket. For example `rt-1234`. Inside this folder would live all the resources I would need related to that work. Could be those random `IMPLEMENTATION.md` files that the models spit out or the entire repository (potentially all this could/should live in the repo but i'd need wider buy in for that). Then each chat I had I'd ask (eventually put this in my custom instructions) to put the output in a markdown file in that folder. I could then easily reference the file in my next session.

Keeping the files in a folder, does mean that I can also easily reference or search them in the future. This makes it helpful to build up a library of work that I can refer back to in future prompts. Perhaps this could be a set of shared resources between team members, constantly building up the knowledge of changes beyound what we see in raw PRs or just JIRA tickets.

```shell
/rt-1234
  /PLAN.md
  /IMPLEMENTATION.md
  /audit.md
  /repo
```

This process in itself is more evolutionary compared to my usage of the plan / implement cycle. But it did then allow me to start moving to the next level of thinking about how i can have multiple agents not working on the codebase, but the task.

### JIRA CLI

Let me just pause for a moment to highlight the [JIRA CLI](https://github.com/ankitpokhrel/jira-cli) tool. This command line took is very fast to get working and to start extracting data from JIRA. Well worth a look.

```shell
$ jira issue view ISSUE-1 [--raw|--plain]
```

### JIRA Skill

Given the access to JIRA data through the CLI I could now download the details of a ticket in the task folder so that I had a local copy to reason about with copilot. The process of downloading the jira ticket details with all the details required me to download both the plain and raw versions to ensure I had all the information such as custom fields.

Given the steps of getting a named jira issue and downloading to a specific folder, this felt like a good candidate for a skill, which I created and can now pretty much just use the command `/jira-fetcher ISSUE-1` to download the details of a ticket into the task folder. Copilot has recently added support for skills - https://docs.github.com/en/copilot/concepts/agents/about-agent-skills.

Want to understand how skills work - Def take a look at this article I found on the topic - https://leehanchung.github.io/blogs/2025/10/26/claude-skills-deep-dive/

### Task Analysis Skill

The next step was to get Copilot to analyse the ticket and determine if there was enough information to proceed to generate a PRD document for an implementing agent to get started on. This set of steps has also been encoded in a Skill. This skill outputs two documents (potentially I could break this skill down further into two) - an analysis document and a PRD document.

The analysis document contains an assessment of the ticket and pulls in all the relevant information from the repo. The purpose of this first stage is to be able to generate a set of instructions that could be passed to a junior developer to implement autonomously. Therefore requiring no further guidance. SHould the jira ticket not contain enough information to derive the solution the proces stops and more information is required in the ticket. This is a potential opportunity for the AI to do reasearch in a wider context but it doesn't do that at the moment.

Why PRD? I have started to explore the PRD output format as this is a common format being used by implementors of the 'Ralph Wiggum' approach to executing agents. Although I am not fully sold on this approach yet, it does give a good structure to work towards. Note that I've found a single cloud coding agent session on copilot has sufficiently implemented the PRDs I have generated so far to a reasonable degree of quality. I've not needed the looping aspect yet.

Once the PRD is generated, it can be reviewed and executing. I'm finding it difficult, much like a PR to determine if it's fit for purpose at the moment so I will need to work on prompting for a better visualisation of the output. Execution is no more than asking Copilot to implement the PRD in the context of the task folder and also to provide an implementation summary at the end.

## On My Personal Projects

Having seen plenty of people using Claude and feeling like I have a better understanding of the fundementals now, I wanted to try it out on my personal projects to see how it could help me.

### Website metadata audit

I used Claude to read my website and RSS feed metadata and audit the metadata for LinkedIn, Twitter, Bluesky, and Mastodon. It gave me ratings and a list of items to improve. I turned this into a prompt, asked Copilot to convert it into issues, then assigned them to Copilot to get started on.

The combination worked well with Claude for analysis and recommendations, Copilot for implementation. This works for me as I can use the free plan of Claude for analysis and planning. Generating the prompts for Copilot to implement via the cloud agents.

### Writing about architecture and tooling

I wrote about [self-contained systems](/2026/01/07/self-contained-systems-for-ai-coding-agents) and also [building custom tooling](/2026/01/21/leveraging-ai-to-build-custom-tooling), though I am wondering if I'm focused too much on building better hammers, when perhaps I should be looking at building bigger or high order systems.

### Authoring the JBang skills post

I [authored a JBang skills post](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang). Started with writing a JBang skill experiment and then asked Copilot to firstly create the details of what I had done, then in a separate context, turn that into a post.

### Other experiments

Started listening to [How AI](https://www.youtube.com/@howiaipodcast) - this is a great resource for seeing how other people are building AI into their workflows - we need more of this.

Have also started reading [Vibe Coding](https://www.amazon.co.uk/Vibe-Coding-Building-Production-Grade-Software/dp/1966280025) by Gene Kim and Steve Yegge - this I'm hoping will give me some more ideas and thinking around scaling my AI efforts.

## Wrap-Up Thoughts

I'm enjoying using Claude (free) and passing prompts to Copilot (paid). Perhaps trial a Claude subscription?

Still not on a path to multiple local agents. Prefer the revised agent tab on the GitHub repo. Getting more flow at scoping changes, but again the GitHub experience is poor in the PR review section. Too much clicking and can't see the context enough. Eclipse used to have a special view that only showed what you'd actually worked on. Perhaps that might work in IntelliJ?

OpenClaw is getting a lot of attention. Feels like I could write something similar using Spring AI. Not sure what I'd use it for right now, but it's an interesting architecture to study.

What I'm left struggling with is that I just don't like CLI interfaces. I want a richer interactive space that expresses what I want to see rather than the lowest common denominator of text and thinking.

The journey continues. One step at a time, one experiment at a time.

## References

- [How AI Podcast](https://www.youtube.com/@howiaipodcast)
- [Vibe Coding by Gene Kim and Steve Yegge](https://www.amazon.co.uk/Vibe-Coding-Building-Production-Grade-Software/dp/1966280025)
- [Self-Contained Systems for AI Coding Agents](/2026/01/07/self-contained-systems-for-ai-coding-agents)
- [Leveraging AI to Build Your Own Custom Tooling](/2026/01/21/leveraging-ai-to-build-custom-tooling)
- [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang)
