package com.robintegg.web.index;

import com.robintegg.web.content.IndexContent;
import com.robintegg.web.content.IndexedContent;
import com.robintegg.web.engine.ContentItem;
import com.robintegg.web.engine.ContentModelVisitor;
import com.robintegg.web.plugins.AggregatorPlugin;
import com.robintegg.web.plugins.Plugins;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class IndexPlugin implements AggregatorPlugin {

  public static final IndexPlugin INSTANCE = new IndexPlugin();

  public static final IndexPlugin create() {
    return INSTANCE;
  }

  private List<IndexedContent> indexedContentList = new ArrayList<>();

  @Override
  public void visit(ContentModelVisitor visitor) {

    // TODO: should probably be the home page registered here

  }

  @Override
  public void add(ContentItem contentItem) {

    if (contentItem instanceof IndexedContent ic) {

        indexedContentList.add(ic);

    }

  }

  public List<IndexContent> getIndexedContent() {
    return indexedContentList.stream()
        .map(IndexedContent::getIndexContent)
        .sorted(Comparator.comparing(IndexContent::getDate).reversed())
        .toList();
  }

  public void registerPlugins() {
    Plugins.aggregatorPlugins.add(this);
  }

}
