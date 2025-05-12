package com.example.learnrest.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateRequest {
  @NotBlank(message = "Token is required")
  private String token;
}
