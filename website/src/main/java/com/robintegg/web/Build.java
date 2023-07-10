package com.robintegg.web;

import com.robintegg.web.builder.WebSiteBuilder;
import com.robintegg.web.theme.DefaultThemePlugin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Build {

    // build the website.
    public static void main(String[] args) {

        // set configuration + theme
        WebSiteBuilder webSiteBuilder = new WebSiteBuilder(
                DefaultThemePlugin.create()
        );

        webSiteBuilder.build();

    }

}
