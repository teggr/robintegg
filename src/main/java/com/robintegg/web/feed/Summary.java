package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Summary {

    @JacksonXmlProperty(isAttribute = true)
    private String type;
    @JacksonXmlText
    @JacksonXmlCData
    private String value;

}
