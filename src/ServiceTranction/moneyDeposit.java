package ServiceTranction;
import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class moneyDeposit {
    public static void depositMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "CALL deposit_money(?, ?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, userId);
            statement.setDouble(2, amount);
            statement.execute();
            System.out.println("Amount deposited successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
