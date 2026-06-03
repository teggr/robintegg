---
layout: post
title: "Hypermedia APIs for Autonomous Agents"
date: "2026-06-03"
description: "An extended write-up of the hypermedia API hypothesis for agent workflows with Spring HATEOAS and HAL-FORMS."
image: /images/hypermedia-apis-for-agents.jpg
tags:
  - ai
  - java
  - spring
  - spring hateoas
  - hypermedia
---
Agent tooling to access APIs has a maintenance problem. We publish cli wrappers, mcp tools, and skill definitions, then spend time keeping them aligned with the live production HTTP API. Out of sync local clients cause non-deterministic errors and reduces the agent's speed and accuracy.

This post walks through an experiment I ran using the [hypermedia-apis-for-autonomous-agents repository](https://github.com/teggr/hypermedia-apis-for-autonomous-agents) to see if agents could understand HTTP APIs better and explains why I think hypermedia is a better default for implementing APIs for stateful agent workflows.

If you want to read more about the experiment, read the project README: [Hypermedia APIs for Autonomous Agents](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/README.md). The rest of this article adds implementation context and practical boundaries.

## The hypothesis

The hypothesis is simple and testable:

1. Raw HTTP API usage without discoverable contracts creates the most ambiguity.
2. Adding an OpenAPI spec improves discoverability and usually improves speed and accuracy.
3. Hypermedia, like HAL-FORMS with it's affordances, improves client understanding, navigation and correctness of stateful flows further because valid next actions are returned with the current resource state.

The key point is not that wrappers are bad. It is that wrappers don't contain all the context, while hypermedia can describe the context-valid next move right now.

## Why hypermedia is interesting for agents

Most agent mistakes in multi-step workflows are transition mistakes. The model can usually call an endpoint. The harder part is deciding if that call is valid for the current state.

A static tool catalog does not naturally encode runtime state transitions. A hypermedia response can. That shifts some decision pressure from prompt engineering and middleware curation into server-published affordances.

In practice, this means the server can tell the agent, "here are the actions currently available for this order", instead of asking the agent to infer that from a global operation list.

## The project and what it compares

I implemented the same order-management domain in two styles: a conventional endpoint-driven API and a hypermedia API that publishes state-aware affordances. Both implementations exposed equivalent business capabilities so the comparison focused on client behavior, not domain scope.

I then ran a set of Copilot-driven tasks against both services and measured how each style behaved under realistic workflow pressure. The tests included common order lifecycle operations such as creating an order, adding and updating items, progressing and cancelling orders, and handling invalid transitions. For each run, I captured service-side metrics before and after execution to compare call volume, invalid attempts, and workflow completion behavior.

## HAL-FORMS in Spring HATEOAS

The hypermedia side uses Spring HATEOAS. If you're working in Java and Spring, this is the practical way to model affordances without inventing your own format.

The HAL-FORMS reference is here: [Spring HATEOAS HAL-FORMS](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms).

HAL-FORMS extends HAL by adding templated actions (`_templates`) to a representation. Those templates can express what inputs are required for the next action and which transitions are valid in the current state.

That is useful for an agent because the representation now carries both state and action hints. The client no longer needs to reconstruct the full workflow from static docs plus guesswork.

This is the kind of response shape I want an agent to consume:

```json
{
  "_links": {
    "self": { "href": "/orders/123" },
    "cancel": { "href": "/orders/123/cancel" }
  },
  "status": "CREATED",
  "_templates": {
    "cancel": {
      "method": "POST",
      "properties": [
        { "name": "reason", "required": true }
      ]
    }
  }
}
```

And this is the corresponding call pattern:

```bash
curl -H "Accept: application/prs.hal-forms+json" \
  http://localhost:8080/orders/123
```

## What changed in behavior

Across the current test runs, the hypermedia implementation generally produced fewer unnecessary API calls and fewer invalid state transitions in multi-step order workflows. In practical terms, when the server published valid next actions from the current resource state, the agent made fewer guess-driven moves and completed flows with less correction overhead.

The conventional implementation was more sensitive to discovery noise and more likely to drift into less relevant calls before converging on the right path. These findings are still directional rather than definitive, but they support the core architectural claim: state-aware affordances appear to reduce transition ambiguity and improve workflow reliability for agent-driven interactions.

## Where this fits and where it does not

I still think OpenAPI and wrappers have a place. For simple, stable workflows they are often enough, and they are easier to adopt incrementally.

The case for hypermedia gets stronger when workflows are stateful, long-lived, and expensive to recover after invalid transitions. In those cases, publishing valid next actions from the server can lower ambiguity and reduce coupling to constantly updated middleware catalogs.

So the practical recommendation for now is:

- Keep static contracts for simple and stable paths.
- Use hypermedia where state transitions are the main failure mode.
- Treat wrappers as accelerators, not the primary source of workflow truth.

If you're evaluating agent use of your APIs, this repo is a good starting point for your own comparison runs:

- [Repository](https://github.com/teggr/hypermedia-apis-for-autonomous-agents)
- [README](https://github.com/teggr/hypermedia-apis-for-autonomous-agents/blob/main/README.md)
- [Spring HATEOAS HAL-FORMS docs](https://docs.spring.io/spring-hateoas/docs/current/reference/html/#mediatypes.hal-forms)
