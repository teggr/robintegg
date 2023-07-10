package com.robintegg.web.theme.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.index.IndexPlugin;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

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

      // TODO: render the pagination

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
                            IndexPlugin.INSTANCE.getIndexedContent().size() > 0,
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
                                                        each(IndexPlugin.INSTANCE.getIndexedContent(), indexContent -> {
                                                            return li()
                                                                    .with(
                                                                            span()
                                                                                    .withClass("post-meta")
                                                                                    .withText(Utils.format( indexContent.getDate())),
                                                                            h3()
                                                                                    .with(
                                                                                            a()
                                                                                                    .withClass("post-link")
                                                                                                    .withHref(Utils.relativeUrl(indexContent.getUrl()))
                                                                                                    .withText(Utils.escape(indexContent.getTitle()))
                                                                                    ),
                                                                            iff(
                                                                                    contentModel.getSite().showExcerpts(),
                                                                                    indexContent.getExcerpt(contentModel)
                                                                            ),
                                                                            iff(
                                                                                    "reading-list".equals(indexContent.getCategory()),
                                                                                    indexContent.getContent(contentModel)
                                                                            ),
                                                                            iff(
                                                                                    indexContent.getTags().size() > 0,
                                                                                    ul()
                                                                                            .withClass("post-tags")
                                                                                            .with(
                                                                                                    each(indexContent.getTags(), tag -> {
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
