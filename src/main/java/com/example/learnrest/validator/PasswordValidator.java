package com.example.learnrest.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
  private static final String PASSWORD_PATTERN = 
        "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$";

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    if (password == null) {
        return false; // Null passwords are invalid
    }
    return password.matches(PASSWORD_PATTERN);
  }
}
