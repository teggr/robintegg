package com.robintegg.web.theme.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class IndexPage {

  public static Page create(String path, int page, int pageSize, boolean next, boolean previous) {

    return Page.builder()
        .path(path)
        .data(Map.of(
            "layout", List.of("home"),
            "list_title", List.of("Posts"),
            "page", List.of(String.valueOf(page)),
            "pageSize", List.of(String.valueOf(pageSize)),
            "next", List.of(String.valueOf(next)),
            "previous", List.of(String.valueOf(previous))
        ))
        .renderFunction(IndexPage::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {
    return each();
  }

}
