package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import j2html.TagCreator;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class PageLayout {

  // layout: default
  public static DomContent create(ContentModel contentModel) {

    return article()
        .withClass("post")
        .with(
            header()
                .withClass("post-header")
                .with(
                    h1()
                        .withClass("post-title")
                        .withText("{{ page.title | escape }}"),
                    div()
                        .withClass("post-content")
                        .with(
                            contentModel.getContent()
                        )
                )
        );

  }

}
