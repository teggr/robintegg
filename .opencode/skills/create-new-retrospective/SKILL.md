---
name: create-new-retrospective
description: Create a new monthly retrospective blog post in Robin Tegg's voice using prior retro examples and the user's supplied notes.
---

# Create a New Retrospective Post

Use this skill when the user wants to start a new monthly or period retrospective post for the site.

## What this skill produces

This skill creates a new markdown post in the site's blog content structure, written in Robin Tegg's voice and shaped to match the retrospective template.

The output should include:
- a new post file in the appropriate `_posts/` location
- YAML front matter matching the template pattern
- a draft body that maps the supplied notes into the template's sections
- the tool/framework/service inventory block from the template, adapted to the current month
- a tone and structure consistent with earlier retrospectives

## Inputs to gather

Before writing, identify:
- the month or period being retroed, such as May, June, or Q2
- the year if it is not obvious from the date context
- the retrospective content the user wants included
- whether the user wants the post to mirror a specific earlier retrospective style
- whether there is a preferred publication date or filename date

If the month is unclear, ask for it before drafting.

## Workflow

1. Treat the edited template as the source of truth.
   - Use `.github/skills/create-new-retrospective/template.md` as the default structure for the post.
   - Keep the front matter, intro, section order, and closing section aligned with that template.
   - Only depart from the template when the user explicitly requests a different shape.

2. Map the supplied notes to the template sections.
   - Use the user input to fill the intro, the main themes section, the writing section, the links section, and the next-month section.
   - Preserve the tool/framework/service inventory block from the template immediately after the introduction, adapting it to the current month rather than omitting it.
   - If a section has too little detail, ask for more specific input before drafting that part.
   - Prefer targeted follow-up questions over inventing content that is not grounded in the notes.

3. Write in the site's voice.
   - Follow the guidance in `.github/skills/voice-skill/SKILL.md`.
   - Keep the tone practical, first-person, and evidence-led.
   - Avoid filler, hype, and generic setup lines.

4. Create the post file.
   - Use the repository naming convention in `_posts/`:
     - `YYYY-MM-DD-title-slug.md`
   - If no publication date is provided, use the current date.
   - Use a title that reflects the retrospective clearly.

5. Add front matter that matches the template pattern.
   - Include `layout`, `title`, `date`, `description`, `image`, and relevant tags where appropriate.
   - Use placeholders only when the user has not supplied the actual values.
   - Ask for missing front matter details when necessary.

6. Run the post-edit-checks review and ensure it passes.
   - Run the post-edit-checks skill before considering the retrospective complete.
   - If the checks report any issue with front matter, image references, formatting, or post readiness, fix it before finalizing.
   - Do not treat the draft as finished until the post-edit-checks review passes.

## Writing guidance

- Use first person naturally where it helps the reflection feel lived-in.
- Keep the writing specific and grounded in actual examples.
- Prefer short paragraphs and clear sectioning over decorative formatting.
- Treat the retrospective as a period snapshot, not a formal essay.
- If the user gives a long block of notes, turn it into a coherent set of themes.
- If the user gives sparse input for a particular section, ask for more detail before drafting that section.
- Do not invent missing section content when the user has not provided enough information.

## Completion checklist

A draft is ready when all of these are true:
- a new post file exists in `_posts/`
- front matter is present and valid
- the voice matches the site's established tone
- the content is organized around a few clear themes
- the retrospective feels like a genuine monthly roundup rather than a generic article
- the post-edit-checks review has been run and passes without remaining issues

## Suggested prompt template

Use this when kicking off the workflow:

Create a new post in my voice for my [Month] retrospective. Use examples from previous retrospectives as reference points. Here is the content I want included:

[content]
