package com.robintegg.web.engine;

import j2html.tags.DomContent;

import java.time.LocalDate;
import java.util.List;

public class BookIndexedContent {
  public static IndexContent map(Book book) {
    return new IndexContent() {
      @Override
      public LocalDate getDate() {
        return book.getAddedDate();
      }

      @Override
      public String getUrl() {
        return book.getUrl();
      }

      @Override
      public String getTitle() {
        return "New Book: " + book.getTitle();
      }

      @Override
      public DomContent getExcerpt(ContentModel contentModel) {
        return book.getExcerpt(contentModel);
      }

      @Override
      public String getCategory() {
        return null;
      }

      @Override
      public DomContent getContent(ContentModel contentModel) {
        return book.getContent(contentModel);
      }

      @Override
      public List<String> getTags() {
        return book.getTags();
      }
    };
  }
}
