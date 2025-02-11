package Service;

import Interfaces.IDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements IDB {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/bankdb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";

    // Приватный конструктор для Singleton
    private DatabaseConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to database");
    }

    // Получение экземпляра Singleton
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseConnection();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize database connection", e);
            }
        }
        return instance;
    }

    // Метод экземпляра для получения соединения
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}
