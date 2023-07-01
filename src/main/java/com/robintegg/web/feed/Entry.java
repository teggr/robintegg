package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class Entry {

    private Title title;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Link> link;
    private OffsetDateTime published;
    private OffsetDateTime updated;
    private String id;
    private Content content;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Author> author;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Category> category;
    private Summary summary;
    @JacksonXmlProperty(localName = "media:thumbnail")
    private MediaThumbnail mediaThumbnail;
    @JacksonXmlProperty(localName = "media:content")
    private MediaContent mediaContent;

}
