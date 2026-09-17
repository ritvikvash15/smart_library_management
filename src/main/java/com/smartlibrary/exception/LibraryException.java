package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Base custom checked exception class for Smart Library System")
public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
