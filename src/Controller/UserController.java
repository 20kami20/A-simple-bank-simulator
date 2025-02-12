package Controller;

import Controller.Interfaces.IUserController;
import Service.UserService;
import models.User;

import java.util.Scanner;

public class UserController implements IUserController {

    private final UserService userService;
    private final Scanner scanner;

    public UserController(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void registerUser() {
        System.out.println("\n=== Registration ===");
        String username = getValidString("Enter username: ", "Username cannot be empty. Please try again.");
        String password = getValidString("Enter password: ", "Password cannot be empty. Please try again.");

        if (userService.registerUser(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    @Override
    public void loginUser() {
        System.out.println("\n=== Login ===");
        String username = getValidString("Enter username: ", "Username cannot be empty. Please try again.");
        String password = getValidString("Enter password: ", "Password cannot be empty. Please try again.");

        User user = userService.loginUser(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername());
        } else {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    @Override
    public void updateUserDetails(int userId) {
        System.out.println("\n=== Update User Details ===");
        String newUsername = getValidString("Enter new username: ", "Username cannot be empty. Please try again.");
        String newPassword = getValidString("Enter new password: ", "Password cannot be empty. Please try again.");

        if (userService.updateUserDetails(new User(userId, newUsername, newPassword))) {
            System.out.println("User details updated successfully!");
        } else {
            System.out.println("Failed to update user details. Please try again.");
        }
    }

    @Override
    public void updatePin(int userId) {
        System.out.println("\n=== Update PIN ===");
        String newPin = getValidString("Enter new PIN: ", "PIN cannot be empty. Please try again.");

        userService.updatePin(userId, newPin);
    }

    @Override
    public void getUserInfo(int userId) {
        System.out.println("\n=== User Info ===");
        userService.getUserInfo(userId);
    }

    @Override
    public void getBalance(int userId) {
        System.out.println("\n=== Balance ===");
        double balance = userService.getBalance(userId);
        System.out.println("Your current balance is: " + balance);
    }

    @Override
    public String getValidString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
    @Override
    public void updateProfile(int userId) {
        new InputValidator(userService).updateUserProfile(userId);
    }
}
