package com.example.learnrest.dto.request.auth;

import com.example.learnrest.validation.PasswordMatches;
import com.example.learnrest.validation.ValidPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class RegisterRequest {
  @NotBlank(message = "Name is required")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @ValidPassword
  private String password;

  @NotBlank
  private String confirmPassword;
}