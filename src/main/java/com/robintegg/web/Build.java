package com.robintegg.web;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.ContentSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class Build {

    // build the website
    public static void main(String[] args) throws IOException {

        var workingDirectory = Paths.get("");
        log.info("working directory: " + workingDirectory.toAbsolutePath());

        // could load some configuration here

        // generate content
        var contentSource = new ContentSource(workingDirectory);

        ContentModel contentModel = new ContentModel();
        contentSource.loadContent(contentModel);

        // need to load in theme / templates
        // TODO: Page404 -> need to add the template rendering engine
        // layouts
       // Map<String, Function<ContentModel,DomContent[]>> renderFunctions = new HashMap<>();
       // renderFunctions.put("404", _404::create);

        // TODO: render site using content
        // raw files
        // static pages (index.html) - layout (home) -> (default)
        // pages generated from collections (podcasts/posts)

        // output model
        System.out.println(contentModel);

    }

}
