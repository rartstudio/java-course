package com.example.learnrest.dto;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonApiListResponse {
  private List<JsonApiResource> data;
  private Map<String, Object> meta;

  public JsonApiListResponse(List<JsonApiResource> data) {
    this.data = data;
  }

  public JsonApiListResponse(List<JsonApiResource> data, Map<String, Object> meta) {
    this.data = data;
    this.meta = meta;
  }
}
