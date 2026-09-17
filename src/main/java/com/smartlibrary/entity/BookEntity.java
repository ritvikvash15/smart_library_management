package com.smartlibrary.entity;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.model.BookCategory;
import com.smartlibrary.model.BookStatus;
import jakarta.persistence.*;

/**
 * JPA Entity representing Book table for ORM mapping.
 * Demonstrates Unit 5: JPA Annotations and Object-Relational Mapping (ORM).
 */
@Entity
@Table(name = "books")
@CourseConcept(unit = 5, concept = "JPA Entity & Annotations")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private BookCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookStatus status;

    @Column(name = "book_type")
    private String bookType;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "location_info")
    private String locationInfo;

    public BookEntity() {}

    public BookEntity(String isbn, String title, String author, BookCategory category, BookStatus status, String bookType, int quantity, String locationInfo) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.bookType = bookType;
        this.quantity = quantity;
        this.locationInfo = locationInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    @Override
    public String toString() {
        return String.format("BookEntity[ID=%d, Title='%s', Author='%s', Status=%s]", id, title, author, status);
    }
}
