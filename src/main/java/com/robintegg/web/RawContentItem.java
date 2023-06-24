package com.robintegg.web;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class RawContentItem implements ContentItem {
    private final String key;
    private final Map<String, List<String>> data;
    @ToString.Exclude
    private final byte[] content;

    public RawContentItem(String key, Map<String, List<String>> data, byte[] content) {
        this.key = key;
        this.data = data;
        this.content = content;
    }
}
