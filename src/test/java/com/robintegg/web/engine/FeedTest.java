package com.robintegg.web.engine;

import com.robintegg.web.feed.Feed;
import org.junit.jupiter.api.Test;

class FeedTest {

    @Test
    void getContent() {

        Feed feed = new Feed();

        ContentModel contentModel = new ContentModel();
        Site site = new Site();
        Author author = new Author();
        author.setName("Robin Tegg");
        site.setAuthor(author);
        contentModel.setSite(site);

        System.out.println(feed.getContent(contentModel));

    }
}