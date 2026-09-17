package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.interface_contract.Exportable;
import com.smartlibrary.interface_contract.Searchable;

/**
 * Abstract Base Class for Library Users.
 * Demonstrates: Abstract Class, Encapsulation, Interface Implementation.
 */
@CourseConcept(unit = 2, concept = "Abstract Class & Encapsulation")
public abstract class User implements Searchable, Exportable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;

    public User() {}

    public User(int id, String name, String email, String phone, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Abstract method: returns maximum number of books user can borrow
    public abstract int getMaxBorrowLimit();

    @Override
    public boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return false;
        String lowerKey = keyword.toLowerCase().trim();
        return name.toLowerCase().contains(lowerKey)
            || email.toLowerCase().contains(lowerKey)
            || String.valueOf(id).equals(lowerKey);
    }

    @Override
    public String toCsvRow() {
        return String.format("%d,\"%s\",\"%s\",\"%s\",%s", id, name, email, phone, role.name());
    }

    @Override
    public String toFormattedText() {
        return String.format("[%d] %s (%s) | Email: %s | Phone: %s | Max Limit: %d",
                id, name, role, email, phone, getMaxBorrowLimit());
    }

    @Override
    public String toString() {
        return toFormattedText();
    }
}
