package com.robintegg.web;

import lombok.ToString;
import org.commonmark.node.Node;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@ToString
public class ContentItem {
    private final String key;
    private final Map<String, List<String>> data;
    private final Node document;

    public ContentItem(String key, Map<String, List<String>> data, Node document) {
        this.key = key;
        this.data = data;
        this.document = document;
    }
}
