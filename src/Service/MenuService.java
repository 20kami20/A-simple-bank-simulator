package Service;

import java.util.Scanner;

public class MenuService {

    public void showMainMenu() {
        System.out.println(" Main Menu ");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
    }

    public void showUserMenu() {
        System.out.println("User Menu");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Check Balance");
        System.out.println("4. Transaction History");
        System.out.println("5. Change PIN");
        System.out.println("6. Logout");
    }

    public int getChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a menu option: ");
        return scanner.nextInt();
    }
}
