---
layout: post
title: "From YouTube Videos to 5-Minute Blog Articles with AI"
date: "2026-06-12"
description: "A workflow that turns any YouTube URL into a 5-minute readable article using AI, with commentary on the tools and techniques along the way."
image: /images/youtube-to-article.jpg
tags:
  - ai
  - workflow
  - youtube
  - github copilot
  - productivity
---

I wanted a practical way to consume long YouTube content faster. I like watching conference talks like [Spring IO](https://www.youtube.com/@SpringIOConference/videos) and long AI chats like [How I AI](https://www.youtube.com/@howiaipodcast/videos), but keeping up with all the content is not realistic when each video is 60 minutes or more. So I built a workflow that turns a YouTube URL into a readable article I can scan in about 5 minutes and I'd like to share some of the AI interactions I had along the way to demonstrate some tools, techniques and services that I use.

## AI Interaction 1: Starting with Google AI Chat

My first move was trying the Google Search AI chat feature to start for exploring options. The feature feels very much like the normal google search and I feel more confident in using this interface for web research. In the same way I felt google gave me good search results, it feels like the ai response now matches that search result but has more accurately dug in an got out the information that I need, whilst still providing the web page references for deeper reading. It also provides a follow prompt for follow up questions.

![google ai search](/images/can-I-read-an-article-for-an-individual-youtube-video-Google-Searcch.png).

That exploration led me to [VideoToBlog](https://videotoblog.com), which was very close to what I wanted.

![video to blog](/images/VideoToBlog-Convert-Videos-to-Blog-Post.png)

VideoToBlog gave me a strong baseline for output quality. The generated articles were solid, and that helped me define the bar for my own tool.

The issue was usage limits for my own volume, being a free tool I quick hit the limits. I also looked at alternatives, but the pricing felt high for how I wanted to use it.

## AI Interaction 2: Scaffolding the App with Copilot

At that point I decided to build my own version: [articulate](https://github.com/teggr/articulate).

I used Copilot Cloud agent to scaffold the initial app and then iterated on the core flow. Here I was able to provide copilot the base url of the application, a screenshot of a convert article and a guide of using Spring Boot 4 and Spring AI. Copilot then pretty much one shotted the first version and using the Cloud Agent is a good approach to use when you have confidence that the agent will do a good job and you don't want to baby sit the agent process. We also include links to the latest Spring docs and told it to use the Google Gemini Flash model.

The initial implementation was pretty accurate and succintly added transcript downloading and article generation.

![articulate form](/images/Articulate-form.png)

## AI Interaction 3: Handling Transcript Retrieval Constraints

So far so, good. However, upon testing the code it became pretty apparent that youtube does not make it easy to get hold of the video transcripts in an automated fashion. Here copilot was pretty good at looping through a number of workarounds and suggestions, but transcript retrieval from YouTube became the main blocker because of IP-based access issues.

Copilot could not bypass the blocking issue directly, but it helped map realistic options. We reviewed third-party transcript APIs and compared costs based on expected usage.

That process surfaced [Supadata](https://supadata.ai/) as a good fit for this project.

## AI Interaction 4: Discovery-Style Prompting for Integration

Here, we perfectly blended a good architecture and ai. For the Supadata integration, I used a clear prompt structure with my discovery workflow: here is the documentation, here is the interface to implement, now plan it and build it.

That made the handoff to AI much cleaner. I got faster iterations because the interface contract and source documentation were explicit from the start.

## AI Interaction 5: Article Generation with Gemini Flash

Once transcript ingestion was stable, I used AI again for article generation itself. I run this through my Google Cloud account, using Gemini Flash because it is low cost for this kind of transformation work.

## AI Interaction 6: Prompt Refinement with Side-by-Side Evaluation

The output was decent early on, then improved as I tightened prompts around structure, tone, and constraints.

I compared my generated output against VideoToBlog and iterated with Copilot in short rounds. That gave me a practical feedback loop: evaluate, adjust prompt, regenerate, compare again.

After a few rounds, the quality was where I wanted it for daily use.

## What Changed in Practice

Now I can grab a YouTube URL from a script or an Apple Shortcut and generate an article quickly. The result is not a replacement for video when delivery style matters, but it is a better default for information-heavy content.

For me, this turns a 60-minute watch into a 5-minute read and makes back catalogs much more manageable.

![articulate article](/images/Articulate-article.png)

## References

- [articulate](https://github.com/teggr/articulate)
- [VideoToBlog](https://videotoblog.com)
- [Supadata](https://supadata.ai)
