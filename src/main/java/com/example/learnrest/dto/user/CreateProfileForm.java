package com.example.learnrest.dto.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.learnrest.validator.ValidImage;

import jakarta.validation.constraints.NotNull;

public class CreateProfileForm {
  @NotNull(message= "Image is required")
  @ValidImage
  private MultipartFile image;

  @NotNull(message = "Date of birth is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dateOfBirth;

  // Getters and Setters
  public MultipartFile getImage() {
    return image;
  }

  public void setImage(MultipartFile image) {
    this.image = image;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
}
