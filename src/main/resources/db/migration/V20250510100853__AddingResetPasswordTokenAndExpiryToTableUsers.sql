-- Migration: AddingResetPasswordTokenAndExpiryToTableUsers
-- Created at: Sat May 10 10:08:53 +07 2025

-- Write your SQL commands here
ALTER TABLE users ADD COLUMN reset_password_token VARCHAR(255);
ALTER TABLE users ADD COLUMN reset_password_token_expiry DATETIME;