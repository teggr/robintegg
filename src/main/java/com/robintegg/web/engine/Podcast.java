package com.robintegg.web.engine;

import com.robintegg.web.utils.Utils;
import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.ToString;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Podcast {

  private final String key;
  private final Map<String, List<String>> data;
  @ToString.Exclude
  private final Node document;
  private final String url;

  public Podcast(String key, Map<String, List<String>> data, Node document) {
    this.key = key;
    this.data = data;
    this.document = document;
    this.url = "/podcasts/" + key + ".html";
  }
  public List<String> getTags() {
    return data.getOrDefault("tags", Collections.emptyList());
  }

  public Map<String, List<String>> getData() {
    return data;
  }

  public String getUrl() {
      return url;
  }

  public String getTitle() {
    return data.get("title").get(0);
  }

  public String getSubtitle() {
    List<String> stringList = data.get("subtitle");
    if(stringList == null) {
      return "";
    }
    return stringList.get(0);
  }
}
