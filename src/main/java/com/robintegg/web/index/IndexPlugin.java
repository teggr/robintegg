package com.robintegg.web.index;

import com.robintegg.web.content.IndexContent;
import com.robintegg.web.content.IndexedContent;
import com.robintegg.web.engine.ContentItem;
import com.robintegg.web.engine.ContentModelVisitor;
import com.robintegg.web.plugins.AggregatorPlugin;
import com.robintegg.web.plugins.Plugins;
import com.robintegg.web.theme.pages.IndexPage;
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

  private final int pageSize = 10;

  @Override
  public void visit(ContentModelVisitor visitor) {


    int pages = indexedContentList.size() / pageSize;
    if (indexedContentList.size() % pageSize != 0) pages++;

    log.info("content={},pageSize={},pages={}", indexedContentList.size(), pageSize, pages);

    for (int i = 0; i < pages; i++) {
      int page = i + 1;
      if (page == 1) {
        visitor.page(IndexPage.create("index.html", page, pageSize));
      } else {
        visitor.page(IndexPage.create("page/" + page + "/index.html", page, pageSize));
      }
    }

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
