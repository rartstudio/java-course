-- Migration: CreateTableUserProfile
-- Created at: Mon May  5 21:24:38 +07 2025

-- Write your SQL commands here
CREATE TABLE user_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  image VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  user_id BIGINT NOT NULL UNIQUE,
  CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);