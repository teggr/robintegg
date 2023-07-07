package com.robintegg.web.site;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocialLink {

  private String icon;
  private String username;
  private String userUrl;

}
