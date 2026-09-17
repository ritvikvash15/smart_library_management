package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Thrown when a requested book is not available for issue")
public class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String message) {
        super(message);
    }
}
