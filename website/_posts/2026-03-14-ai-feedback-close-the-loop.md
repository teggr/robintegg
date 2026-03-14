---
layout: post
title: "AI Feedback: Close the Loop"
date: "2026-03-14"
image: /images/ai-feedback-close-the-loop.png
description: "How to build a feedback loop at the end of every AI-assisted ticket, using a reusable Copilot prompt to update instructions and ArchUnit tests so the AI gets smarter session by session."
tags:
  - ai
  - github copilot
  - feedback
  - archunit
  - workflow
---

We are getting pretty good at the front end of the AI-assisted development cycle. Fetch the ticket, plan the work, implement with TDD, review the diff. There are prompts, skills, and agents to help with every phase. But there is a step that most workflows quietly skip: feeding the learning from a session back into the AI's context for next time.

The classic example plays out like this. Copilot creates a `UserController` in `com.example.controllers`. You know it should live in `com.example.web.api`. You move it, you commit, you move on. Next ticket, same mistake. The session ended and the correction went with it. You learned something. The AI did not.

This is the feedback gap, and closing it is what makes an AI-assisted workflow compound over time rather than just repeat itself.

## Why the Loop Is Left Open

In traditional development you accumulate knowledge. You make a mistake, you learn, and you carry that forward. The code, the tests, and the documentation all reflect what the team has figured out. Future developers (and future you) benefit from it, but without you the knowledge is lost.

With AI-assisted development, the session is a disposable context window. The AI is good at working within the rules it has been given, but those rules have to be explicitly encoded somewhere it can read. If the rules only exist in your head, or buried in a PR comment, they are invisible to the next session.

The two places where you can encode structural knowledge that the AI will actually respect are your **Copilot instructions file** (`.github/copilot-instructions.md`) and your **ArchUnit tests**. One tells the AI what to do before it starts writing code. The other tells the CI system to reject code that broke the rules after the fact. Together they create a two-layer guard that gets stronger with every ticket.

## The ArchUnit Angle

[ArchUnit](https://www.archunit.org/) is a Java library for writing architecture rules as unit tests. If you have not used it before, the idea is simple: you write a test that expresses a structural rule, "controllers must live in the web package" and "services must not depend on controllers," and if that rule is violated the test fails. It runs in your normal test suite, so a violation breaks the build.

For AI-assisted development, ArchUnit is particularly valuable because it does two jobs at once. It enforces the rule programmatically, but it also **documents the rule** in a form that survives sessions. A new session can be pointed at the existing ArchUnit tests to understand the project's structural expectations before writing a single line of code.

Here is a concrete example. Your project has a layered Spring Boot structure:

```
com.example
  ├── web.api        ← @RestController classes live here
  ├── service        ← @Service classes live here
  └── persistence    ← @Repository classes live here
```

An ArchUnit test to enforce this looks like:

```java
@AnalyzeClasses(packages = "com.example")
class ArchitectureTest {

    @ArchTest
    static final ArchRule controllers_must_live_in_web_api_package =
        classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("com.example.web.api..")
            .because("controllers belong in the web.api package");

    @ArchTest
    static final ArchRule services_must_live_in_service_package =
        classes()
            .that().areAnnotatedWith(Service.class)
            .should().resideInAPackage("com.example.service..")
            .because("services belong in the service package");

    @ArchTest
    static final ArchRule services_must_not_depend_on_controllers =
        noClasses()
            .that().resideInAPackage("com.example.service..")
            .should().dependOnClassesThat().resideInAPackage("com.example.web.api..")
            .because("services must not depend on the web layer");
}
```

When Copilot creates `UserController` in the wrong package and the developer does not catch it in review, the build does. The failure message includes the `.because()` text, which gives both the developer and (if you paste the output into a new session) the AI a clear description of the violated rule.

The corresponding instruction in `.github/copilot-instructions.md` would be:

```markdown
## Package Structure

Controllers annotated with `@RestController` must be placed in the
`com.example.web.api` package. Services annotated with `@Service`
must be placed in `com.example.service`. Repositories annotated
with `@Repository` must be placed in `com.example.persistence`.
Do not create classes in `com.example.controllers`, 
`com.example.services`, or similar informal packages.
```

With both the instruction and the ArchUnit test in place, the AI is less likely to make the mistake in the first place, and if it does, CI catches it.

## The Feedback Prompt

The manual version of this (reviewing the session, identifying gaps, and writing new instructions or tests by hand) works but rarely happens. The end of a ticket is the worst possible moment to ask a developer to reflect carefully and write documentation. Everyone wants to move on.

A reusable Copilot prompt changes the dynamic. You run it, it does the reflection for you, it pauses to get your input, and it proposes concrete changes. You approve or adjust, and it applies them. The whole thing takes a few minutes and the project is genuinely better for the next session.

Here is a `tidy.prompt.md` that does this work:

```markdown
    ---
    name: tidy
    description: Review the session, close the feedback loop, and propose updates 
                to Copilot instructions and ArchUnit tests.
    ---

    # Tidy Up: Close the Feedback Loop

    ## Phase 1: Review

    Examine what changed in this session by running:

    ```shell
    git diff main --stat
    git diff main -- "*.java"
    ```

    Look for:
    - Classes placed in unexpected packages
    - Naming conventions that deviate from the existing codebase
    - Dependencies introduced between layers that should not exist
    - Any correction the user had to make manually after the AI's first attempt

    Summarise your findings as a short list of observations. 

    PAUSE: present the observations and ask the user to confirm, add to, or 
    remove items before continuing.

    ## Phase 2: Propose Changes

    For each confirmed observation, propose one or both of the following:

    **A. An update to `.github/copilot-instructions.md`**
    A concise rule the AI should follow in future sessions. State it as a 
    direct instruction. Keep it under three sentences. Propose the exact text 
    to append to the instructions file.

    **B. An ArchUnit test**
    A new `@ArchTest` that encodes the structural rule and would have caught 
    the issue automatically. Use the existing `ArchitectureTest.java` as a 
    base. Include a `.because()` clause that explains the rule in plain English.

    Present the proposed changes clearly. For each one, state:
    - What gap it closes
    - Where the change will be made

    PAUSE: ask the user to approve, reject, or modify each proposal before 
    applying anything.

    ## Phase 3: Apply

    Apply only the approved changes:
    - Append approved instructions to `.github/copilot-instructions.md`
    - Add approved ArchUnit tests to the existing architecture test class

    Confirm each change with a brief summary of what was written and where.
```

The two `PAUSE` checkpoints are the key feature here. The first prevents the prompt from inventing gaps that did not exist. The second prevents it from applying changes the developer has not reviewed. It is the plan/implement pattern applied to the feedback process itself.

Over a month of typical feature work, this compounds. The instructions file grows to reflect how the project actually works, not just how it was initially set up. The ArchUnit suite grows to catch categories of mistake that the team has actually encountered. Both of these artefacts survive well beyond any individual session or developer.

## Tidying in a workflow

If you are working with the workflow agent pattern [described in a previous post](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills), the `tidy` step fits naturally at the end of the workflow sequence, alongside the review phase.

## References

- [ArchUnit](https://www.archunit.org/)
- [GitHub Copilot Custom Instructions](https://docs.github.com/en/copilot/customizing-copilot/adding-repository-custom-instructions-for-github-copilot)
- [GitHub Copilot Prompt Files](https://docs.github.com/en/copilot/customizing-copilot/using-prompt-files-with-github-copilot)
- [Orchestrating AI Workflows with Copilot Agents, Prompts, and Skills](/2026/03/13/orchestrating-ai-workflows-with-agents-prompts-and-skills)
- [Self-Contained Systems for AI Coding Agents](/2026/01/07/self-contained-systems-for-ai-coding-agents)
