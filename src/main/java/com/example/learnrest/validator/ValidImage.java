package com.example.learnrest.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ImageExtensionValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    String message() default "Only .jpg, .jpeg, or .png files are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}