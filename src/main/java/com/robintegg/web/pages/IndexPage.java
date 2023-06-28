package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class IndexPage {

  public static Page create() {

    return Page.builder()
        .path("index.html")
        .data(Map.of("layout", List.of("home")))
        .renderFunction(IndexPage::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {
        return each();
    }

}
