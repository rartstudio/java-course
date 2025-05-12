package com.example.learnrest.dto.request.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.learnrest.validation.ValidImage;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProfileForm {
  @NotNull(message= "Image is required")
  @ValidImage
  private MultipartFile image;

  @NotNull(message = "Date of birth is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dateOfBirth;
}
