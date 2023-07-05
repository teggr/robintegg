package com.robintegg.web.theme;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.plugins.ContentTypePlugin;
import com.robintegg.web.theme.pages.CategoriesPage;
import com.robintegg.web.theme.pages.IndexPage;
import com.robintegg.web.theme.pages.TagsPage;
import com.robintegg.web.theme.pages._404Page;

import java.nio.file.Path;

public class DefaultThemePlugin implements ContentTypePlugin {
  public static DefaultThemePlugin create() {
    return new DefaultThemePlugin();
  }

  @Override
  public void loadContent(Path sourceDirectory, ContentModel contentModel) {

    contentModel.addPage(_404Page.create());
    contentModel.addPage(IndexPage.create());
    contentModel.addPage(CategoriesPage.create());
    contentModel.addPage(TagsPage.create());

  }
}
