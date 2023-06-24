package com.robintegg.web.engine;

import lombok.ToString;

import java.util.function.Supplier;

public class PageContentItem implements ContentItem {
    private final String key;
    @ToString.Exclude
    private final Supplier<Page> pageCreateFunction;

    public PageContentItem(String key, Supplier<Page> pageCreateFunction) {
        this.key = key;
        this.pageCreateFunction = pageCreateFunction;
    }
}
