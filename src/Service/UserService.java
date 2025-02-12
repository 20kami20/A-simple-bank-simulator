package Service;

import Interfaces.IUserService;
import models.User;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Регистрация нового пользователя
    @Override
    public boolean registerUser(User user) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "CALL register_user(?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getPinCode());
            statement.execute();
            System.out.println("User registered successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User loginUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT id, username, password, pin_code, balance FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String pinCode = rs.getString("pin_code");
                double balance = rs.getDouble("balance");
                return new User(id, username, password);
            }
        } catch (SQLException e) {
            System.out.println("Error logging in: " + e.getMessage());
        }
        return null; // Если пользователь не найден, возвращаем null
    }

    // Обновление данных пользователя
    @Override
    public boolean updateUserDetails(User user) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET username = ?, password = ?, pin_code = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getPinCode());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
            System.out.println("User details updated successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
            return false;
        }
    }

    // Обновление PIN-кода пользователя
    public void updatePin(int userId, String newPin) {
        if (!newPin.matches("\\d{4}")) {
            System.out.println("Invalid PIN! Please enter a 4-digit PIN code.");
            return;
        }

        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "CALL update_pin(?, ?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, userId);
            statement.setString(2, newPin);
            statement.execute();
            System.out.println("PIN code updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating PIN code: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getBalance(int userId) {
        double balance = 0;
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT balance FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching balance: " + e.getMessage());
        }
        return balance;
    }


    public void getUserInfo(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT first_name, last_name, email, gender, phone_number, balance FROM users WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String phoneNumber = rs.getString("phone_number"); 
                double balance = rs.getDouble("balance");

                
                System.out.println("\n=== User Info ===");
                System.out.println("User ID: " + userId);  
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + email);
                System.out.println("Phone Number: " + phoneNumber);  
                System.out.println("Gender: " + gender);
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user info: " + e.getMessage());
        }
    }


    public boolean isLoginTaken(String username) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Если есть хотя бы одна запись, логин занят
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
        return false;
    }
    public boolean registerUser(String username, String password) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "INSERT INTO users (username, password, pin_code, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, "0000"); // Установим стандартный PIN
            statement.setDouble(4, 0.0);   // Установим стандартный баланс
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }
    public boolean updateUserDetails(int userId, String firstName, String lastName, String email, String phoneNumber, String gender) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ?, gender = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, gender);
            statement.setInt(6, userId);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;  
        } catch (SQLException e) {
            System.out.println("Error updating user details: " + e.getMessage());
            return false;
        }
    }


}
