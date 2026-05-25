---
layout: post
title: "A Discovery Agent for Your VSCode Workflow"
date: "2026-05-13"
image: /images/discovery-agent.png
tags:
  - ai
  - vscode
  - github copilot
  - agents
  - workflow
---

Plan mode in VSCode Copilot is often too solution-oriented. It reads the context, proposes steps, and gets going. That works when the problem is well-understood, but a lot of real tickets are not. The scope is fuzzy, the dependencies are not fully mapped, and the plan reflects whatever the model thought at first glance rather than what the problem actually requires.

Ask mode sits at the other end. It is conversational and useful for small lookups, but a complex question gets a short answer and you are back to guessing the rest.

The gap between them is discovery: a phase where you investigate without committing to a solution. Not "here is what we will do" and not "answer this one thing", but "let us understand what we are actually dealing with before we plan anything."

## The Discovery Agent

I built a custom VSCode agent for this and published it as a gist.

[gist.github.com/teggr/9e7cf87f41c9112d0cd0cafbb56dc6c3](https://gist.github.com/teggr/9e7cf87f41c9112d0cd0cafbb56dc6c3)

The agent is called Discovery. Its sole responsibility is investigation, not planning and not implementation. It maintains at `/memories/session/discovery.md`. It has no access to editing but has execution which helps replicate issues.

The agent moves through five phases iteratively:

- **Problem Framing** captures the goal, success criteria, and known constraints before any searching begins.
- **Context Discovery** runs Explore subagents in parallel across the relevant parts of the codebase, gathering analogous patterns, integration points, and existing tests.
- **Alignment** surfaces ambiguity that would otherwise derail a plan, using `vscode/askQuestions` when a question matters enough to block a confident conclusion.
- **Synthesis** produces a structured Discovery Brief saved to memory, covering verified facts, open questions, risks, scope boundaries, and decision points that must be resolved before planning.
- **Refinement** keeps that brief current as new information changes the picture.

The output is not a plan. It is the input to a plan. A shared understanding that you and the model both hold before any implementation starts.

## How It Fits in the Workflow

The discovery agent does not operate in isolation. It is one phase in a sequence, and the phases before and after it matter as much as the agent itself.

### Step 1: Load the Ticket Context

Before opening the discovery agent, I use the Atlassian MCP integration to pull the ticket into the session. A simple tool call fetches the Jira issue content, acceptance criteria, and any linked tickets or comments, and makes it available as context. This takes seconds and means the discovery session starts from the actual ticket rather than from memory or a paraphrased description.

Without this step, the discovery agent is working from whatever you typed into the prompt. With it, the agent can reference the real requirements, spot gaps in the acceptance criteria, and flag anything that looks underspecified before you have invested time in the wrong direction.

### Step 2: Run Discovery

With the ticket in context, you invoke the discovery agent and describe what you are investigating. You can request a quick pass, a medium-depth investigation, or a thorough sweep depending on how unfamiliar the area is.

The agent explores the codebase, reads related tests and documentation, follows integration points, and asks clarifying questions if the scope is ambiguous. It does not propose solutions. When it finds something relevant, it records it. When it finds a gap or a risk, it records that too. By the end, the Discovery Brief in `/memories/session/discovery.md` contains a clear statement of what is known, what is assumed, what is open, and what needs a decision before planning can begin.

For familiar territory this might take a few minutes. For a ticket that touches unfamiliar systems or has poorly scoped requirements, running discovery before planning is where you recoup that time.

### Step 3: Plan

Planning after discovery is different from planning cold. The Discovery Brief gives the planning phase verified facts to build on, a list of open questions that need decisions, and a map of where the work actually touches the codebase. The plan addresses real dependencies rather than guessed ones, and the gaps it needs to fill are much smaller.

I find that if the discovery phase has covered the ground well, the plan comes together quickly and requires almost no back-and-forth to refine. The model is not exploring while planning. It already knows what it found.

### Step 4: Implement

By the time implementation starts, you have a grounded plan backed by discovery. The model is not encountering the codebase for the first time and making assumptions to fill the gaps. Most of the uncertainty has already been resolved.

This reduces the frequency of the mid-implementation surprises that cause rework: the dependency that was not considered, the existing pattern that should have been followed, the integration point that behaves differently from what was assumed. Implementation becomes less about the AI figuring out what to do and more about executing what has already been understood.

## Different Types of Task

The benefit of discovery varies with the type of work.

For new features, discovery maps out where the new code should live, what existing patterns it should follow, and where integration with other systems will be required. Planning after that is a matter of filling in the specific steps within a structure that is already understood. Implementation follows a clear path rather than being interrupted by decisions that should have been made earlier.

For debugging, the value is even more direct. Debugging is fundamentally a discovery problem. You are trying to understand why something behaves differently from what is expected. The discovery agent is well suited to this because it is specifically designed to gather evidence, map related areas, and surface assumptions before drawing conclusions. Running discovery on a bug report means the session starts by asking what is actually known versus what is assumed, tracing the path from symptom to potential cause, and identifying which tests already cover the relevant behaviour. A plan for fixing the bug then addresses the actual root cause rather than the first plausible explanation.

For refactoring, discovery identifies the scope of the change, the areas that will be affected, and the tests that currently define the expected behaviour. That boundary-mapping is exactly what you need before touching shared code.

## References

- [Discovery Agent Gist](https://gist.github.com/teggr/9e7cf87f41c9112d0cd0cafbb56dc6c3)
- [Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills)
- [AI Feedback: Close the Loop](/2026/03/14/ai-feedback-close-the-loop)
- [Atlassian MCP Documentation](https://www.atlassian.com/platform/mcp)
