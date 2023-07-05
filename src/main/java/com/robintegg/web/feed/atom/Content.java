package com.robintegg.web.feed.atom;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Content {

    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlProperty(localName = "xml:base", isAttribute = true)
    private String xmlBase;
    @JacksonXmlText
    @JacksonXmlCData
    private String value;

}
