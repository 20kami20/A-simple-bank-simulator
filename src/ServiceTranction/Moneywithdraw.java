package ServiceTranction;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;

public class Moneywithdraw {public static void withdrawMoney(int userId, double amount) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String sql = "CALL withdraw_money(?, ?)";
        CallableStatement statement = connection.prepareCall(sql);
        statement.setInt(1, userId);
        statement.setDouble(2, amount);
        statement.execute();
        System.out.println("Amount withdrawn successfully!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
