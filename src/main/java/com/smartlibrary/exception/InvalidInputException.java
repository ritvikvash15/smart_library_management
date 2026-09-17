package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Thrown when user input violates validation criteria")
public class InvalidInputException extends LibraryException {
    public InvalidInputException(String message) {
        super(message);
    }
}
