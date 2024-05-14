package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthentication {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/database.db";

    public static boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn
                        .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If result set has next, user is authenticated
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
