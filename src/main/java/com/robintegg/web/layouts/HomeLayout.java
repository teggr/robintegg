package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.engine.Post;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class HomeLayout {
    public static Layout create() {
        return Layout.builder()
                .data(Map.of("layout", List.of("default")))
                .renderFunction(HomeLayout::render)
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
                        contentModel.getContent(),
                        iff(
                                contentModel.getPosts().size() > 0,
                                each(
                                        iff(
                                                contentModel.getPage().getListTitle() != null,
                                                h2()
                                                        .withClass("post-list-heading")
                                                        .withText(contentModel.getPage().getListTitle())
                                        ),
                                        ul()
                                                .withClass("post-list")
                                                .with(
                                                        each(contentModel.getPosts().stream().sorted(
                                                                Comparator.comparing(Post::getDate).reversed()
                                                        ).toList(), post -> {
                                                            return li()
                                                                    .with(
                                                                            span()
                                                                                    .withClass("post-meta")
                                                                                    .withText(Utils.format( post.getDate())),
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
                                                                            ),
                                                                            iff(
                                                                                    "reading-list".equals(post.getCategory()),
                                                                                    post.getContent(contentModel)
                                                                            ),
                                                                            iff(
                                                                                    post.getTags().size() > 0,
                                                                                    ul()
                                                                                            .withClass("post-tags")
                                                                                            .with(
                                                                                                    each(post.getTags(), tag -> {
                                                                                                        return li()
                                                                                                                .with(
                                                                                                                        a()
                                                                                                                                .withHref(Utils.relativeUrl("/tags/" + tag))
                                                                                                                                .withText(Utils.escape(tag))
                                                                                                                );
                                                                                                    })
                                                                                            )
                                                                            )
                                                                    );
                                                        })
                                                )
                                )

                        )
                );

    }

}
