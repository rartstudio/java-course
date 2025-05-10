package com.example.learnrest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email); // To check if an email already exists
  Optional<User> findByValidationToken(String validationToken); // Find user by validation token
  Optional<User> findByEmail(String email);
  Optional<User> findByResetPasswordToken(String email);
}
