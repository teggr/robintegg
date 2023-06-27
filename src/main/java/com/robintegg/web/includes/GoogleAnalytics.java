package com.robintegg.web.includes;

import com.robintegg.web.engine.ContentModel;
import j2html.TagCreator;
import j2html.tags.DomContent;

import static j2html.TagCreator.*;

public class GoogleAnalytics {

  public static DomContent create(ContentModel contentModel) {

    return each(
      script()
          .withCondAsync(true)
          .withSrc("https://www.googletagmanager.com/gtag/js?id={{ site.google_analytics }}"),
        script()
            .withText("""
                window['ga-disable-{{ site.google_analytics }}'] = window.doNotTrack === "1" || navigator.doNotTrack === "1" || navigator.doNotTrack === "yes" || navigator.msDoNotTrack === "1";
                  window.dataLayer = window.dataLayer || [];
                  function gtag(){window.dataLayer.push(arguments);}
                  gtag('js', new Date());
                                
                  gtag('config', '{{ site.google_analytics }}');
                """)
    );

  }

}
