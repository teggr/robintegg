---
layout: post
title: "Setting up my AI Cloud Agent"
date: "2026-08-24"
description: "How I turned a bare Ubuntu VPS into a remote development machine for OpenCode, with separated admin and agent accounts, tmux persistence, and browser automation."
image: /images/ai-cloud-agent.jpg
tags:
  - ai
  - agents
  - opencode
  - tools
  - workflow
---

I've been running AI agents locally for a while now, but I kept hitting the same limitation: my laptop has to stay on, attached to a terminal, for as long as the agent is working. What I wanted was to hand an agent a job, close the laptop, and come back later to find it done.

So I set up a cloud agent on an OVH VPS. This post is a run-down of what I did: install OpenCode so it can be left running remotely, accessed securely from a laptop, work across multiple repositories, run builds and tests, interact with GitHub, and perform browser-based testing, all while keeping a sensible privilege boundary.

## Starting with a clean VPS

The first job was unglamorous but important: bring the machine up to date and verify its basic state. I checked the identity of the machine, its operating system, disk and memory availability, and applied the outstanding Ubuntu updates. The update installed a new kernel, so I rebooted and verified the machine was actually running it.

That gave me a known-good baseline before putting any development tooling on the server.

## Separating the administrator from the agent

This was one of the most important decisions, and it came early: the AI agent would not be the administrator account.

The VPS has an `ubuntu` account with administrative access, and a separate `agent` account used for development. The `agent` account can access repositories, run builds and tests, use Git and GitHub, run OpenCode, use Java, Node and Bun, run containers, and drive browser automation. It does not have sudo.

When something genuinely requires a system-level change (installing a browser, Docker, system packages), it has to cross an explicit boundary and be done by the administrator.

For an autonomous development agent, that feels like a much better model than handing the AI unrestricted root access. It became a recurring theme throughout the rest of the setup.

## Making remote access practical

Initially I accessed the VPS through an SSH client on my iPhone, but the intended working environment was my laptop. I configured SSH access using the laptop's existing keys, then introduced Tailscale so the VPS could be reached through its private address instead of exposing every development service directly to the internet.

The pattern:

> The VPS is public infrastructure, but day-to-day administration and development access happens over the private Tailscale network.

## Creating the development workspace

I settled on `~/projects` for the working directory. The reasoning is subtle but useful: these are projects that OpenCode can work on, not just Git repositories. Each project maps naturally to a directory, normally containing a single Git repository:

```text
~/projects/
    j2html-toolkit/
    deploy4j/
    ...
```

That convention became the basis for the project launcher later.

## Java without committing to one version

The first Java environment used Java 25, but I deliberately didn't want to hard-code the machine to one Java version. I installed SDKMAN for the `agent` user and installed Temurin Java 25 through it.

Different projects can now select different JDK versions without touching the operating system's Java installation. This matters more on an AI development machine than a personal one, because the repositories an agent works across may have very different requirements.

## Maven belongs to the project

Another deliberate choice: no globally installed Maven. Projects use their existing `./mvnw` wrapper, so the Maven version is determined by the project itself.

This proved useful immediately: `j2html-toolkit` built successfully using its wrapper with Java 25. The principle:

> Use the project's build tooling wherever the project provides it, rather than imposing global versions on repositories.

## GitHub access for the agent

Next I gave the agent access to GitHub, again with a carefully defined scope: full access to repositories, no GitHub account administration.

I configured GitHub CLI authentication for the `agent` account using a token kept outside the OpenCode configuration itself. There was a small debugging episode here where `GH_TOKEN` existed in the shell configuration but wasn't actually present in the environment. Once that was corrected, commands like `gh api user` and `gh repo view` worked fine.

That gives OpenCode the ability to work with repositories through GitHub without administrative control over the account.

## OpenCode 2

I chose OpenCode 2 over the earlier version. The initial TUI worked, and I tested it against `j2html-toolkit`. OpenCode inspected the repository, built it, and made a small change. That gave me confidence the basic agent/development loop was working.

## Testing the Git workflow

Then I deliberately tested the workflow I actually wanted from the agent. OpenCode created a feature branch from `main`, made a small README improvement, ran `./mvnw test` and passed all 18 modules, committed the change and pushed the branch once the GitHub authentication issue above was corrected. I merged the resulting pull request through GitHub.

This tested more than "can OpenCode edit files?". It demonstrated the complete cycle:

```text
repository
    ↓
feature branch
    ↓
AI modification
    ↓
project tests
    ↓
commit
    ↓
push
    ↓
pull request / merge
```

with `main` untouched during development.

## Multiple repositories

Adding a second project, `deploy4j`, exposed an important requirement: this was a multi-project development machine from the start.

That led to an `opencode-project` launcher. The idea is simple:

```bash
opencode-project deploy4j
```

finds `~/projects/deploy4j` and starts OpenCode there. The same mechanism works for any future project.

## tmux for long-running AI work

tmux was a necessary decision rather than an optional extra. The whole point of this setup is being able to say "here's a job, work on it while I'm away", and without session persistence, closing the terminal kills the agent mid-task. tmux provides that persistence.

The resulting structure is roughly:

```text
SSH
 └── tmux
      └── OpenCode
           └── project
```

Detach from the SSH session, close the laptop, reconnect later, and you're back in the same OpenCode session. I also made `opencode-project` understand existing project-specific tmux sessions, so invoking it again attaches to the running session instead of starting a second OpenCode process.

That turned OpenCode into a genuine remote-agent workflow you can walk away from.

## Browser automation with Playwright MCP

The next major capability was browser access. The OpenCode documentation describes MCP integration including Playwright, so I used that. I installed Bun for the agent and configured Playwright MCP as a local OpenCode MCP server.

There was a little debugging here too. OpenCode could see the MCP configuration but initially couldn't spawn `bunx` because the MCP child process didn't inherit the interactive shell's PATH as expected. Changing the configuration to use the absolute path to `bunx` fixed it, and after restarting OpenCode the MCP server showed as loaded.

## Adding Node as well

I initially tried to avoid Node since Bun could run the MCP package. But Playwright's browser tooling is strongly oriented around the Node ecosystem, and having Node available proved useful in its own right. So I added Node for the `agent` account using nvm.

Both runtimes are now available, which is a much more practical development environment than forcing every JavaScript-related tool through one.

## Installing Chrome at the system level

The remaining Playwright problem was interesting. Playwright MCP was configured to use Chrome, but Chrome wasn't installed. The `agent` account couldn't install it because the browser belongs in a system location, and `agent` deliberately has no sudo.

I kept the privilege model intact and switched to the `ubuntu` administrator account to install Google Chrome system-wide, then verified `/opt/google/chrome/chrome` was available. That preserved the original architecture:

```text
ubuntu
  └── system software / Chrome

agent
  └── OpenCode / Playwright MCP
          └── Chrome
```

The agent doesn't need administrator access just because it needs a browser.

## Proving the browser stack

Finally, I asked OpenCode to perform an actual browser operation rather than simply fetch a web page. It used the Playwright MCP browser to navigate to example.com and produce a screenshot. That proved the entire chain was functioning:

```text
OpenCode 2
    ↓
MCP
    ↓
Playwright
    ↓
Chrome
    ↓
website
```

I did hit a `search` tool error along the way, which turned out to have nothing to do with the browser: OpenCode was trying to invoke a search capability that wasn't installed. The direct Playwright test worked once I explicitly told it not to use search or direct HTTP fetching. That was still a useful validation, because browser automation now demonstrably works independently of any general web-search capability.

## Where we are now

The VPS has evolved from a bare Ubuntu machine into something much closer to a personal remote development agent:

```text
                         Tailscale / SSH
                               │
                               ▼
                         ┌───────────┐
                         │    VPS    │
                         └─────┬─────┘
                               │
                 ┌─────────────┴─────────────┐
                 │                           │
             ubuntu                       agent
          administrator                development
                 │                           │
       system packages                 OpenCode 2
       Docker / Chrome                 Git / GitHub
       kernel / services                Java / SDKMAN
                                      Node / nvm
                                         Bun
                                      Maven wrapper
                                    Playwright MCP
                                         Chrome
                                           │
                                      ~/projects
                                      ├── repo A
                                      ├── repo B
                                      └── ...
                                           │
                                         tmux
                                           │
                                       long-running
                                          agents
```

The next significant piece is Docker. It changes the agent from being able to build source code to being able to reproduce much more realistic development environments: Testcontainers, integration tests, Docker-based Maven builds, local services, container image builds, and eventually deployment workflows.

The intended Docker model stays consistent with the privilege separation too: Docker itself installed and managed by `ubuntu`, while `agent` gets access to the Docker Unix socket. The development environment gets what it needs without ordinary sudo access.

## Summary

What makes this setup work is the progressive separation of concerns:

> ubuntu controls the machine; agent controls the development environment; OpenCode controls the work.

I made the server safe and accessible first, then gave the agent more capabilities only when a real development requirement justified them.

The result is a machine where I can SSH in from my laptop, attach to a persistent project session, give OpenCode a substantial task, disconnect, and come back later with GitHub, Java, Maven, browser automation and soon Docker all available to the agent.

## References

- [OpenCode 2 Documentation](https://opencode.ai/v2/docs/)
- [Playwright MCP](https://github.com/microsoft/playwright-mcp)
- [Tailscale](https://tailscale.com/)
- [SDKMAN!](https://sdkman.io/)
- [tmux](https://github.com/tmux/tmux)
