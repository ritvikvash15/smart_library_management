package com.smartlibrary.entity;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.model.UserRole;
import jakarta.persistence.*;

/**
 * JPA Entity representing User table for ORM mapping.
 * Demonstrates Unit 5: JPA Annotations and Object-Relational Mapping (ORM).
 */
@Entity
@Table(name = "users")
@CourseConcept(unit = 5, concept = "JPA Entity & Annotations")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "department")
    private String department;

    public UserEntity() {}

    public UserEntity(String name, String email, String phone, UserRole role, String registrationNumber, String department) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.registrationNumber = registrationNumber;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    public String toString() {
        return String.format("UserEntity[ID=%d, Name='%s', Email='%s', Role=%s]", id, name, email, role);
    }
}
