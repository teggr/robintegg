package com.robintegg.web;

import com.robintegg.web.content.podcast.PodcastLayout;
import com.robintegg.web.content.post.PostLayout;
import com.robintegg.web.engine.*;
import com.robintegg.web.layouts.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Build {

    // build the website
    public static void main(String[] args) throws IOException {

        var workingDirectory = Paths.get("");
        log.info("working directory: {}",  workingDirectory.toAbsolutePath());

        ContentModel contentModel = new ContentModel();

        String environment = System.getProperty("environment", "local");
        log.info("environment: {}", environment);

        contentModel.environment( environment );

        // could load some configuration here
        Site site = new Site();
        site.setTitle("Robin Tegg");
        Author author = new Author();
        author.setName("Robin Tegg");
        author.setEmail("robin@tegg.me.uk");
        site.setAuthor(author);
        site.setDescription("A Java developer. Based in Leeds, England. Has interest in Software Architecture, Testing, Automation, Tooling and UI design.");
        site.setEmail("robin@tegg.me.uk");
        site.setTwitterUsername("robintegg");
        site.setGithubUsername("teggr");
        site.setLinkedinUsername("robintegg");
        site.setUrl("https://robintegg.com");
        site.setBaseUrl("");

        contentModel.setSite(site);

        contentModel.addSocialLink(new SocialLink("github", "teggr", "https://github.com/teggr"));
        contentModel.addSocialLink(new SocialLink("linkedin", "robintegg", "https://www.linkedin.com/in/robintegg"));
        contentModel.addSocialLink(new SocialLink("robintegg", "twitter", "https://www.twitter.com/robintegg"));

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
        log.info("output directory: {}", outputDirectory.toAbsolutePath());

        Files.createDirectories(outputDirectory);

        // create render engine

        ContentRenderer contentRenderer = new ContentRenderer();
        contentRenderer.render(outputDirectory, layouts, contentModel);

    }

}
