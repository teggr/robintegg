package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class CategoriesLayout {

    // layout: default
    public static Layout create() {
        return Layout.builder()
                .data(Map.of("layout", List.of("default")))
                .renderFunction(CategoriesLayout::render)
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
                                contentModel.getCategories().size() > 0,
                                each(contentModel.getCategories(), category -> {
                                            return each(
                                                    h2()
                                                            .withClass("post-list-heading")
                                                            .withText(Utils.capitalize(category)),
                                                    ul()
                                                            .withClass("post-list")
                                                            .with(
                                                                    each(contentModel.getPostsInCategory(category), post -> {
                                                                        return li()
                                                                                .with(
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
                                )
                        ),
                        p()
                                .withClass("feed-subscribe")
                                .with(
                                        a()
                                                .withHref(Utils.relativeUrl("/feed.xml"))
                                                .with(
                                                        rawHtml(
                                                                String.format("<svg class=\"svg-icon orange\"><use xlink:href=\"%s\"></use></svg><span>Subscribe</span>", Utils.relativeUrl("/assets/minima-social-icons.svg#rss"))
                                                        )
                                                )
                                )
                );

    }

}
