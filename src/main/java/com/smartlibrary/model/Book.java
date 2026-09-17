package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;
import com.smartlibrary.interface_contract.Borrowable;
import com.smartlibrary.interface_contract.Exportable;
import com.smartlibrary.interface_contract.Searchable;

/**
 * Abstract Base Class for Books in the Library System.
 * Demonstrates: Abstract Class, Abstract Methods, Encapsulation, Inheritance Root,
 * Interfaces Implementation, Constructor Overloading, `this` keyword.
 */
@CourseConcept(unit = 2, concept = "Abstract Class & Encapsulation")
public abstract class Book implements Borrowable, Searchable, Exportable {
    private int id;
    private String isbn;
    private String title;
    private String author;
    private BookCategory category;
    private BookStatus status;

    public static final double DEFAULT_DAILY_FINE_RATE = 2.50; // Final constant

    // Default Constructor
    public Book() {
        this.status = BookStatus.AVAILABLE;
    }

    // Overloaded Parametric Constructor using `this` keyword
    public Book(int id, String isbn, String title, String author, BookCategory category) {
        this();
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
    }

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    // Abstract method to be overridden by subclasses
    public abstract String getBookType();

    // Interface Method Implementations
    @Override
    public boolean matchesKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return false;
        String lowerKey = keyword.toLowerCase().trim();
        return title.toLowerCase().contains(lowerKey)
            || author.toLowerCase().contains(lowerKey)
            || isbn.toLowerCase().contains(lowerKey)
            || category.name().toLowerCase().contains(lowerKey);
    }

    @Override
    public String toCsvRow() {
        return String.format("%d,%s,\"%s\",\"%s\",%s,%s,%s",
                id, isbn, title, author, category.name(), status.name(), getBookType());
    }

    @Override
    public String toFormattedText() {
        return String.format("[%d] %s by %s | Category: %s | Status: %s | Type: %s",
                id, title, author, category.getDisplayName(), status, getBookType());
    }

    @Override
    public String toString() {
        return toFormattedText();
    }
}
