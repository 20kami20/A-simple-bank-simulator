package UserService;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;


public class RegistUser {


    public static void registerUser(String username, String password, String pinCode) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "CALL register_user(?, ?, ?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, pinCode);
            statement.execute();
            System.out.println("Registration successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    