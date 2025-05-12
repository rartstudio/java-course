package com.example.learnrest.dto.request.user;

import com.example.learnrest.validation.PasswordMatches;
import com.example.learnrest.validation.ValidPassword;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ChangePasswordRequest {
  @NotBlank(message = "Current password is required")
  public String currentPassword;

  @NotBlank(message = "Password is required")
  @ValidPassword
  public String password;

  @NotBlank
  public String confirmPassword;
}
