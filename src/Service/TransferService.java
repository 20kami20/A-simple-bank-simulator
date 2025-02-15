package Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class TransferService {

    public static final Scanner scanner = new Scanner(System.in);


    public static void initiateTransfer(int senderId) {
        int receiverId = getReceiverId();

        if (senderId == receiverId) {
            System.out.println("You cannot transfer money to yourself.");
            return;
        }

        double transferAmount = getAmount("Enter amount to transfer: ");

        if (transferMoney(senderId, receiverId, transferAmount)) {
            System.out.println("Transferred " + transferAmount + " from User " + senderId + " to User " + receiverId);
        }
    }

    private static int getReceiverId() {
        int receiverId = -1;
        while (true) {
            System.out.print("Enter recipient's user ID: ");
            String input = scanner.nextLine();

            // Проверка, что введен только числовой ID
            try {
                receiverId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid numeric user ID.");
            }
        }
        return receiverId;
    }

    private static double getAmount(String prompt) {
        double amount = -1;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            if (input.matches("\\d+(\\.\\d+)?") && Double.parseDouble(input) > 0) {
                amount = Double.parseDouble(input);
                break;
            } else {
                System.out.println("Invalid input! Please enter a positive number.");
            }
        }
        return amount;
    }

    private static boolean transferMoney(int senderId, int receiverId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "CALL transfer_money(?, ?, ?)";
            CallableStatement statement = connection.prepareCall(sql);


            statement.setInt(1, senderId);
            statement.setInt(2, receiverId);
            statement.setBigDecimal(3, BigDecimal.valueOf(amount));

            statement.execute();
            System.out.println("Transaction completed successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error during transaction: " + e.getMessage());
            return false;
        }
    }
}
