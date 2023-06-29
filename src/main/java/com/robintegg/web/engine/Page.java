package com.robintegg.web.engine;

import j2html.tags.DomContent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Builder
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Page {

  @ToString.Include
  private final String path;
  @ToString.Include
  private boolean includeMenu;
  @ToString.Include
  private final Map<String, List<String>> data;
  private final Function<ContentModel, DomContent> renderFunction;

  public String getTitle() {
    return Optional
        .ofNullable(data.get("title")).map(l -> l.get(0))
        .orElse(null);
  }

  public String getUrl() {
    return path;
  }

  public String getListTitle() {
    return "Posts";
  }

  public LocalDateTime getModifiedDate() {
    return null;
  }

  public List<Author> getAuthor() {
    return null;
  }

  public String getSubtitle() {
    return null;
  }

}
