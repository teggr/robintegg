package com.robintegg.web.content.staticfiles;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.plugins.ContentTypePlugin;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

@Slf4j
public class StaticFilesPlugin implements ContentTypePlugin {

  public static StaticFilesPlugin create() {
    return new StaticFilesPlugin();
  }

  @SneakyThrows
  @Override
  public void loadContent(Path sourceDirectory, ContentModel contentModel) {
// load filers from all none special folders
    log.info("files directory: " + sourceDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(sourceDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .filter(f ->
              {
                String path = f.toAbsolutePath().toString();
                return !path.contains("_")
                    && !path.contains(".git")
                    && !path.contains(".idea")
                    && !path.contains("src")
                    && !path.contains("target")
                    && !path.contains("pom.xml")
                    && !path.contains("README.md")
                    && !path.contains("Gemfile")
                    && !path.contains("html")
                    && !path.contains(".mvn")
                    && !path.contains(".toml")
                    && !path.contains("mvnw")
                    && !path.contains(".github");
              }
          )
          .peek(f -> log.info("{}", f))
          .map(StaticFilesPlugin::readFile)
          .forEach(contentModel::addFile);
    }

  }

  private static StaticFile readFile(Path path) {

    try {

      // Extract filename, filename without extension, and extension using Path methods
      String filename = path.getFileName().toString();
      int dotIndex = filename.lastIndexOf('.');
      String filenameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
      String fileExtension = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);

      return new StaticFile(path.toString(), Collections.emptyMap(), Files.readAllBytes(path));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
