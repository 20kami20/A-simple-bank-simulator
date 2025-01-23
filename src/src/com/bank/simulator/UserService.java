package com.bank.simulator;

import java.sql.*;
import java.util.Scanner;

public class UserService {

    private final String DB_URL = "jdbc:mysql://localhost:3306/bank_simulator";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "Baxa4357@";

    public void registerUser(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.print("Enter your name: ");
            String name = scanner.next();
            System.out.print("Enter a password: ");
            String password = scanner.next();

            String query = "INSERT INTO users (name, password, balance, pin) VALUES (?, ?, 0, ?)";
            System.out.print("Enter a 4-digit PIN: ");
            String pin = scanner.next();

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, pin);
            statement.executeUpdate();

            System.out.println("Registration successful!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists. Try a different name.");
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
        }
    }

    public boolean validateLogin(String name, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    public void displayAccountInfo(String userName) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double balance = resultSet.getDouble("balance");
                System.out.println("Account Information:");
                System.out.println("Name: " + name);
                System.out.println("Balance: $" + balance);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account information: " + e.getMessage());
        }
    }
}