package Controller;

import Service.UserService;
import Service.TransactionService;
import models.User;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private TransactionService transactionService;

    public InputValidator(UserService userService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }


    public static String getValidString(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();


            if (!input.isEmpty()) {
                return input;
            }


            System.out.println(errorMessage);
        }
    }




    public User handleLogin() {
        System.out.println("\n=== Login ===");

        String username = getValidString(
                "Enter username: ",
                "Username cannot be empty. Please try again."
        );

        String password = getValidString(
                "Enter password: ",
                "Password cannot be empty. Please try again."
        );

        User user = userService.loginUser(username, password);

        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername());
            return user;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return null;
        }
    }


    // Регистрация пользователя
    public void handleRegistration() {
        System.out.println("\n=== Registration ===");

        String username = getValidString(
                "Enter username: ",
                "Username cannot be empty. Please try again."
        );

        while (userService.isLoginTaken(username)) {
            System.out.println("This username is already taken. Please choose another.");
            username = getValidString(
                    "Enter username: ",
                    "Username cannot be empty. Please try again."
            );
        }

        String password = getValidString(
                "Enter password: ",
                "Password cannot be empty. Please try again."
        );

        if (userService.registerUser(username, password)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
    public void updateUserProfile(int userId) {
        System.out.println("\n=== Update Profile ===");

        String firstName = getValidString("Enter your first name: ", "First name cannot be empty.");
        String lastName = getValidString("Enter your last name: ", "Last name cannot be empty.");
        String email = getValidString("Enter your email: ", "Email cannot be empty.");
        String phoneNumber = getValidString("Enter your phone number (e.g., +77...): ", "Phone number cannot be empty.");
        String gender = getValidString("Enter your gender (Male/Female/Other): ", "Gender cannot be empty.");

        if (userService.updateUserDetails(userId, firstName, lastName, email, phoneNumber, gender)) {
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Failed to update profile. Please try again.");
        }
    }
}
