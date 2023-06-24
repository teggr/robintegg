package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

public class Index {

    public static Page create() {
        return Page.builder()
                .data(Map.of("layout", List.of("home")))
                .renderFunction(cm -> new DomContent[]{
                })
                .build();

    }

}
