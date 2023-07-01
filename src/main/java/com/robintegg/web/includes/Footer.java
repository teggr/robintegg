package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.utils.Utils;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class Footer {
    public static DomContent create(ContentModel contentModel) {
        return footer()
                .withClass("site-footer h-card")
                .with(
                        data()
                                .withClass("u-url")
                                .attr("href", Utils.relativeUrl("/")),
                        div()
                                .withClass("wrapper")
                                .with(
                                        h2()
                                                .withClass("footer-heading")
                                                .withText(Utils.escape(contentModel.getSite().getTitle())),
                                        div()
                                                .withClass("footer-col-wrapper")
                                                .with(
                                                        div()
                                                                .withClass("footer-col footer-col-1")
                                                                .with(
                                                                        ul()
                                                                                .withClass("contact-list")
                                                                                .with(
                                                                                        li()
                                                                                                .withClass("p-name")
                                                                                                        .with(
                                                                                                                iffElse(
                                                                                                                        contentModel.getSite().getAuthor().getName() != null,
                                                                                                                        text(Utils.escape(contentModel.getSite().getAuthor().getName())),
                                                                                                                        text(Utils.escape(contentModel.getSite().getTitle()))
                                                                                                                )
                                                                                                        ),
                                                                                        iff(
                                                                                                contentModel.getSite().getEmail() != null,
                                                                                                li().with(
                                                                                                        a()
                                                                                                                .withClass("u-email")
                                                                                                                .withHref("mailto:" + contentModel.getSite().getEmail())
                                                                                                                .withText(contentModel.getSite().getEmail()))
                                                                                        )
                                                                                )
                                                                ),
                                                        div()
                                                                .withClass("footer-col footer-col-2")
                                                                .with(
                                                                        div()
                                                                                .withClass("social-links")
                                                                                .with(
                                                                                        Social.create(contentModel))
                                                                ),
                                                        div()
                                                                .withClass("footer-col footer-col-3")
                                                                .with(
                                                                        p()
                                                                                .withText(Utils.escape(contentModel.getSite().getDescription()))
                                                                )

                                                )
                                )
                );
    }
}
