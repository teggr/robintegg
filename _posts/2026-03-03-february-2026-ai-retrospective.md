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

2026 is going to be a big year for all of us in the tech sector because of AI, so I'm going to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once (yet) — or perhaps for those just behind, there's a trail of bread crumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there's not a lot of practical "this is how I use AI in my day-to-day work" type content out there for whatever reason. I'll share what I've been experimenting with, what has worked, what hasn't, and what I'm planning to try next.

**Tools I'm currently using**: GitHub Copilot, Gemini, vscode

# A personal success story for AI generated content

## Part 1 - The article

I recently read an article on DZone about "modern Java GUI frameworks" that was… pretty disappointing. It referenced libraries that are long archived, mixed in things that aren’t UI frameworks at all (Hibernate and Spring??), and generally felt like something written years ago and never revisited.

That wasted half an hour was enough motivation for me to write something I actually wish had existed: an up-to-date overview of the UI options available to Java developers right now, in 2026.

I knew that I probably would only have a certain window of opportunity in which I'd have the energy to put it together so I roped in some ai agents.

My first port of call was Claude, I find the tone of Claude generally the most agreeable when trying to discuss ideas. Within my free limit I was able to quickly plan out the structure of an article that would provide the template for the details.

I find progressing from an empty page to a structure the hardest part of creating any content. Ai really helps me here and I'm happy that the end result is my ideas that have been translated to markdown.

Once the structure was confirmed, in a separate prompt I gave Claude a long list of all the UI projects that I knew about and set it to work to suggest any more that perhaps I wasn't aware of.

Armed with a structure and project list I created a prompt for copilot cloud agent to go create the entries for of all the projects, capture screenshots and double check the fine details. The double checking would later be triple checked to avoid mistakes which would definitely affect the trust of anyone reading the article. This did take a couple of rounds of back and forth as we agreed what it should be capturing, summarising etc. In hindsight, perhaps more of this detail could have been provided up front, but the sight of progress meant I was enagaged and now happy to see this through.

Once copilot was finished, I then went for another round of validation using both ChatGPT and Claude to review the entire article, prompting them both to double down on correctness.

The result was Java UI in 2026 - The Complete Guide: 

[https://robintegg.com/2026/02/08/java-ui-in-2026-the-complete-guide](https://robintegg.com/2026/02/08/java-ui-in-2026-the-complete-guide)

As I was pretty happy with the outcome I then discussed with ChatGPT how I might share this article on the socials as I'd read some recent articles suggesting that each platform usually needs a unique post. This really helped with engaging with the various communities, some of which, like Reddit I'd never posted content before. Quite daunting.

However, I got some overwhelmingly positive feedback across all platforms on the article. It feels like it really resonated with other, who much like myself really wanted a quality article on the matter.

Here's the stats.

![robin tegg february stats]({{site.baseurl}}/images/robintegg-feb-stats.png)

## Part 2 - The article becomes a website

I was pretty chuffed at the response to the article  so I then decided to push it even further to create [https://awesome-java-ui.com/](https://awesome-java-ui.com/).

![awesome java ui]({{site.baseurl}}/images/awesome-java-ui.png)

Here I was able to use AI to collate all the feedback and comments from the Reddit thread to compile a full list, then got it to work on creating the website and then crawling the sites for more screenshots, code snippets and other content. 

![awesome java ui feb stats]({{site.baseurl}}/images/awesome-java-ui-feb-stats.png)

I now have it working pretty autonomously using GitHub issue templates with skills to ensure a consistent approach. I review the output, but it does the work.

Surprisingly this has taken up a lot of my free time this month but my hands are now free for the next mission!

Here's the reddit posts

![reddit posts]({{site.baseurl}}/images/reddit-posts.png)

# Tools

I've seen a couple of tools out in the wild now that look pretty good:

- [https://www.granola.ai/](https://www.granola.ai/) - Capture notes from calls and dictation. What's then pretty awesome is that the AI enhancements that come next once you've got loads of notes - summarization / queries etc.
- [https://wisprflow.ai/](https://wisprflow.ai/) - 1 button dictation that both looks accurate and also corrects and edits your sentences on the fly. Looks to work anywhere you can write text + loads of verbal shortcuts.

# Some links worth reading 

I'm trying to avoid too many doom posts :) Here are some positive articles to help with your AI journey.

[My AI Adoption Journey](https://mitchellh.com/writing/my-ai-adoption-journey) - Mitchell talks about his journey with AI. A key takeaway for me was "Step 3: End-of-day Agents" - kicking off agents at the end of the day to do some work overnight. I use the copilot cloud agent for this for implementing a feature that I've been able to flesh out a little. I maybe use the plan agent first then get the cloud agent to implement. If accessing the Internet, don't forget to configure this otherwise you'll only find out about it in the morning!

[The Agent Will Eat Your System of Record](https://x.com/zain_hoda/status/2019049069134417975?s=46) - If your data in system X or Y can be downloaded and reasoned about by AI, why keep the products around?

[Leading an Agentic Development Team](https://bryanfinster.substack.com/p/leading-an-agentic-development-team?utm_campaign=post&triedRedirect=true) - Some thoughts on scaling up your agent usage

[The AI Vampire](https://steve-yegge.medium.com/the-ai-vampire-eda6e4f07163) - Are you spending too much time with your Agents? I'm certainly starting to feel guilt of this - watch out for burn out.

[Context Engineering for Coding Agents](https://martinfowler.com/articles/exploring-gen-ai/context-engineering-coding-agents.html) - Technical article breaking down how context is used by coding agents.

# Goodbye old friend :(

I've been using IntelliJ now for 10 years after being reluctantly forced to use it over my old trusty - Eclipse. It took me a little while to get used to the new way of doing things, the new shortcuts and double shift to search but over time we gelled and it's been a powerhouse for me since...

Until recently. There's been so much change since I last renewed my personal license and now that I'm leaning on my coding agents more and more, I need an ecosystem at the cutting edge. Unfortunately I've found that the copilot support within IntelliJ is just far enough behind the other platforms that I'm having to be ruthless and change in order to keep up and leverage the latest features. I've therefore ripped off the band-aid and jumped ship to vscode. I've had a previous attempt at this but it didn't stick but this time may be different. The copilot support is first class and integrated everywhere I need it. The terminal support is first class and is incredibly helpful alongside the deeper integration with the copilot services such as the cloud agents. 

So far so good for now.

I'm using  VSCode with pretty minimal plugins for Java/Spring/Copilot with a nice high contrast GitHub theme and thanks to AI I've already built a vscode extension for previewing j2Html snippets

[https://github.com/teggr/j2html-preview](https://github.com/teggr/j2html-preview)

# What do I intend to be looking into in March?

- I've got some short posts lined up around multi-repository workflows, copilot clones, agent feedback and an evolving approach to my jira workflow.
- Continue to evolve the jira task workflow, potentially looking to wrap the skills with custom agents. This will give further context to each of the skills and also in vscode, custom agents can hand off to other agents, which provides a nice workflow out of the box.
- I'd like to be looking at reducing some token usage by creating some tooling for generating cli/mcp/skill packs from specs or docs to get to a working integration perhaps quicker than AI can get there