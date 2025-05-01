-- Migration: AddingUserValidateAtToTableUsers
-- Created at: Thu May  1 13:19:19 +07 2025

-- Write your SQL commands here
ALTER TABLE users ADD COLUMN user_validate_at DATETIME;