---
layout: post
title: Using Github Actions to publish Java 17 statically generated site to Netlify
date: "2023-07-01"
image: /assets/images/carl-heyerdahl-KE0nC8-58MQ-unsplash.jpg
tags:
  - netlify
  - java
  - maven
  - j2html
---
As part of an interest piece around using [j2html](https://j2html.com/) as part of a pure Java approach to web development, I decided to rebuild this site using [j2html](https://j2html.com/). The site was previously generated using the popular ruby static site generator - [jekyll](https://jekyllrb.com/).

After building the site generation code using Maven and Java I was hoping that I would be able to deploy the code using the existing approach that I previously wrote about that used the [Netlify provided build image](https://robintegg.com/2018/01/21/publish-maven-site-to-netlify). **Unfortunately**, the existing build image only supports Java 8 and I'd already written plenty of code against the Java 17 compiler.

There are some Netlify forum entries that discuss this issue that others have also run into but it looks pretty low on their priority list. The main suggestion seems to be to move the build deploy to an external build system + the Netlify CLI app.

My site is hosted on Github at [https://github.com/teggr/robintegg](https://github.com/teggr/robintegg) so moving the build and deploy to Github Actions made most sense and is documented below.

* [Understand Github Actions](https://docs.github.com/en/actions/learn-github-actions/understanding-github-actions) is a useful first read if you have not used Github Actions before.
* Add your first Github Action to [build your maven project](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-maven)
* Add [Netlify publish steps](https://github.com/marketplace/actions/netlify-cli) to the Action
  * Add a Netlify personal access token to your repository's secrets
  * Add your Netlify project's Site ID to your repository's secrets
* In Netlify turn off the repo linking feature of your project.
* Test the integration by pushing to your repo. Unless you have the `--prod` flag this will create a preview branch.
* Enable production deployments by adding the `--prod` argument to the cli command

Here is a snapshot of my Github Action.

```yaml
.github/worfkows

name: Build and deploy

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven
      - name: Build with Maven
        run: mvn --batch-mode clean test exec:java
      - name: Publish
        uses: South-Paw/action-netlify-cli@1.0.1
        id: netlify
        with:
          # be sure to escape any double quotes with a backslash and note that the --json
          # flag has been passed when deploying - if you want the outputs to work you'll need to include it
          args: 'deploy --json --prod --dir \"./target/site\" --message \"production [${{ github.sha }}]\"'
        env:
          NETLIFY_AUTH_TOKEN: ${{ secrets.NETLIFY_AUTH_TOKEN }}
          NETLIFY_SITE_ID: ${{ secrets.NETLIFY_SITE_ID }}
```

HTH