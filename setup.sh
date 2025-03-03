#!/bin/bash
echo "PostgreSQL and JWT Setup"

# Read PostgreSQL username
read -p "Enter PostgreSQL username: " db_username

# Read PostgreSQL password (hidden input)
read -s -p "Enter PostgreSQL password (Press enter if none): " db_password
echo

# Read desired database name
read -p "Enter desired database name: " db_name

# Generate JWT secret key
jwt_secret=$(openssl rand -base64 32)
echo "Generated JWT secret key: $jwt_secret"

# Save credentials to a .env file
echo "DB_USERNAME=${db_username}" > .env
echo "DB_PASSWORD=${db_password}" >> .env
echo "DB_NAME=${db_name}" >> .env
echo "JWT_SECRET_KEY=${jwt_secret}" >> .env

echo "Configuration saved to .env file."

# Create the database
if [ -z "$db_password" ]; then
    PGPASSWORD="" psql -U "$db_username" -d postgres -c "CREATE DATABASE $db_name;"
else
    PGPASSWORD="$db_password" psql -U "$db_username" -d postgres -c "CREATE DATABASE $db_name;"
fi

if [ $? -eq 0 ]; then
    echo "Database '$db_name' created successfully."
else
    echo "Failed to create database '$db_name'. Please check your credentials and PostgreSQL status."
fi

echo "Please ensure to add .env to your .gitignore file to keep your credentials secure."