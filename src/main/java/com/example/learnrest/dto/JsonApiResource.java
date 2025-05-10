package com.example.learnrest.dto;

import java.util.Map;

public class JsonApiResource {
  private String type;
  private String id;
  private Map<String, Object> attributes;

  public JsonApiResource(String type, String id, Map<String, Object> attributes) {
    this.type = type;
    this.id = id;
    this.attributes = attributes;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }
}
