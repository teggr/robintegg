package com.robintegg.web.content.post;

import com.robintegg.web.engine.ContentModel;
import com.robintegg.web.plugins.ContentTypePlugin;
import com.robintegg.web.engine.Page;
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
public class PostContentTypePlugin implements ContentTypePlugin {
  public static PostContentTypePlugin create() {
    return new PostContentTypePlugin();
  }


  @Override
  public List<Page> pages() {
    return List.of();
  }

  @SneakyThrows
  @Override
  public void loadContent(Path sourceDirectory, ContentModel contentModel) {

    // load posts from folder with markdown
    var postsDirectory = sourceDirectory.resolve("_posts");
    log.info("posts directory: " + postsDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(postsDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .peek(f -> log.info("{}", f))
          .map(PostContentTypePlugin::readPost)
          .forEach(contentModel::addPost);
    }
  }


  private static Post readPost(Path path) {

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

        return new Post(filenameWithoutExtension, yamlFrontMatterVisitor.getData(), document);


      }

      return null;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }


}
