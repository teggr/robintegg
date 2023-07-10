package com.robintegg.web.engine;

import com.robintegg.web.content.staticfiles.StaticFile;
import com.robintegg.web.plugins.Plugins;
import com.robintegg.web.site.Site;
import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ToString
public class ContentModel {

  // TODO: these values are more in scope of render time
  private DomContent content = TagCreator.text("");
  @Getter
  @Setter
  private Site site = new Site();
  private Page page;
  private String environment = "local";

  // TODO: these should be streamed to the rendering engine
  private final List<Page> pages = new ArrayList<>();
  private final List<StaticFile> files = new ArrayList<>();
  private final List<ContentItem> items = new ArrayList<>();

  public void add(ContentItem contentItem) {
    // TODO: this would be where the item would be passed
    // onto the next stage rather than stored. only the aggregator
    // plugins would be interested in the stored content
    this.items.add(contentItem);
    Plugins.aggregatorPlugins
        .forEach(aggregatorPlugin -> aggregatorPlugin.add(contentItem));
  }

  // TODO: is this entry point or the plugins? plugins would need to
  // have all the information before creating any container pages
  // currently we wait until the processing occurs
  public <T> List<T> getContentOfType(Class<T> clazz) {
    return this.items.stream()
        .filter(i -> clazz.isAssignableFrom(i.getClass()))
        .map(i -> (T) i)
        .toList();
  }

  public void visit(ContentModelVisitor visitor) {

    // pages
    pages
        .forEach(visitor::page);

    // content model
    items.stream()
        .map(item ->
        {
          log.info("item={}", item);

          return Page.builder()
              .data(item.getData())
              .path(item.getUrl())
              .renderFunction(item::getContent)
              .build();
        })
        .forEach(visitor::page);

    // raw contents
    files
        .forEach(visitor::file);

    Plugins.aggregatorPlugins
        .forEach(aggregatorPlugin -> aggregatorPlugin.visit(visitor));

  }

  public String getLang() {
    // TODO: lang="{{ page.lang | default: site.lang | default: "en" }}"
    return "en";
  }

  public DomContent getContent() {
    return content;
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public void setContent(DomContent domContent) {
    this.content = domContent;
  }

  public void addFile(StaticFile staticFile) {
    this.files.add(staticFile);
  }

  public void addPage(Page page) {
    this.pages.add(page);
  }

  public List<Page> getPages() {
    return pages;
  }

  public void reset() {
    this.content = TagCreator.text("");
    this.page = null;
  }

  public void environment(String environment) {
    this.environment = environment;
  }

  public String getEnvironment() {
    return environment;
  }

}
