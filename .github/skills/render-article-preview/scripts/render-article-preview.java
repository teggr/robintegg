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
import java.util.ArrayList;
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
        boolean rendered = renderContainer(contentRoot);
        if (!rendered) {
            String fallback = contentRoot.text().trim();
            if (!fallback.isEmpty()) {
                System.out.println();
                System.out.println(fallback);
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

    private static boolean renderContainer(Element container) {
        boolean rendered = false;
        for (Element child : container.children()) {
            rendered |= renderElement(child);
        }
        return rendered;
    }

    private static boolean renderElement(Element element) {
        String tag = element.tagName();
        return switch (tag) {
            case "h1", "h2", "h3", "h4", "h5", "h6" -> renderHeading(element);
            case "p" -> renderParagraph(element);
            case "pre" -> renderCodeBlock(element);
            case "blockquote" -> renderBlockquote(element);
            case "ul", "ol" -> renderListBlock(element);
            case "li" -> false;
            default -> renderContainer(element);
        };
    }

    private static boolean renderHeading(Element heading) {
        String text = heading.text().trim();
        if (text.isEmpty()) {
            return false;
        }
        int level = Integer.parseInt(heading.tagName().substring(1));
        System.out.println();
        System.out.println("#".repeat(level) + " " + text);
        return true;
    }

    private static boolean renderParagraph(Element paragraph) {
        String text = paragraph.text().trim();
        if (text.isEmpty()) {
            return false;
        }
        System.out.println();
        System.out.println(text);
        return true;
    }

    private static boolean renderBlockquote(Element blockquote) {
        List<String> paragraphs = new ArrayList<>();
        for (Element child : blockquote.children()) {
            if (!"p".equals(child.tagName())) {
                continue;
            }
            String text = child.text().trim();
            if (!text.isEmpty()) {
                paragraphs.add(text);
            }
        }

        if (!paragraphs.isEmpty()) {
            boolean wroteAny = false;
            for (String text : paragraphs) {
                System.out.println();
                System.out.println("> " + text);
                wroteAny = true;
            }
            return wroteAny;
        }

        String text = flattenWithoutNestedLists(blockquote);
        if (text.isEmpty()) {
            return false;
        }

        System.out.println();
        for (String line : text.split("\\R+")) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                System.out.println("> " + trimmed);
            }
        }
        return true;
    }

    private static boolean renderCodeBlock(Element pre) {
        String text = pre.wholeText().trim();
        if (text.isEmpty()) {
            return false;
        }
        System.out.println();
        String fence = backtickFenceFor(text);
        System.out.println(fence);
        System.out.println(text);
        System.out.println(fence);
        return true;
    }

    private static boolean renderListBlock(Element list) {
        return renderList(list, 0);
    }

    private static boolean renderList(Element list, int indentLevel) {
        boolean ordered = "ol".equals(list.tagName());
        int position = 1;
        boolean rendered = false;
        for (Element child : list.children()) {
            if (!"li".equals(child.tagName())) {
                continue;
            }
            List<String> lines = extractListItemLines(child);
            if (!lines.isEmpty()) {
                String indent = "  ".repeat(indentLevel);
                String marker = ordered ? position + "." : "-";
                System.out.println(indent + marker + " " + lines.get(0));
                for (int i = 1; i < lines.size(); i++) {
                    System.out.println(indent + "  " + lines.get(i));
                }
                rendered = true;
            }
            for (Element nested : child.children()) {
                if ("ul".equals(nested.tagName()) || "ol".equals(nested.tagName())) {
                    rendered |= renderList(nested, indentLevel + 1);
                }
            }
            position++;
        }
        return rendered;
    }

    private static List<String> extractListItemLines(Element listItem) {
        List<String> lines = new ArrayList<>();

        String ownText = listItem.ownText().trim();
        if (!ownText.isEmpty()) {
            lines.add(ownText);
        }

        for (Element child : listItem.children()) {
            if ("ul".equals(child.tagName()) || "ol".equals(child.tagName())) {
                continue;
            }
            String text = "pre".equals(child.tagName()) ? child.wholeText().trim() : child.text().trim();
            if (!text.isEmpty()) {
                lines.add(text);
            }
        }

        if (lines.isEmpty()) {
            String fallback = flattenWithoutNestedLists(listItem);
            if (!fallback.isEmpty()) {
                lines.add(fallback);
            }
        }
        return lines;
    }

    private static String backtickFenceFor(String content) {
        int longestRun = 0;
        int currentRun = 0;
        for (int i = 0; i < content.length(); i++) {
            if (content.charAt(i) == '`') {
                currentRun++;
                longestRun = Math.max(longestRun, currentRun);
            } else {
                currentRun = 0;
            }
        }
        int fenceLength = Math.max(3, longestRun + 1);
        return "`".repeat(fenceLength);
    }

    private static String flattenWithoutNestedLists(Element element) {
        Element copy = element.clone();
        copy.select("ul, ol").remove();
        return copy.text().trim();
    }

}
