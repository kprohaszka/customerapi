#!/bin/bash
echo "PostgreSQL Setup"
read -p "Enter PostgreSQL username: " db_username
read -s -p "Enter PostgreSQL password (Press enter if none): " db_password
echo
read -p "Enter desired database name: " db_name

echo "db.username=${db_username}" > db-config.properties
echo "db.password=${db_password}" >> db-config.properties
echo "db.name=${db_name}" >> db-config.properties

echo "Configuration saved. You can now start the application."