package com.robintegg.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Collection {

  @JsonProperty("$ref")
  private String ref = "collections";
  @JsonProperty("$id")
  private Long id;
  private Long oid;


}
