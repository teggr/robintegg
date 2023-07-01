package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Title {

    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlText
    private String value;

}
