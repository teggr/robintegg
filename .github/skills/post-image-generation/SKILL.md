---
name: post-image-generation
description: 'Generate a consistent landscape editorial image for a blog article. Use when asked to create a blog post image, article image, featured image, or magazine-style image for a post. Reads the article and applies Bloomberg/Vogue-inspired photography, title-only typography, and crop-safe placement.'
---

# Blog Post Image Generation

Create one article-specific image with a consistent editorial style. This skill defines a workflow; loading or editing it alone is not a request to generate an image.

## Read the article

1. Read the supplied article or the complete target post, including its front matter. If no article is supplied and the target is unclear, ask the user to identify it.
2. Derive a visual concept from the article's main subject, concrete examples, and central argument. Prefer a single strong scene or visual metaphor over a collage of every topic.
3. Use the article's title as the only visible text. Do not silently rewrite or shorten it; ask if a long title cannot fit legibly.

## Visual direction

- Use landscape composition with a polished magazine-editorial feel.
- Take broad inspiration from Bloomberg business photography and Vogue fashion editorials: deliberate art direction, striking lighting, restrained colours, tactile detail, and confident composition. Do not copy a particular photograph, magazine cover, masthead, or branding.
- Prefer photographic scenes and tangible objects that reflect the article, rather than diagrams, dashboards, or generic technology clip art.
- Keep the visual language consistent across posts while choosing a scene specific to each article.
- Do not include robots unless the user specifically requests them, even for AI-related articles.
- Include only the title and the image. No subtitles, labels, section panels, captions, explanatory copy, magazine furniture, or incidental legible writing.

## Title placement and cropping

- Prefer a title block on the left or in an upper corner, inset well away from the edges. An upper-centred title is acceptable when it better suits the composition.
- Never place the title near the bottom: the site's layout can cut it off halfway.
- Leave generous negative space around the title and the main subject. As a starting point, inset essential content about 20% from the image edges, then check it against the actual layout.
- Use clear, high-contrast typography over a quiet part of the image. Keep the entire title comfortably inside the safe area, including every line and descender.
- Compose for narrower viewports and centre crops as well as the full landscape image. Left or upper-corner placement must not mean edge-hugging text.
- If site previews are available, inspect the real image container and crop behaviour; do not assume margins guarantee visibility.

## Generate and deliver

1. Build a prompt containing the article-derived scene, landscape editorial direction, exact title, title placement, crop margins, and exclusions above.
2. Use the available image-generation tool only in response to an explicit image request. Supply reference images only when the user asks to use them. If generation is unavailable or fails, report that explicitly; do not claim a prompt is a generated image or automatically retry.
3. Inspect the result for subject relevance, title spelling and legibility, unwanted text, robots, and unsafe cropping. Report any issue that cannot be verified or corrected.
4. When the request includes adding the image to the site, save it under `_static/images/` with a descriptive filename and reference it as `/images/<filename>` in the post's `image:` field. Preserve unrelated post content and existing assets; do not overwrite an existing image without permission.
5. Before editing a post, load `voice-skill` and follow the post instructions. After editing a post, run `post-edit-checks` and verify the referenced image exists.
6. Deliver the generated image and, if integrated, link the updated post and saved asset. Do not claim responsive layout verification unless it was actually performed.
