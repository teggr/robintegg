package com.robintegg.web.theme.includes;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.utils.Utils;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class Social {
    public static DomContent create(ContentModel contentModel) {
        return ul()
            .withClass("social-media-list")
            .with(
                iff(
                        contentModel.getSite().getGithubUsername() != null,
                        li()
                                .with(
                                        a()
                                                .withHref(Utils.escape("https://github.com/" + contentModel.getSite().getGithubUsername()))
                                                .with(
                                                      rawHtml("<svg class=\"svg-icon\"><use xlink:href=\"/images/minima-social-icons.svg#github\"></use></svg>"),
                                                      span()
                                                              .withClass("username")
                                                              .withText(Utils.escape(contentModel.getSite().getGithubUsername()))
                                                )
                                )
                ),
                    iff(
                            contentModel.getSite().getLinkedinUsername() != null,
                            li()
                                    .with(
                                            a()
                                                    .withHref(Utils.escape("https://www.linkedin.com/in/" + contentModel.getSite().getLinkedinUsername()))
                                                    .with(
                                                            rawHtml("<svg class=\"svg-icon\"><use xlink:href=\"/images/minima-social-icons.svg#linkedin\"></use></svg>"),
                                                            span()
                                                                    .withClass("username")
                                                                    .withText(Utils.escape(contentModel.getSite().getLinkedinUsername()))
                                                    )
                                    )
                    ),
                    iff(
                            contentModel.getSite().getTwitterUsername() != null,
                            li()
                                    .with(
                                            a()
                                                    .withHref(Utils.escape("https://www.twitter.com/" + contentModel.getSite().getTwitterUsername()))
                                                    .with(
                                                            rawHtml("<svg class=\"svg-icon\"><use xlink:href=\"/images/minima-social-icons.svg#twitter\"></use></svg>"),
                                                            span()
                                                                    .withClass("username")
                                                                    .withText(Utils.escape(contentModel.getSite().getTwitterUsername()))
                                                    )
                                    )
                    )
            );
    }
}
