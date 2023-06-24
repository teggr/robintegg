package com.robintegg.web.pages;

import com.robintegg.web.engine.Page;
import j2html.tags.DomContent;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;

public class _404 {

    public static Page create() {

        return Page.builder()
                .data(Map.of("layout", List.of("default")))
                .renderFunction(cm ->
                        new DomContent[]{
                                style()
                                        .withMedia("screen")
                                        .withType("text/css")
                                        .withText("""
                                            .container {
                                                margin: 10px auto;
                                                max-width: 600px;
                                                text-align: center;
                                              }
                                              h1 {
                                                margin: 30px 0;
                                                font-size: 4em;
                                                line-height: 1;
                                                letter-spacing: -1px;
                                              }
                                        """),
                                div()
                                        .withClass("container")
                                        .with(
                                        h1("404"),
                                        p()
                                                .with(
                                                        strong("Page not found :(")
                                                ),
                                        p("The requested page could not be found.")
                                )
                        }
                )
                .build();
    }

}
