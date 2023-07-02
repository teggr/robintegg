package com.robintegg.web.engine;

import com.robintegg.web.utils.Utils;
import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.ToString;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.LocalDate;
import java.util.*;

public class Book {
  private final String key;
  @ToString.Exclude
  private final BookEntry bookEntry;
  private final String url;

  public Book(String key, BookEntry bookEntry) {
    this.key = key;
    this.bookEntry = bookEntry;
    this.url = "/books/" + key + ".html";
  }

  public String getUrl() {
    return url;
  }

  public String getTitle() {
    return bookEntry.getBookDetails().getTitle();
  }

  public String getSubtitle() {
    return bookEntry.getBookDetails().getSubtitle();
  }

  public List<String> getTags() {
    return bookEntry.getBookDetails().getTags();
  }

  public String getImageUrl() {
    return bookEntry.getBookDetails().getImageUrl();
  }

  public Map<String, List<String>> getData() {
    return Map.of(
        "tags", getTags(),
        "title", List.of(getTitle()),
        "subtitle", List.of(getSubtitle()),
        "image", List.of(getImageUrl()),
        "authors", List.of(bookEntry.getBookDetails().getAuthor()),
        "date", List.of(bookEntry.getBookshelf().getAdded().toString())
    );
  }
}
