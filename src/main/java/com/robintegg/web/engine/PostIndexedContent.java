package com.robintegg.web.engine;

import j2html.tags.DomContent;

import java.time.LocalDate;
import java.util.List;

public class PostIndexedContent {
  public static IndexContent map(Post post) {
    return new IndexContent() {

      @Override
      public LocalDate getDate() {
        return post.getDate();
      }

      @Override
      public String getUrl() {
        return post.getUrl();
      }

      @Override
      public String getTitle() {
        return post.getTitle();
      }

      @Override
      public DomContent getExcerpt(ContentModel contentModel) {
        return post.getExcerpt(contentModel);
      }

      @Override
      public String getCategory() {
        return post.getCategory();
      }

      @Override
      public DomContent getContent(ContentModel contentModel) {
        return post.getContent(contentModel);
      }

      @Override
      public List<String> getTags() {
        return post.getTags();
      }
    };
  }
}
