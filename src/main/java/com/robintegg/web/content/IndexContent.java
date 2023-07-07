package com.robintegg.web.content;

import com.robintegg.web.engine.ContentItem;
import com.robintegg.web.engine.ContentModel;
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

  String getAuthor();

  String getImage();

}
