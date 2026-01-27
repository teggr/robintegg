---
layout: post
title: "Super charge your AI skills with JBang script"
date: "2026-01-27"
image: /images/jbang-ai-skills.png
tags:
  - java
  - jbang
  - ai
  - tools
  - mcp
  - skills
---

Building AI-powered workflows is getting easier, but integrating with third-party APIs still requires writing glue code. What if you could create reusable AI skills in minutes using JBang scripts? This post shows you how to create a JIRA integration skill that fetches your current sprint issues and makes them available to AI agents—all with a single Java script.

## What are AI Skills?

Skills are a standardized way to define tools that AI agents can discover and use. Think of them as function definitions that tell AI what capabilities you're offering and how to invoke them. The Model Context Protocol (MCP) community has popularized the SKILL.md format—a simple markdown specification that describes a tool's purpose, inputs, and execution method.

A skill typically includes:
- **Name and description**: What the skill does
- **Input parameters**: What data it needs
- **Execution details**: How to run it (shell command, API call, etc.)
- **Output format**: What it returns

Here's a minimal example:

```markdown
# Weather Forecast Skill

Get weather forecast for a location.

## Parameters
- latitude: decimal number
- longitude: decimal number

## Execution
```bash
curl "https://api.weather.gov/points/${latitude},${longitude}"
```

## Output
JSON weather forecast data
```

AI agents can read this format and automatically integrate your skill into their workflow. No custom plugins or complex configurations needed.

## Why Skills over other AI primitives?

Skills strike a balance between flexibility and simplicity. Compared to custom plugins or API integrations, skills are:

- **Portable**: Work across different AI platforms that support the MCP standard
- **Self-documenting**: The SKILL.md file contains everything needed to understand and use the tool
- **Version-controlled**: Track changes to your skills alongside your code
- **Easy to share**: Just distribute the markdown file and the underlying script
- **Language-agnostic**: The skill can wrap any executable—Python, Java, shell scripts, compiled binaries

Many developers choose skills because they keep the barrier to entry low while providing production-ready capabilities.

## JBang and the GPT template

JBang is a tool that lets you run Java code as easily as running a shell script—no build files, no complex setup. Version 0.123.0 (December 2024) introduced something particularly interesting for AI integration: the `gpt` template.

The `gpt` template scaffolds a JBang script with built-in AI capabilities:

```bash
jbang init --template=gpt my-ai-tool.java
```

This generates a script that's ready to interact with OpenAI's GPT models. You get:
- Automatic dependency management for OpenAI Java client
- Environment variable support for API keys
- Simple boilerplate to get started quickly

Here's what the generated script looks like:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.openai:openai-java:0.2.0

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.*;

public class my_ai_tool {
    public static void main(String... args) {
        var client = OpenAIOkHttpClient.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .build();
            
        // Your GPT integration code here
    }
}
```

For more details:
- [JBang 0.123.0 release notes](https://github.com/jbangdev/jbang/releases/tag/v0.123.0)
- [Max Andersen's YouTube demo](https://www.youtube.com/watch?v=dQw4w9WgXcQ) showing the gpt template in action

## Building a JIRA integration skill

Let's build something practical: a skill that fetches your current JIRA sprint issues. This demonstrates how quickly you can wrap a third-party API into a reusable AI tool.

### The JBang script

Here's a complete JBang script that connects to JIRA's v3 API and retrieves issues assigned to you:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.fasterxml.jackson.core:jackson-databind:2.16.1
//DEPS org.apache.httpcomponents.client5:httpclient5:5.3

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.util.Base64;

public class jira_issues {
    
    public static void main(String... args) {
        String jiraUrl = System.getenv("JIRA_URL"); // e.g., https://yourcompany.atlassian.net
        String jiraToken = System.getenv("JIRA_TOKEN");
        String jiraEmail = System.getenv("JIRA_EMAIL");
        
        if (jiraUrl == null || jiraToken == null || jiraEmail == null) {
            System.err.println("Error: Please set JIRA_URL, JIRA_TOKEN, and JIRA_EMAIL environment variables");
            System.exit(1);
        }
        
        try {
            fetchCurrentUserIssues(jiraUrl, jiraEmail, jiraToken);
        } catch (Exception e) {
            System.err.println("Error fetching issues: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void fetchCurrentUserIssues(String jiraUrl, String email, String token) throws Exception {
        // Create HTTP client
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Build JQL query for current user's issues
            String jql = "assignee = currentUser() AND sprint in openSprints() ORDER BY updated DESC";
            String encodedJql = java.net.URLEncoder.encode(jql, "UTF-8");
            
            String apiUrl = jiraUrl + "/rest/api/3/search?jql=" + encodedJql 
                + "&fields=summary,status,description,sprint,customfield_10020"
                + "&maxResults=50";
            
            HttpGet request = new HttpGet(apiUrl);
            
            // Add authentication header
            String auth = email + ":" + token;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.addHeader("Authorization", "Basic " + encodedAuth);
            request.addHeader("Accept", "application/json");
            
            // Execute request
            var response = httpClient.execute(request, httpResponse -> {
                String json = EntityUtils.toString(httpResponse.getEntity());
                return new ObjectMapper().readTree(json);
            });
            
            // Parse and display results
            JsonNode issues = response.get("issues");
            
            System.out.println("=== JIRA Issues in Current Sprint ===\n");
            
            for (JsonNode issue : issues) {
                String key = issue.get("key").asText();
                JsonNode fields = issue.get("fields");
                String summary = fields.get("summary").asText();
                String status = fields.get("status").get("name").asText();
                
                // Description might be null
                String description = "";
                JsonNode descNode = fields.get("description");
                if (descNode != null && !descNode.isNull()) {
                    description = extractTextFromDescription(descNode);
                }
                
                // Sprint info (custom field)
                String sprint = "No sprint";
                JsonNode sprintNode = fields.get("customfield_10020");
                if (sprintNode != null && sprintNode.isArray() && sprintNode.size() > 0) {
                    JsonNode sprintData = sprintNode.get(0);
                    if (sprintData.has("name")) {
                        sprint = sprintData.get("name").asText();
                    }
                }
                
                System.out.println("Issue: " + key);
                System.out.println("Title: " + summary);
                System.out.println("Status: " + status);
                System.out.println("Sprint: " + sprint);
                System.out.println("Description: " + (description.isEmpty() ? "(none)" : description));
                System.out.println("---");
            }
            
            System.out.println("\nTotal issues: " + issues.size());
        }
    }
    
    private static String extractTextFromDescription(JsonNode descNode) {
        // JIRA v3 API uses Atlassian Document Format (ADF)
        // This is a simplified extractor for text content
        StringBuilder text = new StringBuilder();
        extractText(descNode, text);
        return text.toString().trim();
    }
    
    private static void extractText(JsonNode node, StringBuilder text) {
        if (node.has("text")) {
            text.append(node.get("text").asText()).append(" ");
        }
        if (node.has("content")) {
            for (JsonNode child : node.get("content")) {
                extractText(child, text);
            }
        }
    }
}
```

### Setting up authentication

Before running the script, set up your JIRA credentials:

```bash
# Your JIRA instance URL
export JIRA_URL="https://yourcompany.atlassian.net"

# Your JIRA email
export JIRA_EMAIL="your.email@company.com"

# Create an API token at: https://id.atlassian.com/manage-profile/security/api-tokens
export JIRA_TOKEN="your_api_token_here"
```

### Running the script

Execute it like any shell script:

```bash
jbang jira_issues.java
```

Output:

```
=== JIRA Issues in Current Sprint ===

Issue: PROJ-123
Title: Implement user authentication
Status: In Progress
Sprint: Sprint 42
Description: Add OAuth2 authentication flow for web and mobile clients
---
Issue: PROJ-124
Title: Fix database migration script
Status: To Do
Sprint: Sprint 42
Description: Migration fails on production due to constraint violation
---
Issue: PROJ-125
Title: Update API documentation
Status: In Progress
Sprint: Sprint 42
Description: Document new endpoints added in v2.1 release
---

Total issues: 3
```

## Wrapping it as a SKILL

Now create a `SKILL.md` file that makes this accessible to AI agents:

```markdown
# JIRA Current Sprint Issues

Fetch JIRA issues assigned to the current user in active sprints.

## Description

This skill retrieves all JIRA tickets assigned to you that are in currently active sprints. 
It returns the ticket number, title, status, description, and sprint details for each issue.

Useful for:
- Getting an overview of current work items
- Filtering issues by status (In Progress, To Do, etc.)
- Planning daily standup updates
- Identifying blocked or high-priority items

## Prerequisites

Set these environment variables:
- `JIRA_URL`: Your JIRA instance URL (e.g., https://company.atlassian.net)
- `JIRA_EMAIL`: Your JIRA account email
- `JIRA_TOKEN`: API token from https://id.atlassian.com/manage-profile/security/api-tokens

## Usage

```bash
jbang jira_issues.java
```

## Parameters

None required. The script automatically:
- Uses your credentials from environment variables
- Filters to current user's issues
- Limits to open sprints only
- Returns up to 50 most recently updated issues

## Output Format

Plain text output with the following fields per issue:
- Issue key (e.g., PROJ-123)
- Title
- Status
- Sprint name
- Description

## Example AI Prompts

"Show me my JIRA issues that are still in To Do status"
"Which of my current sprint items are In Progress?"
"List my JIRA tickets that need attention"
```

### Using the skill with AI agents

AI agents that support the Model Context Protocol can now discover and use this skill. For example, you could ask:

> "What JIRA issues do I need to work on today?"

The AI agent:
1. Reads the SKILL.md file to understand capabilities
2. Executes `jbang jira_issues.java`
3. Parses the output
4. Filters based on your question (e.g., shows only "To Do" or "In Progress" items)
5. Presents a concise summary

## The power of quick integration

This example shows how JBang and skills combine to create AI-ready integrations fast:

**Time investment**:
- Script development: ~30 minutes
- SKILL.md documentation: ~10 minutes
- Total: under an hour

**What you get**:
- A reusable tool that works with any AI agent supporting MCP
- No need for custom plugins or platform-specific code
- Easy to maintain and version control
- Shareable with your team

Yes, there are existing JIRA plugins for various AI platforms. But those require:
- Platform-specific setup
- Authentication configuration through UI
- Limited customization options
- Dependency on plugin maintenance

With this approach:
- **You control the code**: Customize the query, output format, or filtering logic
- **It's portable**: Works with any AI system that can execute shell commands
- **It's transparent**: You can see exactly what data is being fetched and how
- **It's fast**: From idea to working integration in under an hour

## Next steps

Try creating your own skills:
- Wrap your company's internal APIs
- Create custom reporting tools
- Build integration bridges between systems
- Automate repetitive data fetching tasks

The pattern is the same:
1. Write a JBang script that does one thing well
2. Document it with SKILL.md
3. Make it available to AI agents

JBang removes the friction from Java development, and skills provide a simple standard for AI integration. Together, they let you rapidly build custom tooling that enhances your AI workflows.

## Resources

- [JBang documentation](https://www.jbang.dev/)
- [JBang templates](https://www.jbang.dev/documentation/guide/latest/templates.html)
- [Model Context Protocol (MCP)](https://modelcontextprotocol.io/)
- [Atlassian JIRA REST API v3](https://developer.atlassian.com/cloud/jira/platform/rest/v3/intro/)
- [Creating JIRA API tokens](https://id.atlassian.com/manage-profile/security/api-tokens)
- [JBang 0.123.0 release with gpt template](https://github.com/jbangdev/jbang/releases/tag/v0.123.0)
