package com.example.learnrest.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.learnrest.dto.JsonApiListResponse;
import com.example.learnrest.dto.JsonApiResource;
import com.example.learnrest.dto.JsonApiSingleResponse;

public class JsonApiHelper {

  public static JsonApiListResponse createListResponse(String type, List<Map<String, Object>> attributeList, String message) {
    List<JsonApiResource> data = attributeList.stream().map(attrs -> {
      String id = String.valueOf(attrs.getOrDefault("id", "-"));
      attrs.remove("id");
      return new JsonApiResource(type, id, attrs);
    }).collect(Collectors.toList());

    return new JsonApiListResponse(data, Map.of("message", message));
  }

  public static JsonApiSingleResponse createSingleResponse(String type, Map<String, Object> attributes, String message) {
    String id = String.valueOf(attributes.getOrDefault("id", "-"));
    attributes.remove("id");

    JsonApiResource resource = new JsonApiResource(type, id, attributes);
    return new JsonApiSingleResponse(resource, Map.of("message", message));
  }

  public static JsonApiSingleResponse createEmptySingleResponse(String type, String id, String message) {
    JsonApiResource resource = new JsonApiResource(type, id, null);
    return new JsonApiSingleResponse(resource, Map.of("message", message));
  }
}
