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
            String sql = "INSERT INTO transactions (user_id, type, amount, transaction_date) VALUES (?, 'DEPOSIT', ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setDouble(2, amount);

            System.out.println("Executing SQL: " + statement.toString());  // <-- Добавьте эту строку

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Deposit failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }


    
    @Override
    public void withdrawMoney(int userId, double amount) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            String sql = "INSERT INTO transactions (user_id, type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setString(2, "WITHDRAWAL");
            statement.setDouble(3, amount);

            System.out.println("Executing SQL: " + statement.toString());  // Лог запроса
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Withdrawal failed!");
            }

            connection.commit();  // Фиксируем изменения в БД
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

            // Списываем средства с отправителя
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

            System.out.println("Executing SQL: " + insertTransferStmt.toString());  // Лог запроса
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


    
    @Override
    public void getTransactionHistory(int userId) {
        try (Connection connection = DatabaseConnection.getInstance().getConnection()) {
            
            System.out.println("Fetching transaction history for user ID: " + userId);

            
            String sql = "SELECT " +
                    "    t.id AS transaction_id, " +
                    "    t.amount, " +
                    "    t.transaction_date, " +
                    "    'deposit/withdraw' AS transaction_type, " +
                    "    t.type, " +
                    "    null AS sender_id, " +
                    "    null AS receiver_id " +
                    "FROM " +
                    "    public.transactions t " +
                    "WHERE " +
                    "    t.user_id = ? " +

                    "UNION ALL " +

                    "SELECT " +
                    "    tt.id AS transaction_id, " +
                    "    tt.amount, " +
                    "    tt.transaction_date, " +
                    "    'transfer' AS transaction_type, " +
                    "    'Transfer between users' AS type, " +
                    "    tt.sender_id, " +
                    "    tt.receiver_id " +
                    "FROM " +
                    "    public.transactions_transfer tt " +
                    "WHERE " +
                    "    tt.sender_id = ? OR tt.receiver_id = ? " +
                    "ORDER BY " +
                    "    transaction_date DESC";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, userId);

            
            System.out.println("SQL executed, fetching results...");

            ResultSet rs = statement.executeQuery();

            System.out.println("Transaction History:");
            while (rs.next()) {
                System.out.println("Processing ResultSet row..."); 
                int transactionId = rs.getInt("transaction_id");
                double amount = rs.getDouble("amount");
                Timestamp transactionDate = rs.getTimestamp("transaction_date");
                String transactionType = rs.getString("transaction_type");
                String type = rs.getString("type");
                Integer senderId = rs.getObject("sender_id") != null ? rs.getInt("sender_id") : null;
                Integer receiverId = rs.getObject("receiver_id") != null ? rs.getInt("receiver_id") : null;

                System.out.print("Transaction ID: " + transactionId + ", Amount: " + amount +
                        ", Type: " + transactionType + ", Date: " + transactionDate);
                if ("transfer".equals(transactionType)) {
                    System.out.print(", Sender ID: " + senderId + ", Receiver ID: " + receiverId);
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
    }




}
