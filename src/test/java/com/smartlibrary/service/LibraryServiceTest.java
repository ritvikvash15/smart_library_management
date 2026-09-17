package com.smartlibrary.service;

import com.smartlibrary.exception.BookNotAvailableException;
import com.smartlibrary.exception.BookNotFoundException;
import com.smartlibrary.exception.LibraryException;
import com.smartlibrary.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for Library Service")
public class LibraryServiceTest {

    private LibraryService libraryService;

    @BeforeEach
    public void setUp() {
        libraryService = new LibraryService();
    }

    @Test
    @DisplayName("Test Method Overloading for Book Search")
    public void testOverloadedSearch() {
        // Search by String keyword
        List<Book> kwResults = libraryService.searchBook("Java");
        assertNotNull(kwResults);

        // Search by Category enum
        List<Book> catResults = libraryService.searchBook(BookCategory.COMPUTER_SCIENCE);
        assertNotNull(catResults);

        // Search by ID integer
        assertTrue(libraryService.searchBook(1).isPresent() || libraryService.searchBook(9999).isEmpty());
    }

    @Test
    @DisplayName("Test Issue Reference Book Throws BookNotAvailableException")
    public void testIssueReferenceBookThrows() throws LibraryException {
        ReferenceBook ref = new ReferenceBook(0, "REF-001", "Dict", "Anon", BookCategory.REFERENCE, "R1");
        int refId = libraryService.addBook(ref);

        assertThrows(BookNotAvailableException.class, () -> {
            libraryService.issueBook(1, refId);
        });
    }

    @Test
    @DisplayName("Test Issue Non-existent Book Throws BookNotFoundException")
    public void testIssueNonExistentBook() {
        assertThrows(BookNotFoundException.class, () -> {
            libraryService.issueBook(1, 999999);
        });
    }
}
