package Service;

import Interfaces.IAdminService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminService implements IAdminService {

    
    @Override
    public void blockAccount(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET is_blocked = TRUE WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.execute();
            System.out.println("User account with ID " + userId + " has been blocked.");
        } catch (SQLException e) {
            System.out.println("Error blocking account: " + e.getMessage());
        }
    }

    
    @Override
    public void unblockAccount(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET is_blocked = FALSE WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.execute();
            System.out.println("User account with ID " + userId + " has been unblocked.");
        } catch (SQLException e) {
            System.out.println("Error unblocking account: " + e.getMessage());
        }
    }

    
    @Override
    public void deleteUser(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.execute();
            System.out.println("User with ID " + userId + " has been deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    
    @Override
    public void viewAllUsers() {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT id, username, balance, is_blocked FROM users";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet rs = statement.executeQuery();
            System.out.println("ID | Username | Balance | Blocked");
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                double balance = rs.getDouble("balance");
                boolean isBlocked = rs.getBoolean("is_blocked");
                System.out.println(id + " | " + username + " | " + balance + " | " + (isBlocked ? "Yes" : "No"));
            }
        } catch (Exception e) {
            System.out.println("Error viewing users: " + e.getMessage());
        }
    }

    
    @Override
    public void searchUserByIdOrUsername(String input) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql;
            PreparedStatement statement;

            
            if (input.matches("[0-9]+")) {  
                sql = "SELECT id, username, balance, is_blocked FROM users WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(input));  
            } else {  // Если это строка (username)
                sql = "SELECT id, username, balance, is_blocked FROM users WHERE username = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, input); 
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                double balance = rs.getDouble("balance");
                boolean isBlocked = rs.getBoolean("is_blocked");
                System.out.println("ID: " + id + ", Username: " + username + ", Balance: " + balance + ", Blocked: " + (isBlocked ? "Yes" : "No"));
            } else {
                System.out.println("User not found.");
            }
        } catch (Exception e) {
            System.out.println("Error searching for user: " + e.getMessage());
        }
    }
}
