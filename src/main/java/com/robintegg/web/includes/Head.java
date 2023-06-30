package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class Head {
    public static DomContent create(ContentModel contentModel) {
        return head(
                meta()
                        .withCharset("utf-8"),
                meta()
                        .attr("http-equiv", "X-UA-Compatible")
                        .withContent("IE=edge"),
                meta().
                        withName("viewport")
                        .withContent("width=device-width, initial-scale=1"),
                SEO.render(contentModel),
                link().
                        withRel("stylesheet")
                        // TODO: href="{{ "/assets/css/style.css" | relative_url }}"
                        .withHref("https://robintegg.com/assets/main.css"),
                link().
                        withRel("stylesheet")
                        // TODO: href="{{ "/assets/css/style.css" | relative_url }}"
                        .withHref("https://robintegg.com/assets/css/style.css"),
//        {%- feed_meta -%}
                iff(
                        "production".equals(contentModel.getEnvironment()),
                        GoogleAnalytics.create(contentModel)
                )
        );
    }
}
