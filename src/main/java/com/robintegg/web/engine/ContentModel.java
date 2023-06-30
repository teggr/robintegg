package com.robintegg.web.engine;

import j2html.TagCreator;
import j2html.tags.DomContent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
public class ContentModel {

    private DomContent content = TagCreator.text("");
    @Getter
    @Setter
    private Site site = new Site();
    private List<Podcast> podcasts = new ArrayList<>();
    private List<Post> posts = new ArrayList<>();
    private List<RawContentItem> files = new ArrayList<>();
    private List<Page> pages = new ArrayList<>();
    private Feed feed = new Feed();
    private List<SocialLink> socialLinks = new ArrayList<>();
    private Page page;

    public void visit(ContentModelVisitor visitor) {

        // pages
        pages.stream()
                .forEach(visitor::page);

        // tags
        getTags().stream()
                .forEach(visitor::tag);

        // posts
        posts.stream()
                .forEach(visitor::post);

        // podcast
        podcasts.stream()
                .forEach(visitor::podcast);

    }

    public String getLang() {
        // TODO: lang="{{ page.lang | default: site.lang | default: "en" }}"
        return "en";
    }

    public DomContent getContent() {
        return content;
    }

    public void addSocialLink(SocialLink socialLink) {
        this.socialLinks.add(socialLink);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Post> getTaggedContent() {
        return this.posts.stream().filter(p -> p.getTags().size() > 0).toList();
    }

    public List<String> getTags() {
        return this.posts.stream().flatMap(p -> p.getTags().stream()).distinct().toList();
    }

    public List<Post> getPostsWithTag(String tag) {
        return this.posts.stream().filter(p -> p.getTags().contains(tag)).toList();
    }

    public List<String> getCategories() {
        return this.posts.stream().map(Post::getCategory).filter(Objects::nonNull).distinct().toList();
    }

    public List<Post> getPostsInCategory(String category) {
        return this.posts.stream().filter(p -> category.equals(p.getCategory())).toList();
    }

    public void setContent(DomContent domContent) {
        this.content = domContent;
    }

    public void addPodcast(Podcast podcast) {
        this.podcasts.add(podcast);
    }

    public List<Podcast> getPodcasts() {
        return this.podcasts;
    }

    public void addPost(Post post) {
        this.posts.add(post);
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    public void addFile(RawContentItem rawContentItem) {
        this.files.add(rawContentItem);
    }

    public void addPage(Page page) {
        this.pages.add(page);
    }

    public List<Page> getPages() {
        return pages;
    }

    public Feed getFeed() {
        return feed;
    }

    public void reset() {
        this.content = TagCreator.text("");
        this.page = null;
    }


}
