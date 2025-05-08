package com.example.learnrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learnrest.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>  {
  
}
