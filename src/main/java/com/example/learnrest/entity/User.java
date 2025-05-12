package com.example.learnrest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
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

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch= FetchType.LAZY)
  private UserProfile profile;
}
