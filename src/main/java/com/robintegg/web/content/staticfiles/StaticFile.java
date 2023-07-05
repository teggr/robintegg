package com.robintegg.web.content.staticfiles;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class StaticFile {
    private final String key;
    private final Map<String, List<String>> data;
    @ToString.Exclude
    private final byte[] content;

    public StaticFile(String key, Map<String, List<String>> data, byte[] content) {
        this.key = key;
        this.data = data;
        this.content = content;
    }

    public String getPath() {
        return key;
    }

    public byte[] getContent() {
        return content;
    }

}
