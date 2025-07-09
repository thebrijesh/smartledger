-- ════════════════════════════════════════════════════════════════════════════════════
-- MySQL Initialization Script for SmartLedger
-- ════════════════════════════════════════════════════════════════════════════════════
-- This script runs when MySQL container starts for the first time

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS khatabook 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- Create application user
CREATE USER IF NOT EXISTS 'smartledger'@'%' IDENTIFIED BY 'smartledger123';

-- Grant privileges
GRANT ALL PRIVILEGES ON khatabook.* TO 'smartledger'@'%';

-- Create a read-only user for reporting (optional)
CREATE USER IF NOT EXISTS 'smartledger_readonly'@'%' IDENTIFIED BY 'readonly123';
GRANT SELECT ON khatabook.* TO 'smartledger_readonly'@'%';

-- Flush privileges
FLUSH PRIVILEGES;

-- Use the database
USE khatabook;

-- You can add initial data here if needed
-- INSERT INTO some_table VALUES (...);

-- Show created databases
SHOW DATABASES;
