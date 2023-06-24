package com.robintegg.web.pages;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;
import j2html.TagCreator;
import org.junit.jupiter.api.Test;

class _404Test {

    @Test
    void shouldRender() {

        Page page = _404.create();

        System.out.println(
                TagCreator.html(
                        page.getRenderFunction().apply(new ContentModel())
                ).render()
        );

    }

}