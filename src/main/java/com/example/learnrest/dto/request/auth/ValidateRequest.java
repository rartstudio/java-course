package com.example.learnrest.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public class ValidateRequest {
  @NotBlank(message = "Token is required")
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
