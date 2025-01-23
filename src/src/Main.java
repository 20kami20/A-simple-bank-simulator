import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/bankdb";
        String user = "postgres";
        String password = "0000";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected");

        } catch (SQLException e) {
            System.out.println("Connection error:");
            e.printStackTrace();

        }
    }
}