# GitHub Activity Section

## Overview
The homepage now displays a "Latest GitHub Activity" section above the post list, showing up to 3 of the most recently active repositories from the user's GitHub account.

## Features
- Automatically fetches repositories from GitHub API during site build
- Filters out specified repositories (robintegg, yorkshiregolf)
- Excludes forked repositories
- Displays the 3 most recently pushed repositories
- Shows repository name (linked to GitHub) and description
- Responsive grid layout that adapts to screen size
- Graceful error handling - site builds successfully even if GitHub API is unavailable

## How It Works

### Data Fetching
When the site is built, `SitePlugin` calls the GitHub API to fetch repositories:
1. Reads the `githubUsername` from `site-config.xml`
2. Calls GitHub REST API `/users/{username}/repos?type=owner&sort=pushed&per_page=100`
3. Filters out excluded repositories and forks
4. Selects the top 3 most recently pushed repositories
5. Stores them in the `Site` model

### Rendering
The `HomeLayout` checks if repositories are available:
- If repositories exist, renders the "Latest GitHub Activity" section
- Each repository is displayed as a card with:
  - Repository name (as a clickable link to GitHub)
  - Repository description
- If no repositories are available, the section is not rendered

### Styling
The section uses a responsive CSS grid layout:
- Grid auto-fits columns with minimum width of 250px
- Cards have borders, padding, and hover effects
- Links are styled consistently with the site theme

## Configuration
Repositories to exclude are hardcoded in `SitePlugin.java`:
```java
Set<String> excludedRepos = Set.of("robintegg", "yorkshiregolf");
```

To exclude different repositories, modify this set in the `fetchGitHubActivity` method.

## Error Handling
The implementation includes robust error handling:
- Network errors are logged but don't fail the build
- GitHub API rate limiting is handled gracefully
- If the API is unavailable, the site builds without the activity section
- Errors are logged with helpful messages for debugging

## Dependencies
Uses Java 11+ HttpClient (no additional dependencies required)
