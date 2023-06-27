package com.robintegg.web.layouts;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.includes.Footer;
import com.robintegg.web.includes.Head;
import com.robintegg.web.includes.Header;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class DefaultLayout {

  public DomContent create(ContentModel contentModel) {

    return html()
        .withLang(contentModel.getLang())
        .with(
            Head.create(contentModel),
            body(
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
