package com.robintegg.web.content.podcast;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class PodcastsPage {

  public static Page create() {

    return Page.builder()
        .path("/podcasts/index.html")
        .includeMenu(true)
        .data(Map.of(
            "layout", List.of("default"),
            "title", List.of("Podcasts"),
            "permalink", List.of("/podcasts"),
                "list_title", List.of("Podcasts")
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
                        contentModel.getContentOfType(Podcast.class).size() > 0,
                        each(
                            h2()
                                .withClass("post-list-heading")
                                .withText( contentModel.getPage().getListTitle()),
                            ul()
                                .withClass("post-list")
                                .with(
                                    each( contentModel.getContentOfType(Podcast.class), podcast -> {
                                      return li()
                                          .with(
                                              h3()
                                                  .with(
                                                      a()
                                                          .withClass("post-link")
                                                          .withHref(Utils.relativeUrl(podcast.getUrl()))
                                                          .withText(Utils.relativeUrl(podcast.getTitle()))
                                                  ),
                                              text(podcast.getSubtitle()),
                                              iff(
                                                  podcast.getTags().size() > 0,
                                                  ul()
                                                      .withClass("post-tags")
                                                      .with(
                                                          each(podcast.getTags(), tag -> {
                                                            return li()
                                                                .with(
                                                                    a()
                                                                        .withHref(Utils.relativeUrl("/tags/"+tag))
                                                                        .withText(tag)
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
