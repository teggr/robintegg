# robintegg

[![Netlify Status](https://api.netlify.com/api/v1/badges/cea95cb7-873a-41fb-b028-c2971d68a889/deploy-status)](https://app.netlify.com/sites/awesome-golick-a6b04a/deploys)

https://www.robintegg.com

# Design

## Content

```mermaid
classDiagram
    
    theme *-- includes : has
    theme *-- layouts : has
    theme *-- pages : has
    theme --> feed : uses
    theme --> tags : uses
    theme --> categories : uses
    theme --> index : uses

    pages --> contentitem : made up of
    
    pages --> layouts : uses
    layouts --> includes : uses
    pages --> includes : uses
    
    contentitem <|-- book
    contentitem <|-- podcast
    contentitem <|-- post
    contentitem <|-- staticfiles
    
    tags -- contentitem : built from
    categories -- contentitem  : built from
    index -- contentitem : built from
    
    feed --> index : uses
    
```

## Engine

```mermaid
classDiagram
    
    contentmodel --> contenttypeplugins : finds content\n using
    contentmodel *-- contentitems
    contentmodel --> aggregatorplugins : builds other models\n using content
    
    contentmodel --> contentmodelvisitor : exposes model via\n common types
    contentmodelvisitor --> page : 
    contentmodelvisitor --> files
    contentmodelvisitor --> tags
    
    contentrenderer --> contentmodelvisitor : uses to find\n items to render
    contentrenderer --> contentrenderplugins : use to render content types
    
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

# Configuration

## System properties

```properties
environment=local - name of target environment for built site [local|production] 
drafts=false - include the posts in the _drafts folder [true|false]
```