# Copilot Instructions for robintegg

## Project Overview

This is a personal website and blog for Robin Tegg (https://www.robintegg.com), built using a custom Java-based static site generator. The project generates static HTML from Markdown content files (posts, books, podcasts) and deploys to Netlify.

## Technology Stack

- **Language**: Java 17
- **Build Tool**: Apache Maven 3.9+
- **HTML Generation**: j2html library for programmatic HTML generation
- **Markdown Processing**: CommonMark with YAML front matter support
- **Logging**: SLF4J with simple implementation
- **Testing**: JUnit 5 (Jupiter)
- **Utilities**: Apache Commons Lang3, Jackson for JSON/XML
- **Deployment**: Netlify via GitHub Actions

## Project Structure

The project is a multi-module Maven project with the following structure:

```
robintegg/
├── generate-plugin/     # Core website generation engine
│   ├── src/main/java/com/robintegg/web/
│   │   ├── engine/      # Website builder and content model
│   │   ├── theme/       # Theme rendering and plugins
│   │   ├── content/     # Content type plugins (posts, books, podcasts)
│   │   ├── feed/        # RSS/Atom feed generation
│   │   ├── pages/       # Page generators (index, tags, categories)
│   │   └── utils/       # Utility classes
│   └── src/test/        # Unit tests
├── workflows/           # Raindrop.io integration for bookmarks
├── website/             # Website content and build entry point
│   ├── _posts/          # Blog post markdown files
│   ├── _books/          # Book review markdown files
│   ├── _podcasts/       # Podcast episode markdown files
│   ├── _drafts/         # Draft content (excluded by default)
│   ├── _static/         # Static assets
│   └── src/main/java/   # Build.java main entry point
└── pom.xml              # Parent POM
```

## Architecture and Design

### Content Model
- The website builder loads content from markdown files with YAML front matter
- Content types (posts, books, podcasts) are processed by type-specific plugins
- The content model aggregates items and builds indexes, tag pages, and category pages
- Content is rendered using j2html for type-safe HTML generation

### Plugin System
- **Content Type Plugins**: Define how different content types are loaded and processed
- **Aggregator Plugins**: Build derived models (tags, categories) from content
- **Render Plugins**: Define how content types are rendered to HTML
- **Theme Plugin**: Provides layouts, includes, and page templates

### Visitor Pattern
- `ContentModelVisitor` provides a common interface for traversing the content model
- Separates content structure from rendering logic

## Building and Testing

### Build the project
```bash
mvn clean install
```

### Run tests only
```bash
mvn clean test
```

### Build the website
```bash
# Build with production settings
mvn --batch-mode -P prod clean test exec:java

# Build with local settings
mvn clean test exec:java
```

### System Properties
- `environment=local|production` - Target environment for built site
- `drafts=false|true` - Include posts in _drafts folder
- `workingDirectory=<path>` - Directory containing content (defaults to current directory)

### Output
Generated site is built to: `website/target/site/`

### Local Testing
After building, serve the site locally:
```bash
cd website/target/site
jwebserver
# Or use: python3 -m http.server 8000
```

## Code Style and Conventions

### Java Conventions
- Use **Lombok** annotations for boilerplate reduction (@Slf4j, @Data, @Builder, etc.)
- Follow Java naming conventions (camelCase for methods, PascalCase for classes)
- Use meaningful variable and method names
- Keep methods focused and single-purpose

### HTML Generation
- Use **j2html** fluent API for HTML generation
- Prefer type-safe j2html over string templates
- Build HTML programmatically in Java code

### Markdown Content
- Use YAML front matter for metadata (layout, title, date, tags, etc.)
- Follow existing front matter patterns in _posts/, _books/, _podcasts/
- Date format: `YYYY-MM-DD`
- Images should reference `/images/` path

### Testing
- Write unit tests for core logic and utilities
- Place tests in corresponding package under `src/test/`
- Use JUnit 5 assertions and annotations
- Test file naming: `*Test.java`

## Common Development Tasks

### Adding a New Blog Post
1. Create a new markdown file in `website/_posts/` with naming: `YYYY-MM-DD-title-slug.md`
2. Add YAML front matter with required fields:
   ```yaml
   ---
   layout: post
   title: "Your Post Title"
   date: "YYYY-MM-DD"
   image: /images/your-image.jpg
   tags:
     - tag1
     - tag2
   ---
   ```
3. Write content in Markdown below the front matter
4. Build and test locally before committing

### Adding a New Content Type
1. Create a content model class in `generate-plugin/src/main/java/com/robintegg/web/content/`
2. Implement a content type plugin that extends base plugin classes
3. Add rendering logic in the theme plugin
4. Register the plugin in the WebSiteBuilder
5. Add tests for the new content type

### Modifying the Theme
1. Theme code is in `generate-plugin/src/main/java/com/robintegg/web/theme/`
2. Layouts and includes are defined as j2html components
3. Update `DefaultThemePlugin.java` to modify site-wide styling or structure
4. Test changes by rebuilding the website

### Adding Dependencies
1. Add Maven dependencies to appropriate module's `pom.xml`
2. Use consistent versions across modules when possible
3. Keep dependencies in parent POM when used by multiple modules
4. Run `mvn clean install` after adding dependencies

## CI/CD and Deployment

### GitHub Actions Workflow
- Triggered on push to `master` branch
- Located at `.github/workflows/build-and-deploy.yml`
- Steps:
  1. Checkout code
  2. Setup JDK 17 (Temurin distribution)
  3. Build with Maven: `mvn --batch-mode -P prod clean test exec:java`
  4. Deploy to Netlify using netlify-cli

### Deployment
- Site is automatically deployed to Netlify on successful build
- Production URL: https://www.robintegg.com
- Netlify configuration uses environment variables for auth and site ID

## Key Files to Know

- `website/src/main/java/com/robintegg/web/Build.java` - Main entry point for building the website
- `generate-plugin/src/main/java/com/robintegg/web/engine/WebSiteBuilder.java` - Core website building logic
- `generate-plugin/src/main/java/com/robintegg/web/theme/DefaultThemePlugin.java` - Theme implementation
- `website/site-config.xml` - Website configuration file
- `.github/workflows/build-and-deploy.yml` - CI/CD pipeline

## Tips for Contributors

1. **Make minimal changes**: This is a personal website, so changes should be surgical and focused
2. **Test locally**: Always build and preview the site locally before committing
3. **Follow existing patterns**: Look at existing content and code for conventions
4. **Keep it simple**: The codebase values simplicity and maintainability over complexity
5. **Update documentation**: If adding features, document them in the README or this file

## References

- j2html Documentation: https://j2html.com/
- CommonMark Specification: https://commonmark.org/
- Maven Documentation: https://maven.apache.org/guides/
- Lombok Documentation: https://projectlombok.org/
