package com.robintegg.web.pages;

import com.robintegg.web.engine.Context;
import com.robintegg.web.engine.Page;
import com.robintegg.web.engine.RenderModel;
import com.robintegg.web.theme.pages._404Page;
import j2html.TagCreator;
import org.junit.jupiter.api.Test;

class _404PageTest {

    @Test
    void shouldRender() {

        Page page = _404Page.create();

        RenderModel renderModel = new RenderModel();
        renderModel.setContext(new Context());
        
        System.out.println(
                TagCreator.html(
                        page.getRenderFunction().apply(renderModel)
                ).render()
        );

    }

}