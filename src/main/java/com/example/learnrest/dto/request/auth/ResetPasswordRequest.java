package com.example.learnrest.dto.request.auth;

import com.example.learnrest.validation.PasswordMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ResetPasswordRequest {

  @NotBlank
  private String token;

  @NotBlank
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  @NotBlank
  private String confirmPassword;
}
