package com.robintegg.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.stream.Collectors;

@Slf4j
public class Workflows {

  public static void main(String[] args) throws IOException, InterruptedException {

    var testToken = System.getProperty("test.token", "");
    var collectionId = -1; // unsorted
    var uri = "https://api.raindrop.io/rest/v1/raindrops/" + collectionId;

    log.info("uri={},testToken={}", uri, testToken);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .header("Authorization", "Bearer " + testToken)
        .build();

    HttpResponse<String> response =
        client.send(request, HttpResponse.BodyHandlers.ofString());

    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule());

    var multipleRaindrops = objectMapper.readValue(response.body(), MultipleRaindrops.class);

    LocalDate now = LocalDate.now();
    var date = now.format(DateTimeFormatter.ISO_DATE);

    var filename = date + "-reading-list.md";
    System.out.println(filename);

    var title = "Reading list for week " + now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) + " of " + now.getYear();
    var linkContent = multipleRaindrops.getItems().stream()
        .map(r -> {
          return String.format("* [%s](%s)", r.getTitle(), r.getLink());
        }).collect(Collectors.joining("\n"));

    var content = String.format("""
        ---
        title: %s
        layout: default
        category: reading-list
        date: "%s"
        ---
        %s
        """, title, date, linkContent);

    System.out.println(content);

    // move the items to archive folder
//    UpdateManyRaindrops updateManyRaindrops = new UpdateManyRaindrops();
//    var collection = new Collection();
//    collection.setId(35664647L);
//    collection.setOid(35664647L);
//    updateManyRaindrops.setCollection(
//        collection
//    );
//
//    var putBody = objectMapper.writeValueAsString(updateManyRaindrops);
//    System.out.println(putBody);
//
//    // now clear the unsorted list
//    HttpRequest putRequest = HttpRequest.newBuilder()
//        .uri(URI.create(uri))
//        .header("Authorization", "Bearer " + testToken)
//        .header("Accept", "application/json")
//        .header("Content-Type", "application/json")
//        .PUT(HttpRequest.BodyPublishers.ofString(putBody))
//        .build();
//
//    response =
//        client.send(putRequest, HttpResponse.BodyHandlers.ofString());
//
//    System.out.println(response);
//    System.out.println(response.body());

  }

}
