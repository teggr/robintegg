package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

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
        return div()
                .withClass("home")
                .with(
                        iff(
                                contentModel.getPage().getTitle() != null,
                                h1()
                                        .withClass("page-heading")
                                        .withText(contentModel.getPage().getTitle())
                        ),
                        ul()
                                .withClass("post-list")
                                .with(
                                        each(contentModel.getTags().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList(), tag ->{
                                            return h3()
                                                    .with(
                                                            a()
                                                                    .withClass("post-link")
                                                                    .withHref(Utils.relativeUrl("/tags/" + tag))
                                                                    .withText(Utils.capitalize(tag))
                                                    );
                                        })
                                )
                );
    }


}
