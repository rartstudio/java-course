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

            if (password == null || confirmPassword == null) {
                return false;
            }

            return password.equals(confirmPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
