package com.robintegg.web.theme;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.plugins.ContentRenderPlugin;
import com.robintegg.web.plugins.ContentTypePlugin;
import com.robintegg.web.theme.layouts.*;
import com.robintegg.web.theme.pages.CategoriesPage;
import com.robintegg.web.theme.pages.IndexPage;
import com.robintegg.web.theme.pages.TagsPage;
import com.robintegg.web.theme.pages._404Page;

import java.nio.file.Path;
import java.util.Map;

public class DefaultThemePlugin implements ContentTypePlugin, ContentRenderPlugin {
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

  @Override
  public void loadLayout(Map<String, Layout> layouts) {
    layouts.put("categories", CategoriesLayout.create());
    layouts.put("default", DefaultLayout.create());
    layouts.put("home", HomeLayout.create());
    layouts.put("page", PageLayout.create());
    layouts.put("tag", TagLayout.create());
  }
}
