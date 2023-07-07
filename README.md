# robintegg

[![Netlify Status](https://api.netlify.com/api/v1/badges/cea95cb7-873a-41fb-b028-c2971d68a889/deploy-status)](https://app.netlify.com/sites/awesome-golick-a6b04a/deploys)

https://www.robintegg.com

# Design

## Content

```mermaid
classDiagram
    theme --> contentitem
    theme *-- includes
    theme *-- layouts
    theme *-- pages
    theme --> feed
    theme --> tags
    theme --> categories
    theme --> index
    feed --> contentitem

    contentitem <|-- book
    contentitem <|-- podcast
    contentitem <|-- post
    contentitem <|-- staticfiles
    
    tags -- contentitem 
    categories -- contentitem 
    index -- contentitem
    
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
