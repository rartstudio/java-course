package com.example.learnrest.dto;

import java.util.List;
import java.util.Map;

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
  
  public List<JsonApiResource> getData() {
    return data;
  }

  public void setData(List<JsonApiResource> data) {
    this.data = data;
  }

  public Map<String, Object> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, Object> meta) {
    this.meta = meta;
  }
}
