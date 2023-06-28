package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.each;

public class TagsPage {

  public static Page create() {

    return Page.builder()
        .path("/tags/index.html")
        .includeMenu(true)
        .data(Map.of(
            "layout", List.of("default"),
            "title", List.of("Tags"),
            "permalink", List.of("/tags")
            ))
        .renderFunction(TagsPage::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {
    return each();
  }


}
