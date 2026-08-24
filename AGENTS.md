# AGENTS.md

This is Robin Tegg's personal website and blog: a content-first repo of Markdown
posts built with Electrostatic via JBang and deployed to Netlify.

## Content rules (always apply)

These apply when creating or editing anything in `_posts/`, `_books/`,
`_podcasts/`, `_drafts/`, or `_feeds/`.

Before writing or editing content, read these files:

- `.github/copilot-instructions.md` - project overview, build commands, structure
- `.github/instructions/post.instructions.md` - post front matter, naming, markdown style
- `.github/skills/voice-skill/SKILL.md` - writing voice, anti-filler rules, banned phrases
- `.github/skills/post-edit-checks/SKILL.md` - final review checklist

The hard rules that get missed most often:

- Never use em dashes in prose. Use commas, periods, colons, semicolons, or parentheses.
- No negation framing ("This isn't X. This is Y.", "Not X. Y.", "Less X, more Y."). State the positive claim directly.
- Banned phrases include: "It's worth noting", "delve", "leverage", "utilize", "straightforward", "in order to", "furthermore", "in other words".
- Write first person, contractions on, paragraphs of 2-4 sentences, no preamble before the point.
- Post files follow `YYYY-MM-DD-title-slug.md` in `_posts/`.
- Front matter requires `layout`, `title`, `date`, and a short `description` (80-160 characters).
- Images live in `_static/images/` and are referenced as `/images/<file>`.

## Build

```bash
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 build --base-url=http://localhost:8080
jbang --fresh site.electrostatic:electrostatic-cli:0.0.3 serve --base-url=http://localhost:8080
```

Production build: same command without `--base-url`. Output goes to `generated-site/`.
