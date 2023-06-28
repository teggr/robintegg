package com.robintegg.web.engine;

import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
public class ContentModel {

  private DomContent content = TagCreator.text("");
  @Getter
  @Setter
  private Site site = new Site();
  private PageModel pageModel = new PageModel();
  private Paginator paginator = new Paginator();
  private List<Podcast> podcasts = new ArrayList<>();
  private List<Post> posts = new ArrayList<>();
  private List<RawContentItem> files = new ArrayList<>();
  private List<Page> pages = new ArrayList<>();

  public void visit(ContentModelVisitor visitor) {

    // pages
    pages.stream()
        .forEach(visitor::page);

  }

  public String getLang() {
    // TODO: lang="{{ page.lang | default: site.lang | default: "en" }}"
    return "en";
  }

  public DomContent getContent() {
    return content;
  }

  public List<SocialLink> getSocialLinks() {
    return Collections.emptyList();
  }

  public String getPageTitle() {
    return "My Title";
  }

  public List<Post> getPosts() {
    return Collections.emptyList();
  }

  public PageModel getPage() {
    return pageModel;
  }

  public Paginator getPaginator() {
    return paginator;
  }


  public List<TagModel> getTags() {
    return Collections.emptyList();
  }

  public List<Post> getTaggedContent() {
    return Collections.emptyList();
  }

  public List<CategoryModel> getCategories() {
    return Collections.emptyList();
  }

  public List<Post> getPostsInCategory(CategoryModel category) {
    return Collections.emptyList();
  }

  public List<Podcast> getPodcasts() {
    return Collections.emptyList();
  }

  public void setContent(DomContent domContent) {
    this.content = domContent;
  }

  public void addPodcast(Podcast podcast) {
    this.podcasts.add(podcast);
  }

  public void addPost(Post post) {
    this.posts.add(post);
  }

  public void addFile(RawContentItem rawContentItem) {
    this.files.add(rawContentItem);
  }

  public void addPage(Page page) {
    this.pages.add(page);
  }

  public List<Page> getPages() {
    return pages;
  }

}
