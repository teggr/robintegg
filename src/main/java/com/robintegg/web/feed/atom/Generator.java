package com.robintegg.web.feed.atom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Generator {

    @JacksonXmlProperty(isAttribute = true)
    private String uri;
    @JacksonXmlProperty(isAttribute = true)
    private String version;
    @JacksonXmlText
    private String value;

}
