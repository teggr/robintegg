package com.robintegg.web.engine;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class TextContentItem {
    private final String key;
    private final Map<String, List<String>> data;
    @ToString.Exclude
    private final String content;

    public TextContentItem(String key, Map<String, List<String>> data, String content) {
        this.key = key;
        this.data = data;
        this.content = content;
    }
}
