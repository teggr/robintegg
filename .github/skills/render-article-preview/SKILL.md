---
name: render-article-preview
description: Build the site with JBang and return a reader-facing article preview from generated HTML instead of a raw markdown diff.
---

# Render Article Preview

Use this skill after creating or editing an article when the user wants to review how it reads after generation.

## What this skill does

- verifies Java and JBang are available
- runs the documented site generation command
- finds the generated HTML for the target article
- renders the generated article content into readable plain text
- returns the rendered preview in the session or PR update

## Workflow

1. Confirm environment.

```bash
java -version
jbang version
```

If Java or JBang is missing, report the missing dependency and stop.

2. Build the site from repository root using the documented command.

```bash
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 build --base-url=http://localhost:8080
```

3. Resolve the generated article path.

- Start from the article source filename and derive a slug.
- Locate candidate files in `generated-site/` and select the matching `index.html`.
- If multiple candidates exist, use the one whose title matches the article front matter title.

4. Render readable output from generated HTML using the helper script.

```bash
jbang .github/skills/render-article-preview/scripts/render-article-preview.java <generated-html-path>
```

5. Return the rendered output.

- Include the final rendered article preview in the session response.
- When updating a PR, include the rendered preview section so reviewers can read the article directly without relying on Markdown diff.

## Output contract

Always provide:
- generated HTML file path used for rendering
- rendered title
- rendered body preview

If generation or path resolution fails, report the exact failing command and a short next action.
