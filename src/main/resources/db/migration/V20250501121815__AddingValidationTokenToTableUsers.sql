-- Migration: AddingValidationTokenToTableUsers
-- Created at: Thu May  1 12:18:15 +07 2025

-- Write your SQL commands here
ALTER TABLE users ADD COLUMN validation_token VARCHAR(255);
ALTER TABLE users ADD COLUMN validation_token_expiry DATETIME;