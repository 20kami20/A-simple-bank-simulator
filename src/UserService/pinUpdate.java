package UserService;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;

public class pinUpdate {

    public static void pinUpdate(int userId, String newPin) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "CALL update_pin(?, ?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setInt(1, userId);
            statement.setString(2, newPin);
            statement.execute();
            System.out.println("PIN-CODE updated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
