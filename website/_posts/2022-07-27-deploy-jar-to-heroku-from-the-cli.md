---
layout: post
title: Deploy a Jar to Heroku from the CLI
date: "2022-07-27"
image: /assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg
tags:
  - heroku
  - cli
---

Whilst taking a quick look into the [Hilla Web Framework](https://robintegg.com/2022/07/26/first-look-at-hilla-web-framework.html) the documentation includes some useful steps for deploying a jar to [heroku](https://hilla.dev/docs/tutorials/in-depth-course/production-build-and-deployment).

Deploying a jar straight to heroku is an alternative to the approach that builds and deploys from your git repo.

Those steps are summarised here:

```
heroku login

heroku plugins:install java

heroku create --no-remote --region=eu teggr-some-app-name

heroku config --app=teggr-some-app-name

heroku config:set --app=teggr-some-app-name COM_ROBINTEGG_SOME_VALUE=a value

mvn package -Pproduction

heroku deploy:jar target\some-app-name-1.0-SNAPSHOT.jar --app 
teggr-some-app-name

heroku logs --tail teggr-some-app-name

heroku addons:create heroku-postgresql -a teggr-some-app-name
```
