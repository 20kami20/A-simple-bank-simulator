import Service.*;
import Controller.*;
import models.User;
import repository.TransactionRepository;
import repository.UserRepository;
import java.util.Scanner;

public class MyApplication {
    private static final Scanner scanner = new Scanner(System.in);
    private InputValidator inputValidator;
    private UserService userService;
    private TransactionService transactionService;

    public MyApplication() {

        UserRepository userRepository = new UserRepository();
        TransactionRepository transactionRepository = new TransactionRepository();



        this.userService = new UserService(userRepository);
        this.transactionService = new TransactionService(transactionRepository);
        AdminService adminService = new AdminService();



        inputValidator = new InputValidator(userService);


        UserController userController = new UserController(userService);
        TransactionController transactionController = new TransactionController(transactionService);
        AdminController adminController = new AdminController(adminService);



        while (true) {
            showMainMenu();
            int choice = getValidMenuChoice(1, 4, "Enter your choice: ");

            switch (choice) {
                case 1:

                    handleRegistration(userController);
                    break;

                case 2:

                    handleLogin(userController, transactionController);
                    break;

                case 3:

                    handleAdminLogin(adminController);
                    break;

                case 4:

                    exitApplication();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Register");
        System.out.println("2. Login as User");
        System.out.println("3. Login as Admin");
        System.out.println("4. Exit");
    }

    private void handleRegistration(UserController userController) {
        inputValidator.handleRegistration();
    }

    private void handleLogin(UserController userController, TransactionController transactionController) {
        User user = inputValidator.handleLogin();

        if (user != null) {
            userMenu(user.getId(), userController, transactionController);
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }


    private void handleAdminLogin(AdminController adminController) {
        if (adminLogin()) {
            adminController.showAdminMenu();
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private void exitApplication() {
        System.out.println("Exiting application. Goodbye!");
    }

    private boolean adminLogin() {
        System.out.print("Enter admin username: ");
        String adminUsername = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String adminPassword = scanner.nextLine();


        return "aitu".equals(adminUsername) && "2407".equals(adminPassword);
    }

    private int getValidMenuChoice(int min, int max, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("Invalid choice! Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid numeric value.");
            }
        }
    }

    private void userMenu(int userId, UserController userController, TransactionController transactionController) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. View Transaction History");
            System.out.println("6. Update PIN");
            System.out.println("7. View Account Info");
            System.out.println("8. Update Profile");
            System.out.println("9. Logout");

            int userChoice = getValidMenuChoice(1, 9, "Enter your choice: ");

            switch (userChoice) {
                case 1:
                    userController.getBalance(userId);
                    break;
                case 2:
                    transactionController.depositMoney(userId);
                    break;
                case 3:
                    transactionController.withdrawMoney(userId);
                    break;
                case 4:
                    transactionController.transferMoney(userId);
                    break;
                case 5:
                    transactionController.getTransactionHistory(userId);
                    break;
                case 6:
                    userController.updatePin(userId);
                    break;
                case 7:
                    userController.getUserInfo(userId);
                    break;


                case 8:
                    userController.updateProfile(userId);
                    break;
                case 9:
                    System.out.println("Logging out...");
                    return;



            }
        }
    }

    public void start() {
        System.out.println("Starting application...");
    }

    public static void main(String[] args) {
        new MyApplication().start();
    }
}
