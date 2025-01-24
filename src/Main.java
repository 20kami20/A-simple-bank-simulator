import Service.MenuService;

import UserService.RegistUser;
import java.util.Scanner;
import UserService.Login;
import UserService.pinUpdate;
import ServiceTranction.moneyDeposit;
import ServiceTranction.Moneywithdraw;
import ServiceTranction.TransHistory;
import ServiceTranction.balance;
public class Main {
    public static void main(String[] args) {
        MenuService menu = new MenuService();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            menu.showMainMenu();
            int choice = menu.getChoice();

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.next();
                    System.out.print("Enter password: ");
                    String password = scanner.next();
                    System.out.print("Enter PIN-code: ");
                    String pinCode = scanner.next();
                    RegistUser.registerUser(username, password, pinCode);
                    break;

                case 2:
                    System.out.print("Enter username: ");
                    username = scanner.next();
                    System.out.print("Enter password: ");
                    password = scanner.next();
                    int userId = Login.Login(username, password);
                    if (userId != -1) {
                        while (true) {
                            menu.showUserMenu();
                            int userChoice = menu.getChoice();
                            if (userChoice == 6) break;

                            switch (userChoice) {
                                case 1:
                                    System.out.print("Enter amount deposit: ");
                                    double depositAmount = scanner.nextDouble();
                                    moneyDeposit.depositMoney(userId, depositAmount);
                                    break;

                                case 2:
                                    System.out.print("Enter amount withdraw: ");
                                    double withdrawAmount = scanner.nextDouble();
                                    Moneywithdraw.withdrawMoney(userId, withdrawAmount);
                                    break;

                                case 3:
                                    System.out.println("Your Balance: $" + balance.getBalance(userId));
                                    break;

                                case 4:
                                    TransHistory.getTransactionHistory(userId);
                                    break;

                                case 5:
                                    System.out.print("Enter new PIN: ");
                                    String newPin = scanner.next();
                                    pinUpdate.pinUpdate(userId, newPin);
                                    break;

                                default:
                                    System.out.println("Incorrect choice.");
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("Thank you for using our service!");
                    return;

                default:
                    System.out.println("Incorrect choice.");
            }
        }
    }
}
