package com.robintegg.web;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.ContentModelVisitor;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.engine.Page;
import j2html.rendering.IndentedHtml;
import j2html.tags.DomContent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

@Slf4j
public class ContentRenderer {
  public void render(Path outputDirectory, Map<String, Layout> layouts, ContentModel contentModel) {

    log.info("render=start");

    contentModel.visit(new ContentModelVisitor() {

      @Override
      public void page(Page page) {
        log.info("page={}", page);

        Map<String, List<String>> data = page.getData();
        String layoutName = data.getOrDefault("layout", List.of("default")).get(0);

        DomContent domContent = page.getRenderFunction().apply(contentModel);
        contentModel.setContent(domContent);

        // do i need to add the layout? yes
        Layout layout = layouts.get(layoutName);
        Map<String, List<String>> layoutData = layout.getData();

        // need to update the content for this model here
        DomContent layoutContent = layout.getRenderFunction().apply(contentModel);

        // this is what we want to render
        StringBuilder render = null;
        try {
          render = layoutContent.render(IndentedHtml.inMemory());
          System.out.println(render);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        String path = page.getPath();
        if(path.startsWith("/")) {
          path = path.substring(1);
        }
        var outputFile = outputDirectory.resolve(path);

        // write to file
        try {
          Files.createDirectories(outputFile.getParent().toAbsolutePath());
          Files.writeString(outputFile.toAbsolutePath(), render, StandardCharsets.UTF_8,  StandardOpenOption.CREATE);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

      }
    });

  }
}
