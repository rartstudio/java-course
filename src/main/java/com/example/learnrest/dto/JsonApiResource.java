package com.example.learnrest.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonApiResource {
  private String type;
  private String id;
  private Map<String, Object> attributes;

  public JsonApiResource(String type, String id, Map<String, Object> attributes) {
    this.type = type;
    this.id = id;
    this.attributes = attributes;
  }
}
