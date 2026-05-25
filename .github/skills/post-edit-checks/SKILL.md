---
name: post-edit-checks
description: Performs checks on new or edited blog post content to ensure quality and consistency of front matter.
---

This skill performs review checks on new or edited blog post content before it is finalized. It verifies front matter quality and consistency, and it also acts as the final review checklist for post readiness.

**Always run this skill after creating or editing a blog post**, including when creating a new post from a GitHub issue or agent task.

Checks include:
- Check that the `image:` field is present in the front matter.
- Check that the images referenced in the front matter exist in the static image folder - `website/_static/images`.
- Check that file naming and front matter are complete and valid.
- Check that code examples, links, and markdown formatting meet the post requirements.

## Review Checklist

Before finalizing a blog post, verify:
- [ ] File name follows `YYYY-MM-DD-title-slug.md` pattern.
- [ ] YAML front matter is complete and valid.
- [ ] Title is descriptive and clear.
- [ ] Date is correct in `YYYY-MM-DD` format.
- [ ] Tags are relevant and match existing tag conventions.
- [ ] `image:` field is present in the front matter.
- [ ] Code examples are complete and working.
- [ ] Code blocks have appropriate language identifiers.
- [ ] Images use `{{site.baseurl}}/images/` path.
- [ ] Content leads with practical code examples.
- [ ] Links to external resources are included.
- [ ] Markdown formatting is consistent.

## Scripts

### check-front-matter-images.ps1

Scans all content files in `website/_posts` and `website/_podcasts` directories and verifies that any images referenced in the YAML front matter (via the `image:` field) exist in the `website/_static/images` directory.

**Usage:**
```powershell
# Run from repository root directory
.\.github\skills\post-edit-checks\scripts\check-front-matter-images.ps1
```

**Output:**
- Summary of total files scanned
- List of missing images (if any)
- Count of valid image references