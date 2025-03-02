package com.example.customerapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@PropertySource("file:db-config.properties")
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @PostConstruct
    public void initialize() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (!isDatabaseInitialized(connection)) {
                initializeDatabase(connection);
            } else {
                logger.info("Database already initialized, skipping initialization process.");
            }
        } catch (SQLException e) {
            logger.error("Database initialization failed", e);
            logger.error("Error details: {}", e.getMessage());
            logger.error("SQL State: {}", e.getSQLState());
            logger.error("Error Code: {}", e.getErrorCode());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private boolean isDatabaseInitialized(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT initialized FROM db_initialization LIMIT 1")) {
            return resultSet.next() && resultSet.getBoolean("initialized");
        } catch (SQLException e) {
            logger.warn("Error checking database initialization status: {}", e.getMessage());
            return false;
        }
    }

    private void initializeDatabase(Connection connection) throws SQLException {
        try {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("schema.sql"));
            logger.info("Schema created successfully");

            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "id UUID PRIMARY KEY, " +
                        "username VARCHAR(255) UNIQUE NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "role VARCHAR(50))");
                logger.info("Users table created successfully");
            }

            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
            logger.info("Data inserted successfully");

            try (Statement statement = connection.createStatement()) {
                statement.execute("INSERT INTO db_initialization (initialized) VALUES (true)");
            }
            logger.info("Database marked as initialized");
        } catch (SQLException e) {
            logger.error("Error during database initialization: {}", e.getMessage());
            throw e;
        }
    }
}