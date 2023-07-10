package com.robintegg.web.theme.includes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robintegg.web.engine.ContentModel;
import j2html.tags.DomContent;
import j2html.tags.UnescapedText;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static j2html.TagCreator.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SEO {

    public static final String PROPERTY = "property";
    public static final String DESCRIPTION = "description";
    private static final ObjectMapper objectMapper;

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
                        meta().attr(PROPERTY, "og:title").withContent(contentModel.getPage().getTitle()),
                        meta().attr(PROPERTY, "og:title").withContent(contentModel.getSite().getTitle())
                ),
                meta().withName("author").withContent(contentModel.getSite().getAuthor().getName()),
                meta().attr(PROPERTY, "og:locale").withContent(contentModel.getLang()),
                iffElse(
                        contentModel.getPage().getExcerpt() != null,
                        meta().withName(DESCRIPTION).withContent(contentModel.getPage().getExcerpt()),
                        meta().withName(DESCRIPTION).withContent(contentModel.getSite().getDescription())
                ),
                iffElse(
                        contentModel.getPage().getExcerpt() != null,
                        meta().attr(PROPERTY, "og:description").withContent(contentModel.getPage().getExcerpt()),
                        meta().attr(PROPERTY, "og:description").withContent(contentModel.getSite().getDescription())
                ),
                link().withRel("canonical").withHref(contentModel.getSite().resolveUrl(contentModel.getPage().getUrl())),
                meta().attr(PROPERTY, "og:url").withContent(contentModel.getSite().resolveUrl(contentModel.getPage().getUrl())),
                meta().attr(PROPERTY, "og:site_name").withContent(contentModel.getSite().getTitle()),
                iff(
                        contentModel.getPage().getImageUrl() != null,
                        meta().attr(PROPERTY, "og:image").withContent(contentModel.getPage().getImageUrl())
                ),
                meta().attr(PROPERTY, "og:type").withContent("website"),
                meta().withName("twitter:card").withContent("summary"),
                meta().attr(PROPERTY, "twitter:title").withContent(contentModel.getSite().getAuthor().getName()),
                meta().withName("twitter:site").withContent("@" + contentModel.getSite().getTwitterUsername()),
                meta().withName("twitter:creator").withContent("@" + contentModel.getSite().getTwitterUsername()),
                script()
                        .withType("application/ld+json")
                        .with(
                                new UnescapedText(ldJson(contentModel))
                        )
        );
    }

    @SneakyThrows
    private static String ldJson(ContentModel contentModel) {

        ObjectNode author = objectMapper.createObjectNode();
        author.put("@type", "Person");
        author.put("name", contentModel.getSite().getAuthor().getName());

        ObjectNode jsonObject = objectMapper.createObjectNode();
        jsonObject.put("@context", "https://schema.org");
        jsonObject.put("@type", "WebSite");
        jsonObject.set("author", author);
        jsonObject.put(DESCRIPTION, contentModel.getSite().getDescription());
        jsonObject.put("headline", contentModel.getSite().getTitle());
        jsonObject.put("name", contentModel.getSite().getTitle());
        jsonObject.put("url", contentModel.getSite().getUrl());

        return objectMapper.writeValueAsString(jsonObject);

    }
}
