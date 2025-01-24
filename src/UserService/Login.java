package UserService;

import Service.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class Login {
    public static int Login(String username, String password)
    {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT login_user(?, ?)";
            CallableStatement statement = connection.prepareCall(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt(1);
                if (userId > 0) {
                    System.out.println("Authorization successful!");
                    return userId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        System.out.println("Error Authorization : Invalid username or password.");
        return -1;
    }
}
