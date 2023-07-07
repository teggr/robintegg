# robintegg

[![Netlify Status](https://api.netlify.com/api/v1/badges/cea95cb7-873a-41fb-b028-c2971d68a889/deploy-status)](https://app.netlify.com/sites/awesome-golick-a6b04a/deploys)

https://www.robintegg.com

# Design

```mermaid
classDiagram
    theme -- content
    theme *-- includes
    theme *-- layouts
    theme *-- pages
    theme -- feed
    feed -- content
    
    content <|-- book
    content <|-- podcast
    content <|-- post
    content <|-- staticfiles
    
```

# Finding Podcast Links

https://podnews.net/search?q=

# Building website

```
com.robintegg.web.Build.main();
```

# Running JWebServer

```
cd target\site
C:\Users\teggro01\.jdks\temurin-19.0.2\bin\jwebserver   
```
