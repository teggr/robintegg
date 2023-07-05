package com.robintegg.web.engine;

import com.robintegg.web.pages.CategoriesPage;
import com.robintegg.web.pages.IndexPage;
import com.robintegg.web.pages.TagsPage;
import com.robintegg.web.pages._404Page;
import com.robintegg.web.plugins.ContentTypePlugin;
import com.robintegg.web.plugins.Plugins;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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
          contentTypePlugin.loadContent( sourceDirectory.toAbsolutePath(), contentModel );
        });



    contentModel.addPage(_404Page.create());
    contentModel.addPage(IndexPage.create());
    contentModel.addPage(CategoriesPage.create());
    contentModel.addPage(TagsPage.create());

    // type based pages
    Plugins.contentTypePlugins.stream()
        .map(ContentTypePlugin::pages)
        .flatMap(List::stream)
        .forEach(contentModel::addPage);

  }


}
