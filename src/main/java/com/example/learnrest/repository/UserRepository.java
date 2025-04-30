package com.example.learnrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email); // To check if an email already exists
}
