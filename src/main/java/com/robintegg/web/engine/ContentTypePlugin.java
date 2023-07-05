package com.robintegg.web.engine;

import java.nio.file.Path;
import java.util.List;

public interface ContentTypePlugin {

  List<Page> pages();

  void loadContent(Path sourceDirectory, ContentModel contentModel);

}
