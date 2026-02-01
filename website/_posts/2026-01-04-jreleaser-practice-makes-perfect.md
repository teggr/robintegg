---
layout: post
title: "JReleaser - Practice makes perfect"
date: "2026-01-04"
description: "Getting started with JReleaser for automated Java project releases to Maven Central and multiple distribution channels."
image: /images/practice-makes-perfect.png
tags:
  - java
  - maven
  - jreleaser
  - tools
  - jbang
---

At the start of 2026, I came across a great article by Max Andersen (creator of JBang) titled [Let's make 2026 the year of Java in the terminal!](https://xam.dk/blog/lets-make-2026-the-year-of-java-in-the-terminal/). His enthusiasm for building practical Java tooling that developers can actually use in their day-to-day work really resonated with me. I'm determined to build and release more tooling this year for the Java community, and that means getting serious about the release process.

## Getting started with JReleaser

To make releasing Java projects easier, I followed the excellent [Foojay 2025 guide on publishing a Java Maven project to Maven Central using JReleaser and GitHub Actions](https://foojay.io/today/how-to-publish-a-java-maven-project-to-maven-central-using-jreleaser-and-github-actions-2025-guide/). 

I'll be honest - when I first looked at the guide, it felt overwhelming. Setting up Sonatype accounts, generating GPG keys, configuring YAML files, managing GitHub secrets - there's a lot to take in. The documentation is comprehensive (which is great!), but that also means there's a lot of detail to digest. It's the kind of thing that can make you put it off for another day.

But here's the thing: **it's worth diving in the deep end and just getting started**. Yes, the first time through feels like a slog. You'll probably need to refer back to the guide multiple times, double-check settings, and maybe hit a few bumps along the way. But that's completely normal. The important part is taking that first step. And here's the payoff - **the more you do it, the quicker you get**. What feels like an intimidating mountain of configuration the first time becomes a straightforward checklist you can knock out in minutes.

The key benefits of using JReleaser include:

- **Automated releases**: Configure once, release anywhere
- **Multiple distribution channels**: Maven Central, GitHub Releases, Homebrew, and more
- **Release announcements**: Automatically post to Twitter, Mastodon, or other platforms
- **Changelog generation**: Generate beautiful changelogs from your Git commits
- **Multiple artifact types**: Support for JARs, native images, JBang aliases, and more

## Deploying deploy4j to Maven Central

I put this knowledge into practice by deploying my own tool, **deploy4j**, a port of Kamal (the Rails deployment tool) to Maven Central. Deploy4j makes it easy to deploy containerized applications to your own servers without complex orchestration tools.

### About deploy4j

Deploy4j brings the simplicity and elegance of Kamal to the Java ecosystem. It's a deployment tool that:

- Deploys containerized applications to any server with Docker installed
- Manages zero-downtime deployments with automatic health checks
- Handles environment configuration and secrets management
- Provides simple rollback capabilities

The tool is hosted at [deploy4j.dev](https://deploy4j.dev) and the source code is available on GitHub at [teggr/deploy4j](https://github.com/teggr/deploy4j).

### JBang distribution

One of the best parts about deploy4j is that it can be executed using JBang, which makes distribution a breeze. Users can run the tool without having to manually download and install anything ([see docs for latest version](https://deploy4j.dev/installation/)):

```bash
jbang dev.deploy4j:deploy4j-cli:0.0.3
```

JBang handles downloading the artifact from Maven Central, caching it locally, and executing it with the correct Java version. This is perfect for CLI tools where you want users to get started immediately without friction.

### Setting up JReleaser

Following the Foojay guide, I worked through setting up JReleaser for deploy4j. The setup involves creating a `jreleaser.yml` configuration file, getting your Sonatype account and GPG keys sorted, and configuring a GitHub Actions workflow to handle the release process. The guide has all the details you need to work through each step.

## Working towards 1.0

I'm currently working towards a 1.0 release of deploy4j. Having the artifacts available on Maven Central makes it easy for developers to include deploy4j as a dependency in their own projects. The distribution and notification channels provided by JReleaser will be crucial for getting the word out when the 1.0 release is ready.

JReleaser's ability to:

- Publish to Maven Central automatically
- Create GitHub releases with generated changelogs
- Generate JBang catalogs for easy CLI distribution
- Announce releases on social media platforms
- Support multiple distribution formats

...makes it the perfect tool for managing the complete release lifecycle of Java projects.

## Key takeaways

1. **Don't let comprehensive guides intimidate you**: Yes, there's a lot of documentation. Yes, it can feel overwhelming. But that's better than having no guide at all. Take it one step at a time.
2. **Just start**: The first time through any new process feels slow and uncertain. That's normal. Push through it.
3. **It gets easier fast**: By your second or third time, what felt complicated becomes routine. Practice really does make perfect.
4. **Start with good documentation**: The Foojay guide is comprehensive and walks you through every step
5. **Set up your infrastructure early**: Get your Sonatype account, GPG keys, and GitHub secrets configured before you need them
6. **JBang makes distribution easy**: For CLI tools, JBang provides an amazing user experience
7. **Automate everything**: With JReleaser and GitHub Actions, releases become a single git tag away

## References

- [Let's make 2026 the year of Java in the terminal!](https://xam.dk/blog/lets-make-2026-the-year-of-java-in-the-terminal/) by Max Andersen
- [How to publish a Java Maven project to Maven Central using JReleaser and GitHub Actions (2025 guide)](https://foojay.io/today/how-to-publish-a-java-maven-project-to-maven-central-using-jreleaser-and-github-actions-2025-guide/)
- [JReleaser Official Documentation](https://jreleaser.org/)
- [deploy4j website](https://deploy4j.dev)
- [deploy4j on GitHub](https://github.com/teggr/deploy4j)
- [JBang Documentation](https://www.jbang.dev/)

If you're building Java tools for the community, give JReleaser a try. It takes the pain out of releasing and lets you focus on building great software. Here's to making 2026 the year of Java in the terminal! 🚀
