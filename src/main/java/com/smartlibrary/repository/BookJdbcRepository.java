package com.smartlibrary.repository;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.exception.DatabaseOperationException;
import com.smartlibrary.model.*;
import com.smartlibrary.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Repository for performing CRUD operations on Book records.
 * Demonstrates Unit 5: JDBC PreparedStatement, ResultSet, Exception Handling.
 */
@CourseConcept(unit = 5, concept = "JDBC Prepared Statement & CRUD Operations")
public class BookJdbcRepository {

    private final DatabaseManager dbManager;

    public BookJdbcRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public int saveBook(Book book) throws DatabaseOperationException {
        String sql = """
            INSERT INTO books (isbn, title, author, category, status, book_type, quantity, location_info)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getCategory().name());
            pstmt.setString(5, book.getStatus().name());
            pstmt.setString(6, book.getBookType());

            if (book instanceof PhysicalBook pb) {
                pstmt.setInt(7, pb.getQuantity());
                pstmt.setString(8, pb.getShelfLocation());
            } else if (book instanceof ReferenceBook rb) {
                pstmt.setInt(7, 1);
                pstmt.setString(8, rb.getReadingRoomSection());
            } else {
                pstmt.setInt(7, 1);
                pstmt.setString(8, "General");
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    book.setId(generatedId);
                    return generatedId;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save book to database: " + e.getMessage(), e);
        }
    }

    public Optional<Book> findById(int id) throws DatabaseOperationException {
        String sql = "SELECT * FROM books WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find book by ID: " + e.getMessage(), e);
        }
    }

    public List<Book> findAllBooks() throws DatabaseOperationException {
        String sql = "SELECT * FROM books ORDER BY id ASC;";
        List<Book> books = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve all books: " + e.getMessage(), e);
        }
    }

    public void updateBookStatus(int bookId, BookStatus status) throws DatabaseOperationException {
        String sql = "UPDATE books SET status = ? WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update book status: " + e.getMessage(), e);
        }
    }

    public void updateBookQuantity(int bookId, int quantity) throws DatabaseOperationException {
        String sql = "UPDATE books SET quantity = ?, status = ? WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            BookStatus newStatus = quantity > 0 ? BookStatus.AVAILABLE : BookStatus.ISSUED;
            pstmt.setInt(1, quantity);
            pstmt.setString(2, newStatus.name());
            pstmt.setInt(3, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update book quantity: " + e.getMessage(), e);
        }
    }

    public boolean deleteBook(int bookId) throws DatabaseOperationException {
        String sql = "DELETE FROM books WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete book: " + e.getMessage(), e);
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String isbn = rs.getString("isbn");
        String title = rs.getString("title");
        String author = rs.getString("author");
        BookCategory category = BookCategory.valueOf(rs.getString("category"));
        BookStatus status = BookStatus.valueOf(rs.getString("status"));
        String bookType = rs.getString("book_type");
        int quantity = rs.getInt("quantity");
        String locationInfo = rs.getString("location_info");

        if (bookType != null && bookType.contains("Reference")) {
            ReferenceBook refBook = new ReferenceBook(id, isbn, title, author, category, locationInfo);
            refBook.setStatus(status);
            return refBook;
        } else {
            PhysicalBook physBook = new PhysicalBook(id, isbn, title, author, category, locationInfo, quantity);
            physBook.setStatus(status);
            return physBook;
        }
    }
}
