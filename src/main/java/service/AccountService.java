package service;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Account;


public class AccountService 
{
    public Account login(String username, String password) throws SQLException 
    {

        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) 
        {
            return null;
        }

        String sql = """
                    SELECT user_id, username, password, role
                    FROM users
                    WHERE username = ?
                    LIMIT 1
                    """;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) 
        {
            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) 
            {

                if (rs.next()) 
                {

                    String storedPassword = rs.getString("password");

                    // Verify the entered password
                    if (storedPassword != null && storedPassword.equals(password)) 
                    {

                        // Build Account using the real database values
                        return new Account(rs.getInt("user_id"), rs.getString("username"), storedPassword, rs.getString("role").toUpperCase());
                    }
                }
            }
        }

        // Username or password is incorrect
        return null;
    }
}
