#!/bin/bash
echo "PostgreSQL Setup"

# Read PostgreSQL username
read -p "Enter PostgreSQL username: " db_username

# Read PostgreSQL password (hidden input)
read -s -p "Enter PostgreSQL password (Press enter if none): " db_password
echo

# Read desired database name
read -p "Enter desired database name: " db_name

# Save credentials to a config file
echo "db.username=${db_username}" > db-config.properties
echo "db.password=${db_password}" >> db-config.properties
echo "db.name=${db_name}" >> db-config.properties

echo "Configuration saved."

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