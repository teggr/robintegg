package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

public class TagLayout {

  public static Layout create() {
    return Layout.builder()
        .data(Map.of("layout", List.of("default")))
        .renderFunction(TagLayout::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {

    return div()
        .withClass("home")
        .with(
            h2()
                .withClass("post-list-heading")
                .withId("{{ page.tag }}")
                .withText("{{ page.tag | capitalize }}"),
            ul()
                .withClass("post-list")
                .with(
                    each(contentModel.getTaggedContent(), post -> {
                      return each(
                          span()
                          .withClass("post-meta")
                          .withText("{{ post.date | date: date_format }}"),
                          h3()
                              .with(
                                  a()
                                      .withClass("post-link")
                                      .withHref("{{ post.url | relative_url }}")
                                      .withText("{{ post.title | escape }}")
                              ),
                          iff(
                              contentModel.getSite().showExcerpts(),
                              post.getExcerpt()
                          )
                      );
                    })
                )
        );

  }

}
