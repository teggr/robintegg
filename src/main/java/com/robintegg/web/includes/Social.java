package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import j2html.TagCreator;
import j2html.tags.DomContent;

import java.util.stream.Stream;

import static j2html.TagCreator.*;

public class Social {
    public static DomContent create(ContentModel contentModel) {
        return ul()
            .withClass("social-media-list")
            .with(
                each(contentModel.getSocialLinks(), socialItem -> {
                    return SocialItem.create(contentModel, socialItem);
                })
            );
    }
}
