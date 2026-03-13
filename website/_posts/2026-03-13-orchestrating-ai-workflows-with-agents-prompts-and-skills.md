---
layout: post
title: "Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills"
date: "2026-03-13"
description: "A mental model for AI orchestration that mirrors clean code principles: Agents define the workflow, Prompts execute a phase, and Skills perform the atomic actions."
image: /images/agents-prompts-skills.png
tags:
  - ai
  - github copilot
  - agents
  - workflow
---
This article is a practical guide to thinking about AI workflow design using three Copilot primitives: **Agents**, **Prompts**, and **Skills**. 

We'll explore how separating **Strategy**, **Task**, and **Action** enables you to build workflows for complex multi-step engineering scenarios - like fetching a Jira ticket, planning a solution, implementing with TDD, reviewing diffs, and tidying up. By understanding and applying these primitives, you can orchestrate AI systems that are maintainable, testable, and composable.

I've developed a mental model for AI orchestration that mirrors similar coding principles: **Agents → Prompts → Skills**.

## The Architecture: A Chain of Command

Each primitive - Agent, Prompt, and Skill - can be powerful when used independently. However, assigning clear roles and responsibilities to each helps scale workflows with greater consistency and maintainability.

To build a workflow, I break responsibilities into three distinct layers:

<table>
  <thead>
    <tr>
      <th>Layer</th>
      <th>Role</th>
      <th>Relationship</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><strong>Agent</strong></td>
      <td>The Executive</td>
      <td>Defines the <strong>Workflow</strong> and holds the global goal.</td>
    </tr>
    <tr>
      <td><strong>The Prompt</strong></td>
      <td>Specialist</td>
      <td>Executes a <strong>Phase</strong> of the work (e.g., Research or Implementation).</td>
    </tr>
    <tr>
      <td><strong>The Skill</strong></td>
      <td>Worker</td>
      <td>Performs the <strong>Atomic Action</strong> (e.g., CLI calls or File I/O).</td>
    </tr>
  </tbody>
</table>

![Agents, Prompts and Skills architecture diagram](https://github.com/user-attachments/assets/7d236863-fc4a-4cf7-bedd-c5e689f7b64a)

The diagram shows how an Agent orchestrates multiple Prompts, each of which can invoke one or more Skills to perform specific actions.

## 1. The Agent: Goal oriented context

The Agent is the entry point. It doesn't do the work; it manages the **state** of the project. Think of it as the project manager that holds the global goal and guides the workflow from start to finish. It can suggest next prompts to invoke or the next step to move to.

It sets the tone for the workflow - what are we doing? how can we go about it? what should we be doubling down on during the workflow?

In a `workflow.agent.md`, the Agent has a sequence of steps to reference: fetching a Jira ticket, preparing the work, implementing the solution, reviewing the diff, and tidying up. It will also have rules and instructions specific to the workflow goal.

The Agent provides the context that all subsequent prompts in our session will inherit, ensuring they all operate with the same understanding of the goal.

Here's an example:

```markdown
---
name: workflow
description: Multi-step development workflow agent
---

# Workflow Agent

You are managing a development workflow. Guide the user through each phase:

1. **Fetch** - Get the Jira ticket details (Prompt: /fetch)
2. **Prepare** - Research the codebase and propose a solution (Prompt: /prepare)
3. **Implement** - Build the solution using TDD (Prompt: /implement)
4. **Review** - Diff check and quality control (Prompt: /review)
5. **Tidy** - Clean up and create the feedback loop (Prompt: /tidy)

Wait for confirmation before moving to the next step. Always ask if in doubt.
Pick quality over speed of delivery
Use the suggested prompts
```

Agents are custom instructions that define how the AI should behave across the entire workflow. They are the "Why."

## 2. The Prompt: Contextual Delegation

When the Agent thinks it's time to move it can suggest a **Prompt** to use. Prompts are reusable commands - in `prepare-work.prompt.md`, the prompt's job is to analyse the ticket, assess the codebase, and propose a solution.

A Prompt can be "Agent-aware" via its frontmatter (e.g., `agent: "Workflow"`) but focuses purely on a single reproducible task. It tells the AI exactly *how* to use tools and skills. For instance, instructing it to `PAUSE` for human review after research is complete but before a solution is proposed. Prompts are component parts of the complete flow: reproducible tasks with potentially smaller context that can be composed into larger workflows.

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

Skills are the "hands" of the system. A `ticket-researching` skill defines exactly how to fetch and validate a Jira ticket. Crucially, skills are typically in this model **not user-invocable**. The human doesn't run the skill directly; the **Prompt** calls the **Skill**.

Skills are stateless resource bundles - fixed instructions for reproducible steps that act as shareable building blocks across different workflows. If a ticket lacks enough information, the Skill triggers an error state: "STOP (do not proceed to breakdown)". This prevents the Agent and Prompt from hallucinating a solution based on insufficient data. Skills also handle specific technical prerequisites, like ensuring the Jira CLI is installed before doing any real work.

```markdown
---
name: ticket-researching
description: Fetch and validate a Jira ticket for development work
user-invocable: false
---

# Ticket Researching Skill

## Prerequisites

Ensure the Jira CLI is installed: `jira --version`

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

By making Skills non-invocable by users, you ensure that complex logic - like repository context assessment or ticket validation - only runs within the structured environment of a vetted Prompt. There's no way to accidentally invoke a half-baked workflow.

### 3. Predictable Outcomes

The structured handoff creates a reliable pattern. The **Agent** asks: "What is our overall goal, and where are we in the workflow?" The **Prompt** asks: "What is the specific task for this phase, and how do I execute it?" The **Skill** asks: "What is the exact command or action, and did it succeed?" Each layer has one job and does it well.

### 4. Reusability

A `ticket-researching` skill can be used by a `prepare-work` prompt in a `workflow` agent, but also by a `quick-summary` prompt in a `standup` agent. Skills are building blocks that compose into larger workflows without duplication.

## Summary

Building with AI isn't just about better prompting; it's about **System Design**. By treating Prompts as tasks and Skills as functions, we can build AI workflows that are as maintainable and robust as the code they help us write.

The mental model maps cleanly to clean code principles you already know. **Agents** are like your application's entry points - they own the "Why". **Prompts** are like your service layer - they orchestrate the "What". **Skills** are like your repository or infrastructure layer - they handle the "How".

When you separate these concerns, you get workflows that are testable, composable, and maintainable. You can debug a failing Skill without touching your Prompts or Agents. You can reuse Prompts across different Agents. You can update your tooling (swap Jira for Linear) by changing one Skill.

That's the kind of software design thinking that AI-assisted development actually needs.

## References

- [GitHub Copilot Agent Documentation](https://docs.github.com/en/copilot/concepts/agents)
- [GitHub Copilot Prompt Files](https://docs.github.com/en/copilot/customizing-copilot/using-prompt-files-with-github-copilot)
- [Bring your Java skills to AI Agent Skills with JBang](/2026/01/30/bring-your-java-skills-to-ai-agent-skills-with-jbang)
- [Self-Contained Systems for AI Coding Agents](/2026/01/07/self-contained-systems-for-ai-coding-agents)
