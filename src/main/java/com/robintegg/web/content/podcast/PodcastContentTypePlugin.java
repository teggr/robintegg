package com.robintegg.web.content.podcast;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.plugins.ContentTypePlugin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PodcastContentTypePlugin implements ContentTypePlugin {
  public static PodcastContentTypePlugin create() {
    return new PodcastContentTypePlugin();
  }

  @SneakyThrows
  @Override
  public void loadContent(Path sourceDirectory, ContentModel contentModel) {
    // load podcasts from folder with markdown
    var podcastDirectory = sourceDirectory.resolve("_podcasts");
    log.info("podcasts directory: " + podcastDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(podcastDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .peek(f -> log.info("{}", f))
          .map(PodcastContentTypePlugin::readPodcast)
          .forEach(contentModel::addPodcast);
    }

    contentModel.addPage(PodcastsPage.create());
  }

  private static Podcast readPodcast(Path path) {

    try {
      // Extract filename, filename without extension, and extension using Path methods
      String filename = path.getFileName().toString();
      int dotIndex = filename.lastIndexOf('.');
      String filenameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
      String fileExtension = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);

      if (fileExtension.equals("md")) {

        Extension extension = YamlFrontMatterExtension.create();
        Parser parser = Parser.builder()
            .extensions(List.of(extension))
            .build();

        Node document = parser.parseReader(Files.newBufferedReader(path));

        YamlFrontMatterVisitor yamlFrontMatterVisitor = new YamlFrontMatterVisitor();
        document.accept(yamlFrontMatterVisitor);

        return new Podcast(filenameWithoutExtension, yamlFrontMatterVisitor.getData(), document);

      }

      return null;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
