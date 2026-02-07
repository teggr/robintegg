/// usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+

void main(String... args) {

  // iterate through each post in `website/_posts` and validate the frontmatter image field is either a valid absolute URL or a valid relative path to an image in `website/_static/images`.
  // if we can't find the image, print an error message with the post filename and the invalid image path.
  Path postsDir = Paths.get("website/_posts");
  Path imagesDir = Paths.get("website/_static/images");
  try {
    Files.walk(postsDir)
      .filter(Files::isRegularFile)
      .filter(p -> p.toString().endsWith(".md") || p.toString().endsWith(".markdown"))
      .forEach(path -> {
        try {
          List<String> lines = Files.readAllLines(path);
          int start = -1, end = -1;
          for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).trim().equals("---")) {
              if (start == -1) start = i;
              else {
                end = i;
                break;
              }
            }
          }
          if (start == -1 || end == -1) return;
          Pattern p = Pattern.compile("^\\s*image:\\s*(?:\"([^\"]+)\"|'([^']+)'|(.+))\\s*$", Pattern.CASE_INSENSITIVE);
          String imageValue = null;
          for (int i = start + 1; i < end; i++) {
            Matcher m = p.matcher(lines.get(i));
            if (m.matches()) {
              imageValue = m.group(1) != null ? m.group(1) : (m.group(2) != null ? m.group(2) : m.group(3));
              if (imageValue != null) imageValue = imageValue.trim();
              break;
            }
          }
          if (imageValue == null || imageValue.isEmpty()) return;
          boolean valid = false;
          try {
            URI uri = new URI(imageValue);
            String scheme = uri.getScheme();
            if (scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
              valid = true;
            }
          } catch (Exception ignored) { /* not a valid URI -> treat as relative */ }
          if (!valid) {
            String rel = imageValue;
            if (rel.startsWith("/")) rel = rel.substring(1);
            if (rel.startsWith("images/")) rel = rel.substring("images/".length());
            if (rel.startsWith("Images/")) rel = rel.substring("Images/".length());
            Path candidate = imagesDir.resolve(rel).normalize();
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) valid = true;
          }
          if (!valid) {
            System.err.println("ERROR: " + path + " -> " + imageValue);
          }
        } catch (IOException e) {
          System.err.println("ERROR reading " + path + ": " + e.getMessage());
        }
      });
  } catch (IOException e) {
    System.err.println("ERROR scanning posts: " + e.getMessage());
  }

}
