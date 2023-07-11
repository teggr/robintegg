package com.robintegg.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Raindrop {

  @JsonProperty("_id")
  private Long id;
  private String link;
  private String title;
  private String excerpt;
  private List<Media> media;
  private List<String> tags;
  private OffsetDateTime created;

}
