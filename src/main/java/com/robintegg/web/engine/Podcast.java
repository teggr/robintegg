package com.robintegg.web.engine;

import lombok.ToString;
import org.commonmark.node.Node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Podcast {

  private final String key;
  private final Map<String, List<String>> data;
  @ToString.Exclude
  private final Node document;

  public Podcast(String key, Map<String, List<String>> data, Node document) {
    this.key = key;
    this.data = data;
    this.document = document;
  }
  public List<TagModel> getTags() {
    return null;
  }
}
