package com.robintegg.web.engine;

import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.ToString;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Post {
    private final String key;
    private final Map<String, List<String>> data;
    @ToString.Exclude
    private final Node document;

    public Post(String key, Map<String, List<String>> data, Node document) {
        this.key = key;
        this.data = data;
        this.document = document;
    }

    public LocalDate getDate() {
        List<String> dates = data.get("date");
        if (dates == null) {
            return LocalDate.MAX;
        }
        return LocalDate.parse(dates.get(0));
    }

    public DomContent getExcerpt() {
        return null;
    }

    public String getCategory() {
        List<String> category = data.get("category");
        if (category == null) {
            return null;
        }
        return category.get(0);
    }

    public Collection<String> getTags() {
        return data.getOrDefault("tags", Collections.emptyList());
    }

    public String getUrl() {
        return key;
    }

    public String getTitle() {
        return this.data.get("title").get(0);
    }

    public DomContent getContent() {
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return TagCreator.rawHtml(
                renderer.render(document)
        );
    }
}
