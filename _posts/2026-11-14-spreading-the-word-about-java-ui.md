---
layout: post
title: "Spreading the word about Java UI"
date: "2026-11-14"
description: "How I used Copilot, JBang and GitHub Actions to turn the Awesome Java UI directory into a weekly publishing queue for X."
image: /images/awesome-java-ui-social-publishing.png
tags:
  - java
  - ai
  - github copilot
  - jbang
  - github actions
---

[Awesome Java UI](https://awesome-java-ui.com/) is my directory of frameworks, libraries and tools for building interfaces with Java. That covers desktop, web, mobile and terminal applications, along with the tooling that helps get them built and distributed. I've been collecting these projects because there's quite a bit more going on with Java UI than people might expect.

Getting people to find them is the next job. I want to spread the gospel of Java UI as far as I can, and regularly sharing individual projects seemed like a useful place to start. There's now an X account for that: [@awesomejavaui](https://x.com/awesomejavaui).

![The Awesome Java UI X account, with its directory banner and description of Java desktop, web, mobile and terminal projects](/images/awesome-java-ui-x-account.png)

The inspiration came from Bruno Borges' article, [How I Automated Weekly Twitter/X Posts With Java, JBang and GitHub Actions](https://foojay.io/today/how-i-automated-weekly-twitter-x-posts-with-java-jbang-and-github-actions/). He'd set up weekly posts for [Java Evolved](https://javaevolved.github.io/), taking content already in the repository and giving each entry a turn on the project's account.

That was close enough to what I wanted. I pointed my Copilot agent at the article and asked it to do the same for Awesome Java UI. The result is a small Java publishing script, a queue in Git, a weekly workflow, and our first published post.

## Give the existing content another route out

The useful part of Bruno's approach is that the content already exists. Each project in Awesome Java UI has a Markdown page with a name, a description and a URL. I wanted to turn those pages into a repeatable publishing queue rather than write a separate campaign from scratch.

Copilot adapted that into a JBang script and a GitHub Actions workflow. The script prepares a shuffled queue of projects with draft posts, and the workflow is scheduled to publish one every Monday at 14:00 UTC. The initial queue contained 80 projects, so there's plenty to work through before repeating.

The drafts and publishing position live in the repository. I can review the wording before it goes out, make edits and see where we are in the queue. Each post uses the existing project description, shortened to fit, with a link back to its directory page.

Copilot helped write the machinery; the weekly publisher reads the prepared text. There's no language model writing new promotional copy each time the workflow runs. I still need to keep an eye on how those descriptions read when shortened.

Bruno's article covers the scripts, scheduling and OAuth signing in detail, so I'd start there if you want to build something similar. You can also see our adaptation in [teggr/awesome-java-ui#134](https://github.com/teggr/awesome-java-ui/pull/134).

## Our first post is out

The first published post featured [Gluon](https://awesome-java-ui.com/gluon.html), linking back to its directory page:

![The first Awesome Java UI post on X, featuring Gluon with a description, directory link, Java hashtags and a website preview card](/images/awesome-java-ui-first-post.png)

Seeing the description, link and preview card on the account was a useful end-to-end result. We've got the first post out; the weekly schedule still needs to prove itself over subsequent runs.

This was a good use of Copilot for me. Bruno's article gave us a concrete starting point, and the agent adapted it to my content. The files are small enough that I can inspect what will be published and how the queue moves on.

For now, the next job is to watch the first scheduled runs and improve any drafts that read awkwardly when shortened. If you'd like a regular reminder of what people are building Java interfaces with, follow [@awesomejavaui](https://x.com/awesomejavaui).
