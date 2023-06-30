package com.robintegg.web.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robintegg.web.feed.Author;
import com.robintegg.web.feed.*;

import java.time.OffsetDateTime;
import java.util.List;

public class Feed {

    private static XmlMapper xmlMapper = null;

    static {

        xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
                .build();

    }

    public String getPath() {
        return "feed.xml";
    }


    public String getContent(ContentModel contentModel) {

        AtomFeed feed = AtomFeed.builder()
                .generator(Generator.builder()
                        .uri("https://github.com/tipsy/j2html")
                        .version("v1.6.0")
                        .value("j2html")
                        .build())
                .link(List.of(
                        Link.builder()
                                .href(contentModel.getSite().resolveUrl(contentModel.getFeed().getPath()))
                                .rel("self")
                                .type("application/atom+xml")
                                .build(),
                        Link.builder()
                                .href(contentModel.getSite().getUrl())
                                .rel("alternative")
                                .type("text/html")
                                .build()
                ))
                .updated(OffsetDateTime.now())
                .id(contentModel.getSite().resolveUrl(contentModel.getFeed().getPath()))
                .title(Title.builder()
                        .type("html")
                        .value(contentModel.getSite().getTitle())
                        .build())
                .subtitle(contentModel.getSite().getDescription())
                .author(List.of(
                        Author.builder()
                                .name(contentModel.getSite().getAuthor().getName())
                                .build()
                ))
                .entry(List.of(
                        Entry.builder()
                                .title(Title.builder()
                                        .type("html")
                                        .value("j2html Static Site Generator")
                                        .build())
                                .link(List.of(
                                        Link.builder()
                                                .href("https://www.robintegg.com/2023/06/19/j2html-static-site-generator.html")
                                                .rel("alternate")
                                                .type("text/html")
                                                .title("j2html Static Site Generator")
                                                .build()
                                        )
                                )
                                .published(OffsetDateTime.now())
                                .updated(OffsetDateTime.now())
                                .id("https://www.robintegg.com/2023/06/19/j2html-static-site-generator")
                                .content(Content.builder()
                                        .type("html")
                                        .xmlBase("https://www.robintegg.com/2023/06/19/j2html-static-site-generator.html")
                                        .value("""
                                                <p><a href="https://j2html.com/">j2html</a></p>
                                                """)
                                        .build())
                                .author(List.of(
                                        Author.builder()
                                                .name("Robin Tegg")
                                                .build()
                                ))
                                .category(List.of(
                                        Category.builder().term("java").build(),
                                        Category.builder().term("j2html").build()
                                ))
                                .summary(Summary.builder()
                                        .type("html")
                                        .value("j2html is a fast")
                                        .build())
                                .mediaThumbnail(MediaThumbnail.builder()
                                        .url("https://www.robintegg.com/assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg")
                                        .build())
                                .mediaContent(MediaContent.builder()
                                        .medium("image")
                                        .url("https://www.robintegg.com/assets/images/ben-kolde-bs2Ba7t69mM-unsplash.jpg")
                                        .build())
                                .build()
                ))
                .build();

        try {
            return xmlMapper.writeValueAsString(feed);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
