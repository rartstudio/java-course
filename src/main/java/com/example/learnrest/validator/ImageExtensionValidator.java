package com.example.learnrest.validator;

import org.springframework.web.multipart.MultipartFile;

import com.example.learnrest.validation.ValidImage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageExtensionValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty())
            return false;

        String originalName = file.getOriginalFilename();
        if (originalName == null)
            return false;

        String lower = originalName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
    }
}
