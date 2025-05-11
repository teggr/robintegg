---
layout: post
title: Create iPhone shortcut for sharing links
date: "2025-05-11"
image: /images/maven-4-horizon.jpg
tags:
  - iPhone
  - automation
---

Recently, I found myself needing a quick way to share links from my iPhone directly to my [web service](https://feedhub.dev). While iOS offers many sharing options, there wasn’t a straightforward way to send a link from the Chrome browser’s share page to my custom service. So, I decided to create an iPhone shortcut to solve this problem.

### How It Works

The shortcut I created allows me to share a link from any app (like Chrome) to my web service. Here’s how I set it up:

1. **Open the Shortcuts App**: On your iPhone, open the Shortcuts app and create a new shortcut.
2. **Show in Share Sheet**: Open the short cut options and enable "Show in Share Sheet". This will add an entry in the short cut to handle input from the Share sheet. Here you can specify which share types you want to handle. I'm just handling URLs and web pages for now.
3. **Add scripting**: Add further workflow items to create and set variables, add text etc. In my case I generate a new weblink to my web service from the shared link and open the link in a web view so that it can be edited and tagged.
4. **Save and Name the Shortcut**: Give your shortcut a descriptive name like "Share to My Web Service.".
5. **Run once**: I did have to run the shortcut at least once manually before it showed up in the Share Sheet.

![]({{site.baseurl}}/images/ios_shortcut.png)

### Why This Is Useful

This shortcut has been a game-changer for me as it doesn't require an installed app to handle the web link sharing. It eliminates the need to manually copy and paste links into my web service, saving time and effort. If you have a custom web service or workflow that requires sharing links, this approach could be incredibly useful for you too.

### Potential Use Cases

- Sharing links to a bookmarking service.
- Sending links to a custom API for processing.
- Automating workflows that involve link sharing.

![]({{site.baseurl}}/images/ios_share.png)

If you’re looking for a way to streamline your link-sharing process, I highly recommend giving this a try. It’s a simple yet powerful way to integrate your iPhone with your web service.

The shortcuts can also be exported or shared via an iCloud link: [https://www.icloud.com/shortcuts/3e744937ae9448af8776aec9a222a31a](https://www.icloud.com/shortcuts/3e744937ae9448af8776aec9a222a31a)

Let me know if you’ve created similar shortcuts or have other ideas for automating tasks on iOS!
