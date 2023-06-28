package com.robintegg.web.engine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Site {

    private String title;
    private Author author;
    private String description;

    public boolean paginate() {
        return false;
    }

    public boolean showExcerpts() {
        return false;
    }

}
