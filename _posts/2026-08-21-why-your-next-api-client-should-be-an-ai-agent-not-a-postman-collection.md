---
layout: post
title: "Why Your Next API Call Should Come From an Agent, Not a Postman Collection"
date: "2026-08-21"
description: "I see value shifting from maintaining Postman collections to letting agents derive API calls from intent, contracts, and live responses."
image: /images/agent-not-postman.jpg
tags:
  - ai
  - agents
  - api
  - workflow
  - testing
---

Postman has been the default API tooling for years. I still have it installed, the teams I work for use it and I still think it has a place in a developers setup. But I do not think the static Postman collection-first model delivers as much value as it used to.

The original reasons we leaned so hard on Postman collections in the first place were quickly sending pre-made requests, sharing workflow knowledge, and stitching flows together by hand. It was better than any other tooling at the time. I feel strongly now that I do not get anywhere near that value anymore, perhaps Postman has had its day.

Over time, many teams hit a kind of collection entropy: outdated requests, duplicate requests, abandoned variables, and folders full of historical cruft that no longer represent real workflows. The collection still exists, but the signal-to-noise ratio keeps dropping. So much so that I'd rather avoid using the tool completely unless I really need to.

What do I use instead? Instead of prebuilding and maintaining a large list of requests, I ask an agent on the fly to build requests from the code, the documentation, the live responses and API contracts.

## A day-to-day API scenario

Here is where the shift shows up in normal work. I need to create and validate an order flow, but I do not want to spend time hunting through old collections, reverse engineering half-relevant requests, or wiring variables across a chain that no longer matches current behavior.

The old flow is explicit, but manual:

```bash
curl -X POST http://localhost:8080/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice"}'

curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"<from previous response>"}'

curl -X POST http://localhost:8080/orders/<order-id>/payment \
  -H "Content-Type: application/json" \
  -d '{"method":"card","amount":49.99}'
```

That still works for deterministic checks, but in day-to-day development I now start from intent and let the agent derive the workflow from repository context, API contracts, and available test data:

```text
Using the API contract in this repo and the existing test data fixtures,
create me a new order request for customer Alice with 5 items
coming to a total value of £21.00.

Then:
1. show the exact HTTP request body and endpoint,
2. execute it against local,
3. return the response,
4. and suggest any follow-up requests I should run to validate order state.
```

The practical difference is that I am not pre-wiring every branch before I have evidence. The agent can discover operations, carry IDs across calls, adapt to state transitions, and surface the next checks I should run. I still set guardrails around scope, environment, and rate limits, but the heavy lifting moves from manual request choreography to intent-driven execution.

### Tip: make this reusable in the repo

If this prompt pattern works for your team, move it into a reusable skill in the repository instead of keeping it in chat history. Keep the skill aligned with your API docs, test fixtures, and repository instructions so it evolves with the codebase. Add some useful test data that matches your local or staging environment to improve the accuracy of the requests.

That turns one useful prompt into shared workflow knowledge. You are composing from existing code and instructions, not rebuilding request logic from scratch every time.

## API documentation becomes an execution asset

Leverage the documentation that already exists in your codebase. API docs are not just a handoff artefact for an upstream or client team, they are an internal execution asset for your own development, testing, and automation workflows.

When contracts are clear, agents can do more with less prompting:

- discover operations safely
- understand required fields and auth modes
- infer legal transitions
- generate targeted tests
- explain failures in terms of the contract

Even a small OpenAPI fragment gives an agent usable structure:

```yaml
paths:
  /orders:
    post:
      summary: Create order
      responses:
        "201":
          description: Order created
  /orders/{id}/cancel:
    post:
      summary: Cancel order
      parameters:
        - in: path
          name: id
          required: true
          schema:
            type: string
```

Once that contract is in place, the same context supports multiple tasks: exploratory testing, integration debugging, and client code scaffolding. The documentation turns into shared operational context for both people and tooling.

I wrote about the potential exploratory efficiencies in my [Hypermedia APIs for Autonomous Agents](/2026/06/03/hypermedia-apis-for-autonomous-agents.html) article about documenting apis and making your apis more expressive and help the agents be more efficient and accurate with their requests.

## Summary

Postman collections are still useful for deterministic regression checks, onboarding examples, and shared workflows that must stay explicit. Perhaps now, it's more for users who aren't in a codebase every day or aren't keen on embracing new AI approaches or tooling.

What I am changing is my default for exploratory API work, smoke testing. In fact any type of non-determinitic work. Instead of maintaining large hand-curated collections, I let an agent derive calls from intent, repository context, API contracts, and test data. This lowers the bar for all kinds of testing and automation and ultimately begins to provide value to me.

## References

- [Postman Learning Center](https://learning.postman.com/)
- [OpenAPI Specification](https://spec.openapis.org/oas/latest.html)
- [Hypermedia APIs for Autonomous Agents](/2026/06/03/hypermedia-apis-for-autonomous-agents.html)