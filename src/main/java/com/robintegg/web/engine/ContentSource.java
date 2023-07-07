package com.robintegg.web.engine;

import com.robintegg.web.plugins.Plugins;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class ContentSource {

  private final Path sourceDirectory;

  public ContentSource(Path sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public void loadContent(ContentModel contentModel) throws IOException {

    log.info("source directory: {}", sourceDirectory.toAbsolutePath());

    Plugins.contentTypePlugins.stream()
        .forEach(contentTypePlugin -> {
          contentTypePlugin.loadContent(sourceDirectory, contentModel);
        });

  }

}
