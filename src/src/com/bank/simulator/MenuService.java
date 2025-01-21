package com.bank.simulator;

import java.util.Scanner;

public class MenuService {
    private final UserService userService;
    private final TransactionService transactionService;

    public MenuService(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public void login(Scanner scanner) {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter your password: ");
        String password = scanner.next();

        if (userService.validateLogin(name, password)) {
            System.out.println("Login successful! Welcome, " + name + "!");
            userMenu(scanner, name);
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    private void userMenu(Scanner scanner, String userName) {
        int option;
        do {
            System.out.println("\n1. Check Balance\n2. Deposit\n3. Withdraw\n4. Transaction History\n5. Account Info\n6. Logout");
            System.out.print("Choose an option: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
            option = scanner.nextInt();

            switch (option) {
                case 1 -> userService.displayAccountInfo(userName);
                case 2 -> transactionService.deposit(scanner, userName);
                case 3 -> transactionService.withdraw(scanner, userName);
                case 4 -> transactionService.displayTransactionHistory(userName);
                case 5 -> userService.displayAccountInfo(userName);
                case 6 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (option != 6);
    }
}
