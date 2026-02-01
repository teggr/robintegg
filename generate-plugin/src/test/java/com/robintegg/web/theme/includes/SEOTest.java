package com.robintegg.web.theme.includes;

import com.robintegg.web.engine.Context;
import com.robintegg.web.engine.Page;
import com.robintegg.web.engine.RenderModel;
import com.robintegg.web.site.Author;
import com.robintegg.web.site.Site;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SEOTest {

    @Test
    void shouldIncludeTwitterImageMetaTagWhenImageUrlIsPresent() {
        // Given
        Page page = Page.builder()
                .path("/2026/01/30/test-post.html")
                .data(Map.of(
                        "title", List.of("Test Post"),
                        "image", List.of("/images/test-image.jpg")
                ))
                .build();

        Author author = new Author("Test Author", "test@test.com");
        Site site = new Site();
        site.setTitle("Test Site");
        site.setDescription("Test Description");
        site.setUrl("https://test.com");
        site.setBaseUrl("");  // Empty string to avoid null in URLs
        site.setTwitterUsername("testuser");
        site.setAuthor(author);

        Context context = new Context();
        context.setSite(site);

        RenderModel renderModel = new RenderModel();
        renderModel.setPage(page);
        renderModel.setContext(context);

        // When
        String html = SEO.render(renderModel).render();

        // Then
        assertTrue(html.contains("<meta name=\"twitter:image\" content=\"https://test.com/images/test-image.jpg\">"),
                "HTML should contain twitter:image meta tag with absolute image URL");
    }

    @Test
    void shouldNotIncludeTwitterImageMetaTagWhenImageUrlIsAbsent() {
        // Given
        Page page = Page.builder()
                .path("/2026/01/30/test-post.html")
                .data(Map.of(
                        "title", List.of("Test Post")
                ))
                .build();

        Author author = new Author("Test Author", "test@test.com");
        Site site = new Site();
        site.setTitle("Test Site");
        site.setDescription("Test Description");
        site.setUrl("https://test.com");
        site.setBaseUrl("");  // Empty string to avoid null in URLs
        site.setTwitterUsername("testuser");
        site.setAuthor(author);

        Context context = new Context();
        context.setSite(site);

        RenderModel renderModel = new RenderModel();
        renderModel.setPage(page);
        renderModel.setContext(context);

        // When
        String html = SEO.render(renderModel).render();

        // Then
        assertFalse(html.contains("twitter:image"),
                "HTML should not contain twitter:image meta tag when image URL is absent");
    }
}
