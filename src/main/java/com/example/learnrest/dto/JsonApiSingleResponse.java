package com.example.learnrest.dto;

import java.util.Map;

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

  public JsonApiResource getData() {
    return data;
  }

  public void setData(JsonApiResource data) {
    this.data = data;
  }

  public Map<String, Object> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, Object> meta) {
    this.meta = meta;
  }
}
