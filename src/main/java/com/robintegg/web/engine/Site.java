package com.robintegg.web.engine;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Site {

    private final List<Path> podcasts = new ArrayList<>();

    public void addPodcast(Path podcast) {
        log.info(podcast.getFileName().toString());
    }

    public List<Path> podcasts() {
        return new ArrayList<>(podcasts);
    }

}
