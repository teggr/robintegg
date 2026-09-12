///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21+
//DEPS org.jsoup:jsoup:1.18.1

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class render_article_preview {
    private static final List<String> CONTENT_SELECTORS = List.of(
            "article",
            "main article",
            "main",
            ".post-content",
            ".post",
            ".content"
    );

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: jbang .github/skills/render-article-preview/scripts/render-article-preview.java <generated-html-path>");
            System.exit(1);
        }

        Path htmlPath = Path.of(args[0]);
        if (!Files.exists(htmlPath) || !Files.isRegularFile(htmlPath)) {
            System.err.println("HTML file not found: " + htmlPath);
            System.exit(1);
        }

        Document document = Jsoup.parse(htmlPath.toFile(), "UTF-8");
        document.select("script, style, nav, header, footer, aside, noscript").remove();

        String title = findTitle(document);
        Element contentRoot = findContentRoot(document);

        System.out.println("# " + title);

        Elements blocks = contentRoot.select("h1, h2, h3, h4, h5, h6, p, li, pre, blockquote");
        if (blocks.isEmpty()) {
            String fallback = contentRoot.text().trim();
            if (!fallback.isEmpty()) {
                System.out.println();
                System.out.println(fallback);
            }
            return;
        }

        for (Element block : blocks) {
            String tag = block.tagName();
            String text = "pre".equals(tag) ? block.wholeText().trim() : block.text().trim();
            if (text.isEmpty()) {
                continue;
            }

            switch (tag) {
                case "h1", "h2", "h3", "h4", "h5", "h6" -> {
                    int level = Integer.parseInt(tag.substring(1));
                    System.out.println();
                    System.out.println("#".repeat(level) + " " + text);
                }
                case "li" -> {
                    System.out.println("- " + text);
                }
                case "blockquote" -> {
                    System.out.println();
                    System.out.println("> " + text);
                }
                case "pre" -> {
                    System.out.println();
                    System.out.println("```");
                    System.out.println(text);
                    System.out.println("```");
                }
                default -> {
                    System.out.println();
                    System.out.println(text);
                }
            }
        }
    }

    private static Element findContentRoot(Document document) {
        for (String selector : CONTENT_SELECTORS) {
            Element candidate = document.selectFirst(selector);
            if (candidate != null && !candidate.text().isBlank()) {
                return candidate;
            }
        }
        Element body = document.body();
        return body != null ? body : document;
    }

    private static String findTitle(Document document) {
        String ogTitle = document.select("meta[property=og:title]").attr("content").trim();
        if (!ogTitle.isEmpty()) {
            return ogTitle;
        }
        Element h1 = document.selectFirst("h1");
        if (h1 != null && !h1.text().isBlank()) {
            return h1.text().trim();
        }
        String title = document.title().trim();
        if (!title.isEmpty()) {
            return title;
        }
        return "Article Preview";
    }
}
