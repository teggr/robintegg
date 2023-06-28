package com.robintegg.web.engine;

import j2html.tags.DomContent;
import lombok.ToString;
import org.commonmark.node.Node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Post {
  private final String key;
  private final Map<String, List<String>> data;
  @ToString.Exclude
  private final Node document;

  public Post(String key, Map<String, List<String>> data, Node document) {
    this.key = key;
    this.data = data;
    this.document = document;
  }
  public String getDate() {
    return null;
  }

  public DomContent getExcerpt() {
  return null;
  }

  public String getCategory() {
    return null;
  }

  public Collection<TagModel> getTags() {
    return null;
  }
}
