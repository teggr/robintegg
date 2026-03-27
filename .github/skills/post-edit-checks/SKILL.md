---
name: post-edit-checks
description: Performs checks on new or edited blog post content to ensure quality and consistency of front matter.
---

This skill performs checks on new or edited blog post content to ensure that the front matter adheres to specified quality and consistency standards. It verifies the presence of required fields, checks for valid data types, and ensures that values fall within acceptable ranges. The skill can be configured to enforce specific rules based on the content type and can provide detailed feedback on any issues found during the checks.

**Always run this skill after creating or editing a blog post**, including when creating a new post from a GitHub issue or agent task.

* Check that the images referenced in the front matter exist in the static image folder - `website/_static/images`.

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