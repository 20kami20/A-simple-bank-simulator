package Controller;

import Service.TransactionService;
import java.util.Scanner;

public class TransactionController {
    private final TransactionService transactionService;
    private final Scanner scanner;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.scanner = new Scanner(System.in);
    }


    public void depositMoney(int userId) {
        double amount = getValidAmount("Enter amount to deposit: ");
        transactionService.depositMoney(userId, amount);
    }


    private double getValidAmount(String prompt) {
        double amount = -1;
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();


                if (!input.matches("[0-9]*\\.?[0-9]+")) {
                    System.out.println("Invalid input! Please enter a numeric value.");
                    continue;
                }

                amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("Amount must be positive. Please try again.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid numeric value.");
            }
        }
        return amount;
    }


    public void withdrawMoney(int userId) {
        double amount = getValidAmount("Enter amount to withdraw: ");
        transactionService.withdrawMoney(userId, amount);
    }

    
    public void transferMoney(int senderId) {
        int receiverId = getValidUserId("Enter recipient's user ID: ");
        double amount = getValidAmount("Enter amount to transfer: ");
        transactionService.transferMoney(senderId, receiverId, amount);
    }


    private int getValidUserId(String prompt) {
        int userId = -1;
        while (true) {
            try {
                System.out.print(prompt);
                userId = Integer.parseInt(scanner.nextLine());
                if (userId > 0) {
                    break;
                } else {
                    System.out.println("User ID must be a positive integer. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a numeric value for user ID.");
            }
        }
        return userId;
    }
    public void getTransactionHistory(int userId) {
        transactionService.getTransactionHistory(userId);
    }
}
