package com.smartlibrary.exception;

import com.smartlibrary.annotation.CourseConcept;

@CourseConcept(unit = 3, concept = "Exception Handling", description = "Thrown when user/student record is missing")
public class UserNotFoundException extends LibraryException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
