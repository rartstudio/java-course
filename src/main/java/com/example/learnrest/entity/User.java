package com.example.learnrest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String email;
  private String password;

  private String validationToken;
  private LocalDateTime validationTokenExpiry;
  private LocalDateTime userValidateAt;

  private String resetPasswordToken;
  private LocalDateTime resetPasswordTokenExpiry;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getValidationToken() {
    return validationToken;
  }

  public void setValidationToken(String validationToken) {
    this.validationToken = validationToken;
  }

  public LocalDateTime getValidationTokenExpiry() {
    return validationTokenExpiry;
  }

  public void setValidationTokenExpiry(LocalDateTime validationTokenExpiry) {
    this.validationTokenExpiry = validationTokenExpiry;
  }

  public LocalDateTime getUserValidateAt() {
    return userValidateAt;
  }

  public void setUserValidateAt(LocalDateTime userValidateAt) {
    this.userValidateAt = userValidateAt;
  }

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile profile;

  public UserProfile getProfile() {
    return profile;
  }

  public void setProfile(UserProfile profile) {
    this.profile = profile;
  }

  public String getResetPasswordToken() {
    return resetPasswordToken;
}

  public void setResetPasswordToken(String resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
  }

  public LocalDateTime getResetPasswordTokenExpiry() {
    return resetPasswordTokenExpiry;
  }

  public void setResetPasswordTokenExpiry(LocalDateTime resetPasswordTokenExpiry) {
    this.resetPasswordTokenExpiry = resetPasswordTokenExpiry;
  }
}
