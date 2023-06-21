package com.robintegg.web;

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

        // TODO: render site
        // raw files
        // static pages (index.html) - layout (home) -> (default)
        // pages generated from collections (podcasts/posts)

        // output model
        System.out.println(contentModel);

    }

}
