package com.robintegg.web.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.IndexContent;
import com.robintegg.web.feed.atom.*;
import com.robintegg.web.feed.atom.Author;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
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

  private List<FeedEntry> feedEntries = new ArrayList<>();

  public String getPath() {
    return "feed.xml";
  }

  public String getContent(ContentModel contentModel) {

    feedEntries.sort(Comparator.comparing(FeedEntry::getDate).reversed());

    AtomFeed feed = AtomFeed.builder()
        .generator(Generator.builder()
            .uri("https://github.com/tipsy/j2html")
            .version("v1.6.0")
            .value("j2html")
            .build())
        .link(List.of(
            Link.builder()
                .href(contentModel.getSite().resolveUrl(getPath()))
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
        .id(contentModel.getSite().resolveUrl(getPath()))
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
        .entry(feedEntries.stream()
            .map(fe -> mapToAtomEntry(contentModel, fe))
            .toList()
        )
        .build();

    try {
      return xmlMapper.writeValueAsString(feed);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }

  private Entry mapToAtomEntry(ContentModel contentModel, FeedEntry entry) {
    return
        Entry.builder()
            .title(Title.builder()
                .type("html")
                .value(entry.getTitle())
                .build())
            .link(List.of(
                    Link.builder()
                        .href(entry.getUrl())
                        .rel("alternate")
                        .type("text/html")
                        .title(entry.getTitle())
                        .build()
                )
            )
            .published(entry.getDate().atStartOfDay().atOffset(ZoneOffset.UTC))
            .updated(entry.getModifiedDate().atStartOfDay().atOffset(ZoneOffset.UTC))
            .id(feedId(entry.getUrl()))
            .content(Content.builder()
                .type("html")
                .xmlBase(entry.getUrl())
                .value(entry.getContent().apply(contentModel).render())
                .build())
            .author(List.of(
                Author.builder()
                    .name(entry.getAuthor())
                    .build()
            ))
            .category(entry.getTags().stream()
                .map(tag -> Category.builder()
                    .term(tag)
                    .build()
                )
                .toList()
            )
            .summary(Summary.builder()
                .type("html")
                .value(entry.getExcerpt().apply(contentModel).render())
                .build())
            .mediaThumbnail(MediaThumbnail.builder()
                .url(entry.getImageUrl())
                .build())
            .mediaContent(MediaContent.builder()
                .medium("image")
                .url(entry.getImageUrl())
                .build())
            .build();
  }

  private String feedId(String url) {
    return url.replaceAll("\\.html","");
  }

  public void addEntry(FeedEntry entry) {
    this.feedEntries.add(entry);
  }

  public void addContent(IndexContent post) {
    addEntry(FeedEntry.builder()
        .title(post.getTitle())
        .url(post.getUrl())
        .date(post.getDate())
        .modifiedDate(post.getDate())
        .content(post::getContent)
        .author(post.getAuthor())
        .tags(post.getTags())
        .excerpt(post::getExcerpt)
        .imageUrl(post.getImage())
        .build());
  }

}
