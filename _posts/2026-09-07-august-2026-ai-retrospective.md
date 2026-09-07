---
layout: post
title: "August 2026 AI Retrospective"
date: "2026-09-07"
description: "Agent Skills, background agents, and feedback loops took the centre of my July AI work."
image: /images/ai-retrospective-aug-2026.jpg
tags:
  - ai
  - retrospective
  - github copilot
  - cloud agents
  - skills
---

We're now over halfway through 2026, we're still here building software and I'm continuing to journal some of my personal journey. Hopefully this might provide some confidence that not everyone is managing hundreds of agents at once yet, or perhaps for those just behind, there is a trail of breadcrumbs to follow. This is not meant to be a comprehensive review of all the tools and techniques out there, but rather a personal retrospective.

It feels to me that there is still not a lot of practical content about how people actually use AI in their day-to-day work. I have spent most of August experimenting with agent skills, cloud/background agents, and the trade-offs of local versus cloud AI tooling to find what actually fits into my day-to-day workflow.

## Pausing on local models for now

I’ve been trying to get a local model installed on my laptop that is good enough for coding. What I found was that my machine is pretty under powered to either run a powerful enough model or have quick enough latency to match any of the cloud providers, so have decided to stop wasting time on that for now. There’s plenty more fun stuff to build and I’ll likely spread my costs across a few model providers. Copilot supports BYOK now across its platform so could add an OpenCode Go subscription to my current Copilot Pro+ plan. I feel like I don’t want to be spending more right now.

[Local models won’t win](https://seangoedecke.com/local-models-will-not-win/): a useful reminder that chasing local inference can become a rabbit hole unless you are willing to spend real time optimising for weaker hardware. A similar point was made in [this thread by Thomass](https://x.com/thdxr/status/2086599224674681242?s=61): the trade-off between local control and cloud speed can quickly become a tooling tax rather than a productivity win.

[Local model tool for choosing the right model for a spec](https://x.com/akshay_pachaar/status/2094765529231929361?s=61): a neat example of using a local model as a routing tool rather than as the default coding engine.

## Using agent skills

At work and at home, I’m starting to use agent skills more. I’m mainly focusing on producing skills that either help with a workflow process or provide context to the agent.

Typically I’m starting to look for places where I’m manually doing tasks or having to remember how to do things. Each one of those presents a good opportunity to create a skill. Not generic skills like /grill-me or /show-me, which are really useful, but more like helping to write a PR description in exactly the format I want or provide user test data.

[Rex Horthy's /show-me skill](https://x.com/dexhorthy/status/2087569590268391897?s=46): a good example of using a visualisation skill to make code exploration feel more concrete.

[PStack's verification skill](https://x.com/poteto/status/2094457600259842065?s=61): an interesting example of wrapping an app in a testing CLI and using a feature map to keep validation grounded.

I’d also advise trying to keep skills focused and apply single responsibility principles, because good boundaries mean you can start to compose skills into higher-order workflows and get greater leverage from them.

For example, build an API request builder skill to generate API requests, a workflow skill to chain the requests together, then use the /show-me skill to diagram the flow and attach it all to the PR.

[Why your next API client should be an AI agent, not a Postman collection](https://robintegg.com/2026/08/21/why-your-next-api-client-should-be-an-ai-agent-not-a-postman-collection.html): an approach to letting agents generate API requests on the fly instead of depending on static collections.

[MCP tool design patterns](https://ro14nd.de/mcp-tool-design-patterns/#the-top): a useful reminder that MCPs are most useful when they model real workflow boundaries rather than just exposing endpoint-by-endpoint CRUD surfaces.

[Example front-end plus configuration DAG for agent graphs](https://x.com/0xagility/status/2090502001545126306/video/1): a strong example of turning agent workflows into a diagram that is easier to reason about and share.

## Scaling agent skills

Using agent skills is one thing, sharing them with teams who use multiple harnesses and IDEs is another. Some of the key players in this space have adopted the agent plugin standard, which seems like a good way to bundle and share skills, agents, prompts, and hooks.

[Adopting VS Code agent plugins in AI Toolkit](https://robintegg.com/2026/08/09/adopting-vscode-agent-plugins-in-ai-toolkit.html): a practical example of rolling agent plugin standards into my own AI toolkit.

[Agent customisation in VS Code](https://code.visualstudio.com/docs/agent-customization/agent-plugins): a new standard format for bundling AI artefacts. VS Code supports them in a few places, and I’m starting to use that model as part of my own setup.

[Cursor rules: a unified AI config setup](https://www.keboca.com/articles/cursorrules-ai-how-i-unified-my-cursor-and-claude-config-one-place): a useful pattern for using a shared folder and symlinks so multiple AI harnesses can work from the same source of truth.

## Opencode trial

I’d been seeing some great demos and videos around OpenCode, especially around the ability to run it as a server as I’m looking into approaches for cloud agents and cloud coding. The TUI is nice and responsive and there’s plenty to like about it.

Where I found the friction was that many of my projects have their AI artefacts configured for Copilot in the `.github` directory. To make these work with OpenCode requires some effort and the trade-off isn’t worth it for me right now, mainly because I’m so invested in the Copilot ecosystem. It is a great project though, and it is a little disappointing that I’m not moving to it yet.

[OpenCode 2 web UI](https://x.com/thdxr/status/2089456722087657504?s=61): a nice look at the product direction and workflow experience.

[OpenCode 2 server](https://x.com/thdxr/status/2087234332448403667?s=61): a useful reminder that serving a coding agent in the background is becoming an increasingly normal pattern.

## Background / Cloud agents

Much of my spare time this month has been spent looking into running background agents. The thought here is how I can increase bandwidth by running concurrent jobs both at home and at work, and both locally and remotely.

I think going forward it needs to be a combination of all of those. Background agents picking off tasks and me being able to interact with those tasks on my laptop or on my phone or iPad. Many approaches are running Claude or similar harnesses on a VPS. This is where I’ve started, and I’ll be looking more at this through September.

[Setting up my AI cloud agent](https://robintegg.com/2026/08/24/setting-up-my-ai-cloud-agent.html): first steps in setting up a cloud environment to run agents.

[Why Ramp built Inspect](https://newsletter.pragmaticengineer.com/p/why-ramp-built-inspect): a good look at one company’s real-world background coding agent setup.

[Claude Code on a VPS](https://x.com/levelsio/status/2071162399864889705?s=61): a strong example of the direction people are exploring for remote coding agents.

[Terminus / Tailscale / tmux](https://x.com/dhh/status/2082829325212983297?s=61): a nice reminder that simple, reliable tooling still matters when you are delegating work to the cloud.

[tmux sessions for delegating to an agent](https://x.com/ashxhart/status/2091109763975111129?s=61): a useful pattern for keeping agent sessions alive and accessible from different machines.

[Tailscale vs NetBird](https://netbird.io/knowledge-hub/tailscale-vs-netbird): a thoughtful comparison if you are looking at secure networking options for remote agents.

## My other articles

[Spring Boot 4 observability options](/2026/08/10/spring-boot-4-observability-options.html): Boot UI is an embedded dependency that enhances local Spring Boot development and supports an MCP endpoint for your agent to query and diagnose a running application.

## My AI stack

<table>
  <thead>
    <tr>
      <th>Category</th>
      <th>Using</th>
      <th>Evaluating</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Tools</td>
      <td><a href="https://github.com/features/copilot">GitHub Copilot</a>, <a href="https://gemini.google.com/app?hl=en-GB">Gemini</a>, <a href="https://code.visualstudio.com/">vscode</a>, <a href="https://www.granola.ai/">Granola</a></td>
      <td></td>
    </tr>
    <tr>
      <td>Frameworks</td>
      <td><a href="https://github.github.com/copilot-sdk-java/">Java Copilot SDK</a>, <a href="https://spring.io/projects/spring-ai">Spring AI</a></td>
      <td></td>
    </tr>
    <tr>
      <td>Services</td>
      <td><a href="https://github.com/features/copilot/plans">Github Copilot Pro+</a>, <a href="https://chatgpt.com/pricing/">ChatGPT</a>, <a href="https://openrouter.ai/models?order=pricing-low-to-high">OpenRouter</a></td>
      <td><a href="https://opencode.ai/go">OpenCode Go</a></td>
    </tr>
  </tbody>
</table>