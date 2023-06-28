package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class PodcastsPage {

  public static Page create() {

    return Page.builder()
        .path("podcasts.html")
        .includeMenu(true)
        .data(Map.of(
            "layout", List.of("default"),
            "title", List.of("Podcasts"),
            "permalink", List.of("/podcasts")
            ))
        .renderFunction(PodcastsPage::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {
    return div()
        .withClass("home")
        .with(
            ul()
                .with(
                    iff(
                        contentModel.getPodcasts().size() > 0,
                        each(
                            h2()
                                .withClass("post-list-heading")
                                .withText("{{ page.list_title | default: \"Podcasts\" }}"),
                            ul()
                                .withClass("post-list")
                                .with(
                                    each( contentModel.getPodcasts(), podcast -> {
                                      return li()
                                          .with(
                                              h3()
                                                  .with(
                                                      a()
                                                          .withClass("post-link")
                                                          .withHref("{{ podcast.url | relative_url }}")
                                                          .withText("{{ podcast.title | escape }}")
                                                  ),
                                              text("{{ podcast.subtitle }}"),
                                              iff(
                                                  podcast.getTags().size() > 0,
                                                  ul()
                                                      .withClass("post-tags")
                                                      .with(
                                                          each(podcast.getTags(), tag -> {
                                                            return li()
                                                                .with(
                                                                    a()
                                                                        .withHref("{{ '/tags#' | append: tag | relative_url }}")
                                                                        .withText("{{tag}}")
                                                                );
                                                          })
                                                      )
                                              )
                                          );
                                    } )
                                )
                        )

                    )
                )
        );
  }


}
