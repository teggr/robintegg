---
layout: post
title: "JReleaser - Practice makes perfect"
date: "2026-01-04"
image: /images/publish-maven-to-jitpack.jpg
tags:
  - java
  - maven
  - jreleaser
  - tools
  - jbang
---

At the start of 2026, I came across a great article by Max Andersen (creator of JBang) titled [Let's make 2026 the year of Java in the terminal!](https://xam.dk/blog/lets-make-2026-the-year-of-java-in-the-terminal/). His enthusiasm for building practical Java tooling that developers can actually use in their day-to-day work really resonated with me. I'm determined to build and release more tooling this year for the Java community, and that means getting serious about the release process.

## Getting started with JReleaser

To make releasing Java projects easier, I followed the excellent [Foojay 2025 guide on publishing a Java Maven project to Maven Central using JReleaser and GitHub Actions](https://foojay.io/today/how-to-publish-a-java-maven-project-to-maven-central-using-jreleaser-and-github-actions-2025-guide/). This guide walks you through the entire process of setting up JReleaser with GitHub Actions to automate releases to Maven Central.

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

One of the best parts about deploy4j is that it can be executed using JBang, which makes distribution a breeze. Users can run the tool without having to manually download and install anything:

```bash
jbang deploy4j@teggr
```

JBang handles downloading the artifact from Maven Central, caching it locally, and executing it with the correct Java version. This is perfect for CLI tools where you want users to get started immediately without friction.

### Setting up JReleaser

Following the Foojay guide, I configured JReleaser in my Maven project with a `jreleaser.yml` configuration file:

```yaml
project:
  name: deploy4j
  description: A deployment tool for containerized applications
  links:
    homepage: https://deploy4j.dev
  authors:
    - Robin Tegg
  license: Apache-2.0
  inceptionYear: 2025

release:
  github:
    overwrite: true
    changelog:
      formatted: ALWAYS
      preset: conventional-commits
      contributors:
        format: '- {{contributorName}}{{#contributorUsernameAsLink}} ({{.}}){{/contributorUsernameAsLink}}'

distributions:
  deploy4j:
    type: JAVA_BINARY
    artifacts:
      - path: target/{{distributionName}}-{{projectVersion}}.jar

deploy:
  maven:
    pomchecker:
      version: 1.10.0
    nexus2:
      maven-central:
        active: ALWAYS
        url: https://s01.oss.sonatype.org/service/local
        closeRepository: true
        releaseRepository: true
        stagingRepositories:
          - target/staging-deploy
```

The Maven Central deployment requires:

- A Sonatype JIRA account and approved groupId
- GPG keys for signing artifacts
- GitHub secrets for credentials

Once configured, the GitHub Actions workflow handles everything:

```yaml
- name: Run JReleaser
  env:
    JRELEASER_NEXUS2_USERNAME: ${{ secrets.MAVEN_CENTRAL_USERNAME }}
    JRELEASER_NEXUS2_PASSWORD: ${{ secrets.MAVEN_CENTRAL_TOKEN }}
    JRELEASER_GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
    JRELEASER_GPG_PUBLIC_KEY: ${{ secrets.GPG_PUBLIC_KEY }}
    JRELEASER_GPG_SECRET_KEY: ${{ secrets.GPG_SECRET_KEY }}
    JRELEASER_GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
  run: mvn -B jreleaser:full-release
```

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

1. **Start with good documentation**: The Foojay guide is comprehensive and walks you through every step
2. **Set up your infrastructure early**: Get your Sonatype account, GPG keys, and GitHub secrets configured before you need them
3. **JBang makes distribution easy**: For CLI tools, JBang provides an amazing user experience
4. **Automate everything**: With JReleaser and GitHub Actions, releases become a single git tag away
5. **Practice makes perfect**: The more you release, the smoother the process becomes

## References

- [Let's make 2026 the year of Java in the terminal!](https://xam.dk/blog/lets-make-2026-the-year-of-java-in-the-terminal/) by Max Andersen
- [How to publish a Java Maven project to Maven Central using JReleaser and GitHub Actions (2025 guide)](https://foojay.io/today/how-to-publish-a-java-maven-project-to-maven-central-using-jreleaser-and-github-actions-2025-guide/)
- [JReleaser Official Documentation](https://jreleaser.org/)
- [deploy4j website](https://deploy4j.dev)
- [deploy4j on GitHub](https://github.com/teggr/deploy4j)
- [JBang Documentation](https://www.jbang.dev/)

If you're building Java tools for the community, give JReleaser a try. It takes the pain out of releasing and lets you focus on building great software. Here's to making 2026 the year of Java in the terminal! 🚀
