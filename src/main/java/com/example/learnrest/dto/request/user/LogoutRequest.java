package com.example.learnrest.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {
  @NotBlank(message = "Refresh token is required")
  public String refreshToken;
}
