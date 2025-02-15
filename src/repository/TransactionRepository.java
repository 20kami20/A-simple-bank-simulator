package repository;

import Service.DatabaseConnection;
import models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionRepository {


    public void depositMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error depositing money: " + e.getMessage());
        }
    }


    public void withdrawMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDouble(1, amount);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error withdrawing money: " + e.getMessage());
        }
    }


    public void transferMoney(int senderId, int receiverId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            String deductSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement deductStatement = connection.prepareStatement(deductSql);
            deductStatement.setDouble(1, amount);
            deductStatement.setInt(2, senderId);
            deductStatement.executeUpdate();


            String addSql = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement addStatement = connection.prepareStatement(addSql);
            addStatement.setDouble(1, amount);
            addStatement.setInt(2, receiverId);
            addStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error transferring money: " + e.getMessage());
        }
    }


    public List<Transaction> getTransactionHistory(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "SELECT amount, transaction_date FROM transactions WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                Date transactionDate = resultSet.getTimestamp("transaction_date");


                transactions.add(new Transaction(null, null, amount, transactionDate));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
        return transactions;
    }

}


