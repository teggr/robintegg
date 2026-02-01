---
layout: post
title: IntelliJ syntax highlighting for Java Text Blocks
date: "2024-02-26"
description: "Enable syntax highlighting in IntelliJ Java text blocks using language injection comments for HTML, SQL, JSON, and more."
image: /images/jorge-rosal-nlIxMHQl6Vo-unsplash.jpg
tags:
  - java
  - intellij
  - tips
# Photo by <a href="https://unsplash.com/@yortrosal?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Jorge Rosal</a> on <a href="https://unsplash.com/photos/a-person-standing-in-front-of-a-screen-with-text-nlIxMHQl6Vo?utm_content=creditCopyText&utm_medium=referral&utm_source=unsplash">Unsplash</a>
---

This is a game changing tip for those that use Java Text Blocks in their IntelliJ editors.

At some point JetBrains introduced the [Language Injection](https://www.jetbrains.com/help/idea/using-language-injections.html) feature into IntelliJ.

What's cool about this is that you can now add a comment above a text block or string to enable syntax highlighting.

For example, you could write the following with the language comment above and you'll get syntax highlighting:

```java
//language=HTML
var someHtml = """
                <div id="target">
                  Click here
                </div>
                <div id="other">
                  Trigger the handler
                </div>
                """;

//language=JavaScript
var someJs = """
            $( "#target" ).on( "click", function() {
              alert( "Handler for `click` called." );
            } );
            """;
```

![syntax highlighting output]({{site.baseurl}}/images/syntax-highlighting.png)

Note that the syntax highlighting support only covers what languages you have available in your version of Intellij, so if you've got the community version you may have a reduced set of languages available.