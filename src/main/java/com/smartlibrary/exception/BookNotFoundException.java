package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Thrown when a book ID or ISBN is not found in database/collections")
public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String message) {
        super(message);
    }
}
