package com.robintegg.web.engine;

import com.robintegg.web.content.book.Book;
import com.robintegg.web.content.book.BookEntry;
import com.robintegg.web.content.book.BooksPage;
import com.robintegg.web.content.podcast.Podcast;
import com.robintegg.web.content.podcast.PodcastsPage;
import com.robintegg.web.content.post.Post;
import com.robintegg.web.pages.*;
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
  private static JAXBContext jaxbContext = null;

  static {
    try {
      jaxbContext = JAXBContext.newInstance(BookEntry.class);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  private final Path sourceDirectory;

  public ContentSource(Path sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }


  public void loadContent(ContentModel contentModel) throws IOException {

    log.info("source directory: {}", sourceDirectory.toAbsolutePath());

    // load podcasts from folder with markdown
    var podcastDirectory = sourceDirectory.resolve("_podcasts");
    log.info("podcasts directory: " + podcastDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(podcastDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .peek(f -> log.info("{}", f))
          .map(ContentSource::readPodcast)
          .forEach(contentModel::addPodcast);
    }

    // load posts from folder with markdown
    var postsDirectory = sourceDirectory.resolve("_posts");
    log.info("posts directory: " + postsDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(postsDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .peek(f -> log.info("{}", f))
          .map(ContentSource::readPost)
          .forEach(contentModel::addPost);
    }

    // load books from folder with xml
    var booksDirectory = sourceDirectory.resolve("_books");
    log.info("books directory: " + booksDirectory.toAbsolutePath());

    try (Stream<Path> paths = Files.walk(booksDirectory)) {
      paths
          .filter(Files::isRegularFile)
          .peek(f -> log.info("{}", f))
          .map(ContentSource::readBook)
          .forEach(contentModel::addBook);
    }

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
    contentModel.addPage(PodcastsPage.create());
    contentModel.addPage(TagsPage.create());
    contentModel.addPage(BooksPage.create());

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

  private static Book readBook(Path path) {

    try {
      // Extract filename, filename without extension, and extension using Path methods
      String filename = path.getFileName().toString();
      int dotIndex = filename.lastIndexOf('.');
      String filenameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
      String fileExtension = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);

      if (fileExtension.equals("xml")) {

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        BookEntry book = (BookEntry) unmarshaller.unmarshal(Files.newInputStream(path));

        return new Book(filenameWithoutExtension, book);

      }

      return null;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

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
