package com.example.learnrest.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequest {
  @NotBlank
  private String refreshToken;

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
