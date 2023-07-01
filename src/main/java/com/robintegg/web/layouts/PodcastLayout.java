package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class PodcastLayout {

    public static Layout create() {
        return Layout.builder()
                .data(Map.of("layout", List.of("default")))
                .renderFunction(PodcastLayout::render)
                .build();
    }

    public static DomContent render(ContentModel contentModel) {

        return article()
                .withClass("post h-entry")
                .attr("itemscope")
                .attr("itemtype", "http://schema.org/BlogPosting")
                .with(
                        header()
                                .withClass("post-header")
                                .with(
                                        h1()
                                                .withClass("post-title p-name")
                                                .attr("itemprop", "name headline")
                                                .withText(Utils.escape(contentModel.getPage().getTitle())),
                                        p()
                                                .withClass("post-meta")
                                                .with(
                                                        time()
                                                                .withClass("dt-published")
                                                                .withDatetime(Utils.formatXmlSchema(contentModel.getPage().getDate()))
                                                                .attr("itemprop", "datePublished")
                                                                .withText(Utils.format(contentModel.getPage().getDate())),
                                                        iff(
                                                                contentModel.getPage().getModifiedDate() != null,
                                                                text("~")
                                                        ),
                                                        iff(
                                                                contentModel.getPage().getModifiedDate() != null,
                                                                time()
                                                                        .withClass("dt-modified")
                                                                        .withDatetime(Utils.formatXmlSchema(contentModel.getPage().getModifiedDate()))
                                                                        .attr("itemprop", "dateModified")
                                                                        .withText(Utils.format(contentModel.getPage().getModifiedDate()))
                                                        ),
                                                        iff(
                                                                contentModel.getPage().getAuthor() != null,
                                                                each(
                                                                        text(" • "),
                                                                        each(contentModel.getPage().getAuthor(), author -> {
                                                                            return span()
                                                                                    .attr("itemprop", "author")
                                                                                    .attr("itemscope")
                                                                                    .attr("itemtype", "http://schema.org/Person")
                                                                                    .with(
                                                                                            span()
                                                                                                    .withClass("p-author h-card")
                                                                                                    .attr("itemprop", "name")
                                                                                                    .withText(author)
                                                                                    );
                                                                        })
                                                                )

                                                        )
                                                )

                                ),
                        div()
                                .withClass("post-content e-content")
                                .attr("itemprop", "articleBody")
                                .withText(
                                        contentModel.getPage().getSubtitle()
                                ),
                        a()
                                .withClass("u-url")
                                .withHref("{{ page.podnews_url }}")
                                .withText("Find out more and subscribe to the Podcast on Podnews"),
                        a()
                                .withClass("u-url")
                                .withHref("{{ page.url | relative_url }}")
                                .attr("hidden")
                );

    }

}
