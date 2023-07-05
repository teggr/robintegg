package com.robintegg.web.engine;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.book.BookIndexedContent;
import com.robintegg.web.content.book.BookLayout;
import com.robintegg.web.content.podcast.Podcast;
import com.robintegg.web.content.podcast.PodcastIndexedContent;
import com.robintegg.web.content.podcast.PodcastLayout;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.content.post.PostIndexedContent;
import com.robintegg.web.content.staticfiles.StaticFile;
import com.robintegg.web.feed.Feed;
import com.robintegg.web.feed.FeedEntry;
import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@ToString
public class ContentModel {

  private DomContent content = TagCreator.text("");
  @Getter
  @Setter
  private Site site = new Site();
  private List<Podcast> podcasts = new ArrayList<>();
  private List<Post> posts = new ArrayList<>();
  private List<StaticFile> files = new ArrayList<>();
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
        .map(post ->
            {
              log.info("post={}", post);

              return Page.builder()
                  .data(post.getData())
                  .path(post.getUrl())
                  .renderFunction(post::getContent)
                  .build();
            })
        .forEach(visitor::page);

    // podcast
    podcasts.stream()
        .map(podcast -> {
          log.info("podcast={}", podcast);

          return Page.builder()
              .data(podcast.getData())
              .path(podcast.getUrl())
              .renderFunction(PodcastLayout::render)
              .build();
        })
        .forEach(visitor::page);

    // books
    books.stream()
        .map(book -> {
          log.info("book={}", book);

          return Page.builder()
              .data(book.getData())
              .path(book.getUrl())
              .renderFunction(BookLayout::render)
              .build();
        })
            .forEach(visitor::page);

    // raw contents
    files.stream()
        .forEach(visitor::file);

    // feed
    StaticFile staticFile = new StaticFile(
        feed.getPath(),
        Map.of(),
        feed.getContent(this).getBytes(StandardCharsets.UTF_8)
    );

    visitor.file(staticFile);

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

    this.feed.addContent(PostIndexedContent.map(post));
    this.posts.add(post);
  }

  public List<Post> getPosts() {
    return this.posts;
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
