package com.robintegg.web.theme.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.theme.includes.Footer;
import com.robintegg.web.theme.includes.Head;
import com.robintegg.web.theme.includes.Header;
import com.robintegg.web.theme.includes.PiwikPro;
import j2html.TagCreator;
import j2html.tags.DomContent;

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
            TagCreator.body(
                TagCreator.iff(
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
