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
 * JDBC Repository for Users (Students and Librarians).
 * Demonstrates Unit 5: JDBC Query Execution and Object Mapping.
 */
@CourseConcept(unit = 5, concept = "JDBC User Repository & Polymorphic Object Mapping")
public class UserJdbcRepository {

    private final DatabaseManager dbManager;

    public UserJdbcRepository() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public int saveUser(User user) throws DatabaseOperationException {
        String sql = """
            INSERT INTO users (name, email, phone, role, registration_number, department, employee_id)
            VALUES (?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getRole().name());

            if (user instanceof Student student) {
                pstmt.setString(5, student.getRegistrationNumber());
                pstmt.setString(6, student.getDepartment());
                pstmt.setNull(7, Types.VARCHAR);
            } else if (user instanceof Librarian librarian) {
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setString(7, librarian.getEmployeeId());
            } else {
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    user.setId(generatedId);
                    return generatedId;
                }
            }
            return -1;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to save user to database: " + e.getMessage(), e);
        }
    }

    public Optional<User> findById(int id) throws DatabaseOperationException {
        String sql = "SELECT * FROM users WHERE id = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find user by ID: " + e.getMessage(), e);
        }
    }

    public Optional<User> findByEmail(String email) throws DatabaseOperationException {
        String sql = "SELECT * FROM users WHERE email = ?;";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find user by Email: " + e.getMessage(), e);
        }
    }

    public List<User> findAllUsers() throws DatabaseOperationException {
        String sql = "SELECT * FROM users ORDER BY id ASC;";
        List<User> users = new ArrayList<>();
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve all users: " + e.getMessage(), e);
        }
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        UserRole role = UserRole.valueOf(rs.getString("role"));

        if (role == UserRole.STUDENT) {
            String regNo = rs.getString("registration_number");
            String dept = rs.getString("department");
            return new Student(id, name, email, phone, regNo, dept);
        } else {
            String empId = rs.getString("employee_id");
            return new Librarian(id, name, email, phone, empId);
        }
    }
}
