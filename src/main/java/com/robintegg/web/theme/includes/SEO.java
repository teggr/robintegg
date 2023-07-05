package com.robintegg.web.theme.includes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;
import j2html.tags.UnescapedText;

import static j2html.TagCreator.*;

public class SEO {

    private static ObjectMapper objectMapper = null;

    static {

        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());

    }

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

        ObjectNode author = objectMapper.createObjectNode();
        author.put("@type", "Person");
        author.put("name", contentModel.getSite().getAuthor().getName());

        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("@context", "https://schema.org");
        jsonObject.put("@type", "WebSite");
        jsonObject.put("author", author);
        jsonObject.put("description", contentModel.getSite().getDescription());
        jsonObject.put("headline", contentModel.getSite().getTitle());
        jsonObject.put("name", contentModel.getSite().getTitle());
        jsonObject.put("url", contentModel.getSite().getUrl());

        try {
            return objectMapper.writeValueAsString( jsonObject );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
