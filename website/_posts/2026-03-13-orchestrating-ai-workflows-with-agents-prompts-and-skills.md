---
layout: post
title: "Orchestrating AI Workflows with Agents, Prompts, and Skills"
date: "2026-03-13"
description: "A mental model for AI orchestration that mirrors clean code principles: Agents define the workflow, Prompts execute a phase, and Skills perform the atomic actions."
image: /images/agents-prompts-skills.png
tags:
  - ai
  - github copilot
  - agents
  - workflow
---

Most developers treat GitHub Copilot like a smarter version of StackOverflow. You ask it a question, it gives you an answer, and you move on. But for complex engineering tasks—fetching a Jira ticket, planning a solution, implementing it with TDD, reviewing the diff, and tidying up—"vibes-based" prompting isn't enough. We need a system where **Strategy**, **Task**, and **Action** are strictly separated.

I've developed a mental model for AI orchestration that mirrors clean code principles: **Agents → Prompts → Skills**.

## The Architecture: A Chain of Command

To move from a simple chat to a production-ready workflow, I break responsibilities into three distinct layers:

| Layer | Role | Relationship |
| --- | --- | --- |
| **The Agent** | The Executive | Defines the **Workflow** and holds the global goal. |
| **The Prompt** | The Specialist | Executes a **Phase** of the work (e.g., Research or Implementation). |
| **The Skill** | The Worker | Performs the **Atomic Action** (e.g., CLI calls or File I/O). |

![Agents, Prompts and Skills architecture diagram](https://github.com/user-attachments/assets/7d236863-fc4a-4cf7-bedd-c5e689f7b64a)

The diagram shows how an Agent orchestrates multiple Prompts, each of which can invoke one or more Skills to perform specific actions.

## 1. The Agent: Defining the "Contract"

The Agent is the entry point. It doesn't do the work; it manages the **state** of the project. Think of it as the project manager that holds the global goal and guides the workflow from start to finish.

In a `workflow.agent.md`, the Agent is responsible for moving through a sequence of steps: fetching a Jira ticket, preparing the work, implementing the solution, reviewing the diff, and tidying up. It uses front matter to restrict which tools are available, giving it tight control over the environment. The Agent provides the "frontmatter" context that all subsequent prompts inherit, ensuring they all operate with the same understanding of the goal.

Critically, the Agent acts as a guardrail. It ensures the user doesn't jump straight to "Implementation" before "Research" is confirmed—asking for explicit confirmation before advancing to the next step. It maintains an overall understanding of the goals and access to the resources needed to achieve them: repository structure, ticket details, existing conventions.

```markdown
---
name: workflow
description: Multi-step development workflow agent
tools: allowed
---

# Workflow Agent

You are managing a development workflow. Guide the user through each phase:

1. **Fetch** - Get the Jira ticket details
2. **Prepare** - Research the codebase and propose a solution
3. **Implement** - Build the solution using TDD
4. **Review** - Diff check and quality control
5. **Tidy** - Clean up and create the feedback loop

Wait for confirmation before moving to the next step.
```

Agents are custom instructions that define how the AI should behave across the entire workflow. They are the "Why."

## 2. The Prompt: Contextual Delegation

When the Agent moves to a specific step, it calls a **Prompt**. Prompts are reusable commands—in `prepare-work.prompt.md`, the prompt's job is to analyse the ticket, assess the codebase, and propose a solution.

A Prompt is "Agent-aware" via its frontmatter (e.g., `agent: "Workflow"`) but focuses purely on a single reproducible task. It tells the AI exactly *how* to use its available tools—for instance, instructing it to `PAUSE` for human review after research is complete but before a solution is proposed. Prompts are component parts of the complete flow: reproducible tasks without wider context that can be composed into larger workflows.

```markdown
---
name: prepare-work
description: Research the codebase and propose a solution for the current ticket
agent: workflow
---

# Prepare Work

## Phase 1: Research

Invoke the `ticket-researching` skill to fetch the ticket details.

Once the skill completes, PAUSE and ask the user to confirm the research findings
before proceeding to the solution proposal.

## Phase 2: Proposal

Based on the research, propose a solution. Create a junior-developer-friendly
to-do list with clear, testable steps.
```

Prompts are executed within the context of the Agent, inheriting its state and tool restrictions. They are the "What."

## 3. The Skill: Atomic Capabilities

Skills are the "hands" of the system. A `ticket-researching` skill defines exactly how to fetch and validate a Jira ticket. Crucially, skills are typically **not user-invocable**. The human doesn't run the skill directly; the **Prompt** calls the **Skill**.

Skills are stateless resource bundles—fixed instructions for reproducible steps that act as shareable building blocks across different workflows. If a ticket lacks enough information, the Skill triggers an error state: "STOP (do not proceed to breakdown)". This prevents the Agent and Prompt from hallucinating a solution based on insufficient data. Skills also handle specific technical prerequisites, like ensuring the Jira CLI is installed or verifying the `repos/` directory structure is correct before doing any real work.

```markdown
---
name: ticket-researching
description: Fetch and validate a Jira ticket for development work
user-invocable: false
---

# Ticket Researching Skill

## Prerequisites

Ensure the Jira CLI is installed: `jira --version`

Check the `repos/` directory exists for workspace setup.

## Fetch Ticket

Run: `jira issue view {TICKET_ID} --plain`

## Validate

If the ticket description is insufficient for implementation:
STOP (do not proceed to breakdown). Report: "Ticket lacks sufficient detail."

Otherwise, summarise the ticket with acceptance criteria and technical notes.
```

Skills are the "How."

## Why This Three-Layer Relationship Matters

### 1. Separation of Concerns

You can swap your Jira CLI for a GitHub Issues CLI by updating one **Skill**. You don't have to touch your Agent or your Prompts. The workflow logic stays intact while the underlying tooling changes.

### 2. Safety through Delegation

By making Skills non-invocable by users, you ensure that complex logic—like repository context assessment or ticket validation—only runs within the structured environment of a vetted Prompt. There's no way to accidentally invoke a half-baked workflow.

### 3. Predictable Outcomes

The structured handoff creates a reliable pattern. The **Agent** asks: "What is our overall goal, and where are we in the workflow?" The **Prompt** asks: "What is the specific task for this phase, and how do I execute it?" The **Skill** asks: "What is the exact command or action, and did it succeed?" Each layer has one job and does it well.

### 4. Reusability

A `ticket-researching` skill can be used by a `prepare-work` prompt in a `workflow` agent, but also by a `quick-summary` prompt in a `standup` agent. Skills are building blocks that compose into larger workflows without duplication.

## Seeing It in Action

Here's what this looks like in practice with a multi-repository workflow. The Workflow Agent is activated and claims ticket JWT8, updating its status to "in progress". The `prepare-work` Prompt is invoked next, calling the `ticket-researching` Skill to fetch the ticket, validate the description, and return a structured summary.

At that point the Prompt **PAUSES**—the human reviews the research findings and confirms they're sufficient. The Prompt then generates a dry-run proposal: a junior-developer-friendly to-do list with clear, testable steps. Once the human confirms the proposal, the Workflow Agent moves to the implementation phase, where code is built using TDD, followed by a diff review and a tidy-up phase.

Finally, the Agent creates an audit log and a feedback loop to refine the workflow instructions for next time. Every action taken by the Agent, Prompt, and Skills is recorded, giving you a reproducible trace of exactly what happened and why.

## Summary

Building with AI isn't just about better prompting; it's about **System Design**. By treating Prompts as tasks and Skills as functions, we can build AI workflows that are as maintainable and robust as the code they help us write.

The mental model maps cleanly to clean code principles you already know. **Agents** are like your application's entry points—they own the "Why". **Prompts** are like your service layer—they orchestrate the "What". **Skills** are like your repository or infrastructure layer—they handle the "How".

When you separate these concerns, you get workflows that are testable, composable, and maintainable. You can debug a failing Skill without touching your Prompts or Agents. You can reuse Prompts across different Agents. You can update your tooling (swap Jira for Linear) by changing one Skill.

That's the kind of software design thinking that AI-assisted development actually needs.

## References

- [GitHub Copilot Agent Documentation](https://docs.github.com/en/copilot/concepts/agents)
- [GitHub Copilot Prompt Files](https://docs.github.com/en/copilot/customizing-copilot/using-prompt-files-with-github-copilot)
- [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang)
- [Self-Contained Systems for AI Coding Agents](/2026/01/07/self-contained-systems-for-ai-coding-agents)
