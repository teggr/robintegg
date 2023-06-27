package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;
import static j2html.TagCreator.a;

public class CategoriesLayout {

  // layout: default

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
                contentModel.getCategories().size() > 0,
                each(contentModel.getCategories(), category -> {
                      return each(
                          iff(
                              contentModel.getPage().getListTitle() != null,
                              h2()
                                  .withClass("post-list-heading")
                                  .withText("{{ category[0] | capitalize }}")
                          ),
                          ul()
                              .withClass("post-list")
                              .with(
                                  each(contentModel.getPostsInCategory(category), post -> {
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
                      );
                    }


                )


            ),
            p()
                .withClass("rss-subscribe")
                .with(
                    a()
                        .withHref("{{ \"/feed.xml\" | relative_url }}")
                    //  <svg class="svg-icon orange"><use xlink:href="{{ 'assets/minima-social-icons.svg#rss' | relative_url }}"></use></svg><span>Subscribe</span>

                )
        );

  }

}
