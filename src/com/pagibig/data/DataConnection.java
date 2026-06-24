package com.pagibig.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataConnection {
    private static Map<String, String> env = null;

    // Helper method to parse the .env file
    private static void loadEnv() {
        env = new HashMap<>();
        // Looks for the .env file in the root folder of the project
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load .env file. Falling back to defaults.");
        }
    }

    private static String getEnv(String key, String defaultValue) {
        if (env == null) {
            loadEnv();
        }
        return env.getOrDefault(key, defaultValue);
    }

    public static Connection getConnection() throws SQLException {
        String host = getEnv("DB_HOST", "127.0.0.1");
        String port = getEnv("DB_PORT", "3306");
        String database = getEnv("DB_NAME", "im_project");
        String user = getEnv("DB_USER", "root");
        String password = getEnv("DB_PASSWORD", "");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver missing in classpath.", e);
        }

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("Successfully connected to database: " + database);
        return connection;
    }
    
}
