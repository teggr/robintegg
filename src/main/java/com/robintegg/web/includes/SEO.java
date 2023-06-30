package com.robintegg.web.includes;

import com.google.gson.JsonObject;
import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;
import j2html.tags.UnescapedText;

import static j2html.TagCreator.*;

public class SEO {
    public static DomContent render(ContentModel contentModel) {
        return each(
                iffElse(
                        contentModel.getPage().getTitle() != null,
                        title(contentModel.getPage().getTitle() + " | " + contentModel.getSite().getTitle()),
                        title(contentModel.getSite().getTitle() + " | " + contentModel.getSite().getDescription())
                ),
                meta().withName("generator").withContent("j2html"),
                iffElse(
                        contentModel.getPage().getTitle() != null,
                        meta().attr("property", "og:title").withContent(contentModel.getPage().getTitle()),
                        meta().attr("property", "og:title").withContent(contentModel.getSite().getTitle())
                ),
                meta().withName("author").withContent(contentModel.getSite().getAuthor().getName()),
                meta().attr("property", "og:locale").withContent(contentModel.getLang()),
                iffElse(
                        contentModel.getPage().getExcerpt() != null,
                        meta().withName("description").withContent(contentModel.getPage().getExcerpt()),
                        meta().withName("description").withContent(contentModel.getSite().getDescription())
                ),
                iffElse(
                        contentModel.getPage().getExcerpt() != null,
                        meta().attr("property", "og:description").withContent(contentModel.getPage().getExcerpt()),
                        meta().attr("property", "og:description").withContent(contentModel.getSite().getDescription())
                ),
                link().withRel("canonical").withHref(contentModel.getSite().resolveUrl(contentModel.getPage().getUrl())),
                meta().attr("property", "og:url").withContent(contentModel.getSite().resolveUrl(contentModel.getPage().getUrl())),
                meta().attr("property", "og:site_name").withContent(contentModel.getSite().getTitle()),
                iff(
                        contentModel.getPage().getImageUrl() != null,
                        meta().attr("property", "og:image").withContent(contentModel.getPage().getImageUrl())
                ),
                meta().attr("property", "og:type").withContent("website"),
                meta().withName("twitter:card").withContent("summary"),
                meta().attr("property", "twitter:title").withContent(contentModel.getSite().getAuthor().getName()),
                meta().withName("twitter:site").withContent("@" + contentModel.getSite().getTwitterUsername()),
                meta().withName("twitter:creator").withContent("@" + contentModel.getSite().getTwitterUsername()),
                script()
                        .withType("application/ld+json")
                        .with(
                                new UnescapedText(ldJson(contentModel))
                        )
        );
    }

    private static String ldJson(ContentModel contentModel) {

        JsonObject author = new JsonObject();
        author.addProperty("@type", "Person");
        author.addProperty("name", contentModel.getSite().getAuthor().getName());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("@context", "https://schema.org");
        jsonObject.addProperty("@type", "WebSite");
        jsonObject.add("author", author);
        jsonObject.addProperty("description", contentModel.getSite().getDescription());
        jsonObject.addProperty("headline", contentModel.getSite().getTitle());
        jsonObject.addProperty("name", contentModel.getSite().getTitle());
        jsonObject.addProperty("url", contentModel.getSite().getUrl());

        return jsonObject.toString();

    }
}
