---
layout: post
title: Maven 4 incoming
date: "2025-04-04"
description: "Key changes coming in Maven 4 including Java 17 requirement, subproject terminology, and new BOM package type."
image: /images/maven-4-horizon.jpg
tags:
  - java
  - maven
---

<blockquote class="bluesky-embed" data-bluesky-uri="at://did:plc:z3t34w2cmsf2qwvbfhnef4y4/app.bsky.feed.post/3llxlk4c63s2k" data-bluesky-cid="bafyreicmtojp4bgm5laqa53udiz3k4dcdkgquwhfsgtnwypdcvqleckhsu" data-bluesky-embed-color-mode="system"><p lang="en">Want to know what changes with Maven 4? Grab something to drink and read the following article I wrote for the Maven page (will continuously be updated with further releases):
maven.apache.org/whatsnewinma...

Documentation is fun 😍<br><br><a href="https://bsky.app/profile/did:plc:z3t34w2cmsf2qwvbfhnef4y4/post/3llxlk4c63s2k?ref_src=embed">[image or embed]</a></p>&mdash; Matthias Bünger (<a href="https://bsky.app/profile/did:plc:z3t34w2cmsf2qwvbfhnef4y4?ref_src=embed">@bukama.bsky.social</a>) <a href="https://bsky.app/profile/did:plc:z3t34w2cmsf2qwvbfhnef4y4/post/3llxlk4c63s2k?ref_src=embed">April 4, 2025 at 5:32 AM</a></blockquote><script async src="https://embed.bsky.app/static/embed.js" charset="utf-8"></script>

Saw this recent post on bluesky at thought that I should take a look at what's coming down the pipe -
[https://maven.apache.org/whatsnewinmaven4.html](https://maven.apache.org/whatsnewinmaven4.html)

My key takeaways are below:

⬆️ **Java 17 Requirement:**

You'll need at least Java 17 to run Maven 4. Time to upgrade if you haven't already! It will still compile against older Java versions ☕

🚀 **Modules are subprojects:**

Maven is dropping the module naming to avoid clashing with Java modules. What was modules will now be subprojects 💨

⚙ **bom package type:**

Bill of materials packaging type introduced to help with those dependency managing types like Spring Boot  🌿

✨ **Automatic Versioning of subprojects:**

(My Fav :hearting:) No more need to declare the parent POM version in subprojects (nee modules)! 🛠️

📦 **CI-friendly Variables:**

Built in support for `${revision}` variables in your POM versions! This reduces a lot of pain in CI environments 💪

🔗 **Pre- and post-phases:**

Every lifecycle page will have a `before` and `after` phase to bind plugins to. Very helpful for setting up test data and source generation tooling 🔥

⚠ **Old maven plugins:**

As with any major version change there will be some compatibility issues with older, less well maintained plugins. Hopefully the authors will be able to update their plugins or alternatives will need to be found!

😉 Happy building! 🏗️