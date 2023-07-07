package com.robintegg.web.feed;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.content.post.PostIndexedContent;
import com.robintegg.web.content.staticfiles.StaticFile;
import com.robintegg.web.engine.ContentModelVisitor;
import com.robintegg.web.plugins.AggregatorPlugin;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FeedAggregatorPlugin implements AggregatorPlugin {

  public static final FeedAggregatorPlugin INSTANCE = new FeedAggregatorPlugin();

  public static FeedAggregatorPlugin create() {
    return INSTANCE;
  }

  private Feed feed = new Feed();

  @Override
  public void add(Book book) {
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
  }

  @Override
  public void add(Post post) {
    this.feed.addContent(PostIndexedContent.map(post));
  }

  @Override
  public void visit(ContentModelVisitor visitor) {

    // feed
    StaticFile staticFile = new StaticFile(
        this.feed.getPath(),
        Map.of(),
        contentModel -> this.feed.getContent(contentModel)
            .getBytes(StandardCharsets.UTF_8)
    );

    visitor.file(staticFile);
  }

  public Feed getFeed() {
    return feed;
  }

}
