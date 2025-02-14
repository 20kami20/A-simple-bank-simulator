package Controller;

import Controller.Interfaces.IAdminController;
import Service.AdminService;
import java.util.Scanner;

public class AdminController implements IAdminController {

    private final AdminService adminService;
    private final Scanner scanner;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void showAdminMenu() {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Block Account");
            System.out.println("2. Unblock Account");
            System.out.println("3. Delete User");
            System.out.println("4. View All Users");
            System.out.println("5. Search User by ID or Username");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = getValidMenuChoice(1, 6);

            switch (choice) {
                case 1:
                    blockAccount();
                    break;

                case 2:
                    unblockAccount();
                    break;

                case 3:
                    deleteUser();
                    break;

                case 4:
                    adminService.viewAllUsers();
                    break;

                case 5:
                    searchUserByIdOrUsername();
                    break;

                case 6:
                    System.out.println("Logged out.");
                    return; 

                default:
                    System.out.println("Invalid choice! Please select 1-6.");
            }
        }
    }


    @Override
    public void searchUserByIdOrUsername() {
        System.out.print("Enter user ID or Username to search: ");
        String input = scanner.nextLine();
        adminService.searchUserByIdOrUsername(input);
    }


    @Override
    public void blockAccount() {
        int userId = getValidUserId("Enter user ID to block: ");
        adminService.blockAccount(userId);
    }


    @Override
    public void unblockAccount() {
        int userId = getValidUserId("Enter user ID to unblock: ");
        adminService.unblockAccount(userId);
    }
    @Override
    public void deleteUser() {
        int userId = getValidUserId("Enter user ID to delete: ");
        adminService.deleteUser(userId);
    }


    private int getValidUserId(String prompt) {
        int userId = -1;
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();


                if (input.matches("[0-9]+")) {
                    userId = Integer.parseInt(input);
                    if (userId > 0) {
                        break;
                    } else {
                        System.out.println("User ID must be a positive integer. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input! Please enter a numeric value for user ID.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid numeric value.");
            }
        }
        return userId;
    }


   


    private int getValidMenuChoice(int min, int max) {
        int choice = -1;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    break;
                } else {
                    System.out.println("Please enter a valid option between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid numeric value.");
            }
        }
        return choice;
    }
}
