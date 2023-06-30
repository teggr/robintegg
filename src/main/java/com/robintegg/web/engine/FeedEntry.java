package com.robintegg.web.engine;

import j2html.tags.DomContent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
public class FeedEntry {

  private String title;
  private String url;
  private LocalDate date;
  private LocalDate modifiedDate;
  private Function<ContentModel, DomContent> content;
  private String author;
  private List<String> tags;
  private Function<ContentModel, DomContent> excerpt;
  private String imageUrl;

}
