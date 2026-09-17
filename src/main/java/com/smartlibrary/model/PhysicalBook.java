package com.smartlibrary.model;

import com.smartlibrary.annotation.CourseConcept;

/**
 * Concrete Subclass representing Physical Borrowable Books.
 * Demonstrates: Inheritance, Super Keyword, Method Overriding, Encapsulation.
 */
@CourseConcept(unit = 2, concept = "Inheritance & Method Overriding")
public class PhysicalBook extends Book {
    private String shelfLocation;
    private int quantity;

    public PhysicalBook() {
        super();
    }

    public PhysicalBook(int id, String isbn, String title, String author, BookCategory category, String shelfLocation, int quantity) {
        // Calling superclass constructor
        super(id, isbn, title, author, category);
        this.shelfLocation = shelfLocation;
        this.quantity = quantity;
        if (quantity <= 0) {
            setStatus(BookStatus.ISSUED);
        }
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (this.quantity > 0 && getStatus() == BookStatus.ISSUED) {
            setStatus(BookStatus.AVAILABLE);
        }
    }

    @Override
    public String getBookType() {
        return "Physical Book";
    }

    @Override
    public boolean isBorrowable() {
        return getStatus() == BookStatus.AVAILABLE && quantity > 0;
    }

    @Override
    public int getMaxBorrowDurationDays() {
        return 14; // Physical books can be checked out for 14 days
    }

    @Override
    public double calculateFine(int overdueDays) {
        if (overdueDays <= 0) return 0.0;
        return overdueDays * DEFAULT_DAILY_FINE_RATE;
    }

    @Override
    public String toFormattedText() {
        return super.toFormattedText() + String.format(" | Shelf: %s | Qty: %d", shelfLocation, quantity);
    }
}
