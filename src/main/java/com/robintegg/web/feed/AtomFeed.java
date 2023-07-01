package com.robintegg.web.feed;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@JacksonXmlRootElement(localName = "feed")
public class AtomFeed {

    @JacksonXmlProperty(isAttribute = true)
    private final String xmlns = "http://www.w3.org/2005/Atom";

    private Generator generator;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Link> link;
    private OffsetDateTime updated;
    private String id;
    private Title title;
    private String subtitle;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Author> author;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Entry> entry;

}
