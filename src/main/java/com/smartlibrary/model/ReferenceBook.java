package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

/**
 * Concrete Subclass representing Non-Borrowable Reference Books.
 * Demonstrates: Inheritance, Polymorphism, Method Overriding (cannot be checked out).
 */
@CourseConcept(unit = 2, concept = "Inheritance & Polymorphism")
public class ReferenceBook extends Book {
    private String readingRoomSection;

    public ReferenceBook() {
        super();
        setStatus(BookStatus.REFERENCE_ONLY);
    }

    public ReferenceBook(int id, String isbn, String title, String author, BookCategory category, String readingRoomSection) {
        super(id, isbn, title, author, category);
        this.readingRoomSection = readingRoomSection;
        setStatus(BookStatus.REFERENCE_ONLY);
    }

    public String getReadingRoomSection() {
        return readingRoomSection;
    }

    public void setReadingRoomSection(String readingRoomSection) {
        this.readingRoomSection = readingRoomSection;
    }

    @Override
    public String getBookType() {
        return "Reference Book (Reading Room Only)";
    }

    @Override
    public boolean isBorrowable() {
        return false; // Reference books CANNOT be checked out
    }

    @Override
    public int getMaxBorrowDurationDays() {
        return 0;
    }

    @Override
    public double calculateFine(int overdueDays) {
        return 0.0;
    }

    @Override
    public String toFormattedText() {
        return super.toFormattedText() + String.format(" | Room Section: %s", readingRoomSection);
    }
}
