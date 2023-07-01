package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {

    @JacksonXmlProperty(isAttribute = true)
    private String term;

}
