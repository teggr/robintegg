package com.robintegg.web.engine;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.book.BookIndexedContent;
import com.robintegg.web.content.podcast.Podcast;
import com.robintegg.web.content.podcast.PodcastIndexedContent;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.content.post.PostIndexedContent;
import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@ToString
public class ContentModel {

  private DomContent content = TagCreator.text("");
  @Getter
  @Setter
  private Site site = new Site();
  private List<Podcast> podcasts = new ArrayList<>();
  private List<Post> posts = new ArrayList<>();
  private List<RawContentItem> files = new ArrayList<>();
  private List<Page> pages = new ArrayList<>();
  private Feed feed = new Feed();
  private List<SocialLink> socialLinks = new ArrayList<>();
  private Page page;
  private String environment = "local";
  private List<Book> books = new ArrayList<>();

  public void visit(ContentModelVisitor visitor) {

    // pages
    pages.stream()
        .forEach(visitor::page);

    // tags
    getTags().stream()
        .forEach(visitor::tag);

    // posts
    posts.stream()
        .forEach(visitor::post);

    // podcast
    podcasts.stream()
        .forEach(visitor::podcast);

    // books
    books.stream()
            .forEach(visitor::book);

    // raw contents
    files.stream()
        .forEach(visitor::file);

    // feed
    visitor.feed(feed);

  }

  public String getLang() {
    // TODO: lang="{{ page.lang | default: site.lang | default: "en" }}"
    return "en";
  }

  public DomContent getContent() {
    return content;
  }

  public void addSocialLink(SocialLink socialLink) {
    this.socialLinks.add(socialLink);
  }

  public Page getPage() {
    return page;
  }

  public void setPage(Page page) {
    this.page = page;
  }

  public List<String> getTags() {
    List<String> tags = new ArrayList<>();
    this.books.stream().map(Book::getTags).flatMap(List::stream).forEach(tags::add);
    this.posts.stream().map(Post::getTags).flatMap(List::stream).forEach(tags::add);
    this.podcasts.stream().map(Podcast::getTags).flatMap(List::stream).forEach(tags::add);
    return tags.stream().distinct().sorted().toList();
  }

  public List<IndexContent> getTaggedContent(String tag) {
    List<IndexContent> taggedContentList = new ArrayList<>();
    this.books.stream().filter(p -> p.getTags().contains(tag)).map(BookIndexedContent::map).forEach(taggedContentList::add);
    this.posts.stream().filter(p -> p.getTags().contains(tag)).map(PostIndexedContent::map).forEach(taggedContentList::add);
    this.podcasts.stream().filter(p -> p.getTags().contains(tag)).map(PodcastIndexedContent::map).forEach(taggedContentList::add);
    return taggedContentList.stream().sorted(
        Comparator.comparing(IndexContent::getDate).reversed()
    ).toList();
  }

  public List<String> getCategories() {
    return this.posts.stream().map(Post::getCategory).filter(Objects::nonNull).distinct().toList();
  }

  public List<Post> getPostsInCategory(String category) {
    return this.posts.stream().filter(p -> category.equals(p.getCategory())).toList();
  }

  public void setContent(DomContent domContent) {
    this.content = domContent;
  }

  public void addPodcast(Podcast podcast) {
    this.podcasts.add(podcast);
  }

  public List<Podcast> getPodcasts() {
    return this.podcasts;
  }

  public void addPost(Post post) {
    this.feed.addEntry(FeedEntry.builder()
            .title(post.getTitle())
            .url(post.getUrl())
            .date(post.getDate())
            .modifiedDate(post.getDate())
            .content(post::getContent)
            .author(post.getAuthor())
            .tags(post.getTags())
            .excerpt(post::getExcerpt)
            .imageUrl(post.getImage())
        .build());
    this.posts.add(post);
  }

  public List<Post> getPosts() {
    return this.posts;
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

  public Feed getFeed() {
    return feed;
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

  public void addBook(Book book) {
    this.feed.addEntry(FeedEntry.builder()
        .title(book.getTitle())
        .url(book.getUrl())
        .date(book.getAddedDate())
        .modifiedDate(book.getAddedDate())
        .content(book::getContent)
        .author(book.getAuthor())
        .tags(book.getTags())
        .excerpt(book::getExcerpt)
        .imageUrl(book.getImageUrl())
        .build());
    this.books.add(book);
  }

  public List<Book> getBooks() {
    return books;
  }

  public List<IndexContent> getIndexedContent() {
    List<IndexContent> indexedContentList = new ArrayList<>();
    this.books.stream().map(BookIndexedContent::map).forEach(indexedContentList::add);
    this.posts.stream().map(PostIndexedContent::map).forEach(indexedContentList::add);
    this.podcasts.stream().map(PodcastIndexedContent::map).forEach(indexedContentList::add);
    return indexedContentList.stream().sorted(
        Comparator.comparing(IndexContent::getDate).reversed()
    ).toList();
  }

}
