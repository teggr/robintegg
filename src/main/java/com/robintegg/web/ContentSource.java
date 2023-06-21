package com.robintegg.web;

import lombok.extern.slf4j.Slf4j;
import org.commonmark.Extension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class ContentSource {
    private final Path sourceDirectory;

    public ContentSource(Path sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }


    public void loadContent(ContentModel contentModel) throws IOException {

        log.info("source directory: {}", sourceDirectory.toAbsolutePath());

        // TODO: podcasts
        ContentCollection podcastCollection = new ContentCollection("podcasts");
        // load podcasts from folder with markdown
        var podcastDirectory = sourceDirectory.resolve("_podcasts");
        log.info("podcasts directory: " + podcastDirectory.toAbsolutePath());

        try (Stream<Path> paths = Files.walk(podcastDirectory)) {
            // TODO: type the podcast data
            paths
                    .filter(Files::isRegularFile)
                    .peek(System.out::println)
                    .map(ContentSource::readFile)
                    .forEach(podcastCollection::addItem);
        }

        contentModel.addCollection(podcastCollection);

        // TODO: posts
        ContentCollection postCollection = new ContentCollection("posts");
        // load posts from folder with markdown
        var postsDirectory = sourceDirectory.resolve("_posts");
        log.info("posts directory: " + postsDirectory.toAbsolutePath());

        try (Stream<Path> paths = Files.walk(postsDirectory)) {
            // TODO: type the post data
            paths
                    .filter(Files::isRegularFile)
                    .peek(System.out::println)
                    .map(ContentSource::readFile)
                    .forEach(postCollection::addItem);
        }

        contentModel.addCollection(postCollection);

    }

    private static ContentItem readFile(Path path) {

        try {

            Extension extension = YamlFrontMatterExtension.create();
            Parser parser = Parser.builder()
                    .extensions(List.of(extension))
                    .build();

            Node document = parser.parseReader(Files.newBufferedReader(path));

            YamlFrontMatterVisitor yamlFrontMatterVisitor = new YamlFrontMatterVisitor();
            document.accept(yamlFrontMatterVisitor);

            // Extract filename, filename without extension, and extension using Path methods
            String filename = path.getFileName().toString();
            int dotIndex = filename.lastIndexOf('.');
            String filenameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
            String fileExtension = (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);

            return new ContentItem( filenameWithoutExtension, yamlFrontMatterVisitor.getData(), document);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
