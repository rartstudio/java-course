#!/bin/bash

# Check if a migration name is provided
if [ -z "$1" ]; then
  echo "Error: Migration name is required."
  echo "Usage: ./create_migration.sh <migration_name>"
  exit 1
fi

# Define the directory for Flyway migrations
MIGRATION_DIR="src/main/resources/db/migration"

# Ensure the migration directory exists
if [ ! -d "$MIGRATION_DIR" ]; then
  echo "Error: Migration directory '$MIGRATION_DIR' does not exist."
  echo "Please create the directory or check your project structure."
  exit 1
fi

# Generate a timestamp for the migration file
TIMESTAMP=$(date +"%Y%m%d%H%M%S")
MIGRATION_NAME=$1
FILENAME="${MIGRATION_DIR}/V${TIMESTAMP}__${MIGRATION_NAME}.sql"

# Create the migration file with a template
touch "$FILENAME"
echo "-- Migration: $MIGRATION_NAME" >> "$FILENAME"
echo "-- Created at: $(date)" >> "$FILENAME"
echo "" >> "$FILENAME"
echo "-- Write your SQL commands here" >> "$FILENAME"

# Confirm the creation of the migration file
echo "Created migration file: $FILENAME"