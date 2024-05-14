package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DATABASE_URL = "jdbc:sqlite:src/main/resources/database.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }

    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.out.println("Error disconnecting from the database: " + e.getMessage());
            }
        }
    }
}
