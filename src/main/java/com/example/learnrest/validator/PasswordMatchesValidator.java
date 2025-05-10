package com.example.learnrest.validator;

import java.lang.reflect.Method;

import com.example.learnrest.validation.PasswordMatches;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Method getPassword = obj.getClass().getMethod("getPassword");
            Method getConfirmPassword = obj.getClass().getMethod("getConfirmPassword");

            String password = (String) getPassword.invoke(obj);
            String confirmPassword = (String) getConfirmPassword.invoke(obj);

            if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
              // Show the error at the class level
              context.disableDefaultConstraintViolation();
              context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("confirmPassword") // or omit this to attach to the object
                    .addConstraintViolation();
              return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace(); // Useful for debugging
            return false;
        }
    }
}
