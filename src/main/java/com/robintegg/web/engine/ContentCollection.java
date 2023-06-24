package com.robintegg.web.engine;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ContentCollection {
    private final String name;

    private List<ContentItem> contentItems = new ArrayList<>();

    public ContentCollection(String name) {
        this.name = name;
    }

    public void addItem(ContentItem contentItem) {
        this.contentItems.add(contentItem);
    }

}
