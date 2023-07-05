package com.robintegg.web.engine;

public interface ContentModelVisitor {

    void page(Page page);

    void tag(String tag);

    void file(RawContentItem file);

}
