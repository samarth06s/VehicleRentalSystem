
package com.vehiclerental.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnection {

    private static final String CONFIG_FILE = "/db.properties";

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        try (InputStream input =
                     DBConnection.class.getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new SQLException("Database configuration file not found!");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException("Unable to load database configuration!", e);
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}
