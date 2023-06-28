package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class PostLayout {

  public static Layout create() {
    return Layout.builder()
        .data(Map.of("layout", List.of("default")))
        .renderFunction(PostLayout::render)
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
                .with(
                    contentModel.getContent()
                ),
            a()
                .withClass("u-url")
                .withHref("{{ page.url | relative_url }}")
                .attr("hidden")
        );

  }

}
