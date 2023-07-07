package com.robintegg.web.plugins;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.engine.ContentItem;
import com.robintegg.web.engine.ContentModelVisitor;

public interface AggregatorPlugin {

  void add(Book book);

  void add(Post post);

  void visit(ContentModelVisitor visitor);

  void add(ContentItem contentItem);

}
