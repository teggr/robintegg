package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import j2html.TagCreator;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class PodcastLayout {

  // layout: default
  public static DomContent create(ContentModel contentModel) {

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
                        .withText("{{ page.title | escape }}"),
                    p()
                        .withClass("post-meta")
                        .with(
                            time()
                                .withClass("dt-published")
                                .withDatetime("{{ page.date | date_to_xmlschema }}")
                                .attr("itemprop", "datePublished")
                                .withText("{{ page.date | date: date_format }}"),
                            iff(
                                contentModel.getPage().getModifiedDate() != null,
                                text("~")
                            ),
                            iff(
                                contentModel.getPage().getModifiedDate() != null,
                                time()
                                    .withClass("dt-modified")
                                    .withDatetime("{{ mdate }}")
                                    .attr("itemprop", "dateModified")
                                    .withText(" {{ mdate | date: date_format }}")
                            ),
                            iff(
                                contentModel.getPage().getAuthor() != null,
                                each( contentModel.getPage().getAuthor(), author -> {
                                  return span()
                                      .attr("itemprop", "author")
                                      .attr("itemscope")
                                      .attr("itemtype", "http://schema.org/Person")
                                      .with(
                                          span()
                                              .withClass("p-author h-card")
                                              .attr("itemprop", "name")
                                              .withText("{{ author }}")
                                      );
                                } )
                            )
                        )

                ),
            div()
                .withClass("post-content e-content")
                .attr("itemprop", "articleBody" )
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
