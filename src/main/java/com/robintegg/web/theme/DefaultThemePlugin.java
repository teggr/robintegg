package com.robintegg.web.theme;

import com.robintegg.web.content.book.BookPlugin;
import com.robintegg.web.content.podcast.PodcastPlugin;
import com.robintegg.web.content.post.DraftPostPlugin;
import com.robintegg.web.content.post.PostPlugin;
import com.robintegg.web.content.staticfiles.StaticFilesPlugin;
import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.feed.FeedAggregatorPlugin;
import com.robintegg.web.plugins.ContentRenderPlugin;
import com.robintegg.web.plugins.ContentTypePlugin;
import com.robintegg.web.plugins.Plugins;
import com.robintegg.web.plugins.ThemePlugin;
import com.robintegg.web.theme.layouts.*;
import com.robintegg.web.theme.pages.CategoriesPage;
import com.robintegg.web.theme.pages.IndexPage;
import com.robintegg.web.theme.pages.TagsPage;
import com.robintegg.web.theme.pages._404Page;

import java.nio.file.Path;
import java.util.Map;

public class DefaultThemePlugin implements ContentTypePlugin, ContentRenderPlugin, ThemePlugin {
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

  @Override
  public void registerPlugins() {

    Plugins.contentTypePlugins.add(this);
    Plugins.contentRenderPlugins.add(this);

    BookPlugin.create().registerPlugins();
    PodcastPlugin.create().registerPlugins();
    PostPlugin.create().registerPlugins();
    StaticFilesPlugin.create().registerPlugins();

    FeedAggregatorPlugin.create().registerPlugins();
    DraftPostPlugin.create().registerPlugins();

  }

}
