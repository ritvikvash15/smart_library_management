package com.smartlibrary.util;

import com.smartlibrary.annotation.CourseConcept;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Database Manager for managing JDBC Connections to local SQLite DB.
 * Demonstrates: Singleton Pattern, Static Factory Method, JDBC Initialization, File Management.
 */
@CourseConcept(unit = 5, concept = "Singleton & JDBC Database Connection")
public class DatabaseManager {

    private static volatile DatabaseManager instance;
    private static final String DB_DIR = "data";
    private static final String DB_URL = "jdbc:sqlite:data/library.db";

    private DatabaseManager() {
        initDatabase();
    }

    /**
     * Singleton Instance Getter (Thread-safe Double-Checked Locking)
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    /**
     * Get Connection to SQLite Database
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes Database directory and tables automatically upon startup
     */
    private void initDatabase() {
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create Users Table
            String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    phone TEXT,
                    role TEXT NOT NULL,
                    registration_number TEXT,
                    department TEXT,
                    employee_id TEXT
                );
            """;
            stmt.execute(createUsers);

            // Create Books Table
            String createBooks = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    isbn TEXT NOT NULL,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    category TEXT NOT NULL,
                    status TEXT NOT NULL,
                    book_type TEXT NOT NULL,
                    quantity INTEGER DEFAULT 1,
                    location_info TEXT
                );
            """;
            stmt.execute(createBooks);

            // Create Transactions Table
            String createTransactions = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    book_id INTEGER NOT NULL,
                    issue_date TEXT NOT NULL,
                    due_date TEXT NOT NULL,
                    return_date TEXT,
                    fine_amount REAL DEFAULT 0.0,
                    transaction_type TEXT NOT NULL,
                    returned INTEGER DEFAULT 0,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (book_id) REFERENCES books(id)
                );
            """;
            stmt.execute(createTransactions);

            // Seed Initial Data if empty
            seedInitialData(stmt);

        } catch (SQLException e) {
            System.err.println("Failed to initialize SQLite Database: " + e.getMessage());
        }
    }

    private void seedInitialData(Statement stmt) throws SQLException {
        var rsUser = stmt.executeQuery("SELECT COUNT(*) FROM users;");
        if (rsUser.next() && rsUser.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO users (name, email, phone, role, registration_number, department) VALUES " +
                    "('Alice Smith', 'alice@university.edu', '9876543210', 'STUDENT', '21BCE0001', 'Computer Science')," +
                    "('Bob Jones', 'bob@university.edu', '9876543211', 'STUDENT', '21BCE0002', 'Mathematics');");
            stmt.executeUpdate("INSERT INTO users (name, email, phone, role, employee_id) VALUES " +
                    "('Dr. Sarah Connor', 'sarah@university.edu', '9876543212', 'LIBRARIAN', 'EMP1001');");
        }

        var rsBook = stmt.executeQuery("SELECT COUNT(*) FROM books;");
        if (rsBook.next() && rsBook.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO books (isbn, title, author, category, status, book_type, quantity, location_info) VALUES " +
                    "('978-0134685991', 'Effective Java', 'Joshua Bloch', 'COMPUTER_SCIENCE', 'AVAILABLE', 'Physical Book', 2, 'Rack CS-01')," +
                    "('978-0132350884', 'Clean Code', 'Robert C. Martin', 'COMPUTER_SCIENCE', 'AVAILABLE', 'Physical Book', 3, 'Rack CS-02')," +
                    "('978-0321356680', 'Java Concurrency in Practice', 'Brian Goetz', 'COMPUTER_SCIENCE', 'AVAILABLE', 'Physical Book', 1, 'Rack CS-03')," +
                    "('978-0131103627', 'C Programming Language', 'Brian Kernighan', 'COMPUTER_SCIENCE', 'AVAILABLE', 'Physical Book', 2, 'Rack CS-04')," +
                    "('978-1234567890', 'Java Language Specification', 'James Gosling', 'REFERENCE', 'REFERENCE_ONLY', 'Reference Book (Reading Room Only)', 1, 'Reading Room 101');");
        }
    }
}
