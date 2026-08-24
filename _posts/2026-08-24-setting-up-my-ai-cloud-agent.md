---
layout: post
title: "Setting up my AI Cloud Agent"
date: "2026-08-24"
description: "How I turned a bare Ubuntu VPS into a remote development machine for OpenCode, with separated admin and agent accounts, tmux persistence, and browser automation."
image: /images/my-cloud-agent.jpg
tags:
  - ai
  - agents
  - opencode
  - tools
  - workflow
---

This post is a run-down of what I did to get a cloud based AI agent running from a bare Ubuntu VPS. I've done this for a few reasons:

* I want to be able to code with my agents from any platform
* I want to reduce / spread my AI costs
* I want to give it a go

The approach has been heavily inspired by a few online videos and tweets. Seeing the relatively simple setups and how they were used gave me sufficient direction to get started.

[https://x.com/dhh/status/2082829325212983297?s=61](https://x.com/dhh/status/2082829325212983297?s=61) - DHH on his use of terminus / tailscale / tmux to be able to remotely access and use agents from a phone/tablet/laptop

[https://x.com/thdxr/status/2089456722087657504?s=61](https://x.com/thdxr/status/2089456722087657504?s=61) - Dax from Opencode demonstrating the `/pair` command to share running opencode sessions across machines via tailscale.

To ensure that I had the best chance of getting this done and not getting too lost in the weeds, I used ChatGPT as my main assitant in planing and executing. I'd type the commands but ChatGPT brought the expertise, especially around the linux tooling which wasn't that familiar with. So i'll use "we" to recognise the support.

I gave ChatGPT it the instructions to help me acheieve the goal of running all the tools above with the aim of being able to develop against any of my personal repos using the agent. 

## Starting with a clean VPS

Here ChatGPT was useful in providing me with a spec list of what would be a good enough machine to run the tools. It was then able to go out and find me some options and pricing. In the end I went with an [ovhcloud VPS](https://blog.ovhcloud.com/en/posts/vps-2027/) which struck a nice balance of price and performance, whilst also being hosted in the UK.

Initial access to the new host was via the [Terminus](https://termius.com/index.html) app on my iPhone. This was my first time using a terminal from a phone. After an initial awkward period of getting used to a terminal on a phone it's become easier to work across both iPhone and iPad.

The first job was unglamorous but important: bring the machine up to date and verify its basic state. We checked the identity of the machine, its operating system, disk and memory availability, and applied the outstanding Ubuntu updates. The update installed a new kernel, so we rebooted and verified the machine was actually running it.

That gave us a known-good baseline before putting any development tooling on the server.

## Separating the administrator from the agent

This was a decision from ChatGPT early on - the AI agent should not be the administrator account.

The VPS has an `ubuntu` account with administrative access, and a separate `agent` account used for development. The `agent` account can access repositories, run builds and tests, use Git and GitHub, run OpenCode, use Java, Node and Bun, run containers, and drive browser automation. It does not have sudo.

When something genuinely requires a system-level change (installing a browser, Docker, system packages), it has to cross an explicit boundary and be done by the administrator.

For an autonomous development agent, it was strongly argued that it would be a much better model than handing the AI unrestricted root access. It became a recurring theme throughout the rest of the setup.

## Making remote access practical

Initially I accessed the VPS through the Terminus app on my iPhone, but the intended working environment was my laptop. I configured SSH access using the laptop's existing keys, then introduced [Tailscale](https://tailscale.com/) so the VPS could be reached through its private address instead of exposing every development service directly to the internet.

## Creating the development workspace

We settled on `~/projects` for the working directory. The reasoning is subtle but useful: these are projects that OpenCode can work on, not just Git repositories. Each project maps naturally to a directory, normally containing a single Git repository:

```text
~/projects/
    j2html-toolkit/
    deploy4j/
    ...
```

That convention became the basis for the project launcher later.

## Java without committing to one version

The first Java environment used Java 25, but we deliberately didn't want to hard-code the machine to one Java version. We installed SDKMAN for the `agent` user and installed Temurin Java 25 through it.

Different projects can now select different JDK versions without touching the operating system's Java installation. This matters more on an AI development machine than a personal one, because the repositories an agent works across may have very different requirements.

## Maven belongs to the project

Another deliberate choice: no globally installed Maven. Projects use their existing `./mvnw` wrapper, so the Maven version is determined by the project itself.

This proved useful immediately: `j2html-toolkit` built successfully using its wrapper with Java 25.

## GitHub access for the agent

Next we gave the agent access to GitHub, again with a carefully defined scope: full access to repositories, no GitHub account administration.

We configured GitHub CLI authentication for the `agent` account using a token kept outside the OpenCode configuration itself. There was a small debugging episode here where `GH_TOKEN` existed in the shell configuration but wasn't actually present in the environment. Once that was corrected, commands like `gh api user` and `gh repo view` worked fine.

That gives OpenCode the ability to work with repositories through GitHub without administrative control over the account.

## OpenCode 2

We chose [OpenCode 2](https://opencode.ai/v2/docs) over the earlier version. The initial TUI worked, and I tested it against `j2html-toolkit`. OpenCode inspected the repository, built it, and made a small change. That gave us confidence the basic agent/development loop was working.

## Testing the Git workflow

Then we deliberately tested the workflow I actually wanted from the agent. OpenCode created a feature branch from `main`, made a small README improvement, ran `./mvnw test` and passed all 18 modules, committed the change and pushed the branch once the GitHub authentication issue above was corrected. The resulting pull request was ready to be merged through GitHub.

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

[tmux](https://github.com/tmux/tmux/wiki) was a final necessary decision rather than an optional extra. The whole point of this setup is being able to say "here's a job, work on it while I'm away", and without session persistence, closing the terminal kills the agent mid-task. tmux provides that persistence.

The resulting structure is roughly:

```text
SSH
 └── tmux
      └── OpenCode
           └── project
```

Detach from the SSH session, close the laptop, reconnect later, and you're back in the same OpenCode session. We also made `opencode-project` understand existing project-specific tmux sessions, so invoking it again attaches to the running session instead of starting a second OpenCode process.

That turned OpenCode into a genuine remote-agent workflow you can walk away from.

## Browser automation with Playwright MCP

The next major capability was browser access. The OpenCode documentation describes MCP integration including Playwright, so we used that. We installed Bun for the agent and configured Playwright MCP as a local OpenCode MCP server.

There was a little debugging here too. OpenCode could see the MCP configuration but initially couldn't spawn `bunx` because the MCP child process didn't inherit the interactive shell's PATH as expected. Changing the configuration to use the absolute path to `bunx` fixed it, and after restarting OpenCode the MCP server showed as loaded.

## Adding Node as well

We initially tried to avoid Node since Bun could run the MCP package. But Playwright's browser tooling is strongly oriented around the Node ecosystem, and having Node available proved useful in its own right. So we added Node for the `agent` account using nvm.

Both runtimes are now available, which is a much more practical development environment than forcing every JavaScript-related tool through one.

## Installing Chrome at the system level

The remaining Playwright problem was interesting. Playwright MCP was configured to use Chrome, but Chrome wasn't installed. The `agent` account couldn't install it because the browser belongs in a system location, and `agent` deliberately has no sudo.

We kept the privilege model intact and switched to the `ubuntu` administrator account to install Google Chrome system-wide, then verified `/opt/google/chrome/chrome` was available. That preserved the original architecture:

```text
ubuntu
  └── system software / Chrome

agent
  └── OpenCode / Playwright MCP
          └── Chrome
```

The agent doesn't need administrator access just because it needs a browser.

## Proving the browser stack

Finally, we asked OpenCode to perform an actual browser operation rather than simply fetch a web page. It used the Playwright MCP browser to navigate to example.com and produce a screenshot. That proved the entire chain was functioning:

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

We did hit a `search` tool error along the way, which turned out to have nothing to do with the browser: OpenCode was trying to invoke a search capability that wasn't installed. The direct Playwright test worked once we explicitly told it not to use search or direct HTTP fetching. That was still a useful validation, because browser automation now demonstrably works independently of any general web-search capability.

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

We made the server safe and accessible first, then gave the agent more capabilities only when a real development requirement justified them.

The result is a machine where I can SSH in from my laptop, attach to a persistent project session, give OpenCode a substantial task, disconnect, and come back later with GitHub, Java, Maven, browser automation and soon Docker all available to the agent.

## Where do we go next?

I'm firstly seeing how it goes with this setup. I have a couple of items to look into next.

* Scheduled tasks - Likely a cron job running an open code session
* Multi repo tasks - How can I run jobs across multiple repos
* Github issues - Can an agent take an issue and get all the way to PR
* Worktress - Can't work multiple tickets in parallel with current `project` scoped setup 

## References

- [OpenCode 2 Documentation](https://opencode.ai/v2/docs/)
- [Playwright MCP](https://github.com/microsoft/playwright-mcp)
- [Tailscale](https://tailscale.com/)
- [SDKMAN!](https://sdkman.io/)
- [tmux](https://github.com/tmux/tmux)
