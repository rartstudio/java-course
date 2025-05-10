package com.example.learnrest.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordRequest {

  @NotBlank
  private String token;

  @NotBlank
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String newPassword;

  @NotBlank
  private String confirmPassword;

  // Getters and Setters
  public String getToken() {
      return token;
  }

  public void setToken(String token) {
      this.token = token;
  }

  public String getNewPassword() {
      return newPassword;
  }

  public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
  }

  public String getConfirmPassword() {
      return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
      this.confirmPassword = confirmPassword;
  }
}
