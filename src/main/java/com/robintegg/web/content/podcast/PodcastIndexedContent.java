package com.robintegg.web.content.podcast;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.IndexContent;
import j2html.tags.DomContent;

import java.time.LocalDate;
import java.util.List;

public class PodcastIndexedContent {
  public static IndexContent map(Podcast podcast) {
    return new IndexContent() {

      @Override
      public LocalDate getDate() {
        return podcast.getDate();
      }

      @Override
      public String getUrl() {
        return podcast.getUrl();
      }

      @Override
      public String getTitle() {
        return podcast.getTitle() + " [New Podcast]";
      }

      @Override
      public DomContent getExcerpt(ContentModel contentModel) {
        return podcast.getExcerpt(contentModel);
      }

      @Override
      public String getCategory() {
        return null;
      }

      @Override
      public DomContent getContent(ContentModel contentModel) {
        return podcast.getContent(contentModel);
      }

      @Override
      public List<String> getTags() {
        return podcast.getTags();
      }
    };
  }
}
