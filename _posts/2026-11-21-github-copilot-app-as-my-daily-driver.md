---
layout: post
title: "The GitHub Copilot app as my daily driver"
date: "2026-11-21"
description: "Why the GitHub Copilot app has become my daily driver at work and home, from repository-first sessions to Agent Merge and custom canvases."
tags:
  - ai
  - github copilot
  - agents
  - tools
  - workflow
---

I've been using the [GitHub Copilot app](https://docs.github.com/copilot/concepts/agents/github-copilot-app) as my daily driver, both at work and at home. I'm not really a CLI guy. I like a UI and buttons to click, and the app gives me a way to work with agents that feels natural to me.

I know that won't be everyone's preference. Some people are much happier in a terminal, and that's fine. For me, the app brings together the repositories, sessions and GitHub workflows I already use, without making me remember a command for everything.

## Starting with a repository

The way the app organises work around repositories fits how I think. I can see my repositories, pick the one I want to work on and start an agent session from there. When I have an idea or a task, I usually already know which repo it belongs to, so that's a comfortable place to start composing my thoughts.

> Screenshot to add: The repository view with an agent session ready to start.

It's a small thing, but it makes moving between work and personal projects feel consistent. The repository is the starting point, then I can focus on the task rather than figuring out where to launch it.

## Getting pull requests ready with Agent Merge

Working in a team means a pull request can pick up feedback while I'm doing something else. I find [Agent Merge](https://docs.github.com/en/copilot/how-tos/github-copilot-app/managing-issues-and-pull-requests) useful here, especially alongside the GitHub code review agent. The app can watch a pull request and have the session work on review comments and other blockers as they come up.

> Screenshot to add: Agent Merge watching a pull request and its review feedback.

That reduces the time I spend going back and forth just to get a pull request ready to merge. I still need to review the changes and decide whether the feedback has been addressed properly, but I don't have to be the one manually starting every round of fixes.

## Taking the session into VS Code

I don't always want to interact with an agent to make the next change. Sometimes I want to open the code, look around and edit it myself. From the app I can open a session in VS Code and take the conversation and working context with me.

> Screenshot to add: A Copilot app session opened in VS Code with its conversation and project visible.

That handoff feels seamless in my workflow. I can start by directing an agent in the app, then move into the editor when I want a closer look without having to reconstruct what the session was doing. GitHub has a [walkthrough of the VS Code handoff](https://github.blog/ai-and-ml/github-copilot/how-to-continue-a-github-copilot-app-session-in-vs-code/) if you want to try it.

## Trying my own canvases

I've also started experimenting with writing my own [canvases](https://docs.github.com/en/copilot/how-tos/github-copilot-app/working-with-canvas-extensions). A canvas provides a shared interface for me and an agent to work with, and it can be installed in a repository so it's available alongside the project.

> Screenshot to add: A custom repository canvas open alongside an agent session.

I'm still early in exploring what I want to build with them. What appeals to me is having another way to interact with a repo from inside the app, using an interface shaped around the work instead of relying on chat for every interaction.

## GitHub is part of the workflow

The GitHub integration ties the experience together. I can work with issues and pull requests, resolve comments and view remote sessions from GitHub agents in the same app where I'm starting my own sessions.

> Screenshot to add: GitHub issues, pull request comments or a remote agent session in the app.

I like having that context close to the work. An issue can lead into a session, and the resulting pull request and its feedback remain part of the same workflow rather than a separate trip I have to keep making.

## Letting Copilot Fusion choose

The model picker is another place where I appreciate having fewer decisions to make. With [Copilot Fusion](https://docs.github.com/en/copilot/concepts/models/copilot-fusion), I can let the system choose supported models and execution patterns for the task rather than picking a model myself.

> Screenshot to add: Copilot Fusion selected in the model picker.

There are times when I have a specific reason to choose a model, but most of the time I don't want to weigh up one provider against another before I get started. Fusion gives me an abstraction over that choice, with the available models depending on the plan and policies in use.

Copilot has also added [image generation](https://docs.github.com/en/copilot/concepts/agent-capabilities/agent-powered-workflows-and-operations/image-generation), using a dedicated image model. That means I can generate images for articles in the same place I'm working on them, which is a nice addition to my writing workflow.

## What I want to explore next

There are plenty more features I'm sure I'll find useful, and the gaps I notice seem to get filled quickly. I can't see myself changing my daily driver any time soon, although with the pace of AI tooling, who knows?

The next feature I want to explore more is [automations](https://docs.github.com/en/copilot/how-tos/github-copilot-app/using-automations). They can run recurring agent tasks on a schedule, in response to repository events or on demand. I'm interested in a Grokbot-type experience without the price tag, and I wonder how far automations could take me towards that. I haven't explored enough yet to know whether they'll fit, or what the running costs will look like.
