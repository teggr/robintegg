package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {

    @JacksonXmlProperty(isAttribute = true)
    private String href;
    @JacksonXmlProperty(isAttribute = true)
    private String rel;
    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlProperty(isAttribute = true)
    private String title;

}
