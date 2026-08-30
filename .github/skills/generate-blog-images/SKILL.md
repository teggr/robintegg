---
name: generate-blog-images
description: Generate blog article images through the OpenAI Images API and save them to a requested output path.
---

# Generate Blog Article Images

Use this skill when the user wants a new featured image, hero image, cover image, or supporting illustration for a blog article. The skill is designed for Copilot CLI agents running on this VPS and wraps the OpenAI Images API behind a small command-line script.

## Goal

Create an image that communicates the article's central technical idea at a glance. The result should feel editorial and purposeful, rather than a generic AI illustration or a screenshot substitute.

Use this skill for new and existing posts. Prefer real screenshots for UI walkthroughs, terminal output, and diagrams where visual accuracy matters. Generate an image when an original conceptual or editorial illustration better supports the article.

## Robin's Blog Image Preferences

Default to magazine-style landscape imagery. The preferred feel is a polished editorial shot with a Bloomberg or Vogue sensibility: confident composition, strong subject, restrained colour, clean negative space, and enough visual specificity to feel made for the article.

Avoid robots unless the user specifically asks for them. For AI, agent, and automation posts, prefer workspaces, tools, abstract systems, human-scale scenes, diagrams implied through objects, or editorial metaphors over humanoid robots.

Do not fill the image with writing, sections, labels, UI panels, or diagram callouts. Important textual information should stay in the article, not inside the generated image.

Include the article title in the image by default. Keep the title large, sparse, and positioned inside a safe area. Avoid placing text near the bottom edge because robintegg.com image crops and article layouts can cut it off. Prefer the left side, upper-left, upper-right, or upper-center depending on the composition, with generous margin from every edge. If the user explicitly asks for no text in the image, omit the title entirely.

Use robintegg.com as the target presentation context. Images may appear on the front page, article pages, previews, and narrower viewports, so the subject and any requested title text must survive cropping. Keep the focal subject away from the very bottom and avoid critical detail at the extreme edges.

## Authentication

The image generator authenticates exclusively with the `OPENAI_API_KEY` environment variable.

Never hard-code, print, log, expose, commit, or write the API key to disk. If `OPENAI_API_KEY` is missing or invalid, fail clearly and tell the user that the VPS environment variable must be configured. Never ask the user to paste the key into the conversation.

## Inputs

Read the target post before generating an image. Identify:

- the article title, description, and main argument
- the intended reader and technical subject
- any concrete visual metaphors already present in the article
- whether the image is a featured image or an inline illustration
- the target filename, if the user supplied one

Ask for direction only when the post does not provide enough information to form a specific visual concept. Do not invent product UI, code output, logos, metrics, or claims that the post does not support.

For non-blog use, a natural-language prompt and output path are enough.

## Image Brief

Create a short image brief before generation. It must state:

- **Subject:** the core object, scene, or metaphor
- **Composition:** a clear focal point, uncluttered framing, and crop-safe negative space
- **Style:** magazine-style editorial imagery with Bloomberg/Vogue-style polish, adapted to the technical subject
- **Mood:** practical, thoughtful, and credible
- **Text plan:** include the article title by default; omit text only when the user explicitly asks for no text in the image
- **Avoid:** robots unless specifically requested, watermarks, unreadable code, invented logos, dashboard UI, excessive writing, section labels, and decorative visual clutter

Use technical symbols and abstract structures only when they clarify the subject. A generated image should communicate the article topic without requiring a caption to explain it.

## Generation Prompt

Turn the brief into one self-contained prompt. Keep it specific enough to produce a consistent scene, but do not fill it with style keywords.

Use this default structure:

```text
Editorial featured illustration for a technical blog article about [topic].
[Describe the central subject and its relationship to the article's main idea.]
Magazine-style landscape image with a polished Bloomberg/Vogue editorial feel.
[Describe the composition, lighting, palette, and visual treatment.]
Keep the focal subject away from the bottom edge and extreme corners so it works on robintegg.com front page cards and article pages.
No robots unless specifically requested.
No UI panels, code snippets, diagram labels, or section headings.
Include only this title text: "[exact article title]".
Place the title in a clean crop-safe area away from the bottom edge, preferably left side, upper-left, upper-right, or upper-center with generous margin.
```

If the user explicitly asks for no text in the image, replace the title lines with:

```text
No text, letters, numbers, logos, watermarks, UI panels, code snippets, labels, or section headings.
```

Generate a wide landscape image suitable for an article header. Use 16:9 as the default for blog imagery where the selected OpenAI image model supports it. When exact 16:9 is unavailable, choose the closest wide landscape size supported by the model.

## Script

Use the local wrapper script from the repository root:

```bash
.github/skills/generate-blog-images/scripts/generate-image.sh \
  --prompt "Editorial featured illustration for a technical blog article about ..." \
  --output "_static/images/example-article-image.png"
```

The wrapper:

- reads credentials only from `OPENAI_API_KEY`
- sends the prompt to the OpenAI Images API
- saves the generated image directly to the requested output path
- creates parent directories when needed
- supports a `--size` option for API-supported sizes
- defaults to a wide blog-friendly size
- reports API failures without printing the API key
- uses Bash with `curl`, `jq`, and `base64`

### Options

```text
--prompt TEXT       Required. Natural-language image prompt.
--output PATH      Required. Where to save the generated image.
--size SIZE        Optional. Defaults to 1792x1024 for wide blog imagery.
--model MODEL      Optional. Defaults to dall-e-3.
--quality QUALITY  Optional. Defaults to standard.
--style STYLE      Optional. Only sent when specified.
--help             Show usage.
```

Use `1792x1024` for wide blog featured images with `dall-e-3`. Use model-supported alternatives only when the article needs a square or portrait image. If changing models, check that the chosen `--size`, `--quality`, and any optional `--style` value are valid for that model.

## Workflow

1. Read the post and derive the visual brief from its actual content.
2. Check `_static/images/` for an existing image that already fits the article. Reuse it only when it genuinely represents the same subject.
3. Before generating, record the image request details in the post's front matter. Add an `image_brief` field containing the subject, composition, style, mood, and text plan from the brief. This preserves the direction so it can be refined or used to regenerate the image later. Example:

   ```yaml
   image_brief: "Subject: a developer's desk seen from above with a terminal showing a GitHub repository tree. Composition: centred laptop surrounded by scattered printed code printouts. Style: editorial Bloomberg-feel, cool blue lighting. Mood: analytical, focused. Text: article title upper-left."
   ```

4. Generate the image with `.github/skills/generate-blog-images/scripts/generate-image.sh`.
5. Inspect the output at full size. Regenerate it when the focal subject is unclear, details are visibly distorted, the image contains accidental text, or the result looks like a generic stock illustration.
6. Save the selected image in `_static/images/` with a concise, lowercase, hyphenated filename, such as `spring-ai-mcp-demo.jpg`.
7. Prefer `jpg` for photographic or full-colour editorial images. Use `png` when sharp edges or transparency are needed. Do not convert an image merely to change its extension.
8. For a featured image, set the post front matter to its public path:

   ```yaml
   image: /images/your-image.jpg
   ```

9. For an inline image, add meaningful alt text and reference the same public path:

   ```markdown
   ![A concise description of the visual content](/images/your-image.jpg)
   ```

10. Run the `post-edit-checks` skill after modifying a post, including its `image:` front matter.

## Alt Text

Generate accessible alt text alongside blog images. Keep it concise and describe what is visually present, not the prompt or hidden intent.

Good alt text names the concrete subject and its relevant relationship to the article. Avoid phrases such as "image of", "AI-generated", or keyword stuffing. For decorative images, use empty alt text only when the image adds no meaning to the article.

Example:

```markdown
![A laptop surrounded by connected agent workflow cards](/images/agent-workflow-image.png)
```

## Quality Standard

The completed image should:

- be legible and balanced when displayed as a wide header
- have one obvious focal point
- keep important subject detail away from the bottom edge and extreme corners
- reinforce the article's actual subject and tone
- include the article title by default; omit text only when the user explicitly requests no text
- avoid embedded text other than the article title
- avoid robots unless specifically requested
- avoid fabricated branded elements
- use a filename that clearly relates to the article
- exist under `_static/images/` before it is referenced from Markdown
- include useful alt text when inserted into article body content

## Suggested Prompt

```text
Generate a featured image for my blog post at _posts/YYYY-MM-DD-title-slug.md.
Use the generate-blog-images skill, save the final asset under _static/images/, and update the post's image front matter.
```
