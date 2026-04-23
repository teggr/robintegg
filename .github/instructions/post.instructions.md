---
applyTo: "website/_posts/**"
---

# Copilot Instructions for Blog Posts

## Scope
This file provides instructions for creating and editing blog post files in the `website/_posts/` directory.

## Voice

Use `.github/skills/voice-skill/SKILL.md` for voice, tone, anti-filler, and audience guidance when writing or editing posts.

## File Naming Convention
Blog post files must follow this naming pattern:
```
YYYY-MM-DD-title-slug.md
```

Examples:
- `2025-08-25-exposing-spring-shell-to-mcp-clients.md`
- `2023-06-19-j2html-static-site-generator.md`
- `2019-02-09-testing-spring-boot-applications-with-testcontainers.md`

## YAML Front Matter
Every blog post must start with YAML front matter enclosed in `---` markers. Required and optional fields:

### Required Fields
```yaml
---
layout: post
title: "Your Post Title"
date: "YYYY-MM-DD"
---
```

### Optional Fields
```yaml
image: /images/your-image.jpg
tags:
  - java
  - spring boot
  - tools
category: reading-list
```

### Front Matter Examples

**Standard technical post:**
```yaml
---
layout: post
title: "Testing Spring Boot applications with TestContainers"
date: "2019-02-09"
image: /images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - spring boot
  - testcontainers
  - tools
  - testing
  - docker
---
```

**Reading list post:**
```yaml
---
title: Reading list for July 2021
layout: default
category: reading-list
date: "2021-07-05"
---
```

## Post Structure

- Start with a brief introduction that explains what the post covers
- Use clear section headings (##, ###) to organize content
- Lead with practical examples and working code
- Explain the "why" behind technical decisions
- End with references, next steps, or related resources

## Code-First Structure

Blog posts should be rich with code examples that demonstrate concepts in action:

1. **Show the implementation**: Include actual code snippets that readers can understand and use.
2. **Use runnable examples where possible**: Prefer complete methods, classes, commands, or configuration.
3. **Include setup details when required**: Show Maven/Gradle dependencies, configuration, or setup code when needed to make the example usable.
4. **Demonstrate practical usage**: Use realistic examples from actual projects or workflows.
5. **Add context around examples**: Explain what the code does and why it matters.

## Code Block Formatting
Use fenced code blocks with language identifiers:

```java
@Command
@Service
public class WeatherService {
  @Command(command = "weather-forecast", description = "Get weather forecast")
  @Tool(description = "Get weather forecast for a location")
  public String getWeatherForecast(double latitude, double longitude) {
    // implementation here
  }
}
```

```xml
<dependency>
  <groupId>com.j2html</groupId>
  <artifactId>j2html</artifactId>
  <version>1.6.0</version>
</dependency>
```

```bash
mvn clean install
java -jar target/my-app.jar
```

```properties
spring.application.name=my-app
spring.profiles.active=local
```

## Structural Expectations
- Start with a brief introduction that explains what the post covers
- Include working code examples that demonstrate the concept
- Include links to official documentation and resources
- Include practical use cases and scenarios
- Include step-by-step instructions when appropriate
- Include screenshots when showing UI or visual output

## Images and Assets
Images should be referenced using the following format:
```markdown
![Image description]({{site.baseurl}}/images/image-name.png)
```

Images are stored in `website/_static/images/` and referenced as `/images/` in markdown.

## Common Content Patterns

### Tutorial/How-To Posts
```markdown
Brief intro explaining what you'll build/learn

## Getting Started
Setup instructions with code

## Implementation
Step-by-step with code examples

## Testing
How to verify it works

## References
Links to documentation and related resources
```

### Technical Explanation Posts
```markdown
Brief intro explaining the topic

## What is X?
Clear explanation with context

## How it Works
Technical details with code examples

## Practical Usage
Real-world examples

## Notes and Considerations
Edge cases, gotchas, best practices
```

### Reading List Posts
```markdown
Simple list of links with titles:
* [Article Title](url)
* [Another Article](url)
```

## Series Posts
When writing a series of related posts, link between them:
```markdown
This is the first of a short series of posts:
- Part One - Data access layer integration tests
- [Part Two - Application integration tests](/2019/02/12/testing-spring-boot-applications-with-testcontainers-part-two/)
- [Part Three - UI Tests](/2019/02/24/testing-spring-boot-applications-with-testcontainers-and-selenium-webdriver-part-three/)
```

## Code Examples Best Practices

### Java Code
- Show imports when relevant to understanding
- Include complete method signatures
- Use meaningful variable names
- Add brief comments only when necessary for clarity
- Show both interface and implementation when relevant

### Configuration Examples
- Show complete configuration files when possible
- Highlight the important parts
- Explain what each section does

### Command-Line Examples
- Show full commands that can be copied and run
- Include expected output when helpful
- Show the working directory context when needed

### Maven/Gradle Dependencies
- Always show the full dependency declaration
- Include version numbers
- Mention compatibility notes if relevant

## Internal Links
Link to other posts using relative paths:
```markdown
[previous post](/2023/06/19/j2html-static-site-generator)
```

## External Links
Link to official documentation and authoritative sources:
```markdown
[Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
[GitHub Repository](https://github.com/user/repo)
```

## Markdown Style
- Use ATX-style headers (`##`, `###`) not underline style
- Use fenced code blocks (```) not indented code blocks
- Leave blank lines around headers and code blocks
- Use `**bold**` for emphasis, not `__bold__`
- Use `-` for unordered lists, not `*` or `+`

## Final Review

Use `.github/skills/post-edit-checks/SKILL.md` for the final review checklist before finalizing a post.
