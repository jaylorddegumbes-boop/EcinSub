
package com.scc.dao;

import com.scc.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // REGISTER ACCOUNT
    public boolean registerUser(
            String username,
            String password,
            String role
    ) {
        String sql = """
            INSERT INTO users (username, password, roles, status)
            VALUES (?, ?, ?, 'PENDING')
            """;

        try (
                Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username.trim());
            pstmt.setString(2, password);
            pstmt.setString(3, role.toUpperCase());

            pstmt.executeUpdate();

            System.out.println(
                    "Registration successful!"
            );
            System.out.println(
                    "Your account is PENDING approval."
            );

            return true;

        } catch (SQLException e) {
            System.out.println(
                    "Registration failed: " + e.getMessage()
            );
            return false;
        }
    }

    // LOGIN
    public User login(String username, String password) {
        String sql = """
            SELECT id, username, password, roles, status
            FROM users
            WHERE username = ? AND password = ?
            """;

        try (
                Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, username.trim());
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");

                    if (!"ACTIVE".equalsIgnoreCase(status)) {
                        System.out.println(
                                "Login denied. Account status: " + status
                        );
                        System.out.println(
                                "Please contact the administrator for approval."
                        );
                        return null;
                    }

                    System.out.println("Login successful!");

                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("roles"),
                            status
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Login failed: " + e.getMessage()
            );
            return null;
        }

        System.out.println("Invalid username or password.");
        return null;
    }
}
