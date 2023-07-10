package com.robintegg.web;

import com.robintegg.web.builder.WebSiteBuilder;
import com.robintegg.web.engine.*;
import com.robintegg.web.plugins.Plugins;
import com.robintegg.web.site.Author;
import com.robintegg.web.site.Site;
import com.robintegg.web.site.SocialLink;
import com.robintegg.web.theme.DefaultThemePlugin;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Build {

    // build the website.
    public static void main(String[] args)  {

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
        site.addSocialLink(new SocialLink("github", "teggr", "https://github.com/teggr"));
        site.addSocialLink(new SocialLink("linkedin", "robintegg", "https://www.linkedin.com/in/robintegg"));
        site.addSocialLink(new SocialLink("robintegg", "twitter", "https://www.twitter.com/robintegg"));

        WebSiteBuilder webSiteBuilder = new WebSiteBuilder(
                DefaultThemePlugin.create(),
                site
        );

        webSiteBuilder.build();

    }

}
