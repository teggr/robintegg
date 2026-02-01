package com.robintegg.web.engine;

import com.robintegg.web.content.IndexContent;
import com.robintegg.web.feed.Feed;
import com.robintegg.web.site.Author;
import com.robintegg.web.site.Site;
import j2html.TagCreator;
import j2html.tags.DomContent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedTest {

    @Test
    void feedEntriesShouldUseAbsoluteUrls() {

        Feed feed = new Feed();

        RenderModel renderModel = new RenderModel();
        Context context = new Context();
        Site site = new Site();
        Author author = new Author();
        author.setName("Robin Tegg");
        site.setAuthor(author);
        site.setTitle("my title");
        site.setDescription("description");
        site.setBaseUrl("");
        site.setUrl("https://robintegg.com");
        context.setSite(site);
        renderModel.setContext(context);

        feed.addContent(new IndexContent() {
            @Override
            public LocalDate getDate() {
                return LocalDate.now();
            }

            @Override
            public String getUrl() {
                return "/2026/01/01/test-post.html";
            }

            @Override
            public String getTitle() {
                return "Test Post Title";
            }

            @Override
            public DomContent getExcerpt(RenderModel renderModel) {
                return TagCreator.rawHtml("<p>Test excerpt</p>");
            }

            @Override
            public String getCategory() {
                return "test-category";
            }

            @Override
            public DomContent getContent(RenderModel renderModel) {
                return TagCreator.rawHtml("<p>Test content</p>");
            }

            @Override
            public List<String> getTags() {
                return List.of("test","java");
            }

            @Override
            public String getAuthor() {
                return "Test Author";
            }

            @Override
            public String getImage() {
                return "/images/test.png";
            }
        });

        String feedContent = feed.getContent(renderModel);
        
        // Verify absolute URLs in feed - critical for feed readers
        assertTrue(feedContent.contains("xml:base=\"https://robintegg.com/2026/01/01/test-post.html\""), 
            "xml:base should be an absolute URL for proper content resolution in feed readers");
        assertTrue(feedContent.contains("href=\"https://robintegg.com/2026/01/01/test-post.html\""), 
            "entry link should be an absolute URL");
        assertTrue(feedContent.contains("url=\"https://robintegg.com/images/test.png\""), 
            "media:thumbnail and media:content URLs should be absolute");

    }
}