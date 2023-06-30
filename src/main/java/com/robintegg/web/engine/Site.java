package com.robintegg.web.engine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Site {

    private String title;
    private Author author;
    private String description;
    private String email;
    private String twitterUsername;
    private String githubUsername;
    private String googleAnalytics;
    private String linkedinUsername;
    private String url;
    private String baseUrl;

    public boolean paginate() {
        return false;
    }

    public boolean showExcerpts() {
        return false;
    }

}
