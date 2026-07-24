---
layout: post
title: "Essential MCP Tools for Developers"
date: "2026-07-25"
description: "MCP turns your AI agent into a connected developer. Here are the tools worth adding first: Jira, Confluence, Buildkite, DigitalOcean, and Datadog, with real prompt examples."
image: /images/custom-tooling.jpg
tags:
  - mcp
  - ai
  - github copilot
  - agents
  - tools
  - developer experience
---

Your AI agent is only as useful as the context it has access to. Out of the box, most agents know your code. They don't know your open Jira ticket, the failing Buildkite pipeline, the app deployment on DigitalOcean, or the spike in error rate that Datadog caught at 3am. Model Context Protocol (MCP) is how you connect those systems to the agent and close that gap.

The idea is simple. You configure MCP servers for the tools you already use. Your agent, whether that's GitHub Copilot, Claude, or Cursor, can then call those servers as tools. It searches Jira, reads Confluence, checks build logs, queries your infrastructure, pulls observability data. The context is live and scoped to your permissions. You stop context-switching and start working with a richer loop.

This post is about building that toolset. Not in theory, but with the actual tools available now.

## Start with what costs you context switches

Before picking MCP servers at random, think about where you lose the most time to context switching. For most developers on a product team, the answer is roughly:

- Checking what a ticket actually requires before you start coding.
- Looking up a failing build to understand why it broke.
- Confirming a deployment went out to the right environment.
- Correlating a production error with the code that caused it.

Those four problems map neatly onto four MCP servers: Atlassian Rovo, Buildkite, DigitalOcean, and Datadog.

## Atlassian Rovo MCP: Jira and Confluence

The Atlassian Rovo MCP Server connects your AI agent to Jira, Confluence, Jira Service Management, Bitbucket, and Compass. It became generally available in early 2026 and is hosted by Atlassian, so there's no infrastructure to run yourself. Auth uses OAuth 2.1 and respects your existing Atlassian Cloud permissions.

For day-to-day development, two use cases stand out. The first is pulling Jira context before you start a feature. The second is keeping Confluence documentation in sync while you code.

Configure it for VS Code by adding a remote server entry to `.vscode/mcp.json`:

```json
{
  "servers": {
    "atlassian": {
      "type": "http",
      "url": "https://mcp.atlassian.com/v1/mcp",
      "gallery": true
    }
  }
}
```

Once connected, the agent can search, read, create, and update tickets and pages without you switching tabs.

**Prompt example: pulling ticket context**

```
I'm about to start work on PROJ-1234. 
Fetch the ticket details, acceptance criteria, and any linked design docs in Confluence. 
Summarise what I need to build before I write any code.
```

**Prompt example: updating docs after a change**

```
I've just shipped the new authentication flow described in PROJ-1189. 
Find the Confluence page for the Auth Service and update the "Current Flow" section 
to reflect that we now use PKCE instead of implicit grant.
```

The agent reads the ticket, reads the Confluence page, drafts the update, and posts it. You review the diff before it's committed.

[Atlassian Rovo MCP documentation](https://developer.atlassian.com/cloud/rovo-mcp/) | [GitHub](https://github.com/atlassian/atlassian-mcp-server)

## Buildkite MCP: build and test pipelines

Buildkite's official MCP server gives your agent access to live pipelines, builds, jobs, and log output. When a build fails, you don't have to leave your editor to investigate. You give the agent the job ID and ask it to diagnose.

Install and configure the server with your Buildkite API token:

```json
{
  "servers": {
    "buildkite": {
      "command": "buildkite-mcp-server",
      "args": ["stdio"],
      "env": {
        "BUILDKITE_API_TOKEN": "your-api-token"
      }
    }
  }
}
```

**Prompt example: diagnosing a failing acceptance test**

This is the pattern that makes the investment pay back quickly. You have a failing job. You give the agent the ID and ask for a diagnosis:

```
The Buildkite job 019587a3-f9b2-4c7c-b8d1-ec57f3a2100e failed.
Fetch the job logs, identify which acceptance test failed and why, 
then suggest a fix in the relevant source file.
```

The agent calls `get_build_job` to fetch the structured output, reads the test failure message and stack trace from the logs, finds the relevant test class in your repo, and then either proposes a fix inline or opens a new edit session on the affected file. No copy-pasting log output. No manual grep through 2,000 lines of CI output.

**Prompt example: checking pipeline status before a release**

```
Check the latest build on the main branch of the my-service pipeline. 
If all steps passed, confirm it's ready to release. 
If anything failed, summarise what needs attention.
```

[Buildkite MCP server documentation](https://buildkite.com/docs/apis/mcp-server) | [GitHub](https://github.com/buildkite/buildkite-mcp-server)

## DigitalOcean MCP: infrastructure and App Platform

The DigitalOcean MCP server wraps the full DigitalOcean API and exposes it as callable tools. That covers App Platform, Droplets, Kubernetes clusters, databases, load balancers, and more. It's distributed as an npm package, so setup is a single config entry.

```json
{
  "servers": {
    "digitalocean": {
      "command": "npx",
      "args": ["@digitalocean/mcp"],
      "env": {
        "DIGITALOCEAN_API_TOKEN": "your-do-token"
      }
    }
  }
}
```

**Prompt example: confirming a deployment**

```
Check the current deployment status of the my-api app on DigitalOcean App Platform. 
What version is live and when was it last deployed?
```

**Prompt example: provisioning a dev database**

```
Create a new PostgreSQL 15 development database cluster in the LON1 region, 
with the smallest available node size. 
Name it dev-feature-xyz.
```

The agent handles the API call, polls for the cluster to become available, and returns the connection string. What used to require a trip to the dashboard and a few minutes of waiting is now a prompt that runs in the background while you keep coding.

[DigitalOcean MCP documentation](https://docs.digitalocean.com/reference/api/mcp/) | [GitHub](https://github.com/digitalocean/digitalocean-mcp)

## Datadog MCP: observability

Datadog's MCP server went GA in March 2026 and connects your agent to logs, metrics, traces, monitors, and incident data. The use case that changes how you debug production issues most directly is being able to pull live observability context without leaving your editor.

Authentication uses a Datadog API key and application key:

```json
{
  "servers": {
    "datadog": {
      "command": "npx",
      "args": ["-y", "@datadog/mcp-server"],
      "env": {
        "DD_API_KEY": "your-api-key",
        "DD_APP_KEY": "your-app-key",
        "DD_SITE": "datadoghq.eu"
      }
    }
  }
}
```

**Prompt example: investigating a production error**

```
There's been a spike in 500 errors on the my-api service in the last 30 minutes. 
Fetch the relevant error logs from Datadog, identify the most common error message, 
and find the likely source in the codebase.
```

The agent queries Datadog for recent log events filtered by service and status, surfaces the top error, then searches your repo for the matching code path. You get a diagnosis with source context in one step.

**Prompt example: checking SLO health before a release**

```
Check the SLO compliance for the checkout service over the last 7 days. 
Is the error budget healthy enough to proceed with a release today?
```

[Datadog MCP documentation](https://docs.datadoghq.com/mcp_server/) | [GitHub](https://github.com/datadog-labs/mcp-server)

## Your agent at the centre

With these servers connected, your agent sits at the intersection of your entire developer stack. The diagram below shows the shape of it:

```
                        ┌──────────────────┐
                        │    AI Agent      │
                        │  (Copilot /      │
                        │   Claude /       │
                        │   Cursor)        │
                        └────────┬─────────┘
           ┌────────────┬────────┴────────┬────────────┐
           │            │                 │            │
  ┌────────▼───┐  ┌─────▼──────┐  ┌──────▼────┐  ┌────▼────────┐
  │ Atlassian  │  │ Buildkite  │  │DigitalOcean│  │  Datadog   │
  │ Rovo MCP   │  │    MCP     │  │    MCP    │  │    MCP     │
  │            │  │            │  │           │  │            │
  │ Jira       │  │ Pipelines  │  │ App       │  │ Logs       │
  │ Confluence │  │ Builds     │  │ Platform  │  │ Metrics    │
  │ Bitbucket  │  │ Jobs       │  │ Droplets  │  │ Traces     │
  │            │  │ Logs       │  │ Databases │  │ Monitors   │
  └────────────┘  └────────────┘  └───────────┘  └────────────┘
```

The value compounds as you add servers. A full loop becomes: fetch the ticket requirements from Jira, write the code, confirm the build passed in Buildkite, check the deployment is live on DigitalOcean, verify no new errors appeared in Datadog. That loop used to involve five browser tabs. With MCP it's a sequence of prompts in one context window.

## Where to start

Don't try to connect everything at once. Pick the system that costs you the most context switches today and start there. For most developers on a product team with CI, that's probably Buildkite: the failing build diagnosis pattern alone is worth the setup time.

Once that's working, add the next one. The pattern is the same across all of them: configure the server, connect your agent, and write a few prompts that match your actual workflow. The tools won't find your best use cases for you, but they make the ones you discover easy to act on.

## References

- [Atlassian Rovo MCP documentation](https://developer.atlassian.com/cloud/rovo-mcp/)
- [Atlassian Rovo MCP GitHub](https://github.com/atlassian/atlassian-mcp-server)
- [Buildkite MCP server documentation](https://buildkite.com/docs/apis/mcp-server)
- [Buildkite MCP server GitHub](https://github.com/buildkite/buildkite-mcp-server)
- [DigitalOcean MCP documentation](https://docs.digitalocean.com/reference/api/mcp/)
- [DigitalOcean MCP GitHub](https://github.com/digitalocean/digitalocean-mcp)
- [Datadog MCP documentation](https://docs.datadoghq.com/mcp_server/)
- [Datadog MCP GitHub](https://github.com/datadog-labs/mcp-server)
- [Model Context Protocol overview](https://modelcontextprotocol.io/)
