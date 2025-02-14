package Service;

import Service.Interfaces.ITransactionService;
import repository.TransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    
  @Override
    public void depositMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            String updateBalanceSql = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setInt(2, userId);
            updateBalanceStmt.executeUpdate();

            String sql = "INSERT INTO transactions (user_id, type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setString(2, "DEPOSIT");
            statement.setDouble(3, amount);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Deposit failed!");
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }


    
    @Override
    public void withdrawMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            String checkBalanceSql = "SELECT balance FROM users WHERE id = ?";
            PreparedStatement checkBalanceStmt = connection.prepareStatement(checkBalanceSql);
            checkBalanceStmt.setInt(1, userId);
            ResultSet resultSet = checkBalanceStmt.executeQuery();

            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                if (currentBalance < amount) {
                    System.out.println("Insufficient funds. Withdrawal failed!");
                    return;
                }
            } else {
                System.out.println("User not found!");
                return;
            }

            String updateBalanceSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement updateBalanceStmt = connection.prepareStatement(updateBalanceSql);
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setInt(2, userId);
            updateBalanceStmt.executeUpdate();

            String sql = "INSERT INTO transactions (user_id, type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setString(2, "WITHDRAWAL");
            statement.setDouble(3, amount);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Withdrawal failed!");
            }

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }


    
    @Override
    public boolean transferMoney(int senderId, int receiverId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            connection.setAutoCommit(false); 

            
            String checkBalanceSql = "SELECT balance FROM users WHERE id = ?";
            PreparedStatement checkBalanceStmt = connection.prepareStatement(checkBalanceSql);
            checkBalanceStmt.setInt(1, senderId);
            ResultSet resultSet = checkBalanceStmt.executeQuery();

            if (resultSet.next()) {
                double senderBalance = resultSet.getDouble("balance");
                if (senderBalance < amount) {
                    System.out.println("Insufficient funds.");
                    return false; 
                }
            } else {
                System.out.println("Sender not found.");
                return false; 
            }

        
            String deductSql = "UPDATE users SET balance = balance - ? WHERE id = ?";
            PreparedStatement deductStmt = connection.prepareStatement(deductSql);
            deductStmt.setDouble(1, amount);
            deductStmt.setInt(2, senderId);
            deductStmt.executeUpdate();

            
            String addSql = "UPDATE users SET balance = balance + ? WHERE id = ?";
            PreparedStatement addStmt = connection.prepareStatement(addSql);
            addStmt.setDouble(1, amount);
            addStmt.setInt(2, receiverId);
            addStmt.executeUpdate();

            
            String insertTransferSql = "INSERT INTO transactions_transfer (sender_id, receiver_id, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement insertTransferStmt = connection.prepareStatement(insertTransferSql);
            insertTransferStmt.setInt(1, senderId);
            insertTransferStmt.setInt(2, receiverId);
            insertTransferStmt.setDouble(3, amount);

            System.out.println("Executing SQL: " + insertTransferStmt.toString());  
            int rowsAffected = insertTransferStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Transfer failed!");
            }

            connection.commit();  
            return true;
        } catch (SQLException e) {
            System.out.println("Error during transfer: " + e.getMessage());
            return false;
        }
    }


    





}
