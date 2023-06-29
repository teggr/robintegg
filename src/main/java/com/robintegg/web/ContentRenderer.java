package com.robintegg.web;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.engine.ContentModelVisitor;
import com.robintegg.web.engine.Layout;
import com.robintegg.web.engine.Page;
import j2html.TagCreator;
import j2html.rendering.FlatHtml;
import j2html.tags.DomContent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
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

                contentModel.setPage(page);

                Map<String, List<String>> data = page.getData();
                String layoutName = data.getOrDefault("layout", List.of("default")).get(0);

                log.info("layout={}", layoutName);

                DomContent domContent = page.getRenderFunction().apply(contentModel);

                // do i need to add the layout? yes
                Layout layout = layouts.get(layoutName);

                // need to update the content for this model here
                contentModel.setContent(domContent);
                DomContent layoutContent = layout.getRenderFunction().apply(contentModel);

                Map<String, List<String>> layoutData =  layout.getData();
                List<String> outLayoutName = layoutData != null ? layoutData.get("layout") : null;
                if (outLayoutName != null) {
                    layoutName = outLayoutName.get(0);
                    log.info("outerlayout={}", layoutName);
                    layout = layouts.get(layoutName);
                    contentModel.setContent(layoutContent);
                    layoutContent = layout.getRenderFunction().apply(contentModel);
                }

                // this is what we want to render
                StringBuilder render = null;
                try {
                    render = layoutContent.render(FlatHtml.inMemory());
                    //System.out.println(render);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String path = page.getPath();
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                var outputFile = outputDirectory.resolve(path);

                // write to file
                try {
                    Files.createDirectories(outputFile.getParent().toAbsolutePath());
                    Files.writeString(outputFile.toAbsolutePath(), TagCreator.document() + render.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                contentModel.reset();

            }
        });

    }
}
