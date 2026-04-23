---
name: voice-skill
description: Use this skill when creating or editing markdown content in website/_posts, website/_books, website/_podcasts, website/_drafts, or website/_feeds. Applies Robin Tegg writing voice, anti-filler rules, and balanced technical style.
---

# Robin Tegg Writing Voice

Use this skill only for markdown content in:
- website/_posts/
- website/_books/
- website/_podcasts/
- website/_drafts/
- website/_feeds/

Do not use this skill for code, build scripts, Java classes, or non-content documentation.

## Voice

Write in a practical developer-to-developer tone. Start with the problem, then move quickly to what was tried, what worked, and where the boundaries are.

Keep it evidence-first. Ground claims in code examples, commands, observed behavior, or concrete workflow details.

Use a balanced tone for tools and approaches. Explain strengths and trade-offs without hype.

Default audience across this site is Java developers, technical professionals, engineering leads, and AI-curious builders. Write as if explaining something to a capable peer.

Use first person when sharing experience or judgement. Personal experience is useful when it clarifies why a decision was made or what changed in practice.

## Core Rules

- Lead with substance, no preamble sentence before the point.
- Keep introductions short and move to practical detail quickly.
- Prefer prose over bullet lists unless order or enumeration is essential.
- Use clear section headings and maintain narrative flow.
- Include concrete examples (code, commands, outputs) for technical claims.
- Explain why decisions were made, not only what changed.
- Show, do not just tell. If a concept matters, demonstrate it.
- Avoid corporate or marketing language.
- Avoid buzzwords without substance.
- Avoid emoji and decorative symbols in post body text.
- Avoid em dash as a clause separator in prose, use periods, commas, or parentheses.
- Keep evaluations balanced and explicit about limitations.

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

## Audience Adaptation

### Register 1: Technical Deep Dive

Use for implementation posts, framework comparisons, and architecture walkthroughs.

Expectations:
- Runnable snippets where possible.
- Explicit trade-offs.
- Minimal abstraction.

### Register 2: AI Journey Journal

Use for reflective posts about workflow experiments and tooling habits.

Expectations:
- Personal perspective is allowed.
- Keep conclusions proportional to evidence.
- Focus on what changed and what remains unresolved.

### Cross-Register Constants

Always keep direct tone, practical detail, balanced judgement, and low filler.

In all registers, prefer paragraphs over lists unless sequence, ranking, or grouped reference genuinely matters.

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
