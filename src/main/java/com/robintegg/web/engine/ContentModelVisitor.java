package com.robintegg.web.engine;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.podcast.Podcast;
import com.robintegg.web.content.post.Post;

public interface ContentModelVisitor {

    void page(Page page);

    void tag(String tag);

    void post(Post post);

    void podcast(Podcast podcast);

    void file(RawContentItem file);

    void feed(Feed feed);

    void book(Book book);

}
