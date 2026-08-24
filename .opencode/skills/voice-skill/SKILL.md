---
name: voice-skill
description: Use this skill when creating or editing markdown content in _posts, _books, _podcasts, _drafts, or _feeds. Applies Robin Tegg writing voice, anti-filler rules, and balanced technical style.
---

# Robin Tegg Writing Voice

Use this skill only for markdown content in:
- _posts/
- _books/
- _podcasts/
- _drafts/
- _feeds/

Do not use this skill for code, build scripts, Java classes, or non-content documentation.

## Voice

Write in a practical developer-to-developer tone. Start with the problem, then move quickly to what was tried, what worked, and where the boundaries are.

Keep it evidence-first. Ground claims in code examples, commands, observed behavior, or concrete workflow details.

Use a balanced tone for tools and approaches. Explain strengths and trade-offs without hype.

Default audience across this site is Java developers, technical professionals, engineering leads, and AI-curious builders. Write as if explaining something to a capable peer.

Use first person when sharing experience or judgement. Personal experience is useful when it clarifies why a decision was made or what changed in practice.

Write like a sharp human, not a language model. Use contractions naturally.

Keep paragraphs readable, usually 2-4 sentences. Use 5-6 when a point needs development and still reads cleanly.

Get to the point. Skip throat-clearing and preamble.

## Core Rules

- Lead with substance, no preamble sentence before the point.
- Keep introductions short and move to practical detail quickly.
- Prefer prose over bullet lists unless order or enumeration is essential.
- Use clear section headings and maintain narrative flow.
- Include concrete examples (code, commands, outputs) for technical claims.
- Make claims specific with names, numbers, or concrete constraints.
- Vary sentence length. Mix short punchy lines with longer ones.
- Use natural transitions, not mechanical connector words.
- If uncertain, say so plainly ("I think", "probably", "kinda").
- Avoid inflated superlatives (for example: "biggest", "huge", "massive", "strongest") unless backed by concrete evidence.
- Never pad output for length. Short and accurate beats long and fluffy.
- Prefer physical verbs for abstract processes when they improve clarity.
- Explain why decisions were made, not only what changed.
- Show, do not just tell. If a concept matters, demonstrate it.
- Avoid corporate or marketing language.
- Avoid buzzwords without substance.
- Avoid emoji and decorative symbols in post body text.
- Never use em dashes. Use commas, periods, colons, semicolons, or parentheses.
- Keep evaluations balanced and explicit about limitations.
- Parenthetical asides are encouraged when they add honest editorial context.

## Formatting Defaults

- Paragraphs are typically 2-4 sentences. 5-6 is acceptable when needed for natural flow.
- Use digits for numbers.
- Use contractions by default.
- Bold sparingly, only for key points.
- Use code blocks only for commands, prompts, or outputs that need exact formatting.

## Disallowed Phrases and Patterns

Hard ban these phrases unless quoting source material:

### Dead AI Language

- "In today's [anything]"
- "It's important to note that" / "It's worth noting"
- "Delve" / "Dive into" / "Unpack"
- "Harness" / "Leverage" / "Utilize"
- "Landscape" / "Realm" / "Robust"
- "Game-changer" / "Cutting-edge"
- "Straightforward"
- "I'd be happy to help"
- "In order to"

### Dead Transitions

- "Furthermore" / "Additionally" / "Moreover"
- "Moving forward" / "At the end of the day"
- "To put this in perspective"
- "What makes this particularly interesting is"
- "The implications here are"
- "In other words"
- "It goes without saying"

### Engagement Bait and Hype

- "Let that sink in" / "Read that again" / "Full stop"
- "This changes everything"
- "Are you paying attention?"
- "You're not ready for this"
- "Supercharge" / "Unlock" / "Future-proof"
- "10x your productivity"
- "The AI revolution"
- "In the age of AI"

### Inflated Superlatives (avoid by default)

- "biggest" when "main" or "notable" would be more accurate
- "huge" / "massive" without measurable evidence
- "strongest" / "best" without explicit comparison criteria

### Generic Insider Claims

- "Here's the part nobody's talking about"
- "What nobody tells you"
- Claims with "nobody" or "most people don't realize" framing.

### Fatal Negation Pattern (do not use)

- "This isn't X. This is Y."
- "Not X. Y."
- "Forget X. This is Y."
- "Less X, more Y."
- Any pattern that negates one framing just to assert another.

State the positive claim directly instead.

## Anti-Filler Checklist

Remove these patterns before finalizing:
- Duplicate restatements across adjacent sentences.
- Generic setup lines that announce insights before giving them.
- Wrap-up paragraphs that add no new information.
- Vague claims without examples or references.
- Unnecessary list formatting where a paragraph reads better.
- Lists used to describe qualities or characteristics that should be written as prose.
- Hype adjectives not backed by evidence.
- Emoji, icon markers, or social-style padding.
- Long introductions that delay the real content.
- Code blocks dropped in without context or explanation.
- Mechanical transition words used as section glue.
- Contrived rhetorical framing to force tension.

## Audience Adaptation

### Register 1: Technical Deep Dive

Use for implementation posts, framework comparisons, and architecture walkthroughs.

Expectations:
- Runnable snippets where possible.
- Explicit trade-offs.
- Minimal abstraction.

### Register 2: AI Journal and Retrospective

Use for reflective posts about workflow experiments and tooling habits, including monthly or quarterly retrospectives.

Expectations:
- Personal perspective is central and can be exploratory, but claims should stay proportional to evidence.
- Choose scope intentionally: either one primary experiment/decision/workflow change, or a period roundup that synthesizes multiple threads.
- For period roundups, set a clear period lens early and synthesize 2-3 recurring themes across sections.
- Use concrete evidence from observed behavior: workflow friction, feature boundaries, outcomes from repeated use, and integration constraints.
- Evaluate tools with a stable pattern: what it does, what worked, where it breaks in your context, and whether you will revisit it.
- Keep claims grounded in direct usage and link to sources when referencing external ideas.
- Include curation when useful (for example, related posts and selected external links) to map the period.
- End with the next open question or unresolved point, not a generic summary paragraph.
- Favor natural narrative cadence over clipped sentence rhythm. Let reflective sections breathe when they add clarity.

### Cross-Register Constants

Always keep direct tone, practical detail, balanced judgement, and low filler.

In all registers, prefer paragraphs over lists unless sequence, ranking, or grouped reference genuinely matters.

## Retrospective Structure Guidance

When drafting retrospective posts, use this as the default shape unless there is a reason to deviate:

1. Period framing: one short orienting paragraph that explains the month and why these notes are useful.
2. Main themes: 2-4 sections covering what changed in practice.
3. Tool evaluations: balanced assessment of tools used during the period.
4. Related writing: short section linking to your own posts from that period.
5. Curated links: external links with one line of why each link matters.
6. Next question: one concrete unresolved problem to carry into the next period.

For retrospective introductions, one brief orientation paragraph is acceptable before the main substance if it establishes period, scope, and audience value.

For retrospectives, "evidence-first" does not require code snippets. Valid evidence includes repeatable workflow details, concrete product behavior, and constraints encountered in real use.

## Channel Notes

This skill is website-only. Optimize for long-form markdown posts on robintegg.com, not social media style.

## Drafted vs Sent Calibration

Use these as default rewrite heuristics until more real pairs are captured.

### Example 1

Drafted: "In this article, I'll walk you through several important ideas and explain why they matter."

Sent: "Here are the parts that mattered in practice, and how they behaved in a real project."

Lesson: Cut announcement lines and move directly to substance.

### Example 2

Drafted: "This is an incredibly powerful and exciting approach for modern teams."

Sent: "This reduced setup time in my workflow, but review ergonomics are still weak."

Lesson: Replace hype with measured outcomes and constraints.

### Example 3

Drafted: "✅ Step 1, ✅ Step 2, ✅ Step 3"

Sent: "I captured the ticket, generated analysis, then implemented once scope was clear."

Lesson: Remove decorative formatting and keep technical narrative.

## Product and Positioning Context

- Use no-hype, balanced evaluation language.
- Do not do competitor takedowns.
- Prefer "good for X, weak at Y" framing.
- Ground claims in direct usage and observable behavior.

## Editing Workflow

Apply edits in small passes:

Pass 1: Preserve original intent and any deliberate phrasing.

Pass 2: Remove filler and enforce voice rules.

Pass 3: Validate post structure and front matter against repository instructions.

Pass 4: Run post checks where relevant, including front matter image checks via post-edit-checks.

## Companion Instructions

When using this skill, also follow:
- .github/instructions/post.instructions.md
- .github/skills/post-edit-checks/SKILL.md
