package com.robintegg.web.feed;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.robintegg.web.engine.OffsetDateTimeXmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private OffsetDateTime published;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
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
