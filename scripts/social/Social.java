///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 21
//DEPS com.fasterxml.jackson.core:jackson-databind:2.18.2

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.text.BreakIterator;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Social publishing for robintegg.com.
 *
 * draft   --old-feed old.xml --new-feed new.xml
 *         Finds new articles and writes review files to scripts/social/queue (reviewed through a pull request).
 * draft-article --url URL
 *         Writes a review file for one published article.
 * publish [--dry-run]
 *         Publishes every platform marked "ready" in the queue files and marks it "published".
 *
 * Credentials come from environment variables; a platform without credentials is only printed.
 */
public class Social {

    static final ObjectMapper JSON = new ObjectMapper();
    static final HttpClient HTTP = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(20)).build();
    static final Path QUEUE_DIR = Path.of("scripts/social/queue");
    static final List<String> PLATFORMS = List.of("mastodon", "bluesky", "linkedin", "x");
    static final Map<String, Integer> LIMITS = Map.of("mastodon", 500, "bluesky", 300, "linkedin", 3000, "x", 280);
    static final int MAX_NEW_POSTS = 5;
    static boolean dryRun;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage: draft --old-feed f --new-feed f | draft-article --url URL | publish [--dry-run]");
            System.exit(2);
        }
        Map<String, String> opts = new HashMap<>();
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("--dry-run")) dryRun = true;
            else if (args[i].startsWith("--") && i + 1 < args.length) opts.put(args[i], args[++i]);
        }
        try {
            switch (args[0]) {
                case "draft" -> draft(Path.of(opts.get("--old-feed")), Path.of(opts.get("--new-feed")));
                case "draft-article" -> draftArticleByUrl(opts.get("--url"));
                case "publish" -> publish();
                default -> throw new IllegalArgumentException("unknown command " + args[0]);
            }
        } catch (Exception e) {
            if (args[0].equals("draft-article")) throw e;
            // Never fail the surrounding workflow for social posting problems
            System.err.println("::warning::social " + args[0] + " failed: " + e);
        }
    }

    // ---------------------------------------------------------------- draft

    record Article(String url, String title, String description, String image, String text, List<String> tags) {}

    static void draft(Path oldFeed, Path newFeed) throws Exception {
        Set<String> before = feedLinks(oldFeed);
        if (before.isEmpty()) {
            System.out.println("Previous feed is empty, refusing to announce the whole archive.");
            return;
        }
        List<String> fresh = feedLinks(newFeed).stream().filter(l -> !before.contains(l)).limit(MAX_NEW_POSTS).toList();
        System.out.println("New articles: " + fresh);
        for (String url : fresh) draftArticle(url);
    }

    static void draftArticleByUrl(String url) throws Exception {
        if (url == null || url.isBlank()) throw new IllegalArgumentException("--url is required");
        URI uri = URI.create(url);
        if (!"https".equalsIgnoreCase(uri.getScheme()) || !"robintegg.com".equalsIgnoreCase(uri.getHost())
                || uri.getUserInfo() != null || uri.getPort() != -1) {
            throw new IllegalArgumentException("Article URL must be on https://robintegg.com");
        }
        if (!draftArticle(url)) throw new IllegalStateException("A social draft already exists for " + url);
    }

    static boolean draftArticle(String url) throws Exception {
        Files.createDirectories(QUEUE_DIR);
        Path file = QUEUE_DIR.resolve(slug(url) + ".md");
        if (Files.exists(file)) return false;
        Article a = loadArticle(url);
        Files.writeString(file, render(a, writeTexts(a)));
        System.out.println("Wrote " + file);
        return true;
    }

    static Set<String> feedLinks(Path feed) throws Exception {
        Set<String> links = new LinkedHashSet<>();
        if (!Files.exists(feed) || Files.size(feed) == 0) return links;
        var factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        var doc = factory.newDocumentBuilder().parse(Files.newInputStream(feed));
        var items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            var children = items.item(i).getChildNodes();
            for (int j = 0; j < children.getLength(); j++)
                if (children.item(j).getNodeName().equals("link")) links.add(children.item(j).getTextContent().trim());
        }
        var entries = doc.getElementsByTagName("entry");
        for (int i = 0; i < entries.getLength(); i++) {
            var children = entries.item(i).getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j).getNodeName().equals("link") && children.item(j) instanceof org.w3c.dom.Element e) {
                    String rel = e.getAttribute("rel");
                    if (rel.isEmpty() || rel.equals("alternate")) links.add(e.getAttribute("href"));
                }
            }
        }
        return links;
    }

    static String slug(String url) {
        String path = URI.create(url).getPath().replaceAll("(^/+|/+$)", "").replaceAll("\\.html?$", "");
        return path.isEmpty() ? "home" : path.replaceAll("[^A-Za-z0-9-]+", "-");
    }

    static Article loadArticle(String url) throws Exception {
        String html = get(url);
        String title = unescape(meta(html, "og:title"));
        String description = unescape(meta(html, "og:description"));
        String image = meta(html, "og:image");
        if (!image.isEmpty()) image = URI.create(url).resolve(image).toString();
        List<String> tags = new ArrayList<>();
        Matcher m = Pattern.compile("<meta[^>]+property=[\"']article:tag[\"'][^>]+content=[\"']([^\"']+)", Pattern.CASE_INSENSITIVE).matcher(html);
        while (m.find()) tags.add(unescape(m.group(1)));
        String body = html.replaceAll("(?is)<(script|style|nav|header|footer)[^>]*>.*?</\\1>", " ")
                .replaceAll("(?s)<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        if (body.length() > 6000) body = body.substring(0, 6000);
        return new Article(url, title, description, image, unescape(body), tags);
    }

    static String meta(String html, String property) {
        Matcher m = Pattern.compile("<meta[^>]+property=[\"']" + Pattern.quote(property) + "[\"'][^>]+content=[\"']([^\"']*)", Pattern.CASE_INSENSITIVE).matcher(html);
        if (m.find()) return m.group(1);
        m = Pattern.compile("<meta[^>]+content=[\"']([^\"']*)[\"'][^>]+property=[\"']" + Pattern.quote(property) + "[\"']", Pattern.CASE_INSENSITIVE).matcher(html);
        return m.find() ? m.group(1) : "";
    }

    static String unescape(String s) {
        return s.replace("&quot;", "\"").replace("&#39;", "'").replace("&#x27;", "'").replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
    }

    /** Length as each platform counts it: the text, a blank line and the link. */
    static int length(String platform, String text, String url) {
        int link = switch (platform) {
            case "mastodon", "x" -> 23;
            default -> url.length();
        };
        return graphemes(text) + 2 + link;
    }

    static int graphemes(String s) {
        BreakIterator it = BreakIterator.getCharacterInstance();
        it.setText(s);
        int n = 0;
        while (it.next() != BreakIterator.DONE) n++;
        return n;
    }

    static Map<String, String> writeTexts(Article a) throws Exception {
        String system = """
                You write social media posts for Robin Tegg, a Java developer, announcing a new article on his blog.
                Write in first person, plain and direct. Use only facts from the article. No hype words, no emojis,
                no em dashes. Never put a link, mention or hashtag between parentheses. Do not include the article link,
                it is appended automatically. Use at most two hashtags, derived from the article tags, on Mastodon,
                Bluesky and X, none on LinkedIn.
                Reply with a JSON object with the keys mastodon, bluesky, linkedin and x. Each value is the post text.
                Character limits including the appended link: mastodon 500, bluesky 300, x 280, linkedin 3000.
                LinkedIn may use short paragraphs, the others should be one to three sentences.""";
        String user = "Title: " + a.title() + "\nDescription: " + a.description() + "\nTags: " + a.tags() + "\nURL: " + a.url() + "\n\nArticle:\n" + a.text();
        Map<String, String> texts = new LinkedHashMap<>();
        JsonNode result = JSON.readTree(llm(system, user));
        for (String p : PLATFORMS) {
            String text = result.path(p).asText("").trim();
            for (int attempt = 0; attempt < 3 && (text.isEmpty() || length(p, text, a.url()) > LIMITS.get(p)); attempt++) {
                String retry = "Your " + p + " post was " + (text.isEmpty() ? "missing" : length(p, text, a.url()) + " characters including the link")
                        + ". The limit is " + LIMITS.get(p) + ". Write a shorter one. Reply with JSON {\"text\": \"...\"}.\n\nPrevious:\n" + text;
                text = JSON.readTree(llm(system, user + "\n\n" + retry)).path("text").asText("").trim();
            }
            if (text.isEmpty() || length(p, text, a.url()) > LIMITS.get(p)) {
                System.err.println("::warning::could not fit " + p + " post for " + a.url() + ", marking skipped");
                texts.put(p, "");
            } else texts.put(p, text);
        }
        return texts;
    }

    static String llm(String system, String user) throws Exception {
        String openai = env("OPENAI_API_KEY");
        if (openai == null) throw new IllegalStateException("No OPENAI_API_KEY for the LLM");
        return chat("https://api.openai.com/v1/chat/completions", openai,
                Optional.ofNullable(env("OPENAI_MODEL")).orElse("gpt-4.1"), system, user);
    }

    static String chat(String endpoint, String token, String model, String system, String user) throws Exception {
        ObjectNode body = JSON.createObjectNode().put("model", model).put("temperature", 0.5);
        body.putObject("response_format").put("type", "json_object");
        ArrayNode messages = body.putArray("messages");
        messages.addObject().put("role", "system").put("content", system);
        messages.addObject().put("role", "user").put("content", user);
        HttpResponse<String> r = send(HttpRequest.newBuilder(URI.create(endpoint))
                .header("Authorization", "Bearer " + token).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build());
        if (r.statusCode() / 100 != 2) throw new IllegalStateException(endpoint + " " + r.statusCode() + " " + r.body());
        return JSON.readTree(r.body()).path("choices").path(0).path("message").path("content").asText();
    }

    static String render(Article a, Map<String, String> texts) {
        StringBuilder sb = new StringBuilder("---\n");
        sb.append("url: ").append(a.url()).append('\n');
        sb.append("title: ").append(JSON.valueToTree(a.title())).append('\n');
        sb.append("image: ").append(a.image()).append('\n');
        sb.append("# Platform status: ready (publish on merge), skipped (do not publish), published (done)\n");
        for (String p : PLATFORMS) sb.append(p).append(": ").append(texts.get(p).isEmpty() ? "skipped" : "ready").append('\n');
        sb.append("---\n\nEdit the text under each heading. The article link is appended automatically.\n");
        for (String p : PLATFORMS) sb.append("\n## ").append(p).append("\n\n").append(texts.get(p)).append('\n');
        return sb.toString();
    }

    // -------------------------------------------------------------- publish

    static class Entry {
        Path file;
        Map<String, String> front = new LinkedHashMap<>();
        Map<String, String> texts = new LinkedHashMap<>();
    }

    static Entry parse(Path file) throws Exception {
        Entry e = new Entry();
        e.file = file;
        List<String> lines = Files.readAllLines(file);
        int i = 1;
        for (; i < lines.size() && !lines.get(i).equals("---"); i++) {
            String l = lines.get(i);
            int c = l.indexOf(':');
            if (!l.startsWith("#") && c > 0) e.front.put(l.substring(0, c).trim(), l.substring(c + 1).trim());
        }
        String current = null;
        StringBuilder sb = new StringBuilder();
        for (i = i + 1; i < lines.size(); i++) {
            String l = lines.get(i);
            if (l.startsWith("## ") && PLATFORMS.contains(l.substring(3).trim())) {
                if (current != null) e.texts.put(current, sb.toString().trim());
                current = l.substring(3).trim();
                sb.setLength(0);
            } else if (current != null) sb.append(l).append('\n');
        }
        if (current != null) e.texts.put(current, sb.toString().trim());
        return e;
    }

    static void publish() throws Exception {
        if (!Files.isDirectory(QUEUE_DIR)) return;
        List<Path> files;
        try (var s = Files.list(QUEUE_DIR)) {
            files = s.filter(f -> f.toString().endsWith(".md") && !f.getFileName().toString().equals("README.md")).sorted().toList();
        }
        for (Path f : files) {
            Entry e = parse(f);
            String url = e.front.get("url");
            for (String p : PLATFORMS) {
                if (!"ready".equals(e.front.get(p))) continue;
                String text = e.texts.getOrDefault(p, "");
                if (text.isEmpty() || length(p, text, url) > LIMITS.get(p)) {
                    System.err.println("::warning::" + f + " " + p + " text is empty or too long, left as ready");
                    continue;
                }
                String full = text + "\n\n" + url;
                try {
                    if (dryRun) { System.out.println("[dry-run] " + p + " " + f + ":\n" + full + "\n"); continue; }
                    if (post(p, e, text, url)) {
                        markPublished(e, p);
                        System.out.println("Published " + p + " " + f);
                    } else System.out.println("No credentials for " + p + ", post by hand:\n" + full + "\n");
                } catch (Exception ex) {
                    System.err.println("::warning::" + p + " failed for " + f + ": " + ex.getMessage() + "\nPost by hand:\n" + full);
                }
            }
        }
    }

    static void markPublished(Entry e, String platform) throws Exception {
        List<String> out = new ArrayList<>();
        boolean inFront = false;
        for (String l : Files.readAllLines(e.file)) {
            if (l.equals("---")) inFront = !inFront;
            out.add(inFront && l.startsWith(platform + ":") ? platform + ": published" : l);
        }
        Files.write(e.file, out);
    }

    static boolean post(String platform, Entry e, String text, String url) throws Exception {
        return switch (platform) {
            case "mastodon" -> postMastodon(text + "\n\n" + url, url);
            case "bluesky" -> postBluesky(text, url, unquote(e.front.getOrDefault("title", "")), e.front.getOrDefault("image", ""));
            case "linkedin" -> postLinkedIn(text, url, unquote(e.front.getOrDefault("title", "")));
            case "x" -> postX(text + "\n\n" + url);
            default -> false;
        };
    }

    static String unquote(String s) throws Exception {
        return s.startsWith("\"") ? JSON.readTree(s).asText() : s;
    }

    static boolean postMastodon(String status, String url) throws Exception {
        String base = env("MASTODON_BASE_URL"), token = env("MASTODON_TOKEN");
        if (base == null || token == null) return false;
        check(send(HttpRequest.newBuilder(URI.create(base.replaceAll("/$", "") + "/api/v1/statuses"))
                .header("Authorization", "Bearer " + token).header("Content-Type", "application/json")
                .header("Idempotency-Key", url)
                .POST(HttpRequest.BodyPublishers.ofString(JSON.createObjectNode().put("status", status).put("visibility", "public").toString())).build()));
        return true;
    }

    static boolean postBluesky(String text, String url, String title, String image) throws Exception {
        String handle = env("BLUESKY_HANDLE"), password = env("BLUESKY_APP_PASSWORD");
        if (handle == null || password == null) return false;
        JsonNode session = JSON.readTree(checked(send(HttpRequest.newBuilder(URI.create("https://bsky.social/xrpc/com.atproto.server.createSession"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.createObjectNode().put("identifier", handle).put("password", password).toString())).build())));
        String jwt = session.path("accessJwt").asText(), did = session.path("did").asText();

        String full = text + "\n\n" + url;
        ObjectNode record = JSON.createObjectNode().put("$type", "app.bsky.feed.post").put("text", full)
                .put("createdAt", java.time.Instant.now().toString());
        ArrayNode facets = record.putArray("facets");
        // Links and hashtags are only clickable with facets carrying UTF-8 byte offsets
        Matcher m = Pattern.compile("https?://\\S+|(?<=^|\\s)#\\w+").matcher(full);
        while (m.find()) {
            int start = full.substring(0, m.start()).getBytes(StandardCharsets.UTF_8).length;
            int end = start + m.group().getBytes(StandardCharsets.UTF_8).length;
            ObjectNode facet = facets.addObject();
            facet.putObject("index").put("byteStart", start).put("byteEnd", end);
            ObjectNode feature = facet.putArray("features").addObject();
            if (m.group().startsWith("#")) feature.put("$type", "app.bsky.richtext.facet#tag").put("tag", m.group().substring(1));
            else feature.put("$type", "app.bsky.richtext.facet#link").put("uri", m.group());
        }
        ObjectNode card = record.putObject("embed").put("$type", "app.bsky.embed.external").putObject("external");
        card.put("uri", url).put("title", title).put("description", "");
        if (!image.isEmpty()) {
            try {
                JsonNode blob = JSON.readTree(checked(send(HttpRequest.newBuilder(URI.create("https://bsky.social/xrpc/com.atproto.repo.uploadBlob"))
                        .header("Authorization", "Bearer " + jwt).header("Content-Type", "image/jpeg")
                        .POST(HttpRequest.BodyPublishers.ofByteArray(shrink(getBytes(image)))).build()))).path("blob");
                card.set("thumb", blob);
            } catch (Exception ex) {
                System.err.println("Bluesky thumbnail skipped: " + ex.getMessage());
            }
        }
        ObjectNode body = JSON.createObjectNode().put("repo", did).put("collection", "app.bsky.feed.post");
        body.set("record", record);
        check(send(HttpRequest.newBuilder(URI.create("https://bsky.social/xrpc/com.atproto.repo.createRecord"))
                .header("Authorization", "Bearer " + jwt).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build()));
        return true;
    }

    /** Bluesky rejects blobs of about 1 MB, so lower the JPEG quality until it fits. */
    static byte[] shrink(byte[] original) throws Exception {
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(original));
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        rgb.createGraphics().drawImage(src, 0, 0, java.awt.Color.WHITE, null);
        for (float q = 0.9f; q > 0.2f; q -= 0.1f) {
            var writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            var param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(q);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (var ios = new MemoryCacheImageOutputStream(out)) {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(rgb, null, null), param);
            }
            writer.dispose();
            if (out.size() < 900_000) return out.toByteArray();
        }
        throw new IllegalStateException("image too large");
    }

    static boolean postLinkedIn(String text, String url, String title) throws Exception {
        String token = env("LINKEDIN_TOKEN"), author = env("LINKEDIN_AUTHOR_URN");
        if (token == null || author == null) return false;
        ObjectNode body = JSON.createObjectNode().put("author", author).put("lifecycleState", "PUBLISHED");
        ObjectNode content = body.putObject("specificContent").putObject("com.linkedin.ugc.ShareContent");
        content.putObject("shareCommentary").put("text", text);
        content.put("shareMediaCategory", "ARTICLE");
        content.putArray("media").addObject().put("status", "READY").put("originalUrl", url)
                .putObject("title").put("text", title);
        body.putObject("visibility").put("com.linkedin.ugc.MemberNetworkVisibility", "PUBLIC");
        check(send(HttpRequest.newBuilder(URI.create("https://api.linkedin.com/v2/ugcPosts"))
                .header("Authorization", "Bearer " + token).header("Content-Type", "application/json")
                .header("X-Restli-Protocol-Version", "2.0.0")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString())).build()));
        return true;
    }

    static boolean postX(String text) throws Exception {
        String key = env("X_API_KEY"), secret = env("X_API_SECRET"), token = env("X_ACCESS_TOKEN"), tokenSecret = env("X_ACCESS_SECRET");
        if (key == null || secret == null || token == null || tokenSecret == null) return false;
        String endpoint = "https://api.x.com/2/tweets";
        // OAuth 1.0a: JSON bodies are not part of the signature base string
        TreeMap<String, String> oauth = new TreeMap<>();
        oauth.put("oauth_consumer_key", key);
        oauth.put("oauth_nonce", Long.toHexString(new SecureRandom().nextLong()) + System.nanoTime());
        oauth.put("oauth_signature_method", "HMAC-SHA1");
        oauth.put("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        oauth.put("oauth_token", token);
        oauth.put("oauth_version", "1.0");
        StringJoiner params = new StringJoiner("&");
        oauth.forEach((k, v) -> params.add(enc(k) + "=" + enc(v)));
        String base = "POST&" + enc(endpoint) + "&" + enc(params.toString());
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec((enc(secret) + "&" + enc(tokenSecret)).getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        oauth.put("oauth_signature", Base64.getEncoder().encodeToString(mac.doFinal(base.getBytes(StandardCharsets.UTF_8))));
        StringJoiner header = new StringJoiner(", ", "OAuth ", "");
        oauth.forEach((k, v) -> header.add(enc(k) + "=\"" + enc(v) + "\""));
        check(send(HttpRequest.newBuilder(URI.create(endpoint)).header("Authorization", header.toString())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.createObjectNode().put("text", text).toString())).build()));
        return true;
    }

    // -------------------------------------------------------------- helpers

    static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    static String env(String name) {
        String v = System.getenv(name);
        return v == null || v.isBlank() ? null : v.trim();
    }

    static HttpResponse<String> send(HttpRequest r) throws Exception {
        return HTTP.send(r, HttpResponse.BodyHandlers.ofString());
    }

    static void check(HttpResponse<String> r) {
        if (r.statusCode() / 100 != 2) throw new IllegalStateException(r.uri() + " returned " + r.statusCode() + ": " + r.body());
    }

    static String checked(HttpResponse<String> r) {
        check(r);
        return r.body();
    }

    static String get(String url) throws Exception {
        return checked(send(HttpRequest.newBuilder(URI.create(url)).GET().build()));
    }

    static byte[] getBytes(String url) throws Exception {
        HttpResponse<byte[]> r = HTTP.send(HttpRequest.newBuilder(URI.create(url)).GET().build(), HttpResponse.BodyHandlers.ofByteArray());
        if (r.statusCode() / 100 != 2) throw new IllegalStateException(url + " returned " + r.statusCode());
        return r.body();
    }
}
