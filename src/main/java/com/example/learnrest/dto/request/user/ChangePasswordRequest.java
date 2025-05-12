package com.example.learnrest.dto.request.user;

import com.example.learnrest.validation.PasswordMatches;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordMatches
public class ChangePasswordRequest {
  public String currentPassword;
  public String password;
  public String confirmPassword;
}
