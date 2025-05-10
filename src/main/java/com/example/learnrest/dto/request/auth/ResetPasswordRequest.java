package com.example.learnrest.dto.request.auth;

import com.example.learnrest.validation.PasswordMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class ResetPasswordRequest {

  @NotBlank
  private String token;

  @NotBlank
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  @NotBlank
  private String confirmPassword;

  // Getters and Setters
  public String getToken() {
      return token;
  }

  public void setToken(String token) {
      this.token = token;
  }

  public String getPassword() {
      return password;
  }

  public void setPassword(String password) {
      this.password = password;
  }

  public String getConfirmPassword() {
      return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
      this.confirmPassword = confirmPassword;
  }
}
