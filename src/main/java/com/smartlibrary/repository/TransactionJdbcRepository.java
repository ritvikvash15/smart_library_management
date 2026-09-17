package com.smartlibrary.repository;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.DatabaseOperationException;
import com.smartlibrary.model.LibraryTransaction;
import com.smartlibrary.model.TransactionType;
import com.smartlibrary.util.DatabaseManager;
import com.smartlibrary.util.DateUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Repository for Library Transactions.
 * Demonstrates Unit 5: JDBC PreparedStatement for Transaction records.
 */
@CourseConcept(unit = 5, concept = "JDBC Transaction Management")
public class TransactionJdbcRepository {

    private final DatabaseManager dbManager;

    public TransactionJdbcRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public int saveTransaction(LibraryTransaction tx) throws DatabaseOperationException {
        String sql = """
            INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, transaction_type, returned)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, tx.getUserId());
            pstmt.setInt(2, tx.getBookId());
            pstmt.setString(3, DateUtils.formatDate(tx.getIssueDate()));
            pstmt.setString(4, DateUtils.formatDate(tx.getDueDate()));
            pstmt.setString(5, DateUtils.formatDate(tx.getReturnDate()));
            pstmt.setDouble(6, tx.getFineAmount());
            pstmt.setString(7, tx.getTransactionType().name());
            pstmt.setInt(8, tx.isReturned() ? 1 : 0);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    tx.setTransactionId(id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save transaction: " + e.getMessage(), e);
        }
    }

    public Optional<LibraryTransaction> findById(int id) throws DatabaseOperationException {
        String sql = "SELECT * FROM transactions WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTransaction(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find transaction: " + e.getMessage(), e);
        }
    }

    public List<LibraryTransaction> findActiveByUserId(int userId) throws DatabaseOperationException {
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND returned = 0;";
        List<LibraryTransaction> list = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaction(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve user transactions: " + e.getMessage(), e);
        }
    }

    public List<LibraryTransaction> findAllTransactions() throws DatabaseOperationException {
        String sql = "SELECT * FROM transactions ORDER BY id DESC;";
        List<LibraryTransaction> list = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToTransaction(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve all transactions: " + e.getMessage(), e);
        }
    }

    public void updateReturnStatus(int transactionId, LocalDate returnDate, double fineAmount) throws DatabaseOperationException {
        String sql = "UPDATE transactions SET return_date = ?, fine_amount = ?, returned = 1 WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, DateUtils.formatDate(returnDate));
            pstmt.setDouble(2, fineAmount);
            pstmt.setInt(3, transactionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update transaction return status: " + e.getMessage(), e);
        }
    }

    private LibraryTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int bookId = rs.getInt("book_id");
        LocalDate issueDate = DateUtils.parseDate(rs.getString("issue_date"));
        LocalDate dueDate = DateUtils.parseDate(rs.getString("due_date"));
        LocalDate returnDate = DateUtils.parseDate(rs.getString("return_date"));
        double fineAmount = rs.getDouble("fine_amount");
        TransactionType type = TransactionType.valueOf(rs.getString("transaction_type"));
        boolean returned = rs.getInt("returned") == 1;

        LibraryTransaction tx = new LibraryTransaction(id, userId, bookId, issueDate, dueDate, type);
        tx.setReturnDate(returnDate);
        tx.setFineAmount(fineAmount);
        tx.setReturned(returned);
        return tx;
    }
}
