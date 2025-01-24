package ServiceTranction;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class TransHistory {
    public static void getTransactionHistory(int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM get_transaction_history(?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Transaction History:");
            while (resultSet.next()) {
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                String transactionDate = resultSet.getTimestamp("transaction_date").toString();
                System.out.println(transactionType + ": $" + amount + " on " + transactionDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
