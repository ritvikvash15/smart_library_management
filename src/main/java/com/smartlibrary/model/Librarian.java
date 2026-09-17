package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

/**
 * Concrete Subclass representing Librarian/Admin Users.
 * Demonstrates: Inheritance, Polymorphism.
 */
@CourseConcept(unit = 2, concept = "Inheritance & Subclassing")
public class Librarian extends User {
    private String employeeId;
    public static final int LIBRARIAN_MAX_BORROW_LIMIT = 10;

    public Librarian() {
        super();
        setRole(UserRole.LIBRARIAN);
    }

    public Librarian(int id, String name, String email, String phone, String employeeId) {
        super(id, name, email, phone, UserRole.LIBRARIAN);
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int getMaxBorrowLimit() {
        return LIBRARIAN_MAX_BORROW_LIMIT;
    }

    @Override
    public String toCsvRow() {
        return super.toCsvRow() + String.format(",\"%s\",N/A", employeeId);
    }

    @Override
    public String toFormattedText() {
        return super.toFormattedText() + String.format(" | EmpID: %s", employeeId);
    }
}
