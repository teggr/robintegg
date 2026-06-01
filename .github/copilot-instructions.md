# Copilot Instructions for robintegg

## Project Overview

This is Robin Tegg's personal website and blog. The repo is content-first: Markdown content, static assets, and an Electrostatic build launched through JBang.

## Technology Stack

- Markdown with YAML front matter
- Electrostatic CLI via JBang
- Java 21 build environment
- Netlify deployment via GitHub Actions

## Project Structure

The repository root is the site workspace.

```
robintegg/
├── _posts/            # Blog posts
├── _books/            # Book content
├── _podcasts/         # Podcast content
├── _drafts/           # Draft content
├── _feeds/            # Feed source definitions
├── _static/           # Static assets and images
├── generated-site/    # Build output
├── site-config.xml    # Site configuration
└── .github/workflows/ # CI/CD workflow
```

## Build And Serve

Run the site build from the repository root:

```bash
jbang --fresh site.electrostatic:electrostatic-cli:0.0.1 build --base-url=http://localhost:8080
jbang --fresh site.electrostatic:electrostatic-cli:0.0.1 serve --base-url=http://localhost:8080
```

Use the production build for deployment checks:

```bash
jbang --fresh site.electrostatic:electrostatic-cli:0.0.1 build
```

The generated site is written to `generated-site/`.

## Content Rules

- Use YAML front matter for every post, book, and podcast entry.
- Keep post file names in the format `YYYY-MM-DD-title-slug.md`.
- Use `/images/...` in front matter `image:` fields.
- Use `{{site.baseurl}}/images/...` for inline image references in Markdown.
- Prefer concrete examples, commands, and code blocks in technical posts.
- Avoid the em dash character (`—`) in prose unless it is clearly necessary.

## Markdown And Post Editing

- Blog post content lives in `_posts/`.
- Book content lives in `_books/`.
- Podcast content lives in `_podcasts/`.
- Drafts live in `_drafts/`.
- Static images live in `_static/images/`.

When editing Markdown content, follow the voice and post-quality guidance in:

- `.github/skills/voice-skill/SKILL.md`
- `.github/skills/post-edit-checks/SKILL.md`

## Common Tasks

### Adding a New Blog Post
1. Create a file in `_posts/` using the `YYYY-MM-DD-title-slug.md` naming pattern.
2. Add front matter with at least `layout`, `title`, and `date`.
3. Add an `image:` field if the post has a featured image.
4. Write the article body in Markdown below the front matter.
5. Build or preview locally with the JBang command above.

### Updating Site Configuration
1. Edit `site-config.xml` when site configuration changes.
2. Keep build-related changes aligned with the JBang/Electrostatic workflow.
3. Confirm the generated output still appears correctly in `generated-site/`.

### Working With Images
1. Store image files in `_static/images/`.
2. Reference them from front matter with `/images/<file>`.
3. Reference them in post bodies with `{{site.baseurl}}/images/<file>`.

## CI/CD And Deployment

The GitHub Actions workflow is in `.github/workflows/build-and-deploy.yml`. It checks out the repo, sets up JDK 21, runs the JBang/Electrostatic build, and deploys the `generated-site/` directory to Netlify.

## Key Files

- `site-config.xml` - Site configuration
- `_posts/` - Blog posts
- `_books/` - Book content
- `_podcasts/` - Podcast content
- `_static/images/` - Static images used by the site
- `generated-site/` - Build output
- `.github/workflows/build-and-deploy.yml` - CI/CD pipeline

## Tips For Contributors

1. Make small, focused changes.
2. Keep content paths rooted at the repository top level.
3. Use the JBang command shown above to verify changes locally.
4. Keep documentation in sync with the actual folder layout.

## References

- Electrostatic: https://github.com/teggr/electrostatic
- JBang: https://www.jbang.dev/
- Netlify: https://www.netlify.com/
