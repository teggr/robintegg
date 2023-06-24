package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class Footer {
    public static DomContent create(ContentModel contentModel) {
        return footer()
                .withClass("site-footer h-card")
                .with(
                        data()
                                .withClass("u-url")
                                // TODO: href="{{ "/" | relative_url }}"
                                .attr("href", "/"),
                        div()
                                .withClass("wrapper")
                                .with(
                                        div()
                                                .withClass("footer-col-wrapper")
                                                .with(
                                                        div()
                                                                .withClass("footer-col")
                                                                .with(
                                                                        p()
                                                                                .withClass("feed-subscribe")
                                                                                .with(
                                                                                        a()
                                                                                                // TODO: "{{ site.feed.path | default: 'feed.xml' | absolute_url }}"
                                                                                                .withHref("feed.xml")
                                                                                                .with(
//                                                                                                <svg class="svg-icon orange">
//              <use xlink:href="{{ 'assets/minima-social-icons.svg#rss' | relative_url }}"></use>
//            </svg><span>Subscribe</span>
                                                                                                )
                                                                                ),
                                                                        iff(
                                                                                contentModel.getSite().getAuthor() != null,
                                                                                ul()
                                                                                        .withClass("contact-list")
                                                                                        .with(
                                                                                                iff(
                                                                                                        contentModel.getSite().getAuthor().getName() != null,
                                                                                                        li()
                                                                                                                .withClass("p-name")
                                                                                                                .withText("{{ site.author.name | escape }}")
                                                                                                ),
                                                                                                iff(
                                                                                                        contentModel.getSite().getAuthor().getEmail() != null,
                                                                                                        li().with(
                                                                                                                a()
                                                                                                                        .withClass("u-email")
                                                                                                                        .withHref("mailto:{{ site.author.email }}")
                                                                                                                        .withText("{{ site.author.email }}"))
                                                                                                )
                                                                                        )
                                                                        )
                                                                ),
                                                        div()
                                                                .withClass("footer-col")
                                                                .with(
                                                                        p()
                                                                                .withText("{{ site.description | escape }}")
                                                                )

                                                ),
                                        div()
                                                .withClass("social-links")
                                                .with(
                                                        Social.create(contentModel)                                                )
                                )
                );
    }
}
