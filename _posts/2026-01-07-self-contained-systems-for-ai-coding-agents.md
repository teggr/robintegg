---
layout: post
title: "Self-Contained Systems for AI Coding Agents"
date: "2026-01-07"
description: "How the Self-Contained Systems architecture pattern provides optimal code structure for AI coding agents to understand and modify."
image: /images/scs-ai-agents.jpg
tags:
  - architecture
  - ai
  - scs
  - microservices
---

<blockquote class="bluesky-embed" data-bluesky-uri="at://did:plc:detb4ybuqm4rx75degde6pcq/app.bsky.feed.post/3ma4gvcm7wc2v" data-bluesky-cid="bafyreie7hf7p6nni5rgoytrp2aryrcsq45zakr7mw4cgghawvnmrqgbree" data-bluesky-embed-color-mode="system"><p lang="en">I believe that with AI agents, separated frontend and backend development is slowly coming to an end.
One language, one model, one flow. Less coordination, less friction, more focus on business logic.
AI does not just change how we write code. It changes how we should structure our systems.</p>&mdash; Simon Martinelli (<a href="https://bsky.app/profile/did:plc:detb4ybuqm4rx75degde6pcq?ref_src=embed">@martinelli.ch</a>) <a href="https://bsky.app/profile/did:plc:detb4ybuqm4rx75degde6pcq/post/3ma4gvcm7wc2v?ref_src=embed">December 16, 2025 at 3:16 PM</a></blockquote><script async src="https://embed.bsky.app/static/embed.js" charset="utf-8"></script>

Simon Martinelli's observation and my own experience sparked some further thinking about taking advantage of AI coding agents' potential better performance when working with repositories that contain both UI and domain logic together, and how this relates to broader architectural patterns.

Perhaps it's time to revisit the [Self-Contained Systems (SCS) architecture](https://scs-architecture.org/) pattern through the lens of AI coding agents. SCS provides a natural fit for how AI agents work—LLMs that generate or modify code perform best when they can reason about complete, self-contained slices of functionality. The co-location of UI, domain logic, and data that SCS promotes aligns remarkably well with what AI agents need to understand and modify code effectively.

## Why SCS matters for AI agents

AI coding agents perform optimally when several conditions are met:

1. **Context is localized** — they can see relevant code, UI, and data together in one place
2. **Cause and effect are visible** — changes have clear, testable outcomes within the system
3. **Dependency boundaries are explicit** — agents don't need to reason across opaque layers
4. **Contracts are clear** — URLs, APIs, events, and data schemas are well-defined

The SCS architecture pattern satisfies all of these conditions. Each system owns a full vertical slice—encompassing UI, domain logic, and database—and can be reasoned about in isolation. Systems communicate with each other only through explicit contracts or events.

## Key patterns that help agents

### UI and domain logic together

When UI components and backend logic live in the same codebase, AI agents can see the complete picture. An Orders SCS containing controllers, templates, and database access in one repository allows an agent to understand how user actions map to backend operations, generate UI changes that correctly call backend endpoints, and validate that data flows correctly from UI to persistence.

This co-location reduces hallucinations and improves the correctness of AI-generated code. An agent can see the complete flow from HTTP request to template rendering in a single context, making it easier to add new endpoints or modify existing ones correctly.

### Explicit contracts

AI agents thrive on explicit contracts between systems—REST APIs with clear schemas, event structures for asynchronous communication, and URL patterns for navigation. These contracts allow agents to safely generate code that integrates with other systems without needing to understand their internal implementation.

The key insight is that agents can work confidently within one SCS while respecting the boundaries of others through well-defined interfaces.

### Server-side rendering and HTML fragments

Server-side rendering (SSR) patterns are particularly agent-friendly. When one SCS needs to embed content from another, it can request HTML fragments as opaque contracts. AI agents can reason about embedding these fragments without needing to understand the full implementation details of each system—they simply need to know the fragment endpoint and how to include it.

## Advantages for AI-assisted development

When working with SCS architecture, AI agents gain several advantages:

1. **Localized reasoning** — agents read, update, and test one SCS without worrying about others
2. **Autonomous testing** — unit and integration tests live entirely within the SCS
3. **Predictable integration** — events, APIs, and URL contracts reduce guesswork
4. **Safe deployment** — agents can generate changes in one SCS independently
5. **Reduced coupling** — agents don't need to understand global state or shared databases

The synergy becomes clear: an agent can add a new feature entirely within a single SCS context, using only customer IDs or other identifiers as contracts to other systems, without needing to coordinate across multiple repositories or understand complex dependency chains.

## Challenges to consider

While SCS architecture works well with AI agents, there are some challenges:

**Cross-system workflows** require careful design. When a business process spans multiple systems, agents need clear guidance about event ordering and eventual consistency. Publishing events to notify other systems becomes a key pattern.

**Data duplication** means agents must understand when data is cached versus authoritative. For example, caching a customer name in an Order entity for display purposes requires the agent to understand this is read-only cached data.

**Contract evolution** requires versioning strategies that agents can follow. API versioning (e.g., `/api/v2/orders`) provides a clear signal to agents about compatibility and changes.

## The synergy between AI agents and SCS

A useful mental model is to think of each SCS as a mini-product or website, with AI agents as fast junior developers working inside it. Contracts and fragments are the only bridges to other systems.

This explains why agents could perform better with SCS than with heavily layered microservices or monoliths:

- **Everything needed for reasoning is co-located** — UI, logic, data, and tests are all visible
- **Hidden dependencies are minimized** — only explicit contracts connect systems
- **Navigation and UI integration are explicit** — no magic or framework conventions to learn
- **Testing can be done in isolation** — each SCS is independently testable

The architectural boundaries of SCS naturally align with the cognitive boundaries that AI agents work best within.

When building systems that will be maintained or extended by AI coding agents, the SCS architecture pattern provides a natural fit. It creates clear boundaries, explicit contracts, and localized reasoning—exactly what AI agents need to generate correct, maintainable code.

## References

- [Simon Martinelli's Bluesky post on AI agents and system structure](https://bsky.app/profile/did:plc:detb4ybuqm4rx75degde6pcq/post/3ma4gvcm7wc2v)
- [Self-Contained Systems](https://scs-architecture.org/)