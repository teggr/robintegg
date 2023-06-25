package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.SocialLink;
import j2html.TagCreator;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class SocialItem {
  public static DomContent create(ContentModel contentModel, SocialLink item) {
    return li()
        .with(
            a()
                .withRel("me")
                .withHref(item.getUserUrl())
                .withTarget("_blank")
                .withTitle("{{ entry.title | default: entry.platform }}")
//                <svg class="svg-icon grey">
//      <use xlink:href="{{ '/assets/minima-social-icons.svg#' | append: entry.platform | relative_url }}"></use>
//    </svg>
        );
  }
}
