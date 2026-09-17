package com.smartlibrary.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for Domain Model Classes")
public class ModelTest {

    @Test
    @DisplayName("Test PhysicalBook Inheritance and Overriding")
    public void testPhysicalBook() {
        PhysicalBook book = new PhysicalBook(1, "123456", "Java Programming", "Author A", BookCategory.COMPUTER_SCIENCE, "Rack 1", 2);

        assertTrue(book.isBorrowable());
        assertEquals(14, book.getMaxBorrowDurationDays());
        assertEquals("Physical Book", book.getBookType());

        // Test Fine Calculation for 4 overdue days (rate = 2.50)
        assertEquals(10.0, book.calculateFine(4), 0.01);
        assertEquals(0.0, book.calculateFine(0), 0.01);
    }

    @Test
    @DisplayName("Test ReferenceBook Non-Borrowable Rule")
    public void testReferenceBook() {
        ReferenceBook refBook = new ReferenceBook(2, "654321", "Java Spec", "Gosling", BookCategory.REFERENCE, "Room 101");

        assertFalse(refBook.isBorrowable());
        assertEquals(0, refBook.getMaxBorrowDurationDays());
        assertEquals(0.0, refBook.calculateFine(5), 0.01);
        assertTrue(refBook.toFormattedText().contains("Reference Book"));
    }

    @Test
    @DisplayName("Test Student Borrow Limit")
    public void testStudentLimit() {
        Student student = new Student(1, "John", "john@test.com", "1234567890", "REG001", "CS");
        assertEquals(3, student.getMaxBorrowLimit());
        assertEquals(UserRole.STUDENT, student.getRole());
    }

    @Test
    @DisplayName("Test Keyword Search Matching")
    public void testSearchMatching() {
        PhysicalBook book = new PhysicalBook(1, "978-0134685991", "Effective Java", "Joshua Bloch", BookCategory.COMPUTER_SCIENCE, "Rack 1", 1);

        assertTrue(book.matchesKeyword("Effective"));
        assertTrue(book.matchesKeyword("bloch"));
        assertTrue(book.matchesKeyword("978-0134685991"));
        assertFalse(book.matchesKeyword("Python"));
    }
}
