---
layout: post
title: Easy Maven Publishing with JitPack.io
date: "2025-08-02"
image: /images/publish-maven-to-jitpack.jpg
tags:
  - java
  - maven
  - library
---

If you’ve ever wanted to share a Java library without setting up a full Maven Central or OSSRH workflow, **JitPack.io** is a developer-friendly alternative that lets you publish directly from GitHub. No Sonatype signups. No GPG keys. No ticket systems. Just a `git push` and you’re live.

In this post, I’ll show you how I published my own Java project, [`cli-power-tools`](https://github.com/teggr/cli-power-tools), using JitPack — and how you can do the same in under 10 minutes.

---

## 💡 What Is JitPack?

[JitPack](https://jitpack.io/) is a free service that builds your Maven or Gradle project directly from your GitHub repo. It hosts your builds as Maven artifacts and gives you a public Maven repository to consume them — perfect for fast prototyping, internal tools, or open-source utilities.

---

## ✅ Why Use JitPack?

- 📦 No manual publishing
- 🔒 No account or credentials needed
- 🛠 Built from GitHub commits, branches, or tags
- 🔗 Generates a hosted Maven repo for you

---

## 🧪 Example: Publishing `cli-power-tools`

Let’s walk through how [`cli-power-tools`](https://github.com/teggr/cli-power-tools), a small utility for CLI-based Java apps, is published with JitPack.

### 1. 📁 Structure Your Project

Make sure your project is a standard Java build (Maven or Gradle). Here’s what `cli-power-tools` uses:

- Java 17
- Maven (`pom.xml`)
- GitHub-hosted

💡 *JitPack supports both Maven and Gradle builds out of the box.*

---

### 2. 📦 Build with JitPack

Go to [https://jitpack.io](https://jitpack.io) and paste in your GitHub repo URL:

```
teggr/cli-power-tools
```

Click **"Look Up"**, then select a tag, branch, or commit and click **"Get it"**.

JitPack will generate Maven coordinates like this:

```xml
<dependency>
  <groupId>com.github.teggr</groupId>
  <artifactId>cli-power-tools</artifactId>
  <version>main-SNAPSHOT</version>
</dependency>
```

---

## 💻 Consuming `cli-power-tools`

To use this library in your own project:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.teggr</groupId>
    <artifactId>cli-power-tools</artifactId>
    <version>main-SNAPSHOT</version>
  </dependency>
</dependencies>
```

---

## 🏷 Tag a Release (Optional, but Recommended)

JitPack builds from tags, commits, or branches. For stable versioning, create a Git tag:

```bash
git tag v1.0.0
git push origin v1.0.0
```

---

## 🧩 Troubleshooting Tips

- ✅ Ensure your GitHub repo is public
- 🔍 If your build fails, inspect logs from the JitPack UI
- 📦 Make sure all plugins/dependencies in your `pom.xml` are available in public repositories

---

## 🏁 Final Thoughts

JitPack is a super simple way to share Java libraries with minimal friction. For internal tools, experimental libraries, or lightweight open-source utilities, it’s a great alternative to the traditional Maven Central pipeline. There's also paid options for more serious use cases and private repositories.

If you want to check out or use [`cli-power-tools`](https://github.com/teggr/cli-power-tools), you can try it immediately with:

```xml
<dependency>
  <groupId>com.github.teggr</groupId>
  <artifactId>cli-power-tools</artifactId>
  <version>main-SNAPSHOT</version>
</dependency>
```

Happy hacking! 🚀

---

## 🔗 Resources

- [JitPack Homepage](https://jitpack.io)
- [`cli-power-tools` on GitHub](https://github.com/teggr/cli-power-tools)
- [JitPack Build Page for cli-power-tools](https://jitpack.io/#teggr/cli-power-tools)  
