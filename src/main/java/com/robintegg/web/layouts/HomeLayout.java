package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class HomeLayout {

  public static DomContent create(ContentModel contentModel) {

    return div()
        .withClass("home")
        .with(
            iff(
                contentModel.getPageTitle() != null,
                h1()
                    .withClass("page-heading")
                    .withText(contentModel.getPageTitle())
            ),
            contentModel.getContent(),
            iff(
                contentModel.getPosts().size() > 0,
                iff(
                    contentModel.getPage().getListTitle() != null,
                    h2()
                        .withClass("post-list-heading")
                        .withText(contentModel.getPage().getListTitle())
                )
            ),
            iff(
                contentModel.getPosts().size() > 0,
                ul()
                    .withClass("post-list")
                    .with(
                        each(contentModel.getPosts(), post -> {
                          return li()
                              .with(
                                  span()
                                      .withClass("post-meta")
                                      .withText(post.getDate()),
                                  h3()
                                      .with(
                                          a()
                                              .withClass("post-link")
                                              .withHref("{{ post.url | relative_url }}")
                                              .withText("{{ post.title | escape }}")
                                      ),
                                  iff(
                                      contentModel.getSite().showExcerpts(),
                                      post.getExcerpt()
                                  )
                              );
                        })
                    )
            ),
            iff(
                contentModel.getPosts().size() > 0,
                iff(
                    contentModel.getSite().paginate(),
                    div()
                        .withClass("pager")
                        .with(
                            ul()
                                .withClass("pagination")
                                .with(
                                    iffElse(
                                        contentModel.getPaginator().previousPage(),
                                        li()
                                            .with(
                                                a()
                                                    .withHref("{{ paginator.previous_page_path | relative_url }}")
                                                    .withClass("previous-page")
                                                    .withText("{{ paginator.previous_page }}")
                                            ),
                                        li().with(
                                            div()
                                                .withClass("pager-edge")
                                                .withText("•")
                                        )
                                    ),
                                    li().with(
                                        div()
                                            .withClass("current-page")
                                            .withText("{{ paginator.page }}")
                                    ),
                                    iffElse(
                                        contentModel.getPaginator().nextPage(),
                                        li()
                                            .with(
                                                a()
                                                    .withHref("{{ paginator.next_page_path | relative_url }}")
                                                    .withClass("next-page")
                                                    .withText("{{ paginator.next_page }}")
                                            ),
                                        li()
                                            .with(
                                                div()
                                                    .withClass("pager-edge")
                                                    .withText("•")
                                            )
                                    )
                                )
                        )
                )
            )
        );

  }

}
