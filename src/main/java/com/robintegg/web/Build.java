package com.robintegg.web;

import com.robintegg.web.engine.*;
import com.robintegg.web.layouts.*;
import com.robintegg.web.pages.CategoriesPage;
import com.robintegg.web.pages.IndexPage;
import com.robintegg.web.pages.PodcastsPage;
import com.robintegg.web.pages._404Page;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class Build {

    // build the website
    public static void main(String[] args) throws IOException {

        var workingDirectory = Paths.get("");
        log.info("working directory: " + workingDirectory.toAbsolutePath());

        ContentModel contentModel = new ContentModel();

        // could load some configuration here
        Site site = new Site();
        site.setTitle("Robin Tegg");
        Author author = new Author();
        author.setName("Robin Tegg");
        author.setEmail("robin@tegg.me.uk");
        site.setAuthor(author);
        site.setDescription("A Java developer. Based in Leeds, England. Has interest in Software Architecture, Testing, Automation, Tooling and UI design.");

        contentModel.setSite(site);

        // generate content
        var contentSource = new ContentSource(workingDirectory);
        contentSource.loadContent(contentModel);

        // load layouts
        Map<String, Layout> layouts = new HashMap<>();
        layouts.put("categories", CategoriesLayout.create());
        layouts.put("default", DefaultLayout.create());
        layouts.put("home", HomeLayout.create());
        layouts.put("page", PageLayout.create());
        layouts.put("podcast", PodcastLayout.create());
        layouts.put("post", PostLayout.create());
        layouts.put("tag", TagLayout.create());

        // create output directory
        var outputDirectory = workingDirectory.resolve("target/site");
        log.info("output directory: " + outputDirectory.toAbsolutePath());

        Files.createDirectories(outputDirectory);

        // create render engine

        ContentRenderer contentRenderer = new ContentRenderer();
        contentRenderer.render(outputDirectory, layouts, contentModel);

    }

}
