package com.robintegg.web.plugins;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.Page;

import java.nio.file.Path;
import java.util.List;

public interface ContentTypePlugin {

  List<Page> pages();

  void loadContent(Path sourceDirectory, ContentModel contentModel);

}
