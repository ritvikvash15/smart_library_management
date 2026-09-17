package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

/**
 * Concrete Subclass representing Student Users.
 * Demonstrates: Inheritance, Super Keyword, Polymorphism.
 */
@CourseConcept(unit = 2, concept = "Inheritance & Polymorphism")
public class Student extends User {
    private String registrationNumber;
    private String department;
    public static final int STUDENT_MAX_BORROW_LIMIT = 3;

    public Student() {
        super();
        setRole(UserRole.STUDENT);
    }

    public Student(int id, String name, String email, String phone, String registrationNumber, String department) {
        super(id, name, email, phone, UserRole.STUDENT);
        this.registrationNumber = registrationNumber;
        this.department = department;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int getMaxBorrowLimit() {
        return STUDENT_MAX_BORROW_LIMIT; // Students can borrow up to 3 books
    }

    @Override
    public String toCsvRow() {
        return super.toCsvRow() + String.format(",\"%s\",\"%s\"", registrationNumber, department);
    }

    @Override
    public String toFormattedText() {
        return super.toFormattedText() + String.format(" | RegNo: %s | Dept: %s", registrationNumber, department);
    }
}
