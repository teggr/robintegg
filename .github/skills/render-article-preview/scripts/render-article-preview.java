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
            ".post-content",
            ".post",
            ".content",
            "article",
            "main article",
            "main"
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

        Elements blocks = contentRoot.select("h1, h2, h3, h4, h5, h6, p, pre, blockquote, ul, ol");
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
            if (hasAncestorTag(block, "li")) {
                continue;
            }

            switch (tag) {
                case "h1", "h2", "h3", "h4", "h5", "h6" -> {
                    String text = block.text().trim();
                    if (text.isEmpty()) {
                        continue;
                    }
                    int level = Integer.parseInt(tag.substring(1));
                    System.out.println();
                    System.out.println("#".repeat(level) + " " + text);
                }
                case "ul", "ol" -> {
                    renderList(block, 0);
                }
                case "blockquote" -> {
                    String text = flattenWithoutNestedLists(block);
                    if (text.isEmpty()) {
                        continue;
                    }
                    System.out.println();
                    System.out.println("> " + text);
                }
                case "pre" -> {
                    String text = block.wholeText().trim();
                    if (text.isEmpty()) {
                        continue;
                    }
                    System.out.println();
                    System.out.println("```");
                    System.out.println(text);
                    System.out.println("```");
                }
                default -> {
                    String text = block.text().trim();
                    if (text.isEmpty()) {
                        continue;
                    }
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

    private static void renderList(Element list, int indentLevel) {
        for (Element child : list.children()) {
            if (!"li".equals(child.tagName())) {
                continue;
            }
            String text = flattenWithoutNestedLists(child);
            if (!text.isEmpty()) {
                System.out.println("  ".repeat(indentLevel) + "- " + text);
            }
            for (Element nested : child.children()) {
                if ("ul".equals(nested.tagName()) || "ol".equals(nested.tagName())) {
                    renderList(nested, indentLevel + 1);
                }
            }
        }
    }

    private static String flattenWithoutNestedLists(Element element) {
        Element copy = element.clone();
        copy.select("ul, ol").remove();
        return copy.text().trim();
    }

    private static boolean hasAncestorTag(Element element, String tagName) {
        Element current = element.parent();
        while (current != null) {
            if (tagName.equals(current.tagName())) {
                return true;
            }
            current = current.parent();
        }
        return false;
    }
}
