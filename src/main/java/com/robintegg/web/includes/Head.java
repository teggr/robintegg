package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class Head {
    public static DomContent create(ContentModel contentModel) {
        return head(
                meta()
                        .withCharset("utf-8"),
                meta()
                        .attr("http-equiv","X-UA-Compatible")
                        .withContent("IE=edge"),
                meta().
                    withName("viewport")
                        .withContent("width=device-width, initial-scale=1"),
                //TODO: seo
                link().
                        withRel("stylesheet")
                        // TODO: href="{{ "/assets/css/style.css" | relative_url }}"
                        .withHref("https://robintegg.com/assets/main.css"),
            link().
                withRel("stylesheet")
                // TODO: href="{{ "/assets/css/style.css" | relative_url }}"
                .withHref("https://robintegg.com/assets/css/style.css")
//        {%- feed_meta -%}
//        {%- if jekyll.environment == 'production' and site.google_analytics -%}
//        {%- include google-analytics.html -%}
//        {%- endif -%}
//
//        {%- include custom-head.html -%}
        );
    }
}
