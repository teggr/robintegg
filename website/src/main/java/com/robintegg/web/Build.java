package com.robintegg.web;

import com.robintegg.web.engine.WebSiteBuilder;
import com.robintegg.web.theme.v2.V2ThemePlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class Build {

  // build the website.
  public static void main(String[] args) {

    // set configuration + theme
    WebSiteBuilder webSiteBuilder = new WebSiteBuilder(
      V2ThemePlugin.create()
    );

    String baseUrl = Stream.of(args)
      .filter( s -> s.equals("--local"))
      .findFirst()
      .map(s -> "http://localhost:8000")
      .orElse(null);

    webSiteBuilder.build(baseUrl);

  }

}
