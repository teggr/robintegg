package com.robintegg.web.plugins;

import com.robintegg.web.content.book.BookContentTypePlugin;
import com.robintegg.web.content.podcast.PodcastContentTypePlugin;
import com.robintegg.web.content.post.PostContentTypePlugin;
import com.robintegg.web.content.staticfiles.StaticFilesContentTypePlugin;
import com.robintegg.web.theme.DefaultThemePlugin;

import java.util.List;

public class Plugins {

  public static final List<ContentTypePlugin> contentTypePlugins = List.of(
      BookContentTypePlugin.create(),
      PodcastContentTypePlugin.create(),
      PostContentTypePlugin.create(),
      StaticFilesContentTypePlugin.create(),
      DefaultThemePlugin.create()
  );

  // TODO: add plugin type to attach to consumption of items, such as feed

}
