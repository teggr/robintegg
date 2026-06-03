---
layout: post
title: "Hypermedia APIs for Autonomous Agents"
date: "2026-06-03"
description: "An extended write-up of the hypermedia API hypothesis for agent workflows, with Spring HATEOAS HAL-FORMS and repo-backed evidence."
image: /images/spring-ai-mcp-demo.jpg
tags:
  - ai
  - java
  - spring
  - spring hateoas
  - hypermedia
---
Agent tooling has a maintenance problem. We publish wrappers, tool schemas, and skill definitions, then spend time keeping them aligned with the live API. When they drift, the agent starts guessing.

This post extends the README from [hypermedia-apis-for-autonomous-agents](https://github.com/teggr/hypermedia-apis-for-autonomous-agents) and explains why I think hypermedia is a better default for stateful agent workflows.

If you want the short version first, read the project README: [Hypermedia APIs for Autonomous Agents](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/README.md). The rest of this article adds implementation context and practical boundaries.

## The hypothesis

The hypothesis is simple and testable:

1. Raw API usage without discoverable contracts creates the most ambiguity.
2. OpenAPI improves discoverability and usually improves speed and accuracy.
3. Hypermedia, especially HAL-FORMS affordances, improves stateful flows further because valid next actions are returned with the current resource state.

The key point is not that wrappers are bad. It is that wrappers describe global capability, while hypermedia can describe the context-valid next move right now.

## Why hypermedia is interesting for agents

Most agent mistakes in multi-step workflows are transition mistakes. The model can usually call an endpoint. The harder part is deciding if that call is valid for the current state.

A static tool catalog does not naturally encode runtime state transitions. A hypermedia response can. That shifts some decision pressure from prompt engineering and middleware curation into server-published affordances.

In practice, this means the server can tell the agent, "here are the actions currently available for this order", instead of asking the agent to infer that from a global operation list.

## The project and what it compares

The repository is here: [teggr/hypermedia-apis-for-autonomous-agents](https://github.com/teggr/hypermedia-apis-for-autonomous-agents).

I implemented the same order-management domain in two styles:

- Conventional API: [`reference-services/conventional-api`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/tree/main/reference-services/conventional-api)
- Hypermedia API: [`reference-services/hypermedia-api`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/tree/main/reference-services/hypermedia-api)

Scenarios S1 to S6 were then exercised via Copilot-driven prompts, with service-side measurements captured before and after each run.

Operational details and run steps are in [`test-plans/experiment-runbook.md`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/test-plans/experiment-runbook.md), and the current manual analysis is in [`test-plans/manual-results`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/tree/main/test-plans/manual-results).

## HAL-FORMS in Spring HATEOAS

The hypermedia side uses Spring HATEOAS. If you're working in Java and Spring, this is the practical way to model affordances without inventing your own format.

The HAL-FORMS reference is here: [Spring HATEOAS HAL-FORMS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms).

HAL-FORMS extends HAL by adding templated actions (`_templates`) to a representation. Those templates can express what inputs are required for the next action and which transitions are valid in the current state.

That is useful for an agent because the representation now carries both state and action hints. The client no longer needs to reconstruct the full workflow from static docs plus guesswork.

## What changed in behavior

From the current S1 to S6 directional data, the hypermedia implementation usually showed lower API-call pressure and fewer invalid attempts in the more stateful scenarios.

The conventional implementation was more sensitive to discovery behavior, especially when non-domain calls crept in.

These results are directional, not final proof. Still, they support the architectural intuition: state-aware affordances can reduce transition ambiguity.

If you want to inspect the evidence directly, start with:

- [`summary.md`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/test-plans/manual-results/summary.md)
- [`metrics-deltas-adjusted.csv`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/test-plans/manual-results/metrics-deltas-adjusted.csv)
- [`normalization-notes.md`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/test-plans/manual-results/normalization-notes.md)
- [`verdict-draft.md`](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/test-plans/manual-results/verdict-draft.md)

## Where this fits and where it does not

I still think OpenAPI and wrappers have a place. For simple, stable workflows they are often enough, and they are easier to adopt incrementally.

The case for hypermedia gets stronger when workflows are stateful, long-lived, and expensive to recover after invalid transitions. In those cases, publishing valid next actions from the server can lower ambiguity and reduce coupling to constantly updated middleware catalogs.

So the practical recommendation for now is:

- Keep static contracts for simple and stable paths.
- Use hypermedia where state transitions are the main failure mode.
- Treat wrappers as accelerators, not the primary source of workflow truth.

## What I want to test next

The next step is tighter validation with a larger scenario set and stricter controls around discovery noise. I also want to test mixed strategies, where OpenAPI handles broad discovery and HAL-FORMS handles the stateful transition-heavy segments.

If you're evaluating agent architecture in Java systems, this repo is a good starting point for your own comparison runs:

- [Repository](https://github.com/teggr/hypermedia-apis-for-autonomous-agents)
- [README](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/README.md)
- [Spring HATEOAS HAL-FORMS docs](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms)
