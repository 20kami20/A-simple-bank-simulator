package ServiceTranction;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class balance {
    public static double getBalance(int userId) {
        double balance = 0.0;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT get_balance(?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }
}
