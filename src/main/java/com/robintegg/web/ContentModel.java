package com.robintegg.web;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ContentModel {

    private List<ContentCollection> collections = new ArrayList<>();

    public void addCollection(ContentCollection collection) {
        this.collections.add(collection);
    }

}
