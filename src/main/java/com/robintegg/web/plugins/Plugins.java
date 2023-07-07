package com.robintegg.web.plugins;

import com.robintegg.web.content.book.BookPlugin;
import com.robintegg.web.content.podcast.PodcastPlugin;
import com.robintegg.web.content.post.PostPlugin;
import com.robintegg.web.content.staticfiles.StaticFilesPlugin;
import com.robintegg.web.feed.FeedAggregatorPlugin;
import com.robintegg.web.theme.DefaultThemePlugin;

import java.util.ArrayList;
import java.util.List;

public class Plugins {

  public static final List<ContentTypePlugin> contentTypePlugins = new ArrayList<>();
  public static final List<AggregatorPlugin> aggregatorPlugins = new ArrayList<>();
  public static final List<ContentRenderPlugin> contentRenderPlugins = new ArrayList<>();

  static {

    BookPlugin bookPlugin = BookPlugin.create();
    PodcastPlugin podcastPlugin = PodcastPlugin.create();
    PostPlugin postPlugin = PostPlugin.create();
    StaticFilesPlugin staticFilesPlugin = StaticFilesPlugin.create();
    DefaultThemePlugin defaultThemePlugin = DefaultThemePlugin.create();
    FeedAggregatorPlugin feedAggregatorPlugin = FeedAggregatorPlugin.create();

    // content types
    contentTypePlugins.add(bookPlugin);
    contentTypePlugins.add(podcastPlugin);
    contentTypePlugins.add(postPlugin);
    contentTypePlugins.add(staticFilesPlugin);
    contentTypePlugins.add(defaultThemePlugin);

    // rendering
    contentRenderPlugins.add(podcastPlugin);
    contentRenderPlugins.add(postPlugin);
    contentRenderPlugins.add(defaultThemePlugin);

    // aggregators
    aggregatorPlugins.add(feedAggregatorPlugin);

  }

}
