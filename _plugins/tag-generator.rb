# https://talk.jekyllrb.com/t/is-a-generator-for-creating-tag-pages-from-collections-possible/3820/5
module TagGeneratorPlugin
    class TagPageGenerator < Jekyll::Generator
      safe true
  
      def generate(site)
        tags = site.documents.flat_map { |post| post.data['tags'] || [] }.to_set
        tags.each do |tag|
          site.pages << TagPage.new(site, site.source, tag)
        end
      end
    end
  
    class TagPage < Jekyll::Page
      def initialize(site, base, tag)
        @site = site    # the current site instance.
        @base = base    # path to the source directory.
        @dir  = File.join('tags', tag) # the directory the page will reside in.

        # All pages have the same filename, so define attributes straight away.
        @basename = 'index'      # filename without the extension.
        @ext      = '.html'      # the extension.
        @name     = 'index.html' # basically @basename + @ext.
  
        self.process(@name)
        self.read_yaml(File.join(base, '_layouts'), 'tag.html')
        self.data['tag'] = tag
        self.data['title'] = "Tag: #{tag}"
      end
    end
  end