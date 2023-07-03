package com.robintegg.web.engine;

import j2html.tags.DomContent;

import java.time.LocalDate;
import java.util.List;

public interface IndexContent {
  
  LocalDate getDate();

  String getUrl();

  String getTitle();

  DomContent getExcerpt(ContentModel contentModel);

  String getCategory();

  DomContent getContent(ContentModel contentModel);

  List<String> getTags();

}
