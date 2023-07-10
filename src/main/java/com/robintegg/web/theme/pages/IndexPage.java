package com.robintegg.web.theme.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import com.robintegg.web.engine.Pageable;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.each;

public class IndexPage {

    public static Page create(String path, Pageable pageable) {

        return Page.builder()
                .path(path)
                .pageable(pageable)
                .data(Map.of(
                        "layout", List.of("home"),
                        "list_title", List.of("Posts")
                ))
                .renderFunction(IndexPage::render)
                .build();
    }

    public static DomContent render(ContentModel contentModel) {
        return each();
    }

}
