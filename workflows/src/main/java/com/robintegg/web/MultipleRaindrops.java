package com.robintegg.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class MultipleRaindrops {

  private List<Raindrop> items;

}
