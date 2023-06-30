package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaThumbnail {

    @JacksonXmlProperty(localName = "xmlns:media", isAttribute = true)
    private final String xmlns = "http://search.yahoo.com/mrss/";
    @JacksonXmlProperty(isAttribute = true)
    private String url;

}
