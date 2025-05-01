package com.example.learnrest.util;

import java.util.Map;

import com.example.learnrest.dto.JsonApiResponse;

public class JsonApiHelper {
  public static JsonApiResponse createResponse(String type, Map<String, Object> attributes) {
    // Extract the ID from attributes
    String id = String.valueOf(attributes.getOrDefault("id", "-"));

    // Remove the ID from attributes to avoid redundancy
    attributes.remove("id");

    return new JsonApiResponse(type, id, attributes);
  }
}
