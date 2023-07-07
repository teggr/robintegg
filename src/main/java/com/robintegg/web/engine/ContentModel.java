package com.robintegg.web.engine;

import com.robintegg.web.content.CategorisedContent;
import com.robintegg.web.content.IndexContent;
import com.robintegg.web.content.IndexedContent;
import com.robintegg.web.content.TaggedContent;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@ToString
public class ContentModel {

  private DomContent content = TagCreator.text("");
  @Getter
  @Setter
  private Site site = new Site();
  private List<Page> pages = new ArrayList<>();
  private Page page;
  private String environment = "local";
  private List<StaticFile> files = new ArrayList<>();
  private List<ContentItem> items = new ArrayList<>();

  public void add(ContentItem contentItem) {
    this.items.add(contentItem);
    Plugins.aggregatorPlugins.stream()
        .forEach(aggregatorPlugin -> aggregatorPlugin.add(contentItem));
  }

  public <T> List<T> getContentOfType(Class<T> clazz) {
    return this.items.stream()
        .filter(i -> clazz.isAssignableFrom(i.getClass()))
        .map(i -> (T) i)
        .toList();
  }

  public void visit(ContentModelVisitor visitor) {

    // pages
    pages.stream()
        .forEach(visitor::page);

    // tags
    getTags().stream()
        .forEach(visitor::tag);

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
    files.stream()
        .forEach(visitor::file);

    Plugins.aggregatorPlugins.stream()
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

  public List<String> getTags() {
    return getContentOfType(TaggedContent.class).stream()
        .map(TaggedContent::getTags)
        .flatMap(List::stream)
        .distinct()
        .sorted()
        .toList();
  }

  public List<IndexContent> getTaggedContent(String tag) {
    return getContentOfType(TaggedContent.class).stream()
        .filter(tc -> tc.getTags().contains(tag))
        .filter(i -> i instanceof IndexedContent)
        .map(i -> ((IndexedContent) i).getIndexContent())
        .sorted(Comparator.comparing(IndexContent::getDate).reversed())
        .toList();
  }

  public List<String> getCategories() {
    return getContentOfType(CategorisedContent.class).stream()
        .map(CategorisedContent::getCategory)
        .filter(Objects::nonNull)
        .distinct()
        .sorted()
        .toList();
  }

  public List<IndexContent> getPostsInCategory(String category) {
    return getContentOfType(CategorisedContent.class).stream()
        .filter(i -> Objects.equals(((CategorisedContent) i).getCategory(), category))
        .filter(i -> i instanceof IndexedContent)
        .map(i -> ((IndexedContent) i).getIndexContent())
        .sorted(Comparator.comparing(IndexContent::getDate).reversed())
        .toList();
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

  public List<IndexContent> getIndexedContent() {
    return getContentOfType(IndexedContent.class).stream()
        .map(IndexedContent::getIndexContent)
        .sorted(Comparator.comparing(IndexContent::getDate).reversed())
        .toList();
  }

}
