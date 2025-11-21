---
layout: post
title: "Chrome Extensions Make Great Developer Tools"
date: "2025-11-20"
image: /images/chrome-extensions-developer-tools.jpg
tags:
  - chrome
  - extensions
  - tools
  - web
  - javascript
---

Chrome extensions are one of the most underrated ways to build custom developer tooling and automation. While most developers are familiar with using extensions, fewer realize how easy it is to create your own to solve specific problems in your workflow.

## The Power of Custom Developer Tools

The [Chrome Web Store developer tools category](https://chromewebstore.google.com/category/extensions/productivity/developer) is packed with powerful extensions that developers rely on daily. 

The Chrome extensions ecosystem is vibrant and diverse. There are many applications and services that have created valuable extensions such as AdBlock, 1Password, Evernote, and Grammarly which help to enhance workflows by improving readability, managing credentials, clipping research, and polishing prose, reflecting an active community building interoperable browser tooling.

What makes Chrome extensions particularly powerful for developers is their multipurpose nature. They can:

- Read and modify web page content
- Intercept and inspect HTTP requests
- Provide custom UI overlays on existing websites
- Store data locally or sync across devices
- Integrate with external APIs and services

This versatility opens up countless possibilities for mixing and extending features to build tools tailored to your specific use cases.

## Real-World Use Cases

Here are some practical examples of custom extensions developers might build:

**Form Auto-filling with Private APIs**: Combine form auto-filling capabilities with access to your company's test APIs to automatically complete complex onboarding flows during testing. Instead of manually filling out multi-step registration forms, your extension could pull test data from your staging environment.

**Custom Code Review Tools**: Add buttons or shortcuts to GitHub pull requests that integrate with your team's internal tools, metrics dashboards, or deployment systems.

**Development Environment Switcher**: Create quick toggles to switch between different API endpoints (local, staging, production) by intercepting and rewriting requests—useful when testing web applications against different environments.

These are just starting points—the combination of browser access and JavaScript flexibility means you can automate nearly any repetitive browser-based task.

## Building Your First Extension: GitHub Changelog Reader

Let's walk through creating a simple but functional Chrome extension. We'll build a GitHub Changelog RSS feed reader that displays the latest updates from GitHub's official changelog in a nicely formatted popup.

There's an example project available on [GitHub](https://github.com/teggr/github-changelog-reader).

### Project Structure

Our extension will consist of three main files:

```
github-changelog-reader/
├── manifest.json      # Extension configuration
├── popup.html         # The UI that appears when clicking the extension icon
└── popup.js           # JavaScript to fetch and display the RSS feed
```

### Step 1: Create the Manifest

The `manifest.json` file defines your extension's metadata, permissions, and configuration. Create this file:

```json
{
  "manifest_version": 3,
  "name": "GitHub Changelog Reader",
  "version": "1.0.0",
  "description": "Display the latest GitHub changelog entries",
  "permissions": [
    "activeTab"
  ],
  "host_permissions": [
    "https://github.blog/*"
  ],
  "action": {
    "default_popup": "popup.html",
    "default_icon": {
      "16": "icon16.png",
      "48": "icon48.png",
      "128": "icon128.png"
    }
  },
  "icons": {
    "16": "icon16.png",
    "48": "icon48.png",
    "128": "icon128.png"
  }
}
```

**Key points about the manifest:**

- `manifest_version: 3` uses the latest Chrome extension API (Manifest V3)
- `permissions` and `host_permissions` allow the extension to fetch data from GitHub's blog
- `action.default_popup` specifies the HTML file to show when clicking the extension icon

### Step 2: Create the Popup UI

Create `popup.html` with Bootstrap styling:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>GitHub Changelog</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
  <style>
    body {
      width: 400px;
      max-height: 600px;
      overflow-y: auto;
      padding: 0;
      margin: 0;
    }
    .header {
      background: linear-gradient(135deg, #24292e 0%, #2c3e50 100%);
      color: white;
      padding: 15px;
      position: sticky;
      top: 0;
      z-index: 10;
    }
    .header h5 {
      margin: 0;
      font-weight: 600;
    }
    .changelog-item {
      border-bottom: 1px solid #e1e4e8;
      padding: 12px 15px;
      transition: background-color 0.2s;
    }
    .changelog-item:hover {
      background-color: #f6f8fa;
    }
    .changelog-item:last-child {
      border-bottom: none;
    }
    .changelog-title {
      font-size: 14px;
      font-weight: 500;
      color: #0969da;
      text-decoration: none;
      display: block;
      margin-bottom: 4px;
    }
    .changelog-title:hover {
      text-decoration: underline;
    }
    .changelog-date {
      font-size: 12px;
      color: #656d76;
    }
    #loading {
      text-align: center;
      padding: 30px;
      color: #656d76;
    }
    #error {
      padding: 20px;
      text-align: center;
      color: #d1242f;
    }
  </style>
</head>
<body>
  <div class="header d-flex justify-content-between align-items-center">
    <div class="d-flex align-items-center">
      <i class="bi bi-github fs-4 me-2"></i>
      <h5>GitHub Changelog</h5>
    </div>
    <button id="refresh" class="btn btn-sm btn-light" title="Refresh">
      <i class="bi bi-arrow-clockwise"></i>
    </button>
  </div>

  <div id="loading">
    <div class="spinner-border spinner-border-sm text-secondary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
    <p class="mt-2 mb-0">Loading changelog...</p>
  </div>

  <div id="error" style="display: none;">
    <i class="bi bi-exclamation-triangle fs-3"></i>
    <p class="mt-2">Failed to load changelog</p>
    <button class="btn btn-sm btn-primary" onclick="location.reload()">Try Again</button>
  </div>

  <div id="content"></div>

  <script src="popup.js"></script>
</body>
</html>
```

The UI features:
- A dark header with GitHub icon from Bootstrap Icons
- A refresh button for manually updating the feed
- Loading and error states
- Clean, modern styling with Bootstrap
- Hover effects for better interactivity

### Step 3: Implement the Feed Reader

Create `popup.js` to fetch and parse the RSS feed:

```javascript
// Fetch and display the GitHub changelog RSS feed
async function loadChangelog() {
  const contentDiv = document.getElementById('content');
  const loadingDiv = document.getElementById('loading');
  const errorDiv = document.getElementById('error');

  try {
    // Show loading state
    loadingDiv.style.display = 'block';
    errorDiv.style.display = 'none';
    contentDiv.innerHTML = '';

    // Fetch the RSS feed
    const response = await fetch('https://github.blog/changelog/feed/');
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const xmlText = await response.text();
    
    // Parse the XML
    const parser = new DOMParser();
    const xmlDoc = parser.parseFromString(xmlText, 'text/xml');
    
    // Check for parsing errors
    const parseError = xmlDoc.querySelector('parsererror');
    if (parseError) {
      throw new Error('Error parsing RSS feed');
    }

    // Get all items from the feed
    const items = xmlDoc.querySelectorAll('item');
    
    if (items.length === 0) {
      throw new Error('No items found in feed');
    }

    // Display the first 10 items
    const maxItems = Math.min(items.length, 10);
    let html = '';

    for (let i = 0; i < maxItems; i++) {
      const item = items[i];
      const title = item.querySelector('title')?.textContent || 'No title';
      const link = item.querySelector('link')?.textContent || '#';
      const pubDate = item.querySelector('pubDate')?.textContent || '';

      // Format the date
      let formattedDate = '';
      if (pubDate) {
        const date = new Date(pubDate);
        formattedDate = date.toLocaleDateString('en-US', { 
          month: 'short', 
          day: 'numeric', 
          year: 'numeric' 
        });
      }

      html += `
        <div class="changelog-item">
          <a href="${link}" target="_blank" class="changelog-title">${escapeHtml(title)}</a>
          <div class="changelog-date">
            <i class="bi bi-calendar3"></i> ${formattedDate}
          </div>
        </div>
      `;
    }

    // Hide loading and show content
    loadingDiv.style.display = 'none';
    contentDiv.innerHTML = html;

  } catch (error) {
    console.error('Error loading changelog:', error);
    loadingDiv.style.display = 'none';
    errorDiv.style.display = 'block';
  }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

// Load changelog on popup open
document.addEventListener('DOMContentLoaded', loadChangelog);

// Refresh button handler
document.getElementById('refresh').addEventListener('click', () => {
  loadChangelog();
});
```

**Key features of the JavaScript:**

- Uses the `fetch` API to retrieve the RSS feed from GitHub
- Parses XML using the browser's built-in `DOMParser`
- Extracts and formats the title, link, and publication date from each item
- Displays the 10 most recent changelog entries
- Includes error handling and loading states
- Opens links in new tabs when clicked
- Sanitizes content to prevent XSS attacks

### Step 4: Add Icons

For a professional look, you'll need icon images. You can create simple PNG icons or use icon generators online. Create three sizes:

- `icon16.png` (16x16 pixels)
- `icon48.png` (48x48 pixels)
- `icon128.png` (128x128 pixels)

A simple GitHub logo or octocat icon works well. You can download official GitHub icons from [GitHub Logos and Usage](https://github.com/logos) or create your own using the Bootstrap Icons GitHub icon as a reference.

For a quick start, you can use online tools like [Icon Generator](https://icon.kitchen/) to create icon sets from an SVG or existing image.

## Installing Your Extension

To install and test your extension locally:

1. **Navigate to Chrome Extensions**: Open Chrome and go to `chrome://extensions/`

2. **Enable Developer Mode**: Toggle the "Developer mode" switch in the top-right corner

3. **Load Your Extension**:
   - Click "Load unpacked"
   - Navigate to your `github-changelog-reader` folder
   - Select the folder

4. **Test It**: Click the extension icon in your Chrome toolbar to see your GitHub Changelog reader in action!

Your extension should now display the latest GitHub changelog entries. Click the refresh button to reload the feed, or click any title to open the full changelog entry in a new tab.

## Tips for Development

**Debugging**: 
- Right-click on the extension popup and select "Inspect" to open Chrome DevTools
- Console logs and errors will appear in the popup's DevTools window
- Check the extension's background page logs in `chrome://extensions/` (click "Inspect views: service worker")

**Reloading**: 
- After making code changes, return to `chrome://extensions/`
- Click the refresh icon on your extension to reload it
- Close and reopen the popup to see your changes

**Permissions**: 
- Chrome extensions require explicit permissions for security
- Only request permissions your extension actually needs
- Users can see all requested permissions before installing

## What's Next?

This simple extension demonstrates the basics, but Chrome extensions can do much more. In future posts, we'll explore:

- **Reading from the page**: Extracting and manipulating content from websites you visit
- **Capturing HTTP requests**: Intercepting network traffic to debug or modify API calls
- **Background service workers**: Running code continuously in the background, even when the popup is closed
- **Content scripts**: Injecting JavaScript into web pages to add features or automation
- **Chrome Storage API**: Persisting data locally or syncing settings across devices

## Wrapping Up

Chrome extensions provide a powerful platform for building custom developer tools that integrate seamlessly into your browser workflow. Whether you're automating repetitive tasks, enhancing existing websites, or building entirely new functionality, extensions offer flexibility and ease of development.

The example we built—a GitHub Changelog reader—demonstrates the fundamentals: manifest configuration, UI with Bootstrap, fetching and parsing external data, and basic user interaction. From here, you can extend this pattern to build tools that solve your specific development challenges.

Start small, experiment often, and build tools that make your work easier. The best developer tools are often the ones you create yourself.

## Resources

- [Chrome Extension Documentation](https://developer.chrome.com/docs/extensions/)
- [Chrome Web Store Developer Tools](https://chromewebstore.google.com/category/extensions/productivity/developer)
- [GitHub Changelog Feed](https://github.blog/changelog/feed/)
- [Bootstrap Documentation](https://getbootstrap.com/)
- [Bootstrap Icons](https://icons.getbootstrap.com/)
- [Manifest V3 Migration Guide](https://developer.chrome.com/docs/extensions/develop/migrate)
