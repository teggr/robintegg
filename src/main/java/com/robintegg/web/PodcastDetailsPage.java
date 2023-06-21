package com.robintegg.web;

import j2html.TagCreator;

import java.nio.file.Path;

public class PodcastDetailsPage {

    public static String render(Site site, Path podcast) {
        return TagCreator.html().render();
    }

}
