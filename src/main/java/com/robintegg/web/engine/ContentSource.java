package com.robintegg.web.engine;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.book.BookContentTypePlugin;
import com.robintegg.web.content.book.BookEntry;
import com.robintegg.web.content.book.BooksPage;
import com.robintegg.web.content.podcast.Podcast;
import com.robintegg.web.content.podcast.PodcastContentTypePlugin;
import com.robintegg.web.content.podcast.PodcastsPage;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.content.post.PostContentTypePlugin;
import com.robintegg.web.pages.CategoriesPage;
import com.robintegg.web.pages.IndexPage;
import com.robintegg.web.pages.TagsPage;
import com.robintegg.web.pages._404Page;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class ContentSource {

  private static List<ContentTypePlugin> contentTypePlugins = List.of(
      BookContentTypePlugin.create(),
      PodcastContentTypePlugin.create(),
      PostContentTypePlugin.create()
  );

  private final Path sourceDirectory;

  public ContentSource(Path sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }


  public void loadContent(ContentModel contentModel) throws IOException {

    log.info("source directory: {}", sourceDirectory.toAbsolutePath());

    contentTypePlugins.stream()
        .forEach(contentTypePlugin -> {
          contentTypePlugin.loadContent( sourceDirectory.toAbsolutePath(), contentModel );
        });

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
          .map(ContentSource::readFile)
          .forEach(contentModel::addFile);
    }

    contentModel.addPage(_404Page.create());
    contentModel.addPage(IndexPage.create());
    contentModel.addPage(CategoriesPage.create());
    contentModel.addPage(TagsPage.create());

    // type based pages
    contentTypePlugins.stream()
        .map(ContentTypePlugin::pages)
        .flatMap(List::stream)
        .forEach(contentModel::addPage);

  }

  private static RawContentItem readFile(Path path) {

    try {

      // Extract filename, filename without extension, and extension using Path methods
      String filename = path.getFileName().toString();
      int dotIndex = filename.lastIndexOf('.');
      String filenameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
      String fileExtension = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);

      return new RawContentItem(path.toString(), Collections.emptyMap(), Files.readAllBytes(path));

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
