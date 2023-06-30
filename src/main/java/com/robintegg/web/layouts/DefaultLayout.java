package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.includes.Footer;
import com.robintegg.web.includes.Head;
import com.robintegg.web.includes.Header;
import com.robintegg.web.includes.PiwikPro;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class DefaultLayout {

  public static Layout create() {
    return Layout.builder()
        .renderFunction(DefaultLayout::render)
        .build();
  }

  public static DomContent render(ContentModel contentModel) {

    return html()
        .withLang(contentModel.getLang())
        .with(
            Head.create(contentModel),
            body(
                iff(
                    "production".equals(contentModel.getEnvironment()),
                    PiwikPro.create(contentModel)
                ),
                Header.create(contentModel),
                main()
                    .withClass("page-content")
                    .attr("aria-label", "Content")
                    .with(
                        div()
                            .withClass("wrapper")
                            .with(contentModel.getContent())
                    ),
                Footer.create(contentModel)
            )
        );

  }

}