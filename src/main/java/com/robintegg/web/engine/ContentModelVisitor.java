package com.robintegg.web.engine;

public interface ContentModelVisitor {

    void page(Page page);

    void tag(String tag);

    void post(Post post);

    void podcast(Podcast podcast);

}
