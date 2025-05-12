package com.example.learnrest.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonApiSingleResponse {
  private JsonApiResource data;
  private Map<String, Object> meta;

  public JsonApiSingleResponse(JsonApiResource data) {
    this.data = data;
  }

  public JsonApiSingleResponse(JsonApiResource data, Map<String, Object> meta) {
    this.data = data;
    this.meta = meta;
  }
}
