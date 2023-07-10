package com.robintegg.web.site;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class Site {

    private String title;
    private Author author;
    private String description;
    private String email;
    private String twitterUsername;
    private String githubUsername;
    private String linkedinUsername;
    private String url;
    private String baseUrl;

    private List<SocialLink> socialLinks = new ArrayList<>();

    public void addSocialLink(SocialLink socialLink) {
        this.socialLinks.add(socialLink);
    }

    public boolean showExcerpts() {
        return false;
    }

    public String resolveUrl(String resolveUrl) {
        if(!resolveUrl.startsWith("/")) {
            resolveUrl = "/" + resolveUrl;
        }
        return url + baseUrl + resolveUrl;
    }
}
