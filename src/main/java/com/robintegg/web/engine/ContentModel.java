package com.robintegg.web.engine;

import j2html.tags.DomContent;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ToString
public class ContentModel {

    private List<ContentCollection> collections = new ArrayList<>();

    public void addCollection(ContentCollection collection) {
        this.collections.add(collection);
    }

    public String getLang() {
        // TODO: lang="{{ page.lang | default: site.lang | default: "en" }}"
        return "en";
    }

    public DomContent getContent() {
        return null;
    }

    public Site getSite() {
        return null;
    }
}
