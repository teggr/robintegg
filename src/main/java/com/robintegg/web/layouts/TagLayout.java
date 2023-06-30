package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

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
                                .withId(contentModel.getPage().getTag())
                                .withText(Utils.capitalize(contentModel.getPage().getTag())),
                        ul()
                                .withClass("post-list")
                                .with(
                                        each(contentModel.getPostsWithTag(contentModel.getPage().getTag()), post -> {
                                            return each(
                                                    span()
                                                            .withClass("post-meta")
                                                            .withText(Utils.format(post.getDate())),
                                                    h3()
                                                            .with(
                                                                    a()
                                                                            .withClass("post-link")
                                                                            .withHref(Utils.relativeUrl(post.getUrl()))
                                                                            .withText(Utils.escape(post.getTitle()))
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
