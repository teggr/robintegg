package com.robintegg.web.theme.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class PageLayout {

  public static Layout create() {
    return Layout.builder()
        .data(Map.of("layout", List.of("default")))
        .renderFunction(PageLayout::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {

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
