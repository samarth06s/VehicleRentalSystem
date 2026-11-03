package com.vehiclerental.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DBConnection {

    private static final Path CONFIG_FILE =
            Path.of(System.getProperty("user.home"),
                    ".vehicle-rental-system",
                    "db.properties");

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        if (!Files.exists(CONFIG_FILE)) {
            throw new SQLException(
                    "Database configuration not found: " + CONFIG_FILE);
        }

        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new SQLException(
                    "Unable to load database configuration!", e);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || username == null || password == null) {
            throw new SQLException(
                    "Incomplete database configuration!");
        }

        return DriverManager.getConnection(url, username, password);
    }
}