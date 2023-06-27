package com.robintegg.web.engine;

import j2html.tags.DomContent;
import lombok.ToString;

import javax.print.attribute.Attribute;
import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Collection;
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

    public List<PageModel> getPagePaths() {
        return null;
    }

    public List<String> getTitles() {
        return null;
    }

    public List<SocialLink> getSocialLinks() {
        return null;
    }

    public String getPageTitle() {
        return null;
    }

    public List<PostModel> getPosts() {
        return null;
    }

    public PageModel getPage() {
        return null;
    }

    public Paginator getPaginator() {
        return null;
    }


    public List<TagModel> getTags() {
        return null;
    }

    public List<PostModel> getTaggedContent() {
        return null;
    }

    public List< CategoryModel> getCategories() {
        return null;
    }

    public List<PostModel> getPostsInCategory(CategoryModel category) {
        return null;
    }
}
