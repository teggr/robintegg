package com.robintegg.web.theme.includes;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.feed.FeedPlugin;
import com.robintegg.web.utils.Utils;
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
            .withHref(Utils.relativeUrl("/assets/main.css")),
        link().
            withRel("stylesheet")
            .withHref(Utils.relativeUrl("/assets/css/style.css")),
        link()
            .withType("application/atom+xml")
            .withRel("alternate")
            .withHref(FeedPlugin.INSTANCE.getFeed().getPath()) // TODO: need to make available via the theme and plugins
            .withTitle(contentModel.getSite().getTitle())
    );
  }
}
